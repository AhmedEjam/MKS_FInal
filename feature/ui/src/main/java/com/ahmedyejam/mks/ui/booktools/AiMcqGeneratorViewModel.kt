package com.ahmedyejam.mks.ui.booktools

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedyejam.mks.data.model.AI_PROVIDERS
import com.ahmedyejam.mks.data.model.AiProviderDescriptor
import com.ahmedyejam.mks.data.model.ParsedMcq
import com.ahmedyejam.mks.data.preferences.DataStoreManager
import com.ahmedyejam.mks.data.repository.AiMcqProgress
import com.ahmedyejam.mks.data.repository.AiMcqRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.cancelChildren
import javax.inject.Inject

data class AiMcqGeneratorUiState(
    val bookId: Long = 0L,
    /** Text pasted or imported by the user. */
    val inputText: String = "",
    /** Label for chapter/section (used in MCQ numbering). */
    val chapterNum: String = "1",
    /** Human-readable section name (used in prompt context). */
    val sectionName: String = "Section",
    /** Quiz title for the newly created quiz. */
    val quizTitle: String = "",
    /** MCQs produced by the last run (for preview). */
    val previewMcqs: List<ParsedMcq> = emptyList(),
    /** True while generation or saving is in progress. */
    val isRunning: Boolean = false,
    /** Chunk progress (0 if not started). */
    val progressChunk: Int = 0,
    val progressTotal: Int = 0,
    val progressFound: Int = 0,
    /** Set when generation completed and quiz was saved. */
    val savedQuizId: Long? = null,
    val error: String? = null,
    val successMessage: String? = null,
    /** True when a cloud run is blocked awaiting first-time privacy consent. */
    val pendingPrivacyConsent: Boolean = false,
    /** Provider name to show in the consent dialog. */
    val pendingProviderName: String = "",
)

@HiltViewModel
class AiMcqGeneratorViewModel @Inject constructor(
    private val aiMcqRepository: AiMcqRepository,
    val dataStoreManager: DataStoreManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AiMcqGeneratorUiState())
    val uiState = _uiState.asStateFlow()

    /** Live progress stream from the repository — collected below. */
    val progress = aiMcqRepository.progress.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        AiMcqProgress.Idle,
    )

    /** All available AI providers for the picker UI. */
    val availableProviders: List<AiProviderDescriptor> = AI_PROVIDERS

    init {
        // Observe the repository progress and mirror it into UI state
        viewModelScope.launch {
            aiMcqRepository.progress.collect { p ->
                when (p) {
                    is AiMcqProgress.Idle -> _uiState.value = _uiState.value.copy(isRunning = false)
                    is AiMcqProgress.Processing -> _uiState.value = _uiState.value.copy(
                        isRunning = true,
                        progressChunk = p.chunk,
                        progressTotal = p.totalChunks,
                        progressFound = p.foundSoFar,
                        error = null,
                    )
                    is AiMcqProgress.Done -> _uiState.value = _uiState.value.copy(
                        isRunning = false,
                        savedQuizId = p.quizId,
                        successMessage = "✅ ${p.count} questions saved to a new quiz!",
                        error = null,
                    )
                    is AiMcqProgress.Error -> _uiState.value = _uiState.value.copy(
                        isRunning = false,
                        error = p.message,
                    )
                }
            }
        }
    }

    fun setBookId(id: Long) {
        _uiState.value = _uiState.value.copy(bookId = id)
    }

    fun updateInputText(text: String) {
        _uiState.value = _uiState.value.copy(inputText = text, error = null)
    }

    fun updateChapterNum(num: String) {
        _uiState.value = _uiState.value.copy(chapterNum = num)
    }

    fun updateSectionName(name: String) {
        _uiState.value = _uiState.value.copy(sectionName = name)
    }

    fun updateQuizTitle(title: String) {
        _uiState.value = _uiState.value.copy(quizTitle = title)
    }

    private var generationJob: kotlinx.coroutines.Job? = null

    fun startGeneration() {
        val state = _uiState.value
        if (state.inputText.isBlank()) {
            _uiState.value = state.copy(error = "Please paste or import some text first.")
            return
        }
        if (state.bookId <= 0L) {
            _uiState.value = state.copy(error = "No book selected. Navigate from a book dashboard.")
            return
        }

        // Gate cloud runs behind first-time privacy consent, matching the prompt-deck path. MCQ
        // generation sends the user's section text to an external provider, so it must not bypass
        // the consent the prompt deck already enforces. Local Ollama needs no gate.
        viewModelScope.launch {
            val providerId = dataStoreManager.aiProviderId.first()
            val isCloud = !providerId.startsWith("ollama")
            val noticeShown = dataStoreManager.aiPrivacyNoticeShown.first()
            if (isCloud && !noticeShown) {
                val providerName = AI_PROVIDERS.firstOrNull { it.id == providerId }?.name ?: providerId
                _uiState.value = _uiState.value.copy(
                    pendingPrivacyConsent = true,
                    pendingProviderName = providerName,
                )
                return@launch
            }
            runGeneration()
        }
    }

    fun confirmPrivacyConsent() {
        viewModelScope.launch {
            dataStoreManager.setAiPrivacyNoticeShown(true)
            _uiState.value = _uiState.value.copy(pendingPrivacyConsent = false)
            runGeneration()
        }
    }

    fun cancelPrivacyConsent() {
        _uiState.value = _uiState.value.copy(pendingPrivacyConsent = false)
    }

    private fun runGeneration() {
        val state = _uiState.value
        _uiState.value = state.copy(isRunning = true, error = null, savedQuizId = null, successMessage = null)

        generationJob = viewModelScope.launch {
            aiMcqRepository.generateAndSave(
                bookId = state.bookId,
                quizTitle = state.quizTitle.ifBlank { "AI Quiz – ${state.sectionName}" },
                sectionText = state.inputText,
                chapterNum = state.chapterNum,
                sectionName = state.sectionName,
            )
        }
    }

    fun cancelGeneration() {
        generationJob?.cancel()
        generationJob = null
        aiMcqRepository.resetProgress()
        _uiState.value = _uiState.value.copy(isRunning = false, error = null)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetAfterSuccess() {
        _uiState.value = _uiState.value.copy(
            savedQuizId = null,
            successMessage = null,
            inputText = "",
            progressChunk = 0,
            progressTotal = 0,
            progressFound = 0,
        )
        aiMcqRepository.resetProgress()
    }

    // ── DataStore AI settings pass-throughs ───────────────────────────────────

    fun saveAiProvider(providerId: String, baseUrl: String, apiKey: String, chatModel: String, visionModel: String) {
        viewModelScope.launch {
            dataStoreManager.setAiProviderId(providerId)
            dataStoreManager.setAiBaseUrl(baseUrl)
            dataStoreManager.setAiApiKey(apiKey)
            dataStoreManager.setAiChatModel(chatModel)
            dataStoreManager.setAiVisionModel(visionModel)
        }
    }

    fun setReviewEnabled(enabled: Boolean) {
        viewModelScope.launch { dataStoreManager.setAiMcqReviewEnabled(enabled) }
    }

    fun setExtractionMode(mode: String) {
        viewModelScope.launch { dataStoreManager.setAiMcqExtractionMode(mode) }
    }
}
