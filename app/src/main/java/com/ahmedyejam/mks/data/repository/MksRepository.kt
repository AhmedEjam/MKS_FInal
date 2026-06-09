package com.ahmedyejam.mks.data.repository

import com.ahmedyejam.mks.data.local.FileManager
import com.ahmedyejam.mks.data.local.WorkspaceDefaults
import com.ahmedyejam.mks.data.local.dao.BookDao
import com.ahmedyejam.mks.data.local.dao.QuestionDao
import com.ahmedyejam.mks.data.local.dao.QuizDao
import com.ahmedyejam.mks.data.local.dao.SessionDao
import com.ahmedyejam.mks.data.local.dao.*
import com.ahmedyejam.mks.data.local.entity.*
import com.ahmedyejam.mks.data.model.CategoryWithMetadata
import com.ahmedyejam.mks.data.model.ExportResult
import com.ahmedyejam.mks.data.model.FlashcardGenerationConfig
import com.ahmedyejam.mks.data.import.repository.ImportLibraryManager
import com.ahmedyejam.mks.data.import.model.ParsedQuestion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.File

enum class SortOption { LAST_EDIT, LAST_STUDIED, TITLE, COMPLETION, QUESTION_COUNT, ACCURACY, PROGRESS }

data class KnowledgeSummary(
    val totalBooks: Int = 0,
    val totalQuizzes: Int = 0,
    val totalQuestions: Int = 0,
    val unansweredQuestions: Int = 0,
    val questionsWithNotes: Int = 0,
    val questionsWithAssets: Int = 0,
    val questionsWithSources: Int = 0,
    val markedQuestions: Int = 0,
    val droppedQuestions: Int = 0,
    val missedQuestions: Int = 0,
    val weakQuestions: Int = 0,
    val flashcardDecks: Int = 0,
    val totalFlashcards: Int = 0,
    val dueFlashcards: Int = 0,
    val weakFlashcards: Int = 0,
    val totalBlueprints: Int = 0,
    val blueprintsDueForReview: Int = 0,
    val linkedBlueprints: Int = 0,
    val promptDecks: Int = 0,
    val promptCards: Int = 0,
    val promptRuns: Int = 0,
    val savedPromptOutputs: Int = 0,
    val openMistakes: Int = 0,
    val fixedMistakes: Int = 0,
    val mistakesDueForReview: Int = 0,
    val pendingMistakesForReview: Int = 0,
    val reviewSchedulesDue: Int = 0
)

data class BookKnowledgeSummary(
    val bookId: Long,
    val totalQuizzes: Int = 0,
    val totalQuestions: Int = 0,
    val unansweredQuestions: Int = 0,
    val questionsWithNotes: Int = 0,
    val questionsWithAssets: Int = 0,
    val questionsWithSources: Int = 0,
    val markedQuestions: Int = 0,
    val droppedQuestions: Int = 0,
    val missedQuestions: Int = 0,
    val weakQuestions: Int = 0,
    val flashcardDecks: Int = 0,
    val totalFlashcards: Int = 0,
    val totalBlueprints: Int = 0,
    val promptDecks: Int = 0,
    val promptCards: Int = 0,
    val promptRuns: Int = 0,
    val savedPromptOutputs: Int = 0,
    val openMistakes: Int = 0,
    val reviewSchedulesDue: Int = 0
)

data class QuizKnowledgeSummary(
    val quizId: Long,
    val totalQuestions: Int = 0,
    val unansweredQuestions: Int = 0,
    val markedQuestions: Int = 0,
    val droppedQuestions: Int = 0,
    val missedQuestions: Int = 0,
    val questionsWithNotes: Int = 0,
    val questionsWithAssets: Int = 0,
    val questionsWithSources: Int = 0
)

data class BookStudyBundle(
    val book: BookEntity,
    val quizzes: List<QuizEntity>,
    val questionsByQuiz: Map<Long, List<QuestionEntity>>,
    val flashcardDecks: List<FlashcardDeckEntity>,
    val slideshowCourses: List<SlideshowCourseEntity> = emptyList(),
    val noteBlueprints: List<NoteBlueprintEntity> = emptyList(),
    val prompts: List<PromptEntity> = emptyList(),
    val promptDecks: List<PromptDeckEntity> = emptyList(),
    val sourceDocuments: List<SourceDocumentEntity> = emptyList()
) {
    val questions: List<QuestionEntity>
        get() = quizzes.flatMap { quiz -> questionsByQuiz[quiz.id].orEmpty() }
}

