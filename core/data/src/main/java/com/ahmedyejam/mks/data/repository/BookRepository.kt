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
class BookRepository @Inject constructor(

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
    private val noteCollectionDao: com.ahmedyejam.mks.data.local.dao.NoteCollectionDao,
    private val noteBlueprintDao: NoteBlueprintDao,
    private val promptDao: PromptDao,
    private val studySessionDao: com.ahmedyejam.mks.data.local.dao.StudySessionDao,
    private val knowledgeStudySessionDao: KnowledgeStudySessionDao,
    private val questionCategoryDao: QuestionCategoryDao,
    private val assetReferenceDao: AssetReferenceDao,
    private val questionAssetDao: QuestionAssetDao,
    private val sourceDocumentDao: SourceDocumentDao,
    private val promptDeckDao: PromptDeckDao,
    private val promptCardDao: PromptCardDao,
    private val promptRunDao: PromptRunDao,
    private val mistakeLogDao: MistakeLogDao,
    private val quizRepositoryProvider: javax.inject.Provider<QuizRepository>,
    private val assetRepositoryProvider: javax.inject.Provider<AssetRepository>,
    private val annotationDao: AnnotationDao,
    private val deletePreviewService: DeletePreviewService? = null,
    private val categoryMergePreviewService: CategoryMergePreviewService? = null,
    private val clearMarksPreviewService: ClearMarksPreviewService? = null,
    private val assetReferenceAuditService: AssetReferenceAuditService? = null

) {

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
    suspend fun ensureWorkspaceSettings(workspaceId: Long) {
        if (workspaceDao.getSettingsByWorkspaceId(workspaceId) == null) {
            workspaceDao.insertSettings(WorkspaceSettingsEntity(workspaceId = workspaceId))
        }
    }


    suspend fun getWorkspaceById(id: Long): WorkspaceEntity? = workspaceDao.getWorkspaceById(id)

    suspend fun getWorkspaceByIdIncludingDeleted(id: Long): WorkspaceEntity? = workspaceDao.getWorkspaceByIdIncludingDeleted(id)

    fun getDeletedWorkspaces(): Flow<List<WorkspaceEntity>> = workspaceDao.getDeletedWorkspacesFlow()

    suspend fun getWorkspaceByExternalId(externalId: String): WorkspaceEntity? = workspaceDao.getWorkspaceByExternalId(externalId)

    suspend fun insertWorkspace(workspace: WorkspaceEntity): Long = workspaceDao.insertWorkspace(workspace)

    suspend fun updateWorkspace(workspace: WorkspaceEntity) = workspaceDao.updateWorkspace(workspace)
    

    suspend fun deleteWorkspace(workspace: WorkspaceEntity) {
        val now = System.currentTimeMillis()
        workspaceDao.softDeleteWorkspaceById(workspace.id, now)
        bookDao.softDeleteBooksByWorkspaceId(workspace.id, now)
    }


    suspend fun restoreWorkspace(workspaceId: Long) {
        val workspace = workspaceDao.getWorkspaceByIdIncludingDeleted(workspaceId) ?: return
        val deletedAtFilter = workspace.deletedAt ?: return
        val now = System.currentTimeMillis()
        workspaceDao.restoreWorkspaceById(workspaceId, now)
        bookDao.restoreBooksByWorkspaceId(workspaceId, now, deletedAtFilter)
    }


    suspend fun permanentlyDeleteWorkspace(workspace: WorkspaceEntity) = workspaceDao.hardDeleteWorkspace(workspace)


    suspend fun getWorkspaceSettings(workspaceId: Long): WorkspaceSettingsEntity? = workspaceDao.getSettingsByWorkspaceId(workspaceId)

    suspend fun insertWorkspaceSettings(settings: WorkspaceSettingsEntity): Long = workspaceDao.insertSettings(settings)

    suspend fun updateWorkspaceSettings(settings: WorkspaceSettingsEntity) = workspaceDao.updateSettings(settings)

    // Approved Stage 2B/2C: marked/dropped workflows and mistake log.

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
        val mistakes = mistakeLogDao.getMistakesByBookId(bookId).first()
        val questionAssets = questionAssetDao.getAssetsByBookId(bookId).first()

        return BookStudyBundle(
            book = book,
            quizzes = quizzes,
            questionsByQuiz = questionsByQuiz,
            flashcardDecks = flashcardDecks,
            slideshowCourses = slideshowCourses,
            noteBlueprints = noteBlueprints,
            prompts = prompts,
            promptDecks = promptDecks,
            sourceDocuments = sourceDocuments,
            mistakes = mistakes,
            questionAssets = questionAssets
        )
    }

    suspend fun insertBook(book: BookEntity): Long {
        val workspaceId = if (book.workspaceId == 0L) {
            getOrCreateDefaultWorkspace().id
        } else {
            book.workspaceId
        }
        
        val finalBook = if (book.coverImage?.startsWith("http", ignoreCase = true) == true) {
            val localPath = fileManager.downloadAndSaveImage(book.coverImage!!)
            if (localPath != null) book.copy(workspaceId = workspaceId, coverImage = localPath) else book.copy(workspaceId = workspaceId)
        } else {
            book.copy(workspaceId = workspaceId)
        }
        val id = bookDao.insertBook(finalBook)
        assetRepositoryProvider.get().replaceOwnerAssetReferences("book", id, listOf(finalBook.coverImage))
        return id
    }

    suspend fun updateBook(book: BookEntity) {
        val updated = book.copy(updatedAt = System.currentTimeMillis(), lastEditedAt = System.currentTimeMillis())
        bookDao.updateBook(updated)
        assetRepositoryProvider.get().replaceOwnerAssetReferences("book", updated.id, listOf(updated.coverImage))
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
        assetRepositoryProvider.get().releaseBookTreeAssets(book)
        bookDao.hardDeleteBook(book)
    }

    // Quizzes

    suspend fun previewBookDeletion(bookId: Long) = deletePreviewService?.previewBookDeletion(bookId)
    suspend fun updateLastStudied(quizId: Long) {
        val now = System.currentTimeMillis()
        val quiz = quizDao.getQuizById(quizId) ?: return
        quizDao.updateQuiz(quiz.copy(lastStudiedAt = now))
        
        val book = bookDao.getBookById(quiz.bookId) ?: return
        bookDao.updateBook(book.copy(lastStudiedAt = now))
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




    fun getBookCompletion(bookId: Long): Flow<Float> = quizDao.getQuizzesByBookId(bookId)
        .flatMapLatest { quizzes ->
            if (quizzes.isEmpty()) return@flatMapLatest flowOf(0f)
            combine(quizzes.map { getQuizCompletion(it.id) }) { completions ->
                completions.average().toFloat()
            }
        }

    // --- Knowledge Bank CRUD and Sync ---

    // Slideshow Courses

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

    fun getBookQuestionCount(bookId: Long): Flow<Int> = quizDao.getBookQuestionCount(bookId)

    fun getDeletedBooks(workspaceId: Long): Flow<List<BookEntity>> = bookDao.getDeletedBooksByWorkspaceFlow(workspaceId)

    fun getQuizCompletion(quizId: Long): kotlinx.coroutines.flow.Flow<Float> = quizRepositoryProvider.get().getQuizCompletion(quizId)
}
