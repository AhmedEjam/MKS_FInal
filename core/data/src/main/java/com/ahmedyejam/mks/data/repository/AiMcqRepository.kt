package com.ahmedyejam.mks.data.repository

import android.util.Log
import com.ahmedyejam.mks.data.local.entity.QuizEntity
import com.ahmedyejam.mks.data.model.AiProviderConfig
import com.ahmedyejam.mks.data.model.McqPrompts
import com.ahmedyejam.mks.data.model.ParsedMcq
import com.ahmedyejam.mks.data.network.McqRunConfig
import com.ahmedyejam.mks.data.network.McqService
import com.ahmedyejam.mks.data.preferences.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "AiMcqRepository"

/** Sealed progress state for the AI MCQ generation pipeline. */
sealed class AiMcqProgress {
    object Idle : AiMcqProgress()
    data class Processing(
        val chunk: Int,
        val totalChunks: Int,
        val foundSoFar: Int,
    ) : AiMcqProgress()
    data class Done(val count: Int, val quizId: Long) : AiMcqProgress()
    data class Error(val message: String) : AiMcqProgress()
}

/**
 * Orchestrates the full "text → MCQs → QuizEntity + QuestionEntity" pipeline.
 *
 * Reads AI provider configuration from [DataStoreManager], delegates the
 * extraction/generation/review pipeline to [McqService], then persists the
 * resulting questions via [QuizRepository].
 */
@Singleton
class AiMcqRepository @Inject constructor(
    private val mcqService: McqService,
    private val quizRepository: QuizRepository,
    private val dataStoreManager: DataStoreManager,
) {
    private val _progress = MutableStateFlow<AiMcqProgress>(AiMcqProgress.Idle)
    val progress: Flow<AiMcqProgress> = _progress.asStateFlow()

    /**
     * Run the MCQ pipeline on [sectionText] and save the results as a new quiz
     * inside the given book.
     *
     * @param bookId        Target book. The new quiz is created as a child of this book.
     * @param quizTitle     Title for the newly created quiz.
     * @param sectionText   The educational text to process (OCR output or manual paste).
     * @param chapterNum    Chapter label for MCQ numbering, e.g. "3".
     * @param sectionName   Section label for prompt context, e.g. "Cardiac Physiology".
     */
    suspend fun generateAndSave(
        bookId: Long,
        quizTitle: String,
        sectionText: String,
        chapterNum: String = "1",
        sectionName: String = "Section",
    ) {
        _progress.value = AiMcqProgress.Processing(0, 1, 0)
        try {
            // ── 1. Load provider settings from DataStore ───────────────────────
            val providerId = dataStoreManager.aiProviderId.first()
            val baseUrl = dataStoreManager.aiBaseUrl.first()
            val apiKey = dataStoreManager.aiApiKey.first()
            val chatModel = dataStoreManager.aiChatModel.first()
            val reviewEnabled = dataStoreManager.aiMcqReviewEnabled.first()
            val extractionMode = dataStoreManager.aiMcqExtractionMode.first()

            val providerConfig = AiProviderConfig(
                providerId = providerId,
                baseUrl = baseUrl,
                apiKey = apiKey,
                model = chatModel,
            )

            val extractionPrompt = if (extractionMode == "simple") McqPrompts.EXT_SIMPLE else McqPrompts.EXT_DEFAULT
            val generationPrompt = if (extractionMode == "simple") McqPrompts.GEN_SIMPLE else McqPrompts.GEN_DEFAULT

            val runConfig = McqRunConfig(
                provider = providerConfig,
                chapterNum = chapterNum,
                sectionName = sectionName,
                extractionPrompt = extractionPrompt,
                generationPrompt = generationPrompt,
                reviewPrompt = if (reviewEnabled) McqPrompts.REV_DEFAULT else null,
                reviewEnabled = reviewEnabled,
            )

            // ── 2. Run MCQ pipeline ────────────────────────────────────────────
            val mcqs = mcqService.processMCQ(
                sectionText = sectionText,
                config = runConfig,
                onProgress = { progress ->
                    _progress.value = AiMcqProgress.Processing(
                        chunk = progress.chunk,
                        totalChunks = progress.totalChunks,
                        foundSoFar = progress.foundSoFar,
                    )
                },
            )

            if (mcqs.isEmpty()) {
                _progress.value = AiMcqProgress.Error(
                    "No MCQs found or generated. Try a different extraction mode or section text."
                )
                return
            }

            // ── 3. Create quiz entity ──────────────────────────────────────────
            val quiz = QuizEntity(
                externalId = "ai_gen_${System.currentTimeMillis()}",
                bookId = bookId,
                title = quizTitle.ifBlank { "AI Generated Quiz – $sectionName" },
                description = "Generated from: $sectionName (Chapter $chapterNum) · ${mcqs.size} questions",
                questionCount = mcqs.size,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                lastEditedAt = System.currentTimeMillis(),
            )
            val quizId = quizRepository.insertQuiz(quiz)
            Log.d(TAG, "Created quiz #$quizId with ${mcqs.size} questions")

            // ── 4. Insert questions ────────────────────────────────────────────
            val questions = mcqs.mapIndexed { index, mcq ->
                mcq.toQuestionEntity(quizId = quizId, order = index + 1)
            }
            quizRepository.insertQuestions(questions)

            _progress.value = AiMcqProgress.Done(count = mcqs.size, quizId = quizId)
        } catch (e: kotlinx.coroutines.CancellationException) {
            _progress.value = AiMcqProgress.Idle
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Pipeline failed", e)
            _progress.value = AiMcqProgress.Error(
                e.message ?: "Unknown error during MCQ generation"
            )
        }
    }

    /** Build an [AiProviderConfig] from the current DataStore settings (for preview / testing). */
    suspend fun currentProviderConfig(): AiProviderConfig = AiProviderConfig(
        providerId = dataStoreManager.aiProviderId.first(),
        baseUrl = dataStoreManager.aiBaseUrl.first(),
        apiKey = dataStoreManager.aiApiKey.first(),
        model = dataStoreManager.aiChatModel.first(),
    )

    fun resetProgress() {
        _progress.value = AiMcqProgress.Idle
    }
}