class MksRepository(
    private val workspaceDao: WorkspaceDao,
    private val bookDao: BookDao,
    private val quizDao: QuizDao,
    private val questionDao: QuestionDao,
    private val sessionDao: SessionDao,
    private val categoryMetadataDao: CategoryMetadataDao,
    private val fileManager: FileManager,
    private val exportManager: ExportManager? = null,
    private val importManager: ImportLibraryManager? = null,
    private val flashcardDeckDao: FlashcardDeckDao,
    private val flashcardDao: FlashcardDao,
    private val learningSessionDao: LearningSessionDao,
    private val slideshowCourseDao: SlideshowCourseDao,
    private val courseSlideDao: CourseSlideDao,
    private val noteBlueprintDao: NoteBlueprintDao,
    private val promptDao: PromptDao,
    private val knowledgeStudySessionDao: KnowledgeStudySessionDao,
    private val questionCategoryDao: QuestionCategoryDao,
    private val assetReferenceDao: AssetReferenceDao,
    private val questionAssetDao: QuestionAssetDao,
    private val sourceDocumentDao: SourceDocumentDao,
    private val promptDeckDao: PromptDeckDao,
    private val promptCardDao: PromptCardDao,
    private val promptRunDao: PromptRunDao,
    private val mistakeLogDao: MistakeLogDao,
    private val annotationDao: AnnotationDao,
    private val deletePreviewService: com.ahmedyejam.mks.data.preview.DeletePreviewService? = null,
    private val categoryMergePreviewService: com.ahmedyejam.mks.data.preview.CategoryMergePreviewService? = null,
    private val clearMarksPreviewService: com.ahmedyejam.mks.data.preview.ClearMarksPreviewService? = null,
    private val assetReferenceAuditService: com.ahmedyejam.mks.data.repair.AssetReferenceAuditService? = null
) {

    // Workspaces
    fun getAllWorkspaces(): Flow<List<WorkspaceEntity>> = workspaceDao.getAllWorkspacesFlow()
    suspend fun getDefaultWorkspace(): WorkspaceEntity? = workspaceDao.getDefaultWorkspace()
    suspend fun getOrCreateDefaultWorkspace(): WorkspaceEntity {
        workspaceDao.getWorkspaceByExternalId(WorkspaceDefaults.DEFAULT_EXTERNAL_ID)?.let { return it }
        workspaceDao.getDefaultWorkspace()?.let { existing ->
            val updated = existing.copy(
                externalId = WorkspaceDefaults.DEFAULT_EXTERNAL_ID,
                name = WorkspaceDefaults.DEFAULT_NAME,
                description = WorkspaceDefaults.DEFAULT_DESCRIPTION,
                isDefault = true,
                deletedAt = null,
                updatedAt = System.currentTimeMillis()
            )
            workspaceDao.updateWorkspace(updated)
            ensureWorkspaceSettings(updated.id)
            return updated
        }

        val workspaceId = workspaceDao.insertWorkspace(
            WorkspaceEntity(
                externalId = WorkspaceDefaults.DEFAULT_EXTERNAL_ID,
                name = WorkspaceDefaults.DEFAULT_NAME,
                description = WorkspaceDefaults.DEFAULT_DESCRIPTION,
                isDefault = true
            )
        )
        ensureWorkspaceSettings(workspaceId)
        return workspaceDao.getWorkspaceById(workspaceId)
            ?: WorkspaceEntity(
                id = workspaceId,
                externalId = WorkspaceDefaults.DEFAULT_EXTERNAL_ID,
                name = WorkspaceDefaults.DEFAULT_NAME,
                description = WorkspaceDefaults.DEFAULT_DESCRIPTION,
                isDefault = true
            )
    }

    private suspend fun ensureWorkspaceSettings(workspaceId: Long) {
        if (workspaceDao.getSettingsByWorkspaceId(workspaceId) == null) {
            workspaceDao.insertSettings(WorkspaceSettingsEntity(workspaceId = workspaceId))
        }
    }

    suspend fun getWorkspaceById(id: Long): WorkspaceEntity? = workspaceDao.getWorkspaceById(id)
    suspend fun getWorkspaceByExternalId(externalId: String): WorkspaceEntity? = workspaceDao.getWorkspaceByExternalId(externalId)
    suspend fun insertWorkspace(workspace: WorkspaceEntity): Long = workspaceDao.insertWorkspace(workspace)
    suspend fun updateWorkspace(workspace: WorkspaceEntity) = workspaceDao.updateWorkspace(workspace)
    suspend fun deleteWorkspace(workspace: WorkspaceEntity) = workspaceDao.softDeleteWorkspaceById(workspace.id, System.currentTimeMillis())

    suspend fun restoreWorkspace(workspaceId: Long) = workspaceDao.restoreWorkspaceById(workspaceId, System.currentTimeMillis())

    suspend fun permanentlyDeleteWorkspace(workspace: WorkspaceEntity) = workspaceDao.hardDeleteWorkspace(workspace)

    suspend fun getWorkspaceSettings(workspaceId: Long): WorkspaceSettingsEntity? = workspaceDao.getSettingsByWorkspaceId(workspaceId)
    suspend fun insertWorkspaceSettings(settings: WorkspaceSettingsEntity): Long = workspaceDao.insertSettings(settings)
    suspend fun updateWorkspaceSettings(settings: WorkspaceSettingsEntity) = workspaceDao.updateSettings(settings)

    // Approved Stage 2B/2C: marked/dropped workflows and mistake log.
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

    suspend fun clearMarksForQuizWithPreview(quizId: Long): com.ahmedyejam.mks.data.simulation.ChangeSimulationResult {
        return clearMarksPreviewService?.previewClearMarksForQuiz(quizId)
            ?: com.ahmedyejam.mks.data.simulation.ChangeSimulationResult("Clear Marks", "Preview unavailable")
    }

    suspend fun applyClearMarksForQuiz(quizId: Long) {
        questionDao.clearMarksForQuiz(quizId, System.currentTimeMillis())
    }

    fun getAllMistakes(): Flow<List<MistakeLogEntryEntity>> = mistakeLogDao.getAllMistakes()
    fun getMistakesByBookId(bookId: Long): Flow<List<MistakeLogEntryEntity>> = mistakeLogDao.getMistakesByBookId(bookId)
    fun getMistakesByQuizId(quizId: Long): Flow<List<MistakeLogEntryEntity>> = mistakeLogDao.getMistakesByQuizId(quizId)

    suspend fun insertMistake(entry: MistakeLogEntryEntity): Long = mistakeLogDao.insertMistake(entry)

    // Annotations: polymorphic highlights/margin notes owned by QUESTION, SLIDE, NOTE, SOURCE, or ASSET.
    fun getAnnotationsByWorkspaceId(workspaceId: Long): Flow<List<AnnotationEntity>> =
        annotationDao.getAnnotationsByWorkspaceId(workspaceId)

    fun getAnnotationsByBookId(bookId: Long): Flow<List<AnnotationEntity>> =
        annotationDao.getAnnotationsByBookId(bookId)

    fun getAnnotationsByOwner(ownerType: String, ownerId: Long): Flow<List<AnnotationEntity>> =
        annotationDao.getAnnotationsByOwner(ownerType, ownerId)

    suspend fun getAnnotationById(annotationId: Long): AnnotationEntity? =
        annotationDao.getAnnotationById(annotationId)

    suspend fun insertAnnotation(annotation: AnnotationEntity): Long {
        require(annotation.ownerType in AnnotationOwnerType.all) { "Unsupported annotation ownerType: ${annotation.ownerType}" }
        val now = System.currentTimeMillis()
        return annotationDao.insertAnnotation(annotation.copy(createdAt = annotation.createdAt.takeIf { it > 0 } ?: now, updatedAt = now))
    }

    suspend fun createAnnotationForOwner(
        bookId: Long,
        ownerType: String,
        ownerId: Long,
        selectedText: String? = null,
        noteBody: String? = null,
        colorLabel: String = AnnotationColorLabel.YELLOW,
        positionDataJson: String? = null
    ): Long {
        require(ownerType in AnnotationOwnerType.all) { "Unsupported annotation ownerType: $ownerType" }
        val workspaceId = bookDao.getBookById(bookId)?.workspaceId ?: getOrCreateDefaultWorkspace().id
        val now = System.currentTimeMillis()
        return annotationDao.insertAnnotation(
            AnnotationEntity(
                workspaceId = workspaceId,
                bookId = bookId,
                ownerType = ownerType,
                ownerId = ownerId,
                selectedText = selectedText?.trim()?.takeIf { it.isNotBlank() },
                noteBody = noteBody?.trim()?.takeIf { it.isNotBlank() },
                colorLabel = colorLabel.ifBlank { AnnotationColorLabel.YELLOW },
                positionDataJson = positionDataJson?.trim()?.takeIf { it.isNotBlank() },
                createdAt = now,
                updatedAt = now
            )
        )
    }

    suspend fun updateAnnotation(annotation: AnnotationEntity) {
        require(annotation.ownerType in AnnotationOwnerType.all) { "Unsupported annotation ownerType: ${annotation.ownerType}" }
        annotationDao.updateAnnotation(annotation.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun softDeleteAnnotation(annotationId: Long) =
        annotationDao.softDeleteAnnotationById(annotationId, System.currentTimeMillis())

    suspend fun restoreAnnotation(annotationId: Long) =
        annotationDao.restoreAnnotationById(annotationId, System.currentTimeMillis())

    suspend fun softDeleteAnnotationsByOwner(ownerType: String, ownerId: Long) {
        require(ownerType in AnnotationOwnerType.all) { "Unsupported annotation ownerType: $ownerType" }
        annotationDao.softDeleteAnnotationsByOwner(ownerType, ownerId, System.currentTimeMillis())
    }

    suspend fun restoreAnnotationsByOwner(ownerType: String, ownerId: Long) {
        require(ownerType in AnnotationOwnerType.all) { "Unsupported annotation ownerType: $ownerType" }
        annotationDao.restoreAnnotationsByOwner(ownerType, ownerId, System.currentTimeMillis())
    }

    suspend fun permanentlyDeleteAnnotation(annotationId: Long) =
        annotationDao.permanentlyDeleteAnnotationById(annotationId)

    suspend fun permanentlyDeleteAnnotationsByOwner(ownerType: String, ownerId: Long) {
        require(ownerType in AnnotationOwnerType.all) { "Unsupported annotation ownerType: $ownerType" }
        annotationDao.permanentlyDeleteAnnotationsByOwner(ownerType, ownerId)
    }

    suspend fun cleanupDeletedAnnotationsOlderThan(olderThan: Long): Int =
        annotationDao.cleanupDeletedAnnotationsOlderThan(olderThan)

    suspend fun autoLogWrongAnswer(
        bookId: Long,
        quizId: Long,
        questionId: Long,
        sessionId: Long?,
        selectedAnswer: String?,
        correctAnswer: String?
    ): Long {
        val existing = mistakeLogDao.findByQuestionAndSession(questionId, sessionId)
        if (existing != null) return existing.id

        val now = System.currentTimeMillis()
        return mistakeLogDao.insertMistake(
            MistakeLogEntryEntity(
                bookId = bookId,
                quizId = quizId,
                questionId = questionId,
                sessionId = sessionId,
                selectedAnswer = selectedAnswer,
                correctAnswer = correctAnswer,
                reviewAt = now + 24L * 60L * 60L * 1000L,
                createdAt = now,
                updatedAt = now
            )
        )
    }

    suspend fun markMistakeFixed(mistakeId: Long) {
        mistakeLogDao.markFixed(mistakeId, System.currentTimeMillis())
    }

    suspend fun snoozeMistake(mistakeId: Long, reviewAt: Long) {
        mistakeLogDao.snooze(mistakeId, reviewAt, System.currentTimeMillis())
    }

    suspend fun deleteMistake(entry: MistakeLogEntryEntity) {
        mistakeLogDao.softDeleteMistakeById(entry.id, System.currentTimeMillis())
    }

    suspend fun restoreMistake(mistakeId: Long) {
        mistakeLogDao.restoreMistakeById(mistakeId, System.currentTimeMillis())
    }

    suspend fun permanentlyDeleteMistake(entry: MistakeLogEntryEntity) {
        mistakeLogDao.hardDeleteMistake(entry)
    }


    suspend fun previewBookDeletion(bookId: Long) = deletePreviewService?.previewBookDeletion(bookId)
    suspend fun previewQuizDeletion(quizId: Long) = deletePreviewService?.previewQuizDeletion(quizId)
    suspend fun previewCategoryMerge(source: String, target: String) = categoryMergePreviewService?.previewMerge(source, target)

    suspend fun auditAssetReferences() = assetReferenceAuditService?.audit()

    private fun isTrackableLocalAsset(path: String?): Boolean {
        val value = path?.trim().orEmpty()
        if (value.isBlank()) return false
        return !value.startsWith("http://", ignoreCase = true) &&
            !value.startsWith("https://", ignoreCase = true) &&
            !value.startsWith("content://", ignoreCase = true) &&
            !value.startsWith("data:", ignoreCase = true) &&
            !value.startsWith("assets/", ignoreCase = true)
    }

    private suspend fun registerAssetReference(ownerType: String, ownerId: Long, path: String?) {
        val assetPath = path?.trim()?.takeIf { isTrackableLocalAsset(it) } ?: return
        assetReferenceDao.insertReference(
            AssetReferenceEntity(
                path = assetPath,
                ownerType = ownerType,
                ownerId = ownerId
            )
        )
    }

    private suspend fun replaceOwnerAssetReferences(ownerType: String, ownerId: Long, paths: List<String?>) {
        val cleaned = paths
            .mapNotNull { it?.trim()?.takeIf { value -> isTrackableLocalAsset(value) } }
            .distinct()
        val previousPaths = assetReferenceDao.getReferencesForOwner(ownerType, ownerId)
            .map { it.path }
            .distinct()
        assetReferenceDao.replaceOwnerReferences(ownerType, ownerId, cleaned)
        previousPaths
            .filterNot { it in cleaned }
            .forEach { path ->
                if (assetReferenceDao.countReferencesForPath(path) == 0) {
                    fileManager.deleteImage(path)
                }
            }
    }

    private suspend fun releaseAssetReference(ownerType: String, ownerId: Long, path: String?) {
        val assetPath = path?.trim()?.takeIf { isTrackableLocalAsset(it) } ?: return
        assetReferenceDao.deleteReference(ownerType, ownerId, assetPath)
        if (assetReferenceDao.countReferencesForPath(assetPath) == 0) {
            fileManager.deleteImage(assetPath)
        }
    }

    private suspend fun releaseOwnerAssets(ownerType: String, ownerId: Long) {
        val references = assetReferenceDao.getReferencesForOwner(ownerType, ownerId)
        assetReferenceDao.deleteReferencesForOwner(ownerType, ownerId)
        references.map { it.path }.distinct().forEach { path ->
            if (assetReferenceDao.countReferencesForPath(path) == 0) {
                fileManager.deleteImage(path)
            }
        }
    }

    private suspend fun softDeleteOwnerAnnotations(ownerType: String, ownerId: Long, deletedAt: Long) {
        if (ownerType in AnnotationOwnerType.all) {
            annotationDao.softDeleteAnnotationsByOwner(ownerType, ownerId, deletedAt)
        }
    }

    private suspend fun restoreOwnerAnnotations(ownerType: String, ownerId: Long, updatedAt: Long) {
        if (ownerType in AnnotationOwnerType.all) {
            annotationDao.restoreAnnotationsByOwner(ownerType, ownerId, updatedAt)
        }
    }

    private suspend fun permanentlyDeleteOwnerAnnotations(ownerType: String, ownerId: Long) {
        if (ownerType in AnnotationOwnerType.all) {
            annotationDao.permanentlyDeleteAnnotationsByOwner(ownerType, ownerId)
        }
    }

    private suspend fun softDeleteQuestionAnnotationTree(questionId: Long, deletedAt: Long) {
        questionAssetDao.getAssetsByQuestionIdNow(questionId).forEach { asset ->
            softDeleteOwnerAnnotations(AnnotationOwnerType.ASSET, asset.id, deletedAt)
        }
        softDeleteOwnerAnnotations(AnnotationOwnerType.QUESTION, questionId, deletedAt)
    }

    private suspend fun permanentlyDeleteQuestionAnnotationTree(questionId: Long) {
        questionAssetDao.getAssetsByQuestionIdIncludingDeleted(questionId).forEach { asset ->
            permanentlyDeleteOwnerAnnotations(AnnotationOwnerType.ASSET, asset.id)
        }
        permanentlyDeleteOwnerAnnotations(AnnotationOwnerType.QUESTION, questionId)
    }

    private suspend fun softDeleteQuizAnnotationTree(quizId: Long, deletedAt: Long) {
        questionDao.getQuestionsByQuizIdNow(quizId).forEach { question ->
            softDeleteQuestionAnnotationTree(question.id, deletedAt)
        }
    }

    private suspend fun permanentlyDeleteQuizAnnotationTree(quizId: Long) {
        questionDao.getQuestionsByQuizIdIncludingDeleted(quizId).forEach { question ->
            permanentlyDeleteQuestionAnnotationTree(question.id)
        }
    }

    private suspend fun softDeleteSlideshowAnnotationTree(courseId: Long, deletedAt: Long) {
        courseSlideDao.getSlidesByCourseIdNow(courseId).forEach { slide ->
            softDeleteOwnerAnnotations(AnnotationOwnerType.SLIDE, slide.id, deletedAt)
        }
    }

    private suspend fun restoreSlideshowAnnotationTree(courseId: Long, updatedAt: Long) {
        courseSlideDao.getSlidesByCourseIdNow(courseId).forEach { slide ->
            restoreOwnerAnnotations(AnnotationOwnerType.SLIDE, slide.id, updatedAt)
        }
    }

    private suspend fun permanentlyDeleteSlideshowAnnotationTree(courseId: Long) {
        courseSlideDao.getSlidesByCourseIdIncludingDeleted(courseId).forEach { slide ->
            permanentlyDeleteOwnerAnnotations(AnnotationOwnerType.SLIDE, slide.id)
        }
    }

    private suspend fun syncQuestionCategories(questionId: Long, categories: List<String>) {
        questionCategoryDao.replaceCategories(questionId, categories)
    }

    private suspend fun releaseQuestionAssets(question: QuestionEntity) {
        questionAssetDao.getAssetsByQuestionIdIncludingDeleted(question.id).forEach { asset ->
            releaseOwnerAssets("question_asset", asset.id)
        }
        releaseOwnerAssets("question", question.id)
    }

    private suspend fun releaseQuizTreeAssets(quiz: QuizEntity) {
        releaseOwnerAssets("quiz", quiz.id)
        questionDao.getQuestionsByQuizIdIncludingDeleted(quiz.id).forEach { releaseQuestionAssets(it) }
    }

    private suspend fun releaseBookTreeAssets(book: BookEntity) {
        releaseOwnerAssets("book", book.id)
        quizDao.getQuizzesByBookId(book.id).first().forEach { releaseQuizTreeAssets(it) }
        flashcardDeckDao.getFlashcardDecksByBookId(book.id).first().forEach { deck ->
            releaseOwnerAssets("flashcard_deck", deck.id)
            flashcardDao.getFlashcardsByDeckId(deck.id).first().forEach { card ->
                releaseOwnerAssets("flashcard", card.id)
            }
        }
        slideshowCourseDao.getCoursesByBookId(book.id).first().forEach { course ->
            releaseOwnerAssets("slideshow_course", course.id)
            courseSlideDao.getSlidesByCourseId(course.id).first().forEach { slide ->
                releaseOwnerAssets("course_slide", slide.id)
            }
        }
        sourceDocumentDao.getSourcesByBookIdNow(book.id).forEach { source ->
            releaseOwnerAssets("source_document", source.id)
        }
    }

    suspend fun rebuildDerivedIndexes() {
        questionCategoryDao.clearAllCategories()
        questionDao.getAllQuestionsFlow().first().forEach { question ->
            syncQuestionCategories(question.id, question.categories)
        }

        assetReferenceDao.clearAllReferences()
        bookDao.getAllBooksFlow().first().forEach { book ->
            replaceOwnerAssetReferences("book", book.id, listOf(book.coverImage))
            quizDao.getQuizzesByBookId(book.id).first().forEach { quiz ->
                replaceOwnerAssetReferences("quiz", quiz.id, listOf(quiz.coverImage))
                questionDao.getQuestionsByQuizId(quiz.id).first().forEach { question ->
                    replaceOwnerAssetReferences("question", question.id, listOf(question.imagePath))
                    questionAssetDao.getAssetsByQuestionIdNow(question.id).forEach { asset ->
                        replaceOwnerAssetReferences("question_asset", asset.id, listOf(asset.localPath))
                    }
                }
            }
            flashcardDeckDao.getFlashcardDecksByBookId(book.id).first().forEach { deck ->
                replaceOwnerAssetReferences("flashcard_deck", deck.id, listOf(deck.coverImage))
                flashcardDao.getFlashcardsByDeckId(deck.id).first().forEach { card ->
                    replaceOwnerAssetReferences("flashcard", card.id, listOf(card.imagePath))
                }
            }
            slideshowCourseDao.getCoursesByBookId(book.id).first().forEach { course ->
                replaceOwnerAssetReferences("slideshow_course", course.id, listOf(course.coverImage))
                courseSlideDao.getSlidesByCourseId(course.id).first().forEach { slide ->
                    replaceOwnerAssetReferences("course_slide", slide.id, listOf(slide.imagePath))
                }
            }
            sourceDocumentDao.getSourcesByBookIdNow(book.id).forEach { source ->
                replaceOwnerAssetReferences("source_document", source.id, listOf(source.localPath))
            }
        }
    }

    // Books
    fun getAllBooks(sortBy: SortOption = SortOption.TITLE): Flow<List<BookEntity>> = bookDao.getAllBooksSortedFlow(sortBy.name)

    fun getBooksByWorkspace(workspaceId: Long, sortBy: SortOption = SortOption.TITLE): Flow<List<BookEntity>> =
        bookDao.getBooksByWorkspaceSortedFlow(workspaceId, sortBy.name)

    fun getAllFields(): Flow<List<String>> = bookDao.getAllBooksFlow().map { books ->
        books.asSequence().flatMap { it.fields }.distinct().sorted().toList()
    }

    suspend fun getBookById(id: Long) = bookDao.getBookById(id)
    suspend fun getBookStudyBundle(bookId: Long): BookStudyBundle? {
        val book = bookDao.getBookById(bookId) ?: return null
        val quizzes = quizDao.getQuizzesByBookId(bookId).first()
        val questionsByQuiz = quizzes.associate { quiz ->
            quiz.id to questionDao.getQuestionsByQuizId(quiz.id).first().sortedBy { it.id }
        }
        val flashcardDecks = flashcardDeckDao.getFlashcardDecksByBookId(bookId).first()
        val slideshowCourses = slideshowCourseDao.getCoursesByBookId(bookId).first()
        val noteBlueprints = noteBlueprintDao.getNotesByBookId(bookId).first()
        val prompts = promptDao.getPromptsByBookId(bookId).first()
        val promptDecks = promptDeckDao.getDecksByBookId(bookId).first()
        val sourceDocuments = sourceDocumentDao.getSourcesByBookId(bookId).first()

        return BookStudyBundle(
            book = book,
            quizzes = quizzes,
            questionsByQuiz = questionsByQuiz,
            flashcardDecks = flashcardDecks,
            slideshowCourses = slideshowCourses,
            noteBlueprints = noteBlueprints,
            prompts = prompts,
            promptDecks = promptDecks,
            sourceDocuments = sourceDocuments
        )
    }
    suspend fun insertBook(book: BookEntity): Long {
        val workspaceId = if (book.workspaceId == 0L) {
            getOrCreateDefaultWorkspace().id
        } else {
            book.workspaceId
        }
        
        val finalBook = if (book.coverImage?.startsWith("http", ignoreCase = true) == true) {
            val localPath = fileManager.downloadAndSaveImage(book.coverImage)
            if (localPath != null) book.copy(workspaceId = workspaceId, coverImage = localPath) else book.copy(workspaceId = workspaceId)
        } else {
            book.copy(workspaceId = workspaceId)
        }
        val id = bookDao.insertBook(finalBook)
        replaceOwnerAssetReferences("book", id, listOf(finalBook.coverImage))
        return id
    }
    suspend fun updateBook(book: BookEntity) {
        val updated = book.copy(updatedAt = System.currentTimeMillis(), lastEditedAt = System.currentTimeMillis())
        bookDao.updateBook(updated)
        replaceOwnerAssetReferences("book", updated.id, listOf(updated.coverImage))
    }
    suspend fun deleteBook(book: BookEntity): BookEntity {
        if (book.isSystem) throw IllegalStateException("Cannot delete system book")
        val now = System.currentTimeMillis()
        annotationDao.softDeleteAnnotationsByBookId(book.id, now)
        bookDao.softDeleteBookById(book.id, now)
        return book.copy(deletedAt = now, updatedAt = now)
    }

    suspend fun restoreBook(bookId: Long) {
        val now = System.currentTimeMillis()
        bookDao.restoreBookById(bookId, now)
        annotationDao.restoreAnnotationsByBookId(bookId, now)
    }

    suspend fun permanentlyDeleteBook(book: BookEntity) {
        if (book.isSystem) throw IllegalStateException("Cannot delete system book")
        annotationDao.permanentlyDeleteAnnotationsByBookId(book.id)
        releaseBookTreeAssets(book)
        bookDao.hardDeleteBook(book)
    }

    // Quizzes
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
            val localPath = fileManager.downloadAndSaveImage(quiz.coverImage)
            if (localPath != null) quiz.copy(coverImage = localPath) else quiz
        } else {
            quiz
        }
        
        val saved = finalQuiz.copy(
            createdAt = if (finalQuiz.createdAt == 0L) System.currentTimeMillis() else finalQuiz.createdAt,
            updatedAt = System.currentTimeMillis(),
        )
        val id = quizDao.insertQuiz(saved)
        replaceOwnerAssetReferences("quiz", id, listOf(saved.coverImage))
        refreshBookStats(finalQuiz.bookId)
        return id
    }
    suspend fun updateQuiz(quiz: QuizEntity) {
        val updated = quiz.copy(updatedAt = System.currentTimeMillis(), lastEditedAt = System.currentTimeMillis())
        quizDao.updateQuiz(updated)
        replaceOwnerAssetReferences("quiz", updated.id, listOf(updated.coverImage))
        refreshBookStats(quiz.bookId)
    }

    // Questions
    fun getQuestionsByQuizId(quizId: Long): Flow<List<QuestionEntity>> = questionDao.getQuestionsByQuizId(quizId)
    fun searchAllQuestions(query: String): Flow<List<QuestionEntity>> = questionDao.searchAllQuestionsFlow(query)
    fun getQuestionsByCategoryFlow(category: String): Flow<List<QuestionEntity>> = questionCategoryDao.getQuestionsByCategoryFlow(category)
    suspend fun getQuestionsByCategory(category: String): List<QuestionEntity> = questionCategoryDao.getQuestionsByCategory(category)
    suspend fun getQuestionById(id: Long) = questionDao.getQuestionById(id)
    suspend fun getQuestionsByIds(ids: List<Long>) = questionDao.getQuestionsByIds(ids)
    suspend fun insertQuestion(question: QuestionEntity): Long {
        val finalQuestion = if (question.imagePath?.startsWith("http", ignoreCase = true) == true) {
            val localPath = fileManager.downloadAndSaveImage(question.imagePath)
            if (localPath != null) question.copy(imagePath = localPath) else question
        } else {
            question
        }
        
        val saved = finalQuestion.copy(
            createdAt = if (finalQuestion.createdAt == 0L) System.currentTimeMillis() else finalQuestion.createdAt,
            updatedAt = System.currentTimeMillis(),
        )
        val id = questionDao.insertQuestion(saved)
        syncQuestionCategories(id, saved.categories)
        replaceOwnerAssetReferences("question", id, listOf(saved.imagePath))
        refreshQuizStats(finalQuestion.quizId)
        return id
    }
    suspend fun insertQuestions(questions: List<QuestionEntity>): List<Long> {
        val now = System.currentTimeMillis()
        val updatedQuestions = questions.map { question ->
            val finalQuestion = if (question.imagePath?.startsWith("http", ignoreCase = true) == true) {
                val localPath = fileManager.downloadAndSaveImage(question.imagePath)
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
            syncQuestionCategories(id, question.categories)
            replaceOwnerAssetReferences("question", id, listOf(question.imagePath))
        }
        questions.asSequence().map { it.quizId }.distinct().forEach { refreshQuizStats(it) }
        return ids
    }

    private suspend fun updateQuestionsInPlace(questions: List<QuestionEntity>) {
        if (questions.isEmpty()) return
        val now = System.currentTimeMillis()
        val affectedQuizIds = questions.map { it.quizId }.distinct()
        questions.forEach { question ->
            val updated = question.copy(updatedAt = now, lastEditedAt = now)
            questionDao.updateQuestion(updated)
            syncQuestionCategories(updated.id, updated.categories)
        }
        affectedQuizIds.forEach { refreshQuizStats(it) }
    }
    suspend fun updateQuestion(question: QuestionEntity) {
        val updated = question.copy(updatedAt = System.currentTimeMillis(), lastEditedAt = System.currentTimeMillis())
        questionDao.updateQuestion(updated)
        syncQuestionCategories(updated.id, updated.categories)
        replaceOwnerAssetReferences("question", updated.id, listOf(updated.imagePath))
        refreshQuizStats(question.quizId)
    }
    suspend fun deleteQuestion(question: QuestionEntity) {
        val now = System.currentTimeMillis()
        softDeleteQuestionAnnotationTree(question.id, now)
        questionAssetDao.softDeleteAssetsForQuestion(question.id, now)
        questionDao.softDeleteQuestionById(question.id, now)
        refreshQuizStats(question.quizId)
    }

    suspend fun restoreQuestion(questionId: Long) {
        val now = System.currentTimeMillis()
        questionDao.restoreQuestionById(questionId, now)
        restoreOwnerAnnotations(AnnotationOwnerType.QUESTION, questionId, now)
    }

    suspend fun permanentlyDeleteQuestion(question: QuestionEntity) {
        permanentlyDeleteQuestionAnnotationTree(question.id)
        releaseQuestionAssets(question)
        questionCategoryDao.deleteCategoriesForQuestion(question.id)
        questionDao.hardDeleteQuestion(question)
        refreshQuizStats(question.quizId)
    }
    suspend fun updateQuestionMetrics(id: Long, isCorrect: Boolean, timeSpentMs: Long = 0) {
        val question = questionDao.getQuestionById(id) ?: return
        val now = System.currentTimeMillis()
        
        questionDao.updatePerformanceMetrics(
            id = id,
            isCorrect = isCorrect,
            isCorrectInt = if (isCorrect) 1 else 0,
            timeSpentMs = timeSpentMs,
            now = now
        )

        refreshQuizStats(question.quizId)
    }

    // Question assets / media attachments
    fun getQuestionAssets(questionId: Long): Flow<List<QuestionAssetEntity>> =
        questionAssetDao.getAssetsByQuestionId(questionId)

    fun getQuestionIdsWithAssetsFlow(): Flow<List<Long>> = questionAssetDao.getQuestionIdsWithAssetsFlow()

    fun getQuestionIdsWithAssetsForQuizFlow(quizId: Long): Flow<List<Long>> =
        questionAssetDao.getQuestionIdsWithAssetsForQuizFlow(quizId)

    suspend fun getQuestionAssetsNow(questionId: Long): List<QuestionAssetEntity> =
        questionAssetDao.getAssetsByQuestionIdNow(questionId)

    suspend fun insertQuestionAsset(asset: QuestionAssetEntity): Long {
        val prepared = prepareQuestionAsset(asset).copy(
            createdAt = if (asset.createdAt == 0L) System.currentTimeMillis() else asset.createdAt,
            updatedAt = System.currentTimeMillis()
        )
        val id = questionAssetDao.insertAsset(prepared)
        if (prepared.isPrimary) {
            questionAssetDao.clearOtherPrimaryAssets(prepared.questionId, id, System.currentTimeMillis())
        }
        replaceOwnerAssetReferences("question_asset", id, listOf(prepared.localPath))
        return id
    }

    suspend fun updateQuestionAsset(asset: QuestionAssetEntity) {
        val old = questionAssetDao.getAssetById(asset.id)
        val prepared = prepareQuestionAsset(asset).copy(updatedAt = System.currentTimeMillis())
        questionAssetDao.updateAsset(prepared)
        if (prepared.isPrimary) {
            questionAssetDao.clearOtherPrimaryAssets(prepared.questionId, prepared.id, System.currentTimeMillis())
        }
        if (old?.localPath != prepared.localPath) {
            old?.localPath?.let { releaseAssetReference("question_asset", old.id, it) }
        }
        replaceOwnerAssetReferences("question_asset", prepared.id, listOf(prepared.localPath))
    }

    suspend fun deleteQuestionAsset(asset: QuestionAssetEntity) {
        val now = System.currentTimeMillis()
        softDeleteOwnerAnnotations(AnnotationOwnerType.ASSET, asset.id, now)
        questionAssetDao.softDeleteAssetById(asset.id, now)
    }

    suspend fun restoreQuestionAsset(assetId: Long) {
        val now = System.currentTimeMillis()
        questionAssetDao.restoreAssetById(assetId, now)
        restoreOwnerAnnotations(AnnotationOwnerType.ASSET, assetId, now)
    }

    suspend fun permanentlyDeleteQuestionAsset(asset: QuestionAssetEntity) {
        permanentlyDeleteOwnerAnnotations(AnnotationOwnerType.ASSET, asset.id)
        releaseOwnerAssets("question_asset", asset.id)
        questionAssetDao.hardDeleteAsset(asset)
    }

    suspend fun reorderQuestionAssets(assets: List<QuestionAssetEntity>) {
        val now = System.currentTimeMillis()
        assets.forEachIndexed { index, asset ->
            questionAssetDao.updateAssetOrder(asset.id, index, now)
        }
    }

    private fun prepareQuestionAsset(asset: QuestionAssetEntity): QuestionAssetEntity {
        val trimmedLocal = asset.localPath?.trim()?.takeIf { it.isNotBlank() }
        val trimmedUrl = asset.externalUrl?.trim()?.takeIf { it.isNotBlank() }
        val now = System.currentTimeMillis()

        if (trimmedLocal != null && trimmedLocal.startsWith("http", ignoreCase = true)) {
            return asset.copy(
                localPath = null,
                externalUrl = trimmedUrl ?: trimmedLocal,
                updatedAt = now
            )
        }

        if (trimmedLocal != null && (trimmedLocal.startsWith("content://") || trimmedLocal.startsWith("file://"))) {
            val copied = fileManager.copyAssetUriToInternalStorage(trimmedLocal, asset.fileName)
            copied.path?.let { copiedPath ->
                return asset.copy(
                    localPath = copiedPath,
                    mimeType = asset.mimeType ?: copied.mimeType,
                    fileName = asset.fileName ?: copied.fileName,
                    fileSizeBytes = asset.fileSizeBytes ?: copied.fileSizeBytes,
                    updatedAt = now
                )
            }
        }

        return asset.copy(localPath = trimmedLocal, externalUrl = trimmedUrl, updatedAt = now)
    }



    // Source documents / citations
    fun getSourceDocumentsByBookId(bookId: Long): Flow<List<SourceDocumentEntity>> =
        sourceDocumentDao.getSourcesByBookId(bookId)

    suspend fun getSourceDocumentsByBookIdNow(bookId: Long): List<SourceDocumentEntity> =
        sourceDocumentDao.getSourcesByBookIdNow(bookId)

    suspend fun getSourceDocumentById(id: Long): SourceDocumentEntity? = sourceDocumentDao.getSourceById(id)

    suspend fun insertSourceDocument(source: SourceDocumentEntity): Long {
        val now = System.currentTimeMillis()
        val prepared = prepareSourceDocument(source).copy(
            createdAt = if (source.createdAt == 0L) now else source.createdAt,
            updatedAt = now
        )
        val id = sourceDocumentDao.insertSource(prepared)
        replaceOwnerAssetReferences("source_document", id, listOf(prepared.localPath))
        return id
    }

    suspend fun updateSourceDocument(source: SourceDocumentEntity) {
        val old = sourceDocumentDao.getSourceById(source.id)
        val prepared = prepareSourceDocument(source).copy(updatedAt = System.currentTimeMillis())
        sourceDocumentDao.updateSource(prepared)
        if (old?.localPath != prepared.localPath) {
            old?.localPath?.let { releaseAssetReference("source_document", old.id, it) }
        }
        replaceOwnerAssetReferences("source_document", prepared.id, listOf(prepared.localPath))
    }

    suspend fun deleteSourceDocument(source: SourceDocumentEntity) {
        val now = System.currentTimeMillis()
        softDeleteOwnerAnnotations(AnnotationOwnerType.SOURCE, source.id, now)
        sourceDocumentDao.softDeleteSourceById(source.id, now)
    }

    suspend fun restoreSourceDocument(sourceId: Long) {
        val now = System.currentTimeMillis()
        sourceDocumentDao.restoreSourceById(sourceId, now)
        restoreOwnerAnnotations(AnnotationOwnerType.SOURCE, sourceId, now)
    }

    suspend fun permanentlyDeleteSourceDocument(source: SourceDocumentEntity) {
        permanentlyDeleteOwnerAnnotations(AnnotationOwnerType.SOURCE, source.id)
        questionAssetDao.clearSourceReference(source.id, System.currentTimeMillis())
        releaseOwnerAssets("source_document", source.id)
        sourceDocumentDao.hardDeleteSource(source)
    }

    suspend fun createSourceDocumentAndQuestionAsset(source: SourceDocumentEntity, asset: QuestionAssetEntity): Long {
        val sourceId = insertSourceDocument(source)
        return insertQuestionAsset(asset.copy(sourceDocumentId = sourceId, assetType = QuestionAssetType.SOURCE_REFERENCE))
    }

    private fun prepareSourceDocument(source: SourceDocumentEntity): SourceDocumentEntity {
        val trimmedLocal = source.localPath?.trim()?.takeIf { it.isNotBlank() }
        val trimmedUrl = source.externalUrl?.trim()?.takeIf { it.isNotBlank() }
        val now = System.currentTimeMillis()
        if (trimmedLocal != null && trimmedLocal.startsWith("http", ignoreCase = true)) {
            return source.copy(localPath = null, externalUrl = trimmedUrl ?: trimmedLocal, updatedAt = now)
        }
        if (trimmedLocal != null && (trimmedLocal.startsWith("content://") || trimmedLocal.startsWith("file://"))) {
            val copied = fileManager.copyAssetUriToInternalStorage(trimmedLocal, source.title)
            copied.path?.let { copiedPath ->
                return source.copy(localPath = copiedPath, updatedAt = now)
            }
        }
        return source.copy(localPath = trimmedLocal, externalUrl = trimmedUrl, updatedAt = now)
    }

    fun getLinkedBlueprintsForQuestion(bookId: Long, questionId: Long): Flow<List<NoteBlueprintEntity>> =
        noteBlueprintDao.getNotesByLinkedQuestion(bookId, questionId)

    suspend fun createBlueprintFromQuestion(
        bookId: Long,
        questionId: Long,
        mode: String = BlueprintMode.CONCEPT_TEMPLATE
    ): Long {
        val question = questionDao.getQuestionById(questionId)
            ?: throw IllegalArgumentException("Question not found for blueprint creation.")
        val body = blueprintBodyFromQuestion(question, mode)
        return insertNoteBlueprint(
            NoteBlueprintEntity(
                externalId = java.util.UUID.randomUUID().toString(),
                bookId = bookId,
                title = question.text.take(80).ifBlank { "Question blueprint" },
                summary = question.explanation?.take(180),
                body = body,
                bulletPoints = question.options,
                tags = question.categories,
                blueprintMode = mode,
                linkedQuestionsJson = encodeLongList(listOf(question.id)),
                linkedAssetsJson = "[]",
                reviewStatus = BlueprintReviewStatus.NEW,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                sourceQuestionId = question.id
            )
        )
    }

    suspend fun createBlueprintFromQuestions(
        bookId: Long,
        title: String,
        questionIds: List<Long>,
        mode: String = BlueprintMode.MISTAKE_REVIEW
    ): Long {
        val questions = questionDao.getQuestionsByIds(questionIds).sortedBy { questionIds.indexOf(it.id) }
        if (questions.isEmpty()) throw IllegalArgumentException("No questions selected for blueprint creation.")
        val body = questions.joinToString(separator = "\n\n---\n\n") { question -> blueprintBodyFromQuestion(question, mode) }
        return insertNoteBlueprint(
            NoteBlueprintEntity(
                externalId = java.util.UUID.randomUUID().toString(),
                bookId = bookId,
                title = title.ifBlank { "Question group blueprint" },
                summary = "Generated from ${questions.size} question(s).",
                body = body,
                bulletPoints = questions.map { it.text.take(160) },
                tags = questions.flatMap { it.categories }.distinct(),
                blueprintMode = mode,
                linkedQuestionsJson = encodeLongList(questions.map { it.id }),
                linkedAssetsJson = "[]",
                reviewStatus = BlueprintReviewStatus.NEW,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                sourceQuestionId = questions.firstOrNull()?.id
            )
        )
    }

    suspend fun addArticlesFromQuestions(
        bookId: Long,
        questionIds: List<Long>,
        config: com.ahmedyejam.mks.data.model.ArticleGenerationConfig
    ): List<Long> {
        val questions = questionDao.getQuestionsByIds(questionIds).sortedBy { questionIds.indexOf(it.id) }
        val ids = mutableListOf<Long>()
        val now = System.currentTimeMillis()
        
        for (q in questions) {
            val title = if (config.includeStemAsTitle) q.text.take(80) else "Generated Article"
            val bodyBuilder = StringBuilder()
            if (config.includeExplanationInBody && !q.explanation.isNullOrBlank()) {
                bodyBuilder.append(q.explanation).append("\n\n")
            }
            if (config.includeHintInBody && !q.hint.isNullOrBlank()) {
                bodyBuilder.append("Hint: ${q.hint}\n\n")
            }
            if (config.includeReferenceInBody && !q.reference.isNullOrBlank()) {
                bodyBuilder.append("Reference: ${q.reference}\n\n")
            }
            val body = bodyBuilder.toString().trim()
            
            val bulletPoints = if (config.includeOptionsAsBulletPoints) q.options else emptyList()
            val tags = if (config.includeTags) q.categories else emptyList()
            
            val id = insertNoteBlueprint(
                NoteBlueprintEntity(
                    externalId = java.util.UUID.randomUUID().toString(),
                    bookId = bookId,
                    title = title,
                    summary = q.text.take(180),
                    body = body,
                    bulletPoints = bulletPoints,
                    tags = tags,
                    blueprintMode = config.articleMode,
                    linkedQuestionsJson = encodeLongList(listOf(q.id)),
                    linkedAssetsJson = "[]",
                    reviewStatus = BlueprintReviewStatus.NEW,
                    createdAt = now,
                    updatedAt = now,
                    sourceQuestionId = q.id
                )
            )
            ids.add(id)
        }
        return ids
    }

    suspend fun createBlueprintFromMarkedQuestions(bookId: Long, title: String): Long {
        val questions = getBookStudyBundle(bookId)?.questions.orEmpty().filter { it.isMarked }
        return createBlueprintFromQuestions(bookId, title, questions.map { it.id }, BlueprintMode.MISTAKE_REVIEW)
    }

    suspend fun createBlueprintFromMissedQuestions(bookId: Long, title: String): Long {
        val questions = getBookStudyBundle(bookId)?.questions.orEmpty().filter { question ->
            question.attempts > 0 && (question.correctCount < question.attempts || question.lastAttemptResult == false)
        }
        return createBlueprintFromQuestions(bookId, title, questions.map { it.id }, BlueprintMode.MISTAKE_REVIEW)
    }

    suspend fun createFlashcardDeckFromBlueprint(noteId: Long): Long {
        val note = noteBlueprintDao.getNoteById(noteId) ?: throw IllegalArgumentException("Blueprint not found.")
        val deckId = insertFlashcardDeck(
            FlashcardDeckEntity(
                externalId = java.util.UUID.randomUUID().toString(),
                bookId = note.bookId,
                title = "${note.title} Flashcards",
                description = "Generated from blueprint: ${note.title}"
            )
        )
        val cards = buildFlashcardsFromBlueprint(deckId, note)
        if (cards.isNotEmpty()) insertFlashcards(cards)
        return deckId
    }

    suspend fun appendBlueprintToSourceQuestionNote(noteId: Long) {
        val note = noteBlueprintDao.getNoteById(noteId) ?: return
        val questionId = note.sourceQuestionId ?: parseLongList(note.linkedQuestionsJson).firstOrNull() ?: return
        val question = questionDao.getQuestionById(questionId) ?: return
        val appended = buildString {
            question.notes?.takeIf { it.isNotBlank() }?.let {
                append(it.trim())
                append("\n\n")
            }
            append("Blueprint: ")
            append(note.title)
            append("\n")
            append(note.body.trim())
        }
        updateQuestion(question.copy(notes = appended))
    }

    private fun blueprintBodyFromQuestion(question: QuestionEntity, mode: String): String {
        val correctAnswer = question.correctAnswers.mapNotNull(question.options::getOrNull).joinToString("; ")
        val heading = when (mode) {
            BlueprintMode.DISEASE_TEMPLATE -> "Disease / condition blueprint"
            BlueprintMode.DRUG_TEMPLATE -> "Drug blueprint"
            BlueprintMode.MISTAKE_REVIEW -> "Mistake-review blueprint"
            else -> "Concept blueprint"
        }
        return buildString {
            append(heading)
            append("\n\nQuestion\n")
            append(question.text)
            append("\n\nCorrect answer\n")
            append(correctAnswer.ifBlank { "Add the correct answer or concept here." })
            question.explanation?.takeIf { it.isNotBlank() }?.let {
                append("\n\nExplanation\n")
                append(it)
            }
            question.hint?.takeIf { it.isNotBlank() }?.let {
                append("\n\nHint\n")
                append(it)
            }
            question.reference?.takeIf { it.isNotBlank() }?.let {
                append("\n\nReference\n")
                append(it)
            }
        }
    }

    private fun buildFlashcardsFromBlueprint(deckId: Long, note: NoteBlueprintEntity): List<FlashcardEntity> {
        val bullets = note.bulletPoints.filter { it.isNotBlank() }
        val cards = mutableListOf<FlashcardEntity>()
        cards += FlashcardEntity(
            externalId = java.util.UUID.randomUUID().toString(),
            deckId = deckId,
            frontText = note.title,
            backText = note.body,
            hint = note.summary,
            tags = note.tags + note.blueprintMode,
            orderIndex = 0,
            sourceQuestionId = note.sourceQuestionId
        )
        bullets.forEachIndexed { index, point ->
            cards += FlashcardEntity(
                externalId = java.util.UUID.randomUUID().toString(),
                deckId = deckId,
                frontText = "${note.title}: key point ${index + 1}",
                backText = point,
                tags = note.tags + note.blueprintMode,
                orderIndex = index + 1,
                sourceQuestionId = note.sourceQuestionId
            )
        }
        return cards
    }

    private fun encodeLongList(values: List<Long>): String = values.distinct().joinToString(prefix = "[", postfix = "]")

    private fun parseLongList(json: String): List<Long> =
        Regex("\\d+").findAll(json).mapNotNull { it.value.toLongOrNull() }.toList()

    suspend fun getAdaptiveQuestionsByBook(bookId: Long, limit: Int) = questionDao.getAdaptiveQuestionsByBook(bookId, limit)

    // Sessions
    fun getSessionsByQuizId(quizId: Long): Flow<List<SessionEntity>> = sessionDao.getSessionsByQuizId(quizId)
    suspend fun getSessionById(id: Long) = sessionDao.getSessionById(id)
    suspend fun insertSession(session: SessionEntity): Long {
        val id = sessionDao.insertSession(session.copy(
            createdAt = if (session.createdAt == 0L) System.currentTimeMillis() else session.createdAt,
            updatedAt = System.currentTimeMillis()
        ))
        updateLastStudied(session.quizId)
        return id
    }
    suspend fun updateSession(session: SessionEntity) {
        sessionDao.updateSession(session.copy(updatedAt = System.currentTimeMillis()))
        if (session.isCompleted) {
            refreshQuizStats(session.quizId)
        }
        updateLastStudied(session.quizId)
    }
    suspend fun deleteSession(session: SessionEntity) = sessionDao.softDeleteSessionById(session.id, System.currentTimeMillis())

    suspend fun restoreSession(sessionId: Long) = sessionDao.restoreSessionById(sessionId, System.currentTimeMillis())

    suspend fun permanentlyDeleteSession(session: SessionEntity) = sessionDao.hardDeleteSession(session)

    suspend fun deleteQuiz(quiz: QuizEntity): QuizEntity {
        if (quiz.isSystem) throw IllegalStateException("Cannot delete system quiz")
        val now = System.currentTimeMillis()
        softDeleteQuizAnnotationTree(quiz.id, now)
        sessionDao.softDeleteSessionsByQuizId(quiz.id, now)
        questionDao.softDeleteQuestionsByQuizId(quiz.id, now)
        quizDao.softDeleteQuizById(quiz.id, now)
        refreshBookStats(quiz.bookId)
        return quiz.copy(deletedAt = now, updatedAt = now)
    }

    suspend fun restoreQuiz(quizId: Long) {
        quizDao.restoreQuizById(quizId, System.currentTimeMillis())
    }

    suspend fun permanentlyDeleteQuiz(quiz: QuizEntity): QuizEntity {
        if (quiz.isSystem) throw IllegalStateException("Cannot delete system quiz")
        permanentlyDeleteQuizAnnotationTree(quiz.id)
        releaseQuizTreeAssets(quiz)
        quizDao.hardDeleteQuiz(quiz)
        refreshBookStats(quiz.bookId)
        return quiz
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
        refreshBookStats(quiz.bookId)
    }

    suspend fun refreshBookStats(bookId: Long) {
        val total = quizDao.getBookQuestionCount(bookId).first()
        val quizzes = quizDao.getQuizzesByBookId(bookId).first()
        val answered = quizzes.sumOf { it.answeredCount }
        val completion = if (total == 0) 0f else answered.toFloat() / total
        
        // Weighted accuracy by attempts
        // For simplicity and since accuracy is already cached in Quiz, we could average it, 
        // but it's better to fetch all questions for the book if possible for true accuracy.
        // Actually, let's fetch all questions for the book to be precise.
        val questions = questionDao.getAdaptiveQuestionsByBook(bookId, Int.MAX_VALUE)
        val totalAttempts = questions.sumOf { it.attempts }
        val totalCorrect = questions.sumOf { it.correctCount }
        val accuracy = if (totalAttempts == 0) 0f else totalCorrect.toFloat() / totalAttempts

        bookDao.updateQuestionCount(bookId, total)
        bookDao.updateAnsweredCount(bookId, answered)
        bookDao.updateTotalAttempts(bookId, totalAttempts)
        bookDao.updateCompletionPercentage(bookId, completion)
        bookDao.updateAccuracyPercentage(bookId, accuracy)
    }

    private suspend fun updateLastStudied(quizId: Long) {
        val now = System.currentTimeMillis()
        val quiz = quizDao.getQuizById(quizId) ?: return
        quizDao.updateQuiz(quiz.copy(lastStudiedAt = now))
        
        val book = bookDao.getBookById(quiz.bookId) ?: return
        bookDao.updateBook(book.copy(lastStudiedAt = now))
    }

    suspend fun clearAllCategories() {
        val now = System.currentTimeMillis()
        quizDao.clearAllQuizCategories(now)
        questionDao.clearAllQuestionCategories(now)
        questionCategoryDao.clearAllCategories()
        categoryMetadataDao.deleteAllMetadata()
    }

    suspend fun deleteCategory(category: String) {
        // 1. Clear category from quizzes
        val quizzes = quizDao.getQuizzesByCategory(category).first()
        quizzes.forEach { quiz ->
            if (quiz.category == category) {
                quizDao.updateQuiz(quiz.copy(category = null))
            }
        }

        // 2. Remove category from questions
        val questions = questionCategoryDao.getQuestionsByCategory(category)
        val updatedQuestions = questions.map { question ->
            question.copy(categories = question.categories.filter { it != category })
        }
        updateQuestionsInPlace(updatedQuestions)

        // 3. Delete metadata
        categoryMetadataDao.deleteMetadataByName(category)
    }

    suspend fun getMergePreview(source: String, target: String): Int {
        val questionsFromSource = questionCategoryDao.getQuestionsByCategory(source)
        val questionsInTarget = questionCategoryDao.getQuestionsByCategory(target).asSequence().map { it.id }.toSet()
        // Count questions that will be moved and are NOT already in target
        return questionsFromSource.count { it.id !in questionsInTarget }
    }

    suspend fun renameCategory(oldName: String, newName: String) {
        // 1. Update quizzes
        val quizzes = quizDao.getQuizzesByCategory(oldName).first()
        quizzes.forEach { quiz ->
            if (quiz.category == oldName) {
                quizDao.updateQuiz(quiz.copy(category = newName))
            }
        }

        // 2. Update questions
        val questions = questionCategoryDao.getQuestionsByCategory(oldName)
        val updatedQuestions = questions.map { question ->
            val newCategories = question.categories.map { if (it == oldName) newName else it }.distinct()
            question.copy(categories = newCategories)
        }
        updateQuestionsInPlace(updatedQuestions)

        // 3. Update metadata
        val metadata = categoryMetadataDao.getMetadataForCategory(oldName)
        if (metadata != null) {
            categoryMetadataDao.deleteMetadataByName(oldName)
            categoryMetadataDao.insertMetadata(metadata.copy(name = newName))
        }
    }

    suspend fun mergeCategory(source: String, target: String) {
        // 1. Update quizzes: if a quiz has 'source', change to 'target'
        val quizzes = quizDao.getQuizzesByCategory(source).first()
        quizzes.forEach { quiz ->
            if (quiz.category == source) {
                quizDao.updateQuiz(quiz.copy(category = target))
            }
        }

        // 2. Update questions: if a question has 'source', replace with 'target' or just add 'target' and remove 'source'
        val questions = questionCategoryDao.getQuestionsByCategory(source)
        val updatedQuestions = questions.map { question ->
            val newCategories = (question.categories.filter { it != source } + target).distinct()
            question.copy(categories = newCategories)
        }
        updateQuestionsInPlace(updatedQuestions)

        // 3. Delete source metadata
        categoryMetadataDao.deleteMetadataByName(source)
    }

    suspend fun createQuizFromCategory(category: String, title: String, bookId: Long): Long {
        val quizId = quizDao.insertQuiz(
            QuizEntity(
                externalId = java.util.UUID.randomUUID().toString(),
                bookId = bookId,
                title = title,
                description = "Quiz created from category: $category",
                category = category,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        )

        val questions = questionCategoryDao.getQuestionsByCategory(category)
        val duplicatedQuestions = questions.map { q ->
            q.copy(
                id = 0, // New ID for new quiz
                externalId = java.util.UUID.randomUUID().toString(),
                quizId = quizId,
                attempts = 0,
                correctCount = 0
            )
        }
        insertQuestions(duplicatedQuestions)
        refreshQuizStats(quizId)
        return quizId
    }

    // Category Metadata
    fun getCategoryMetadata() = categoryMetadataDao.getAllMetadata()
    suspend fun updateCategoryMetadata(metadata: CategoryMetadataEntity) = 
        categoryMetadataDao.insertMetadata(metadata)

    fun getAllCategoriesWithMetadata(): Flow<List<CategoryWithMetadata>> = 
        combine(
            questionDao.getAllQuestionsFlow(),
            quizDao.getAllQuizzesFlow(),
            categoryMetadataDao.getAllMetadata()
        ) { allQuestions, allQuizzes, metadataList ->
            val quizCategoryMap = allQuizzes.associateBy({ it.id }) { it.category }
            val metadataMap = metadataList.associateBy { it.name }
            
            // Map to store running stats per category
            class CatStats {
                var qCount = 0
                var answered = 0
                var attempts = 0L
                var correct = 0L
                var lastEditedAt = 0L
            }
            val categoryStats = mutableMapOf<String, CatStats>()
            
            allQuestions.forEach { question ->
                // Collect unique categories for this question to avoid double-counting if 
                // it's in both question.categories and quizCategoryMap
                val uniqueCats = mutableSetOf<String>()
                question.categories.forEach { if (it.isNotBlank()) uniqueCats.add(it) }
                quizCategoryMap[question.quizId]?.let { if (it.isNotBlank()) uniqueCats.add(it) }

                uniqueCats.forEach { cat ->
                    val stats = categoryStats.getOrPut(cat) { CatStats() }
                    stats.qCount++
                    if (question.attempts > 0) stats.answered++
                    stats.attempts += question.attempts
                    stats.correct += question.correctCount
                    stats.lastEditedAt = maxOf(stats.lastEditedAt, question.lastEditedAt)
                }
            }
            
            // Ensure categories from quizzes with no questions are included
            allQuizzes.forEach { quiz ->
                quiz.category?.let { cat ->
                    if (cat.isNotBlank()) {
                        categoryStats.getOrPut(cat) { CatStats() }
                    }
                }
            }

            categoryStats.map { (name, stats) ->
                val accuracy = if (stats.attempts == 0L) 0f else stats.correct.toFloat() / stats.attempts

                CategoryWithMetadata(
                    name = name,
                    questionCount = stats.qCount,
                    answeredCount = stats.answered,
                    accuracyPercentage = accuracy,
                    lastEditedAt = stats.lastEditedAt,
                    metadata = metadataMap[name]
                )
            }.sortedWith(compareByDescending<CategoryWithMetadata> { it.isPinned }.thenBy { it.name })
        }.flowOn(Dispatchers.Default)

    fun getCategoryQuestionCount(category: String): Flow<Int> = 
        questionCategoryDao.getQuestionsByCategoryFlow(category).map { it.size }


    suspend fun exportQuizToZip(quizId: Long, outputStream: java.io.OutputStream): ExportResult {
        return exportManager?.exportQuizToZip(quizId, outputStream)
            ?: ExportResult(success = false, errorMessage = "Export manager unavailable.")
    }

    suspend fun exportBundleToZip(bookId: Long, outputStream: java.io.OutputStream): ExportResult {
        return exportManager?.exportBundleToZip(bookId, outputStream)
            ?: ExportResult(success = false, errorMessage = "Export manager unavailable.")
    }

    suspend fun exportAllToZip(outputStream: java.io.OutputStream): ExportResult {
        return exportManager?.exportAllToZip(outputStream)
            ?: ExportResult(success = false, errorMessage = "Export manager unavailable.")
    }

    /**
     * R1 repair gate: repository-level schema-7 export wrappers.
     *
     * LibraryViewModel routes the user-facing export buttons through the
     * repository, not directly through ExportManager. Stage 4D/4F added the
     * schema-7 writer on ExportManager, but the repository wrappers were
     * missing in the audited Stage 4 Final base. Keeping these wrappers here
     * preserves the existing architecture and makes the Stage 4 export UI
     * compile against the schema-7 exchange path.
     */
    suspend fun exportQuizToSchema7Zip(quizId: Long, outputStream: java.io.OutputStream): ExportResult {
        return exportManager?.exportQuizToSchema7Zip(quizId, outputStream)
            ?: ExportResult(success = false, errorMessage = "Export manager unavailable.")
    }

    suspend fun exportBundleToSchema7Zip(bookId: Long, outputStream: java.io.OutputStream): ExportResult {
        return exportManager?.exportBundleToSchema7Zip(bookId, outputStream)
            ?: ExportResult(success = false, errorMessage = "Export manager unavailable.")
    }

    suspend fun exportAllToSchema7Zip(outputStream: java.io.OutputStream): ExportResult {
        return exportManager?.exportAllToSchema7Zip(outputStream)
            ?: ExportResult(success = false, errorMessage = "Export manager unavailable.")
    }

    suspend fun getImportPreview(uri: android.net.Uri) = importManager?.getImportPreview(uri)

    suspend fun importFromUri(
        uri: android.net.Uri,
        strategy: com.ahmedyejam.mks.data.import.model.MergeStrategy = com.ahmedyejam.mks.data.import.model.MergeStrategy.MERGE_ONLY,
        targetBookId: Long? = null,
        targetQuizId: Long? = null,
        allowInsecureRemoteImages: Boolean = false,
        onProgress: (Float, String) -> Unit = { _, _ -> },
    ): com.ahmedyejam.mks.data.import.model.ImportResult? {
        val result = importManager?.import(
            uri = uri,
            strategy = strategy,
            targetBookId = targetBookId,
            targetQuizId = targetQuizId,
            allowInsecureRemoteImages = allowInsecureRemoteImages,
            onProgress = onProgress
        )
        if (result?.success == true) {
            result.affectedQuizIds.forEach { refreshQuizStats(it) }
            result.affectedBookIds.forEach { refreshBookStats(it) }
            if (targetQuizId != null && targetQuizId !in result.affectedQuizIds) {
                refreshQuizStats(targetQuizId)
            }
        }
        return result
    }

    suspend fun importCompiledQuestions(
        title: String,
        targetBookId: Long?,
        targetQuizId: Long? = null,
        newBookTitle: String? = null,
        questions: List<ParsedQuestion>
    ): com.ahmedyejam.mks.data.import.model.ImportResult? {
        val result = importManager?.importQuestions(
            title = title,
            questions = questions,
            targetBookId = targetBookId,
            targetQuizId = targetQuizId,
            newBookTitle = newBookTitle
        )
        if (result?.success == true) {
            result.affectedQuizIds.forEach { refreshQuizStats(it) }
            result.affectedBookIds.forEach { refreshBookStats(it) }
            if (targetQuizId != null && targetQuizId !in result.affectedQuizIds) {
                refreshQuizStats(targetQuizId)
            }
        }
        return result
    }

    fun detectFormat(uri: android.net.Uri): com.ahmedyejam.mks.data.import.model.ImportFormat {
        return importManager?.detectFormat(uri) ?: com.ahmedyejam.mks.data.import.model.ImportFormat.UNKNOWN
    }

    fun saveImage(uri: android.net.Uri): String? = fileManager.saveImage(uri)

    fun resolveImagePath(path: String?): String? {
        if (path.isNullOrBlank()) return null
        if (path.startsWith("http") || path.startsWith("content://") || path.startsWith("assets/") || path.startsWith("data:")) return path
        
        // If it's an absolute path, verify it exists and is in the allowed images directory
        if (path.startsWith("/")) {
            return if (fileManager.getFile(path) != null) path else null
        }

        // Assume relative to internal images directory
        val imagesDir = File(fileManager.getContext().filesDir, "images")
        val file = File(imagesDir, path)
        return if (file.exists()) file.absolutePath else null
    }

    fun getBookQuestionCount(bookId: Long): Flow<Int> = quizDao.getBookQuestionCount(bookId)

    fun getQuizQuestionCount(quizId: Long): Flow<Int> = quizDao.getQuestionCount(quizId)

    fun getQuizCompletion(quizId: Long): Flow<Float> = combine(
        quizDao.getQuestionCount(quizId),
        sessionDao.getLatestSessionForQuiz(quizId)
    ) { total, session ->
        if (total == 0) 0f
        else if (session == null) 0f
        else if (session.isCompleted) 1f
        else {
            val answered = session.answers.size
            (answered.toFloat() / total).coerceIn(0f, 1f)
        }
    }

    // --- Flashcard decks and cards ---
    fun getFlashcardDecksByBookId(bookId: Long): Flow<List<FlashcardDeckEntity>> =
        flashcardDeckDao.getFlashcardDecksByBookId(bookId)

    suspend fun getFlashcardDeckById(id: Long) = flashcardDeckDao.getFlashcardDeckById(id)

    fun observeFlashcardDeckById(id: Long): Flow<FlashcardDeckEntity?> =
        flashcardDeckDao.observeFlashcardDeckById(id)

    suspend fun createFlashcardDeckFromQuestions(
        bookId: Long,
        title: String,
        description: String? = null,
        questionIds: List<Long>,
        clearMarksAfter: Boolean = false,
        config: FlashcardGenerationConfig = FlashcardGenerationConfig.DEFAULT
    ): Long {
        val questions = questionDao.getQuestionsByIds(questionIds).sortedBy { questionIds.indexOf(it.id) }
        if (questions.isEmpty()) throw IllegalArgumentException("No questions selected for flashcard generation.")
        val now = System.currentTimeMillis()
        val deckId = insertFlashcardDeck(
            FlashcardDeckEntity(
                externalId = java.util.UUID.randomUUID().toString(),
                bookId = bookId,
                title = title.ifBlank { "Generated flashcards" },
                description = description,
                createdAt = now,
                updatedAt = now,
                lastEditedAt = now
            )
        )
        addFlashcardsFromQuestionsToDeck(deckId, questions.map { it.id }, config)
        if (clearMarksAfter) {
            questions.filter { it.isMarked }.forEach { question ->
                questionDao.updateQuestion(question.copy(isMarked = false, updatedAt = now, lastEditedAt = now))
            }
        }
        return deckId
    }

    suspend fun createFlashcardDeckFromMarkedQuestions(
        bookId: Long,
        title: String,
        description: String? = null,
        clearMarksAfter: Boolean = false,
        config: FlashcardGenerationConfig = FlashcardGenerationConfig.DEFAULT
    ): Long {
        val questions = getBookStudyBundle(bookId)?.questions.orEmpty().filter { it.isMarked }
        return createFlashcardDeckFromQuestions(bookId, title, description, questions.map { it.id }, clearMarksAfter, config)
    }

    suspend fun createFlashcardDeckFromMissedQuestions(
        bookId: Long,
        title: String,
        description: String? = null,
        config: FlashcardGenerationConfig = FlashcardGenerationConfig.DEFAULT
    ): Long {
        val questions = getBookStudyBundle(bookId)?.questions.orEmpty().filter { question ->
            question.attempts > 0 && (question.correctCount < question.attempts || question.lastAttemptResult == false)
        }
        return createFlashcardDeckFromQuestions(bookId, title, description, questions.map { it.id }, clearMarksAfter = false, config)
    }

    suspend fun addFlashcardsFromQuestionsToDeck(
        deckId: Long, 
        questionIds: List<Long>,
        config: FlashcardGenerationConfig = FlashcardGenerationConfig.DEFAULT
    ): Int {
        val deck = flashcardDeckDao.getFlashcardDeckById(deckId) ?: return 0
        val questions = questionDao.getQuestionsByIds(questionIds).sortedBy { questionIds.indexOf(it.id) }
        val existingCount = flashcardDao.countCardsInDeck(deckId)
        val cards = questions.mapIndexed { index, question ->
            questionToFlashcard(deckId, question, existingCount + index, config).copy(
                sourceQuestionId = question.id
            )
        }
        if (cards.isNotEmpty()) {
            insertFlashcards(cards)
        }
        return cards.size
    }

    private fun questionToFlashcard(deckId: Long, question: QuestionEntity, orderIndex: Int, config: FlashcardGenerationConfig): FlashcardEntity {
        val frontText = buildString {
            if (config.includeStemInFront) {
                append(question.text)
            }
            if (config.includeOptionsInFront && question.options.isNotEmpty()) {
                if (isNotEmpty()) append("\n\n")
                question.options.forEachIndexed { i, opt -> append("${(i + 65).toChar()}) $opt\n") }
            }
        }.trim()

        val backText = buildString {
            if (config.includeAnswerInBack) {
                val answerText = question.correctAnswers
                    .mapNotNull(question.options::getOrNull)
                    .joinToString(separator = "\n")
                    .ifBlank { "Review the source material for the expected answer." }
                append(answerText)
            }
            
            if (config.includeExplanationInBack && !question.explanation.isNullOrBlank()) {
                if (isNotEmpty()) append("\n\n")
                append("Explanation\n${question.explanation}")
            }
            
            if (config.includeHintInBack && !question.hint.isNullOrBlank()) {
                if (isNotEmpty()) append("\n\n")
                append("Hint\n${question.hint}")
            }
            
            if (config.includeReferenceInBack && !question.reference.isNullOrBlank()) {
                if (isNotEmpty()) append("\n\n")
                append("Reference\n${question.reference}")
            }
            
            if (config.includeAdditionalInfoInBack && !question.additionalInfo.isNullOrBlank()) {
                if (isNotEmpty()) append("\n\n")
                append("Additional Info\n${question.additionalInfo}")
            }
        }.trim()

        return FlashcardEntity(
            externalId = java.util.UUID.randomUUID().toString(),
            deckId = deckId,
            frontText = frontText.ifBlank { "Empty Front" },
            backText = backText.ifBlank { "Empty Back" },
            hint = question.hint.takeIf { !config.includeHintInBack }, // Only use standalone hint if not in back
            imagePath = if (config.includeImageInFront || config.includeImageInBack) question.imagePath else null,
            tags = question.categories,
            orderIndex = orderIndex,
            sourceQuestionId = question.id,
            syncConfig = emptyMap()
        )
    }


    suspend fun createFlashcardDeckFromBook(
        bookId: Long,
        title: String,
        description: String? = null,
        coverImage: String? = null,
        config: FlashcardGenerationConfig = FlashcardGenerationConfig.DEFAULT
    ): Long {
        val bundle = getBookStudyBundle(bookId)
            ?: throw IllegalArgumentException("Book $bookId not found")
        val now = System.currentTimeMillis()
        val deckId = insertFlashcardDeck(
            FlashcardDeckEntity(
                externalId = java.util.UUID.randomUUID().toString(),
                bookId = bookId,
                title = title,
                description = description,
                coverImage = coverImage ?: bundle.book.coverImage,
                createdAt = now,
                updatedAt = now,
                lastEditedAt = now
            )
        )
        val cards = bundle.quizzes.flatMap { quiz ->
            bundle.questionsByQuiz[quiz.id].orEmpty().mapIndexed { index, question ->
                questionToFlashcard(deckId, question, index, config).copy(
                    tags = (question.categories + listOfNotNull(quiz.category)).distinct(),
                    createdAt = now,
                    updatedAt = now,
                    frontText = if (!config.includeStemInFront || question.text.isBlank()) "${quiz.title} ${index + 1}" else questionToFlashcard(deckId, question, index, config).frontText
                )
            }
        }
        if (cards.isNotEmpty()) {
            insertFlashcards(cards)
        }
        return deckId
    }

    suspend fun insertFlashcardDeck(deck: FlashcardDeckEntity): Long {
        var finalDeck = deck
        if (deck.coverImage?.startsWith("http", ignoreCase = true) == true) {
            val localPath = fileManager.downloadAndSaveImage(deck.coverImage)
            if (localPath != null) finalDeck = deck.copy(coverImage = localPath)
        }
        val id = flashcardDeckDao.insertFlashcardDeck(finalDeck)
        replaceOwnerAssetReferences("flashcard_deck", id, listOf(finalDeck.coverImage))
        refreshFlashcardDeckStats(id)
        refreshBookStats(finalDeck.bookId)
        return id
    }

    suspend fun updateFlashcardDeck(deck: FlashcardDeckEntity) {
        val updated = deck.copy(updatedAt = System.currentTimeMillis(), lastEditedAt = System.currentTimeMillis())
        flashcardDeckDao.updateFlashcardDeck(updated)
        replaceOwnerAssetReferences("flashcard_deck", updated.id, listOf(updated.coverImage))
        val refreshedDeck = refreshFlashcardDeckStats(deck.id) ?: updated
        refreshBookStats(refreshedDeck.bookId)
    }

    suspend fun deleteFlashcardDeck(deck: FlashcardDeckEntity) {
        val now = System.currentTimeMillis()
        flashcardDao.softDeleteAllCardsInDeck(deck.id, now)
        flashcardDeckDao.softDeleteFlashcardDeckById(deck.id, now)
        refreshBookStats(deck.bookId)
    }

    suspend fun restoreFlashcardDeck(deckId: Long) {
        flashcardDeckDao.restoreFlashcardDeckById(deckId, System.currentTimeMillis())
    }

    suspend fun permanentlyDeleteFlashcardDeck(deck: FlashcardDeckEntity) {
        val cards = flashcardDao.getFlashcardsByDeckId(deck.id).first()
        cards.forEach { releaseOwnerAssets("flashcard", it.id) }
        releaseOwnerAssets("flashcard_deck", deck.id)
        flashcardDeckDao.hardDeleteFlashcardDeck(deck)
        refreshBookStats(deck.bookId)
    }

    fun getFlashcardsByDeckId(deckId: Long): Flow<List<FlashcardEntity>> = flashcardDao.getFlashcardsByDeckId(deckId)

    suspend fun insertFlashcard(card: FlashcardEntity): Long {
        var finalCard = card
        if (card.imagePath?.startsWith("http", ignoreCase = true) == true) {
            val localPath = fileManager.downloadAndSaveImage(card.imagePath)
            if (localPath != null) finalCard = card.copy(imagePath = localPath)
        }
        val id = flashcardDao.insertFlashcard(finalCard)
        replaceOwnerAssetReferences("flashcard", id, listOf(finalCard.imagePath))
        val deck = refreshFlashcardDeckStats(finalCard.deckId)
        deck?.let { refreshBookStats(it.bookId) }
        return id
    }

    suspend fun insertFlashcards(cards: List<FlashcardEntity>): List<Long> {
        val now = System.currentTimeMillis()
        val updated = cards.map { card ->
            val finalCard = if (card.imagePath?.startsWith("http", ignoreCase = true) == true) {
                val localPath = fileManager.downloadAndSaveImage(card.imagePath)
                if (localPath != null) card.copy(imagePath = localPath) else card
            } else card
            finalCard.copy(createdAt = now, updatedAt = now)
        }
        val ids = flashcardDao.insertFlashcards(updated)
        ids.zip(updated).forEach { (id, card) ->
            replaceOwnerAssetReferences("flashcard", id, listOf(card.imagePath))
        }
        updated.map { it.deckId }.distinct().forEach { deckId ->
            val deck = refreshFlashcardDeckStats(deckId)
            deck?.let { refreshBookStats(it.bookId) }
        }
        return ids
    }

    suspend fun updateFlashcard(card: FlashcardEntity) {
        val updated = card.copy(updatedAt = System.currentTimeMillis())
        flashcardDao.updateFlashcard(updated)
        replaceOwnerAssetReferences("flashcard", updated.id, listOf(updated.imagePath))
        val deck = refreshFlashcardDeckStats(card.deckId)
        deck?.let { refreshBookStats(it.bookId) }
    }

    suspend fun deleteFlashcard(card: FlashcardEntity) {
        flashcardDao.softDeleteFlashcardById(card.id, System.currentTimeMillis())
        val deck = refreshFlashcardDeckStats(card.deckId)
        deck?.let { refreshBookStats(it.bookId) }
    }

    suspend fun restoreFlashcard(cardId: Long) {
        flashcardDao.restoreFlashcardById(cardId, System.currentTimeMillis())
    }

    suspend fun permanentlyDeleteFlashcard(card: FlashcardEntity) {
        releaseOwnerAssets("flashcard", card.id)
        flashcardDao.hardDeleteFlashcard(card)
        val deck = refreshFlashcardDeckStats(card.deckId)
        deck?.let { refreshBookStats(it.bookId) }
    }

    suspend fun reorderFlashcards(cards: List<FlashcardEntity>) {
        val now = System.currentTimeMillis()
        cards.forEachIndexed { index, card ->
            flashcardDao.updateCardOrder(card.id, index, now)
        }
        cards.firstOrNull()?.deckId?.let { refreshFlashcardDeckStats(it) }
    }

    suspend fun moveFlashcards(cardIds: List<Long>, targetDeckId: Long) {
        val now = System.currentTimeMillis()
        val originalDecks = mutableSetOf<Long>()
        
        cardIds.chunked(999).forEach { chunk ->
            val cards = flashcardDao.getFlashcardsByIds(chunk)
            cards.forEach { originalDecks.add(it.deckId) }
            flashcardDao.moveCardsToDeck(chunk, targetDeckId, now)
        }
        
        originalDecks.forEach { refreshFlashcardDeckStats(it) }
        refreshFlashcardDeckStats(targetDeckId)
    }

    suspend fun copyFlashcards(cardIds: List<Long>, targetDeckId: Long) {
        val existingCount = flashcardDao.countCardsInDeck(targetDeckId)
        val now = System.currentTimeMillis()
        
        val copiedCards = cardIds.chunked(999).flatMap { chunk ->
            flashcardDao.getFlashcardsByIds(chunk).mapIndexed { index, card ->
                card.copy(
                    id = 0,
                    externalId = java.util.UUID.randomUUID().toString(),
                    deckId = targetDeckId,
                    orderIndex = existingCount + index,
                    attempts = 0,
                    correctCount = 0,
                    difficulty = null,
                    dueAt = 0,
                    reviewCount = 0,
                    lastReviewedAt = 0,
                    createdAt = now,
                    updatedAt = now,
                    deletedAt = null
                )
            }
        }
        
        if (copiedCards.isNotEmpty()) {
            insertFlashcards(copiedCards)
        }
    }

    suspend fun rateFlashcard(card: FlashcardEntity, rating: String) {
        val now = System.currentTimeMillis()
        val normalized = rating.lowercase().trim().ifBlank { "good" }
        val correctIncrement = when (normalized) {
            "easy", "good" -> 1
            else -> 0
        }
        val day = 24L * 60L * 60L * 1000L
        val nextDueAt = when (normalized) {
            "again" -> now + 10L * 60L * 1000L
            "easy" -> now + maxOf(3L, (card.reviewCount + 1L) * 2L) * day
            "good" -> now + maxOf(1L, card.reviewCount + 1L) * day
            else -> now + day
        }
        val updated = card.copy(
            attempts = card.attempts + 1,
            correctCount = card.correctCount + correctIncrement,
            difficulty = normalized,
            lastReviewedAt = now,
            updatedAt = now
        )
        updateFlashcard(updated)
        flashcardDao.markReviewed(card.id, now, nextDueAt)
        val deck = flashcardDeckDao.getFlashcardDeckById(card.deckId)
        deck?.let { flashcardDeckDao.updateFlashcardDeck(it.copy(lastStudiedAt = now, updatedAt = now)) }
    }

    private suspend fun refreshFlashcardDeckStats(deckId: Long): FlashcardDeckEntity? {
        val deck = flashcardDeckDao.getFlashcardDeckById(deckId) ?: return null
        val cards = flashcardDao.getFlashcardsByDeckId(deckId).first()
        val totalCards = cards.size
        val studiedCards = cards.count { it.attempts > 0 }
        val totalAttempts = cards.sumOf { it.attempts }
        val totalCorrect = cards.sumOf { it.correctCount }
        val mastery = if (totalAttempts == 0) 0f else totalCorrect.toFloat() / totalAttempts
        val now = System.currentTimeMillis()
        val refreshed = deck.copy(
            cardCount = totalCards,
            studiedCount = studiedCards,
            masteryPercentage = mastery.coerceIn(0f, 1f),
            updatedAt = now
        )
        flashcardDeckDao.updateFlashcardDeck(refreshed)
        return refreshed
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    fun getBookCompletion(bookId: Long): Flow<Float> = quizDao.getQuizzesByBookId(bookId)
        .flatMapLatest { quizzes ->
            if (quizzes.isEmpty()) return@flatMapLatest flowOf(0f)
            combine(quizzes.map { getQuizCompletion(it.id) }) { completions ->
                completions.average().toFloat()
            }
        }

    // --- Knowledge Bank CRUD and Sync ---

    // Slideshow Courses
    fun getSlideshowCoursesByBookId(bookId: Long): Flow<List<SlideshowCourseEntity>> = slideshowCourseDao.getCoursesByBookId(bookId)
    suspend fun getSlideshowCourseById(id: Long) = slideshowCourseDao.getCourseById(id)
    suspend fun insertSlideshowCourse(course: SlideshowCourseEntity): Long {
        var finalCourse = course
        // Download cover image if it's an HTTP URL
        if (course.coverImage?.startsWith("http", ignoreCase = true) == true) {
            val localPath = fileManager.downloadAndSaveImage(course.coverImage)
            if (localPath != null) finalCourse = course.copy(coverImage = localPath)
        }
        val saved = finalCourse.copy(createdAt = System.currentTimeMillis(), updatedAt = System.currentTimeMillis(), lastEditedAt = System.currentTimeMillis())
        val id = slideshowCourseDao.insertCourse(saved)
        replaceOwnerAssetReferences("slideshow_course", id, listOf(saved.coverImage))
        refreshBookStats(course.bookId)
        return id
    }
    suspend fun updateSlideshowCourse(course: SlideshowCourseEntity) {
        var finalCourse = course
        // Download cover image if it's an HTTP URL
        if (course.coverImage?.startsWith("http", ignoreCase = true) == true) {
            val localPath = fileManager.downloadAndSaveImage(course.coverImage)
            if (localPath != null) finalCourse = course.copy(coverImage = localPath)
        }
        val updated = finalCourse.copy(updatedAt = System.currentTimeMillis(), lastEditedAt = System.currentTimeMillis())
        slideshowCourseDao.updateCourse(updated)
        replaceOwnerAssetReferences("slideshow_course", updated.id, listOf(updated.coverImage))
    }
    suspend fun deleteSlideshowCourse(course: SlideshowCourseEntity) {
        val now = System.currentTimeMillis()
        softDeleteSlideshowAnnotationTree(course.id, now)
        slideshowCourseDao.softDeleteCourseById(course.id, now)
        refreshBookStats(course.bookId)
    }

    suspend fun restoreSlideshowCourse(courseId: Long) {
        val now = System.currentTimeMillis()
        slideshowCourseDao.restoreCourseById(courseId, now)
        restoreSlideshowAnnotationTree(courseId, now)
    }

    suspend fun permanentlyDeleteSlideshowCourse(course: SlideshowCourseEntity) {
        permanentlyDeleteSlideshowAnnotationTree(course.id)
        val slides = courseSlideDao.getSlidesByCourseIdIncludingDeleted(course.id)
        slides.forEach { releaseOwnerAssets("course_slide", it.id) }
        releaseOwnerAssets("slideshow_course", course.id)
        slideshowCourseDao.hardDeleteCourse(course)
        refreshBookStats(course.bookId)
    }

    // Course Slides
    fun getSlidesByCourseId(courseId: Long): Flow<List<CourseSlideEntity>> = courseSlideDao.getSlidesByCourseId(courseId)
    suspend fun insertCourseSlide(slide: CourseSlideEntity): Long {
        var finalSlide = slide
        // Download image if it's an HTTP URL
        if (slide.imagePath?.startsWith("http", ignoreCase = true) == true) {
            val localPath = fileManager.downloadAndSaveImage(slide.imagePath)
            if (localPath != null) finalSlide = slide.copy(imagePath = localPath)
        }
        val saved = finalSlide.copy(createdAt = System.currentTimeMillis(), updatedAt = System.currentTimeMillis())
        val id = courseSlideDao.insertSlide(saved)
        replaceOwnerAssetReferences("course_slide", id, listOf(saved.imagePath))
        refreshCourseStats(slide.courseId)
        return id
    }

    suspend fun insertCourseSlides(slides: List<CourseSlideEntity>): List<Long> {
        val now = System.currentTimeMillis()
        val updated = slides.map { slide ->
            val finalSlide = if (slide.imagePath?.startsWith("http", ignoreCase = true) == true) {
                val localPath = fileManager.downloadAndSaveImage(slide.imagePath)
                if (localPath != null) slide.copy(imagePath = localPath) else slide
            } else slide
            finalSlide.copy(createdAt = now, updatedAt = now)
        }
        
        // We do a manual insert slide-by-slide to return IDs for asset reference replacing, 
        // since Dao insertSlides does not return a list of IDs. Or we can just insert one by one.
        val ids = mutableListOf<Long>()
        for (slide in updated) {
            val id = courseSlideDao.insertSlide(slide)
            replaceOwnerAssetReferences("course_slide", id, listOf(slide.imagePath))
            ids.add(id)
        }
        
        updated.map { it.courseId }.distinct().forEach { refreshCourseStats(it) }
        return ids
    }

    suspend fun reorderCourseSlides(slides: List<CourseSlideEntity>) {
        val now = System.currentTimeMillis()
        val updated = slides.mapIndexed { index, slide ->
            slide.copy(orderIndex = index, updatedAt = now)
        }
        courseSlideDao.updateSlides(updated)
        slides.firstOrNull()?.courseId?.let { refreshCourseStats(it) }
    }

    private fun questionToSlide(courseId: Long, question: QuestionEntity, orderIndex: Int, config: com.ahmedyejam.mks.data.model.SlideGenerationConfig): CourseSlideEntity {
        val title = if (config.includeStemInTitle && question.text.isNotBlank()) question.text.trim() else "Slide ${orderIndex + 1}"
        
        val body = buildString {
            if (config.includeOptionsInBody) {
                val optionsText = question.options.joinToString(separator = "\n") { "- $it" }
                if (optionsText.isNotBlank()) append(optionsText)
            }
            if (config.includeAnswerInBody) {
                val answerText = question.correctAnswers
                    .mapNotNull(question.options::getOrNull)
                    .joinToString(separator = "\n")
                if (answerText.isNotBlank()) {
                    if (isNotEmpty()) append("\n\n")
                    append("Answer:\n$answerText")
                }
            }
            if (config.includeExplanationInBody && !question.explanation.isNullOrBlank()) {
                if (isNotEmpty()) append("\n\n")
                append("Explanation:\n${question.explanation}")
            }
        }.trim().ifBlank { "No content" }

        val notes = buildString {
            if (config.includeHintInSpeakerNotes && !question.hint.isNullOrBlank()) {
                append("Hint: ${question.hint}\n")
            }
            if (config.includeReferenceInSpeakerNotes && !question.reference.isNullOrBlank()) {
                append("Reference: ${question.reference}\n")
            }
        }.trim()

        val now = System.currentTimeMillis()
        return CourseSlideEntity(
            externalId = java.util.UUID.randomUUID().toString(),
            courseId = courseId,
            title = title,
            body = body,
            speakerNotes = notes.ifBlank { null },
            imagePath = if (config.includeImage) question.imagePath else null,
            orderIndex = orderIndex,
            sourceQuestionId = question.id,
            syncConfig = emptyMap(),
            createdAt = now,
            updatedAt = now
        )
    }

    suspend fun addCourseSlidesFromQuestionsToCourse(courseId: Long, questionIds: List<Long>, config: com.ahmedyejam.mks.data.model.SlideGenerationConfig = com.ahmedyejam.mks.data.model.SlideGenerationConfig.DEFAULT): Int {
        val course = slideshowCourseDao.getCourseById(courseId) ?: return 0
        val questions = questionDao.getQuestionsByIds(questionIds).sortedBy { questionIds.indexOf(it.id) }
        val existingSlides = courseSlideDao.getSlidesByCourseIdNow(courseId)
        val existingCount = existingSlides.size
        
        val slides = questions.mapIndexed { index, question ->
            questionToSlide(courseId, question, existingCount + index, config)
        }
        
        if (slides.isNotEmpty()) {
            insertCourseSlides(slides)
        }
        return slides.size
    }

    suspend fun copyCourseSlides(slideIds: List<Long>, targetCourseId: Long) {
        val existingCount = courseSlideDao.countSlidesInCourse(targetCourseId)
        val now = System.currentTimeMillis()
        
        val copiedSlides = slideIds.chunked(999).flatMap { chunk ->
            courseSlideDao.getSlidesByIds(chunk).mapIndexed { index, slide ->
                slide.copy(
                    id = 0,
                    externalId = java.util.UUID.randomUUID().toString(),
                    courseId = targetCourseId,
                    orderIndex = existingCount + index,
                    isCompleted = false,
                    createdAt = now,
                    updatedAt = now,
                    deletedAt = null
                )
            }
        }
        
        if (copiedSlides.isNotEmpty()) {
            insertCourseSlides(copiedSlides)
        }
    }

    suspend fun moveCourseSlides(slideIds: List<Long>, targetCourseId: Long) {
        val now = System.currentTimeMillis()
        val originalCourses = mutableSetOf<Long>()
        
        slideIds.chunked(999).forEach { chunk ->
            val slides = courseSlideDao.getSlidesByIds(chunk)
            slides.forEach { originalCourses.add(it.courseId) }
            courseSlideDao.moveSlidesToCourse(chunk, targetCourseId, now)
        }
        
        originalCourses.forEach { refreshCourseStats(it) }
        refreshCourseStats(targetCourseId)
    }

    suspend fun createSlideshowCourseFromQuestions(bookId: Long, title: String, description: String?, questionIds: List<Long>, clearMarksAfter: Boolean = false): Long {
        val now = System.currentTimeMillis()
        val courseId = insertSlideshowCourse(
            SlideshowCourseEntity(
                externalId = java.util.UUID.randomUUID().toString(),
                bookId = bookId,
                title = title,
                description = description,
                createdAt = now,
                updatedAt = now,
                lastEditedAt = now
            )
        )
        
        addCourseSlidesFromQuestionsToCourse(courseId, questionIds)
        
        if (clearMarksAfter && questionIds.isNotEmpty()) {
            val questions = questionDao.getQuestionsByIds(questionIds).map { it.copy(isMarked = false) }
            questionDao.updateQuestions(questions)
        }
        return courseId
    }
    suspend fun updateCourseSlide(slide: CourseSlideEntity) {
        var finalSlide = slide
        // Download image if it's an HTTP URL
        if (slide.imagePath?.startsWith("http", ignoreCase = true) == true) {
            val localPath = fileManager.downloadAndSaveImage(slide.imagePath)
            if (localPath != null) finalSlide = slide.copy(imagePath = localPath)
        }
        val updated = finalSlide.copy(updatedAt = System.currentTimeMillis())
        courseSlideDao.updateSlide(updated)
        replaceOwnerAssetReferences("course_slide", updated.id, listOf(updated.imagePath))
        refreshCourseStats(slide.courseId)
    }
    suspend fun deleteCourseSlide(slide: CourseSlideEntity) {
        val now = System.currentTimeMillis()
        softDeleteOwnerAnnotations(AnnotationOwnerType.SLIDE, slide.id, now)
        courseSlideDao.softDeleteSlideById(slide.id, now)
        refreshCourseStats(slide.courseId)
    }

    suspend fun restoreCourseSlide(slideId: Long) {
        val now = System.currentTimeMillis()
        courseSlideDao.restoreSlideById(slideId, now)
        restoreOwnerAnnotations(AnnotationOwnerType.SLIDE, slideId, now)
    }

    suspend fun permanentlyDeleteCourseSlide(slide: CourseSlideEntity) {
        permanentlyDeleteOwnerAnnotations(AnnotationOwnerType.SLIDE, slide.id)
        releaseOwnerAssets("course_slide", slide.id)
        courseSlideDao.hardDeleteSlide(slide)
        refreshCourseStats(slide.courseId)
    }

    private suspend fun refreshCourseStats(courseId: Long) {
        val course = slideshowCourseDao.getCourseById(courseId) ?: return
        val slides = courseSlideDao.getSlidesByCourseId(courseId).first()
        val total = slides.size
        val studied = slides.count { it.isCompleted }
        val progress = if (total == 0) 0f else studied.toFloat() / total
        slideshowCourseDao.updateCourse(course.copy(slideCount = total, studiedSlideCount = studied, progress = progress, updatedAt = System.currentTimeMillis()))
    }

    // Note Blueprints
    fun getNoteBlueprintsByBookId(bookId: Long): Flow<List<NoteBlueprintEntity>> = noteBlueprintDao.getNotesByBookId(bookId)
    suspend fun getNoteBlueprintById(id: Long) = noteBlueprintDao.getNoteById(id)
    suspend fun insertNoteBlueprint(note: NoteBlueprintEntity): Long {
        return noteBlueprintDao.insertNote(note.copy(createdAt = System.currentTimeMillis(), updatedAt = System.currentTimeMillis()))
    }
    suspend fun updateNoteBlueprint(note: NoteBlueprintEntity) {
        noteBlueprintDao.updateNote(note.copy(updatedAt = System.currentTimeMillis()))
    }
    suspend fun deleteNoteBlueprint(note: NoteBlueprintEntity) {
        val now = System.currentTimeMillis()
        softDeleteOwnerAnnotations(AnnotationOwnerType.NOTE, note.id, now)
        noteBlueprintDao.softDeleteNoteById(note.id, now)
    }

    suspend fun restoreNoteBlueprint(noteId: Long) {
        val now = System.currentTimeMillis()
        noteBlueprintDao.restoreNoteById(noteId, now)
        restoreOwnerAnnotations(AnnotationOwnerType.NOTE, noteId, now)
    }

    suspend fun permanentlyDeleteNoteBlueprint(note: NoteBlueprintEntity) {
        permanentlyDeleteOwnerAnnotations(AnnotationOwnerType.NOTE, note.id)
        noteBlueprintDao.hardDeleteNote(note)
    }

    // Prompts
    fun getPromptsByBookId(bookId: Long): Flow<List<PromptEntity>> = promptDao.getPromptsByBookId(bookId)
    suspend fun getPromptById(id: Long) = promptDao.getPromptById(id)
    suspend fun insertPrompt(prompt: PromptEntity): Long {
        return promptDao.insertPrompt(prompt.copy(createdAt = System.currentTimeMillis(), updatedAt = System.currentTimeMillis()))
    }
    suspend fun updatePrompt(prompt: PromptEntity) {
        promptDao.updatePrompt(prompt.copy(updatedAt = System.currentTimeMillis()))
    }
    suspend fun deletePrompt(prompt: PromptEntity) = promptDao.softDeletePromptById(prompt.id, System.currentTimeMillis())

    suspend fun restorePrompt(promptId: Long) = promptDao.restorePromptById(promptId, System.currentTimeMillis())

    suspend fun permanentlyDeletePrompt(prompt: PromptEntity) = promptDao.hardDeletePrompt(prompt)

    // True Prompt Decks
    fun getPromptDecksByBookId(bookId: Long): Flow<List<PromptDeckEntity>> =
        promptDeckDao.getDecksByBookId(bookId)

    suspend fun getPromptDeckById(id: Long): PromptDeckEntity? = promptDeckDao.getDeckById(id)

    fun getPromptCardsByDeckId(deckId: Long): Flow<List<PromptCardEntity>> =
        promptCardDao.getCardsByDeckId(deckId)

    suspend fun getPromptCardsByDeckIdNow(deckId: Long): List<PromptCardEntity> =
        promptCardDao.getCardsByDeckIdNow(deckId)

    fun getPromptRunsByDeckId(deckId: Long): Flow<List<PromptRunEntity>> =
        promptRunDao.getRunsByDeckId(deckId)

    suspend fun getPromptRunsByDeckIdNow(deckId: Long): List<PromptRunEntity> =
        promptRunDao.getRunsByDeckIdNow(deckId)

    suspend fun insertPromptDeck(deck: PromptDeckEntity, seedDefaultCards: Boolean = false): Long {
        val now = System.currentTimeMillis()
        val id = promptDeckDao.insertDeck(deck.copy(createdAt = now, updatedAt = now))
        if (seedDefaultCards) seedDefaultPromptCards(id)
        return id
    }

    suspend fun updatePromptDeck(deck: PromptDeckEntity) {
        promptDeckDao.updateDeck(deck.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun deletePromptDeck(deck: PromptDeckEntity) = promptDeckDao.softDeleteDeckById(deck.id, System.currentTimeMillis())

    suspend fun restorePromptDeck(deckId: Long) = promptDeckDao.restoreDeckById(deckId, System.currentTimeMillis())

    suspend fun permanentlyDeletePromptDeck(deck: PromptDeckEntity) = promptDeckDao.hardDeleteDeck(deck)

    suspend fun insertPromptCard(card: PromptCardEntity): Long {
        val now = System.currentTimeMillis()
        val deck = promptDeckDao.getDeckById(card.deckId)
        val id = promptCardDao.insertCard(card.copy(createdAt = now, updatedAt = now))
        deck?.let { promptDeckDao.updateDeck(it.copy(updatedAt = now)) }
        return id
    }

    suspend fun updatePromptCard(card: PromptCardEntity) {
        val now = System.currentTimeMillis()
        promptCardDao.updateCard(card.copy(updatedAt = now))
        promptDeckDao.getDeckById(card.deckId)?.let { promptDeckDao.updateDeck(it.copy(updatedAt = now)) }
    }

    suspend fun deletePromptCard(card: PromptCardEntity) = promptCardDao.softDeleteCardById(card.id, System.currentTimeMillis())

    suspend fun restorePromptCard(cardId: Long) = promptCardDao.restoreCardById(cardId, System.currentTimeMillis())

    suspend fun permanentlyDeletePromptCard(card: PromptCardEntity) = promptCardDao.hardDeleteCard(card)

    suspend fun recordPromptRun(
        cardId: Long,
        inputValuesJson: String,
        renderedPrompt: String,
        outputText: String? = null,
        linkedAssetType: String? = null,
        linkedAssetId: Long? = null
    ): Long {
        val now = System.currentTimeMillis()
        val id = promptRunDao.insertRun(
            PromptRunEntity(
                promptCardId = cardId,
                inputValuesJson = inputValuesJson,
                renderedPrompt = renderedPrompt,
                outputText = outputText?.takeIf { it.isNotBlank() },
                linkedAssetType = linkedAssetType,
                linkedAssetId = linkedAssetId,
                createdAt = now
            )
        )
        promptCardDao.recordUse(cardId, now, now)
        promptCardDao.getCardById(cardId)?.let { card ->
            promptDeckDao.getDeckById(card.deckId)?.let { deck -> promptDeckDao.updateDeck(deck.copy(updatedAt = now)) }
        }
        return id
    }

    suspend fun createDefaultPromptDeck(bookId: Long, title: String, description: String? = null): Long {
        return insertPromptDeck(
            PromptDeckEntity(
                bookId = bookId,
                title = title.ifBlank { "Study prompt deck" },
                description = description
            ),
            seedDefaultCards = true
        )
    }

    private suspend fun seedDefaultPromptCards(deckId: Long) {
        val now = System.currentTimeMillis()
        val cards = listOf(
            PromptCardEntity(
                deckId = deckId,
                title = "Quiz generator",
                promptText = "Create exam-style questions from this material:\n\n{selectedQuestions}\n\nReturn questions with answer choices, correct answers, and explanations.",
                variablesJson = "[\"selectedQuestions\"]",
                outputType = PromptOutputType.QUIZ,
                sortOrder = 0,
                createdAt = now,
                updatedAt = now
            ),
            PromptCardEntity(
                deckId = deckId,
                title = "Flashcard generator",
                promptText = "Convert this material into concise flashcards. Use Front / Back / Hint format:\n\n{selectedQuestions}",
                variablesJson = "[\"selectedQuestions\"]",
                outputType = PromptOutputType.FLASHCARDS,
                sortOrder = 1,
                createdAt = now,
                updatedAt = now
            ),
            PromptCardEntity(
                deckId = deckId,
                title = "Blueprint maker",
                promptText = "Turn this content into a structured study blueprint with headings, bullets, mistakes to avoid, and review cues:\n\n{selectedQuestions}",
                variablesJson = "[\"selectedQuestions\"]",
                outputType = PromptOutputType.BLUEPRINT,
                sortOrder = 2,
                createdAt = now,
                updatedAt = now
            )
        )
        promptCardDao.insertCards(cards)
    }

    fun renderPromptTemplate(promptText: String, values: Map<String, String>): String {
        var rendered = promptText
        values.forEach { (key, value) ->
            rendered = rendered.replace("{$key}", value)
        }
        return rendered
    }

    suspend fun convertPromptOutputToNote(bookId: Long, title: String, outputText: String, promptCardId: Long? = null): Long {
        val noteId = insertNoteBlueprint(
            NoteBlueprintEntity(
                externalId = java.util.UUID.randomUUID().toString(),
                bookId = bookId,
                title = title.ifBlank { "Prompt output note" },
                summary = "Saved from prompt output.",
                body = outputText.ifBlank { "No output text was provided." },
                bulletPoints = emptyList(),
                tags = listOf("prompt-output"),
                blueprintMode = BlueprintMode.SIMPLE_NOTE,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        )
        promptCardId?.let { recordPromptRun(it, "{}", "", outputText, "NOTE", noteId) }
        return noteId
    }

    suspend fun convertPromptOutputToBlueprint(bookId: Long, title: String, outputText: String, promptCardId: Long? = null): Long {
        val noteId = insertNoteBlueprint(
            NoteBlueprintEntity(
                externalId = java.util.UUID.randomUUID().toString(),
                bookId = bookId,
                title = title.ifBlank { "Prompt blueprint" },
                summary = "Structured blueprint saved from prompt output.",
                body = outputText.ifBlank { "No output text was provided." },
                bulletPoints = outputText.lines().map { it.trim().trimStart('-', '*') }.filter { it.isNotBlank() }.take(12),
                tags = listOf("prompt-output", "blueprint"),
                blueprintMode = BlueprintMode.CONCEPT_TEMPLATE,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        )
        promptCardId?.let { recordPromptRun(it, "{}", "", outputText, "BLUEPRINT", noteId) }
        return noteId
    }

    suspend fun convertPromptOutputToFlashcards(bookId: Long, title: String, outputText: String, promptCardId: Long? = null): Long {
        val deckId = insertFlashcardDeck(
            FlashcardDeckEntity(
                externalId = java.util.UUID.randomUUID().toString(),
                bookId = bookId,
                title = title.ifBlank { "Prompt flashcards" },
                description = "Generated from prompt output."
            )
        )
        val cards = parsePromptOutputFlashcards(deckId, outputText)
        if (cards.isNotEmpty()) insertFlashcards(cards) else insertFlashcard(
            FlashcardEntity(
                externalId = java.util.UUID.randomUUID().toString(),
                deckId = deckId,
                frontText = title.ifBlank { "Prompt output" },
                backText = outputText.ifBlank { "No output text was provided." },
                tags = listOf("prompt-output")
            )
        )
        promptCardId?.let { recordPromptRun(it, "{}", "", outputText, "FLASHCARDS", deckId) }
        return deckId
    }

    private fun parsePromptOutputFlashcards(deckId: Long, outputText: String): List<FlashcardEntity> {
        val blocks = outputText.split(Regex("\\n\\s*\\n"))
        return blocks.mapIndexedNotNull { index, raw ->
            val lines = raw.lines().map { it.trim() }.filter { it.isNotBlank() }
            val front = lines.firstOrNull { it.startsWith("Front", ignoreCase = true) }
                ?.substringAfter(':', "")?.trim()
            val back = lines.firstOrNull { it.startsWith("Back", ignoreCase = true) }
                ?.substringAfter(':', "")?.trim()
            if (front.isNullOrBlank() || back.isNullOrBlank()) null else FlashcardEntity(
                externalId = java.util.UUID.randomUUID().toString(),
                deckId = deckId,
                frontText = front,
                backText = back,
                tags = listOf("prompt-output"),
                orderIndex = index
            )
        }
    }

    suspend fun getLibraryKnowledgeSummary(): KnowledgeSummary {
        val now = System.currentTimeMillis()
        return KnowledgeSummary(
            totalBooks = bookDao.countAll(),
            totalQuizzes = quizDao.countAll(),
            totalQuestions = questionDao.countAll(),
            unansweredQuestions = questionDao.countUnanswered(),
            questionsWithNotes = questionDao.countWithNotes(),
            questionsWithAssets = questionAssetDao.countDistinctQuestionsWithAssets(),
            questionsWithSources = questionAssetDao.countDistinctQuestionsWithSources(),
            markedQuestions = questionDao.countMarked(),
            droppedQuestions = questionDao.countDropped(),
            missedQuestions = questionDao.countMissed(),
            weakQuestions = questionDao.countWeak(),
            flashcardDecks = flashcardDeckDao.countAll(),
            totalFlashcards = flashcardDao.countAll(),
            dueFlashcards = flashcardDao.countDueFlashcards(now),
            weakFlashcards = flashcardDao.countWeakFlashcards(),
            totalBlueprints = noteBlueprintDao.countAll(),
            blueprintsDueForReview = noteBlueprintDao.countDueBlueprints(now),
            linkedBlueprints = noteBlueprintDao.countLinkedBlueprints(),
            promptDecks = promptDeckDao.countAll(),
            promptCards = promptCardDao.countAll(),
            promptRuns = promptRunDao.countAll(),
            savedPromptOutputs = promptRunDao.countSavedOutputs(),
            openMistakes = mistakeLogDao.countOpenMistakes(),
            fixedMistakes = mistakeLogDao.countFixedMistakes(),
            mistakesDueForReview = mistakeLogDao.countDueMistakes(now),
            pendingMistakesForReview = mistakeLogDao.countPendingMistakes(now)
        )
    }

    suspend fun getBookKnowledgeSummary(bookId: Long): BookKnowledgeSummary {
        val bundle = getBookStudyBundle(bookId) ?: return BookKnowledgeSummary(bookId)
        val questions = bundle.questions.filter { !it.isDropped }
        val allQuestionsWithDropped = bundle.questions
        val assets = questionAssetDao.getAssetsByBookId(bookId).first()
        var flashcards = 0
        for (deck in bundle.flashcardDecks) {
            flashcards += flashcardDao.countCardsInDeck(deck.id)
        }
        return BookKnowledgeSummary(
            bookId = bookId,
            totalQuizzes = bundle.quizzes.size,
            totalQuestions = questions.size,
            unansweredQuestions = questions.count { it.attempts == 0 },
            questionsWithNotes = questions.count { !it.notes.isNullOrBlank() },
            questionsWithAssets = assets.map { it.questionId }.distinct().size,
            questionsWithSources = assets.filter { it.sourceDocumentId != null }.map { it.questionId }.distinct().size,
            markedQuestions = questions.count { it.isMarked },
            droppedQuestions = allQuestionsWithDropped.count { it.isDropped },
            missedQuestions = questions.count { it.attempts > 0 && (it.correctCount < it.attempts || it.lastAttemptResult == false) },
            weakQuestions = questions.count { it.attempts >= 2 && it.correctCount * 2 < it.attempts },
            flashcardDecks = bundle.flashcardDecks.size,
            totalFlashcards = flashcards,
            totalBlueprints = bundle.noteBlueprints.size,
            promptDecks = promptDeckDao.countByBookId(bookId),
            promptCards = promptCardDao.countByBookId(bookId),
            promptRuns = promptRunDao.countByBookId(bookId),
            savedPromptOutputs = promptRunDao.countSavedOutputsByBookId(bookId),
            openMistakes = mistakeLogDao.getMistakesByBookId(bookId).first().count { !it.isFixed }
        )
    }

    suspend fun getQuizKnowledgeSummary(quizId: Long): QuizKnowledgeSummary {
        val allQuestions = questionDao.getQuestionsByQuizId(quizId).first()
        val questions = allQuestions.filter { !it.isDropped }
        val assets = questionAssetDao.getAssetsByQuizId(quizId).first()
        return QuizKnowledgeSummary(
            quizId = quizId,
            totalQuestions = questions.size,
            unansweredQuestions = questions.count { it.attempts == 0 },
            markedQuestions = questions.count { it.isMarked },
            droppedQuestions = allQuestions.count { it.isDropped },
            missedQuestions = questions.count { it.attempts > 0 && (it.correctCount < it.attempts || it.lastAttemptResult == false) },
            questionsWithNotes = questions.count { !it.notes.isNullOrBlank() },
            questionsWithAssets = assets.map { it.questionId }.distinct().size,
            questionsWithSources = assets.filter { it.sourceDocumentId != null }.map { it.questionId }.distinct().size
        )
    }

    // Sync Logic
    suspend fun syncDerivedAssets(questionId: Long) {
        val question = questionDao.getQuestionById(questionId) ?: return
        
        // Sync Flashcards
        val cards = flashcardDao.getFlashcardsBySourceQuestionId(questionId)
        cards.forEach { card ->
            if (card.syncConfig["text"] == true) {
                flashcardDao.updateFlashcard(card.copy(frontText = question.text, updatedAt = System.currentTimeMillis()))
            }
        }

        // Sync Slides
        val slides = courseSlideDao.getSlidesBySourceQuestionId(questionId)
        slides.forEach { slide ->
            if (slide.syncConfig["body"] == true) {
                courseSlideDao.updateSlide(slide.copy(body = question.text, updatedAt = System.currentTimeMillis()))
            }
        }

        // Sync Notes - update timestamp when source question changes
        val notes = noteBlueprintDao.getNotesBySourceQuestionId(questionId)
        notes.forEach { note ->
            noteBlueprintDao.updateNote(note.copy(updatedAt = System.currentTimeMillis()))
        }
    }

    // Learning Sessions Management
    fun getLearningSessionsByTarget(targetType: String, targetId: Long): Flow<List<KnowledgeStudySessionEntity>> =
        knowledgeStudySessionDao.getSessionsByTarget(targetType, targetId)

    fun getLearningSessionsByTargetType(targetType: String): Flow<List<KnowledgeStudySessionEntity>> =
        knowledgeStudySessionDao.getSessionsByTargetType(targetType)

    suspend fun getActiveSessionByTarget(targetType: String, targetId: Long): KnowledgeStudySessionEntity? =
        knowledgeStudySessionDao.getActiveSessionByTarget(targetType, targetId)

    suspend fun getLearningSessionById(sessionId: Long): KnowledgeStudySessionEntity? =
        knowledgeStudySessionDao.getSessionById(sessionId)

    suspend fun createLearningSession(targetType: String, targetId: Long, stateJson: String = ""): Long {
        val now = System.currentTimeMillis()
        val session = KnowledgeStudySessionEntity(
            targetType = targetType,
            targetId = targetId,
            stateJson = stateJson,
            isCompleted = false,
            createdAt = now,
            updatedAt = now
        )
        return knowledgeStudySessionDao.insertSession(session)
    }

    suspend fun updateLearningSession(session: KnowledgeStudySessionEntity) {
        knowledgeStudySessionDao.updateSession(session.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun completeLearningSession(sessionId: Long) {
        val session = knowledgeStudySessionDao.getSessionById(sessionId) ?: return
        knowledgeStudySessionDao.updateSession(session.copy(isCompleted = true, updatedAt = System.currentTimeMillis()))
    }

    suspend fun deleteLearningSession(session: KnowledgeStudySessionEntity) {
        knowledgeStudySessionDao.softDeleteSessionById(session.id, System.currentTimeMillis())
    }

    suspend fun restoreLearningSession(sessionId: Long) {
        knowledgeStudySessionDao.restoreSessionById(sessionId, System.currentTimeMillis())
    }

    suspend fun permanentlyDeleteLearningSession(session: KnowledgeStudySessionEntity) {
        knowledgeStudySessionDao.hardDeleteSession(session)
    }
}
