package com.ahmedyejam.mks.data.repository

import android.net.Uri
import com.ahmedyejam.mks.data.importer.model.ImportFormat
import com.ahmedyejam.mks.data.importer.model.ImportResult
import com.ahmedyejam.mks.data.importer.model.MergeStrategy
import com.ahmedyejam.mks.data.importer.model.ParsedQuestion
import com.ahmedyejam.mks.data.importer.repository.ImportLibraryManager
import com.ahmedyejam.mks.data.local.FileManager
import com.ahmedyejam.mks.data.local.WorkspaceDefaults
import com.ahmedyejam.mks.data.local.dao.AnnotationDao
import com.ahmedyejam.mks.data.local.dao.AssetReferenceDao
import com.ahmedyejam.mks.data.local.dao.BookDao
import com.ahmedyejam.mks.data.local.dao.CategoryMetadataDao
import com.ahmedyejam.mks.data.local.dao.CourseSlideDao
import com.ahmedyejam.mks.data.local.dao.FlashcardDao
import com.ahmedyejam.mks.data.local.dao.FlashcardDeckDao
import com.ahmedyejam.mks.data.local.dao.KnowledgeStudySessionDao
import com.ahmedyejam.mks.data.local.dao.LearningSessionDao
import com.ahmedyejam.mks.data.local.dao.MistakeLogDao
import com.ahmedyejam.mks.data.local.dao.NoteBlueprintDao
import com.ahmedyejam.mks.data.local.dao.PromptCardDao
import com.ahmedyejam.mks.data.local.dao.PromptDao
import com.ahmedyejam.mks.data.local.dao.PromptDeckDao
import com.ahmedyejam.mks.data.local.dao.PromptRunDao
import com.ahmedyejam.mks.data.local.dao.QuestionAssetDao
import com.ahmedyejam.mks.data.local.dao.QuestionCategoryDao
import com.ahmedyejam.mks.data.local.dao.QuestionDao
import com.ahmedyejam.mks.data.local.dao.QuizDao
import com.ahmedyejam.mks.data.local.dao.SessionDao
import com.ahmedyejam.mks.data.local.dao.SlideshowCourseDao
import com.ahmedyejam.mks.data.local.dao.SourceDocumentDao
import com.ahmedyejam.mks.data.local.dao.WorkspaceDao
import com.ahmedyejam.mks.data.local.entity.AnnotationColorLabel
import com.ahmedyejam.mks.data.local.entity.AnnotationEntity
import com.ahmedyejam.mks.data.local.entity.AnnotationOwnerType
import com.ahmedyejam.mks.data.local.entity.AssetReferenceEntity
import com.ahmedyejam.mks.data.local.entity.BlueprintMode
import com.ahmedyejam.mks.data.local.entity.BlueprintReviewStatus
import com.ahmedyejam.mks.data.local.entity.BookEntity
import com.ahmedyejam.mks.data.local.entity.CategoryMetadataEntity
import com.ahmedyejam.mks.data.local.entity.CourseSlideEntity
import com.ahmedyejam.mks.data.local.entity.FlashcardDeckEntity
import com.ahmedyejam.mks.data.local.entity.FlashcardEntity
import com.ahmedyejam.mks.data.local.entity.KnowledgeStudySessionEntity
import com.ahmedyejam.mks.data.local.entity.MistakeLogEntryEntity
import com.ahmedyejam.mks.data.local.entity.NoteBlueprintEntity
import com.ahmedyejam.mks.data.local.entity.PromptCardEntity
import com.ahmedyejam.mks.data.local.entity.PromptDeckEntity
import com.ahmedyejam.mks.data.local.entity.PromptEntity
import com.ahmedyejam.mks.data.local.entity.PromptOutputType
import com.ahmedyejam.mks.data.local.entity.PromptRunEntity
import com.ahmedyejam.mks.data.local.entity.QuestionAssetEntity
import com.ahmedyejam.mks.data.local.entity.QuestionAssetType
import com.ahmedyejam.mks.data.local.entity.QuestionEntity
import com.ahmedyejam.mks.data.local.entity.QuestionType
import com.ahmedyejam.mks.data.local.entity.QuizEntity
import com.ahmedyejam.mks.data.local.entity.SessionEntity
import com.ahmedyejam.mks.data.local.entity.SlideshowCourseEntity
import com.ahmedyejam.mks.data.local.entity.SourceDocumentAssetEntity
import com.ahmedyejam.mks.data.local.entity.SourceDocumentEntity
import com.ahmedyejam.mks.data.local.entity.WorkspaceEntity
import com.ahmedyejam.mks.data.local.entity.WorkspaceSettingsEntity
import com.ahmedyejam.mks.data.model.ArticleGenerationConfig
import com.ahmedyejam.mks.data.model.CategoryWithMetadata
import com.ahmedyejam.mks.data.model.ExportResult
import com.ahmedyejam.mks.data.model.FlashcardGenerationConfig
import com.ahmedyejam.mks.data.model.SlideGenerationConfig
import com.ahmedyejam.mks.data.preview.CategoryMergePreviewService
import com.ahmedyejam.mks.data.preview.ClearMarksPreviewService
import com.ahmedyejam.mks.data.preview.DeletePreviewService
import com.ahmedyejam.mks.data.repair.AssetReferenceAuditService
import com.ahmedyejam.mks.data.simulation.ChangeSimulationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepository @Inject constructor(
    private val bookDao: BookDao,
    private val quizDao: QuizDao,
    private val questionDao: QuestionDao,
    private val sessionDao: SessionDao,
    private val questionCategoryDao: QuestionCategoryDao,
    private val categoryMetadataDao: CategoryMetadataDao,
    private val deletePreviewService: DeletePreviewService? = null,
    private val categoryMergePreviewService: CategoryMergePreviewService? = null,
    private val clearMarksPreviewService: ClearMarksPreviewService? = null,
    private val bookRepository: BookRepository,
    private val assetRepository: AssetRepository,
    private val fileManager: FileManager,
    private val questionAssetDao: QuestionAssetDao
) {

    fun getQuizzesByBookId(bookId: Long, sortBy: SortOption = SortOption.TITLE): Flow<List<QuizEntity>> = quizDao.getQuizzesByBookIdSorted(bookId, sortBy.name)

    fun getQuizzesByCategory(category: String, sortBy: SortOption = SortOption.TITLE): Flow<List<QuizEntity>> = quizDao.getQuizzesByCategorySorted(category, sortBy.name)

    fun getAllQuizzesFlow(): Flow<List<QuizEntity>> = quizDao.getAllQuizzesFlow()

    fun getAllCategories(): Flow<List<String>> = combine(
        quizDao.getAllCategories(),
        questionCategoryDao.getAllQuestionCategories()
    ) { quizCategories, questionCategories ->
        (quizCategories + questionCategories).filter { it.isNotBlank() }.distinct().sorted()
    }

    suspend fun getQuizById(id: Long) = quizDao.getQuizById(id)

    suspend fun insertQuiz(quiz: QuizEntity): Long {
        val finalQuiz = if (quiz.coverImage?.startsWith("http", ignoreCase = true) == true) {
            val localPath = fileManager.downloadAndSaveImage(quiz.coverImage!!)
            if (localPath != null) quiz.copy(coverImage = localPath) else quiz
        } else {
            quiz
        }
        
        val saved = finalQuiz.copy(
            createdAt = if (finalQuiz.createdAt == 0L) System.currentTimeMillis() else finalQuiz.createdAt,
            updatedAt = System.currentTimeMillis(),
        )
        val id = quizDao.insertQuiz(saved)
        assetRepository.replaceOwnerAssetReferences("quiz", id, listOf(saved.coverImage))
        bookRepository.refreshBookStats(finalQuiz.bookId)
        return id
    }

    suspend fun updateQuiz(quiz: QuizEntity) {
        val updated = quiz.copy(updatedAt = System.currentTimeMillis(), lastEditedAt = System.currentTimeMillis())
        quizDao.updateQuiz(updated)
        assetRepository.replaceOwnerAssetReferences("quiz", updated.id, listOf(updated.coverImage))
        bookRepository.refreshBookStats(quiz.bookId)
    }

    // Questions

    suspend fun deleteQuiz(quiz: QuizEntity): QuizEntity {
        if (quiz.isSystem) throw IllegalStateException("Cannot delete system quiz")
        val now = System.currentTimeMillis()
        assetRepository.softDeleteQuizAnnotationTree(quiz.id, now)
        sessionDao.softDeleteSessionsByQuizId(quiz.id, now)
        questionDao.softDeleteQuestionsByQuizId(quiz.id, now)
        quizDao.softDeleteQuizById(quiz.id, now)
        bookRepository.refreshBookStats(quiz.bookId)
        return quiz.copy(deletedAt = now, updatedAt = now)
    }


    suspend fun restoreQuiz(quizId: Long) {
        val quiz = quizDao.getQuizByIdIncludingDeleted(quizId) ?: return
        val book = bookDao.getBookByIdIncludingDeleted(quiz.bookId)
        if (book != null && book.deletedAt != null) {
            bookRepository.restoreBook(book.id)
        }
        val now = System.currentTimeMillis()
        val deletedAtFilter = quiz.deletedAt ?: now
        quizDao.restoreQuizById(quizId, now)
        questionDao.restoreQuestionsByQuizId(quizId, now, deletedAtFilter)
        sessionDao.restoreSessionsByQuizId(quizId, now, deletedAtFilter)
        assetRepository.restoreOwnerAnnotations("quiz", quizId, now)
        bookRepository.refreshBookStats(quiz.bookId)
    }


    suspend fun permanentlyDeleteQuiz(quiz: QuizEntity): QuizEntity {
        if (quiz.isSystem) throw IllegalStateException("Cannot delete system quiz")
        assetRepository.permanentlyDeleteQuizAnnotationTree(quiz.id)
        assetRepository.releaseQuizTreeAssets(quiz)
        quizDao.hardDeleteQuiz(quiz)
        bookRepository.refreshBookStats(quiz.bookId)
        return quiz
    }


    suspend fun previewQuizDeletion(quizId: Long) = deletePreviewService?.previewQuizDeletion(quizId)

    suspend fun previewCategoryMerge(source: String, target: String) = categoryMergePreviewService?.previewMerge(source, target)


    suspend fun clearMarksForQuizWithPreview(quizId: Long): ChangeSimulationResult {
        return clearMarksPreviewService?.previewClearMarksForQuiz(quizId)
            ?: ChangeSimulationResult("Clear Marks", "Preview unavailable")
    }


    suspend fun applyClearMarksForQuiz(quizId: Long) {
        questionDao.clearMarksForQuiz(quizId, System.currentTimeMillis())
    }


    suspend fun refreshQuizStats(quizId: Long) {
        quizDao.refreshQuestionCount(quizId)
        
        val questions = questionDao.getQuestionsByQuizId(quizId).first()
        val total = questions.size
        val answered = questions.count { it.attempts > 0 }
        val completion = if (total == 0) 0f else answered.toFloat() / total
        
        val totalAttempts = questions.sumOf { it.attempts }
        val totalCorrect = questions.sumOf { it.correctCount }
        val accuracy = if (totalAttempts == 0) 0f else totalCorrect.toFloat() / totalAttempts
        
        quizDao.updateCompletionPercentage(quizId, completion)
        quizDao.updateAnsweredCount(quizId, answered)
        quizDao.updateTotalAttempts(quizId, totalAttempts)
        quizDao.updateAccuracyPercentage(quizId, accuracy)
        
        // Propagate to book
        val quiz = quizDao.getQuizById(quizId) ?: return
        bookRepository.refreshBookStats(quiz.bookId)
    }


    fun getQuestionsByQuizId(quizId: Long): Flow<List<QuestionEntity>> = questionDao.getQuestionsByQuizId(quizId)

    fun searchAllQuestions(query: String): Flow<List<QuestionEntity>> = questionDao.searchAllQuestionsFlow(query)

    fun getQuestionsByCategoryFlow(category: String): Flow<List<QuestionEntity>> = questionCategoryDao.getQuestionsByCategoryFlow(category)

    suspend fun getQuestionsByCategory(category: String): List<QuestionEntity> = questionCategoryDao.getQuestionsByCategory(category)

    suspend fun getQuestionById(id: Long) = questionDao.getQuestionById(id)

    suspend fun getQuestionsByIds(ids: List<Long>) = questionDao.getQuestionsByIds(ids)

    suspend fun insertQuestion(question: QuestionEntity): Long {
        val finalQuestion = if (question.imagePath?.startsWith("http", ignoreCase = true) == true) {
            val localPath = fileManager.downloadAndSaveImage(question.imagePath!!)
            if (localPath != null) question.copy(imagePath = localPath) else question
        } else {
            question
        }
        
        val saved = finalQuestion.copy(
            createdAt = if (finalQuestion.createdAt == 0L) System.currentTimeMillis() else finalQuestion.createdAt,
            updatedAt = System.currentTimeMillis(),
        )
        val id = questionDao.insertQuestion(saved)
        assetRepository.syncQuestionCategories(id, saved.categories)
        assetRepository.replaceOwnerAssetReferences("question", id, listOf(saved.imagePath))
        refreshQuizStats(finalQuestion.quizId)
        return id
    }

    suspend fun insertQuestions(questions: List<QuestionEntity>): List<Long> {
        val now = System.currentTimeMillis()
        val updatedQuestions = questions.map { question ->
            val finalQuestion = if (question.imagePath?.startsWith("http", ignoreCase = true) == true) {
                val localPath = fileManager.downloadAndSaveImage(question.imagePath!!)
                if (localPath != null) question.copy(imagePath = localPath) else question
            } else {
                question
            }
            
            finalQuestion.copy(
                createdAt = if (finalQuestion.createdAt == 0L) now else finalQuestion.createdAt,
                updatedAt = now
            )
        }
        val ids = questionDao.insertQuestions(updatedQuestions)
        ids.zip(updatedQuestions).forEach { (id, question) ->
            assetRepository.syncQuestionCategories(id, question.categories)
            assetRepository.replaceOwnerAssetReferences("question", id, listOf(question.imagePath))
        }
        questions.asSequence().map { it.quizId }.distinct().forEach { refreshQuizStats(it) }
        return ids
    }


    suspend fun updateQuestion(question: QuestionEntity) {
        val updated = question.copy(updatedAt = System.currentTimeMillis(), lastEditedAt = System.currentTimeMillis())
        questionDao.updateQuestion(updated)
        assetRepository.syncQuestionCategories(updated.id, updated.categories)
        assetRepository.replaceOwnerAssetReferences("question", updated.id, listOf(updated.imagePath))
        refreshQuizStats(question.quizId)
    }

    suspend fun deleteQuestion(question: QuestionEntity) {
        val now = System.currentTimeMillis()
        assetRepository.softDeleteQuestionAnnotationTree(question.id, now)
        questionAssetDao.softDeleteAssetsForQuestion(question.id, now)
        questionDao.softDeleteQuestionById(question.id, now)
        refreshQuizStats(question.quizId)
    }


    suspend fun restoreQuestion(questionId: Long) {
        val now = System.currentTimeMillis()
        questionDao.restoreQuestionById(questionId, now)
        assetRepository.restoreOwnerAnnotations(AnnotationOwnerType.QUESTION, questionId, now)
    }


    suspend fun permanentlyDeleteQuestion(question: QuestionEntity) {
        assetRepository.permanentlyDeleteQuestionAnnotationTree(question.id)
        assetRepository.releaseQuestionAssets(question)
        questionCategoryDao.deleteCategoriesForQuestion(question.id)
        questionDao.hardDeleteQuestion(question)
        refreshQuizStats(question.quizId)
    }

    suspend fun updateQuestionMark(questionId: Long, isMarked: Boolean, reason: String? = null) {
        val question = questionDao.getQuestionById(questionId) ?: return
        val now = System.currentTimeMillis()
        questionDao.updateQuestion(
            question.copy(
                isMarked = isMarked,
                markedAt = if (isMarked) now else null,
                markReason = if (isMarked) reason else null,
                updatedAt = now,
                lastEditedAt = now
            )
        )
    }


    suspend fun updateQuestionDrop(questionId: Long, isDropped: Boolean, reason: String? = null) {
        val question = questionDao.getQuestionById(questionId) ?: return
        val now = System.currentTimeMillis()
        questionDao.updateQuestion(
            question.copy(
                isDropped = isDropped,
                droppedAt = if (isDropped) now else null,
                droppedReason = if (isDropped) reason else null,
                updatedAt = now,
                lastEditedAt = now
            )
        )
    }


    suspend fun updateCategoryMetadata(metadata: CategoryMetadataEntity) = 
        categoryMetadataDao.insertMetadata(metadata)


    suspend fun getSessionById(id: Long) = sessionDao.getSessionById(id)

    suspend fun insertSession(session: SessionEntity): Long {
        val id = sessionDao.insertSession(session.copy(
            createdAt = if (session.createdAt == 0L) System.currentTimeMillis() else session.createdAt,
            updatedAt = System.currentTimeMillis()
        ))
        bookRepository.updateLastStudied(session.quizId)
        return id
    }

    suspend fun updateSession(session: SessionEntity) {
        sessionDao.updateSession(session.copy(updatedAt = System.currentTimeMillis()))
        if (session.isCompleted) {
            refreshQuizStats(session.quizId)
        }
        bookRepository.updateLastStudied(session.quizId)
    }

    suspend fun deleteSession(session: SessionEntity) = sessionDao.softDeleteSessionById(session.id, System.currentTimeMillis())


}
