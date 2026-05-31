package com.ahmedyejam.mks.data.repository

import androidx.room.withTransaction
import com.ahmedyejam.mks.data.import.model.ParsedQuestion
import com.ahmedyejam.mks.data.local.FileManager
import com.ahmedyejam.mks.data.local.MksDatabase
import com.ahmedyejam.mks.data.local.dao.BookDao
import com.ahmedyejam.mks.data.local.dao.QuestionDao
import com.ahmedyejam.mks.data.local.dao.QuizDao
import com.ahmedyejam.mks.data.local.dao.SessionDao
import com.ahmedyejam.mks.data.local.entity.CategoryMetadataEntity
import com.ahmedyejam.mks.data.local.entity.BookEntity
import com.ahmedyejam.mks.data.local.entity.QuestionEntity
import com.ahmedyejam.mks.data.local.entity.QuizEntity
import com.ahmedyejam.mks.data.local.entity.SessionEntity
import com.ahmedyejam.mks.data.model.CategoryWithMetadata
import com.ahmedyejam.mks.data.import.repository.ImportLibraryManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.File
import java.util.UUID

enum class SortOption { LAST_EDIT, LAST_STUDIED, TITLE, COMPLETION, QUESTION_COUNT }

class MksRepository(
    private val bookDao: BookDao,
    private val quizDao: QuizDao,
    private val questionDao: QuestionDao,
    private val sessionDao: SessionDao,
    private val categoryMetadataDao: com.ahmedyejam.mks.data.local.dao.CategoryMetadataDao,
    private val database: MksDatabase,
    private val fileManager: FileManager,
    private val exportManager: ExportManager? = null,
    private val importManager: ImportLibraryManager? = null
) {

    suspend fun importParsedQuestions(
        title: String,
        description: String,
        targetBookId: Long?,
        parsedQuestions: List<ParsedQuestion>
    ): Long {
        if (parsedQuestions.isEmpty()) throw IllegalArgumentException("No parsed questions to import")

        val quizId = database.withTransaction {
            val now = System.currentTimeMillis()
            val localBookId = targetBookId ?: bookDao.insertBook(
                BookEntity(
                    externalId = UUID.randomUUID().toString(),
                    title = title,
                    description = description,
                    createdAt = now,
                    updatedAt = now,
                    contentUpdatedAt = now,
                    lastEditedAt = now
                )
            )

            val localQuizId = quizDao.insertQuiz(
                QuizEntity(
                    externalId = UUID.randomUUID().toString(),
                    bookId = localBookId,
                    title = title,
                    description = description,
                    createdAt = now,
                    updatedAt = now,
                    contentUpdatedAt = now,
                    lastEditedAt = now
                )
            )

            val questionEntities = parsedQuestions.mapNotNull { parsed ->
                val hasImage = !parsed.imageDataUrl.isNullOrBlank() || !parsed.imageSource.isNullOrBlank()
                if (parsed.stem.isBlank() && parsed.options.isEmpty() && !hasImage) {
                    null
                } else {
                    val localImagePath = when {
                        !parsed.imageDataUrl.isNullOrBlank() -> fileManager.saveBase64AsImage(parsed.imageDataUrl)
                        !parsed.imageSource.isNullOrBlank() && parsed.imageSource.startsWith("http") -> fileManager.downloadAndSaveImage(parsed.imageSource) ?: parsed.imageSource
                        !parsed.imageSource.isNullOrBlank() && parsed.imageSource.startsWith("/") -> parsed.imageSource
                        else -> null
                    }

                    QuestionEntity(
                        externalId = UUID.randomUUID().toString(),
                        quizId = localQuizId,
                        text = parsed.stem,
                        type = if (parsed.correctAnswers.size > 1) QuestionType.MULTIPLE_CHOICE else QuestionType.SINGLE_CHOICE,
                        options = parsed.options.map { it.text },
                        correctAnswers = parsed.correctAnswers.mapNotNull { correctId ->
                            parsed.options.indexOfFirst { it.id == correctId }.takeIf { it >= 0 }
                        }.distinct().sorted(),
                        explanation = parsed.explanation?.takeIf { it.isNotBlank() },
                        hint = parsed.hint?.takeIf { it.isNotBlank() },
                        reference = parsed.reference?.takeIf { it.isNotBlank() },
                        imagePath = localImagePath,
                        imageSource = parsed.imageSource?.takeUnless { it.startsWith("data:") },
                        categories = parsed.categories.map { it.trim() }.filter { it.isNotBlank() }.distinct(),
                        additionalInfo = parsed.additionalInfo?.takeIf { it.isNotBlank() },
                        createdAt = now,
                        updatedAt = now,
                        lastEditedAt = now
                    )
                }
            }

            if (questionEntities.isNotEmpty()) {
                questionDao.insertQuestions(questionEntities)
            }

            localQuizId
        }

        refreshQuizStats(quizId)
        return quizId
    }

    // Books
    fun getAllBooks(sortBy: SortOption = SortOption.TITLE): Flow<List<BookEntity>> = bookDao.getAllBooksSortedFlow(sortBy.name)

    fun getAllFields(): Flow<List<String>> = bookDao.getAllBooksFlow().map { books ->
        books.flatMap { it.fields }.distinct().sorted()
    }

    suspend fun getBookById(id: Long) = bookDao.getBookById(id)
    suspend fun insertBook(book: BookEntity): Long {
        val finalBook = if (book.coverImage?.startsWith("http") == true) {
            val localPath = fileManager.downloadAndSaveImage(book.coverImage)
            if (localPath != null) book.copy(coverImage = localPath) else book
        } else {
            book
        }
        return bookDao.insertBook(finalBook)
    }
    suspend fun updateBook(book: BookEntity) {
        if (book.isSystem) {
            // Potentially restrict certain updates for system books
        }
        bookDao.updateBook(book.copy(updatedAt = System.currentTimeMillis(), lastEditedAt = System.currentTimeMillis()))
    }
    suspend fun deleteBook(book: BookEntity): BookEntity {
        if (book.isSystem) throw IllegalStateException("Cannot delete system book")
        fileManager.deleteImage(book.coverImage)
        // Also delete quiz images and question images for this book
        val quizzes = quizDao.getQuizzesByBookId(book.id).first()
        quizzes.forEach { quiz ->
            fileManager.deleteImage(quiz.coverImage)
            questionDao.getQuestionsByQuizId(quiz.id).first().forEach { 
                fileManager.deleteImage(it.imagePath)
            }
        }
        bookDao.deleteBook(book)
        return book
    }

    // Quizzes
    fun getQuizzesByBookId(bookId: Long, sortBy: SortOption = SortOption.TITLE): Flow<List<QuizEntity>> = quizDao.getQuizzesByBookIdSorted(bookId, sortBy.name)
    fun getQuizzesByCategory(category: String): Flow<List<QuizEntity>> = quizDao.getQuizzesByCategory(category)
    fun getAllQuizzesFlow(): Flow<List<QuizEntity>> = quizDao.getAllQuizzesFlow()
    fun getAllCategories(): Flow<List<String>> = quizDao.getAllCategories()
    suspend fun getQuizById(id: Long) = quizDao.getQuizById(id)
    suspend fun insertQuiz(quiz: QuizEntity): Long {
        val finalQuiz = if (quiz.coverImage?.startsWith("http") == true) {
            val localPath = fileManager.downloadAndSaveImage(quiz.coverImage)
            if (localPath != null) quiz.copy(coverImage = localPath) else quiz
        } else {
            quiz
        }
        
        val id = quizDao.insertQuiz(finalQuiz.copy(
            createdAt = if (finalQuiz.createdAt == 0L) System.currentTimeMillis() else finalQuiz.createdAt,
            updatedAt = System.currentTimeMillis()
        ))
        refreshBookStats(finalQuiz.bookId)
        return id
    }
    suspend fun updateQuiz(quiz: QuizEntity) {
        quizDao.updateQuiz(quiz.copy(updatedAt = System.currentTimeMillis(), lastEditedAt = System.currentTimeMillis()))
        refreshBookStats(quiz.bookId)
    }

    // Questions
    fun getQuestionsByQuizId(quizId: Long): Flow<List<QuestionEntity>> = questionDao.getQuestionsByQuizId(quizId)
    fun searchAllQuestions(query: String): Flow<List<QuestionEntity>> = questionDao.searchAllQuestionsFlow(query)
    fun getQuestionsByCategoryFlow(category: String): Flow<List<QuestionEntity>> = questionDao.getQuestionsByCategoryFlow(category)
    suspend fun getQuestionsByCategory(category: String): List<QuestionEntity> = questionDao.getQuestionsByCategory(category)
    suspend fun getQuestionById(id: Long) = questionDao.getQuestionById(id)
    suspend fun insertQuestion(question: QuestionEntity): Long {
        val finalQuestion = if (question.imagePath?.startsWith("http") == true) {
            val localPath = fileManager.downloadAndSaveImage(question.imagePath)
            if (localPath != null) question.copy(imagePath = localPath) else question
        } else {
            question
        }
        
        val id = questionDao.insertQuestion(finalQuestion.copy(
            createdAt = if (finalQuestion.createdAt == 0L) System.currentTimeMillis() else finalQuestion.createdAt,
            updatedAt = System.currentTimeMillis()
        ))
        refreshQuizStats(finalQuestion.quizId)
        return id
    }
    suspend fun insertQuestions(questions: List<QuestionEntity>): List<Long> {
        val now = System.currentTimeMillis()
        val updatedQuestions = questions.map { question ->
            val finalQuestion = if (question.imagePath?.startsWith("http") == true) {
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
        questions.map { it.quizId }.distinct().forEach { refreshQuizStats(it) }
        return ids
    }
    suspend fun updateQuestion(question: QuestionEntity) {
        questionDao.updateQuestion(question.copy(updatedAt = System.currentTimeMillis(), lastEditedAt = System.currentTimeMillis()))
        refreshQuizStats(question.quizId)
    }
    suspend fun deleteQuestion(question: QuestionEntity) {
        fileManager.deleteImage(question.imagePath)
        questionDao.deleteQuestion(question)
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
    suspend fun deleteSession(session: SessionEntity) = sessionDao.deleteSession(session)

    suspend fun deleteQuiz(quiz: QuizEntity): QuizEntity {
        if (quiz.isSystem) throw IllegalStateException("Cannot delete system quiz")
        fileManager.deleteImage(quiz.coverImage)
        // Also delete question images
        questionDao.getQuestionsByQuizId(quiz.id).first().forEach { 
            fileManager.deleteImage(it.imagePath)
        }
        quizDao.deleteQuiz(quiz)
        refreshBookStats(quiz.bookId)
        return quiz
    }

    private suspend fun refreshQuizStats(quizId: Long) {
        quizDao.refreshQuestionCount(quizId)
        val completion = getQuizCompletion(quizId).first()
        quizDao.updateCompletionPercentage(quizId, completion)
        
        // Propagate to book
        val quiz = quizDao.getQuizById(quizId)
        if (quiz != null) {
            refreshBookStats(quiz.bookId)
        }
    }

    private suspend fun refreshBookStats(bookId: Long) {
        val count = quizDao.getBookQuestionCount(bookId).first()
        bookDao.updateQuestionCount(bookId, count)
        val completion = getBookCompletion(bookId).first()
        bookDao.updateCompletionPercentage(bookId, completion)
    }

    private suspend fun updateLastStudied(quizId: Long) {
        val now = System.currentTimeMillis()
        val quiz = quizDao.getQuizById(quizId) ?: return
        quizDao.updateQuiz(quiz.copy(lastStudiedAt = now))
        
        val book = bookDao.getBookById(quiz.bookId) ?: return
        bookDao.updateBook(book.copy(lastStudiedAt = now))
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
        val questions = questionDao.getQuestionsByCategory(category)
        val updatedQuestions = questions.map { question ->
            question.copy(categories = question.categories.filter { it != category })
        }
        questionDao.insertQuestions(updatedQuestions)

        // 3. Delete metadata
        categoryMetadataDao.deleteMetadataByName(category)
    }

    suspend fun getMergePreview(source: String, target: String): Int {
        val questionsFromSource = questionDao.getQuestionsByCategory(source)
        val questionsInTarget = questionDao.getQuestionsByCategory(target).map { it.id }.toSet()
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
        val questions = questionDao.getQuestionsByCategory(oldName)
        val updatedQuestions = questions.map { question ->
            val newCategories = question.categories.map { if (it == oldName) newName else it }.distinct()
            question.copy(categories = newCategories)
        }
        questionDao.insertQuestions(updatedQuestions)

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
        val questions = questionDao.getQuestionsByCategory(source)
        val updatedQuestions = questions.map { question ->
            val newCategories = (question.categories.filter { it != source } + target).distinct()
            question.copy(categories = newCategories)
        }
        questionDao.insertQuestions(updatedQuestions)

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

        val questions = questionDao.getQuestionsByCategory(category)
        val duplicatedQuestions = questions.map { q ->
            q.copy(
                id = 0, // New ID for new quiz
                externalId = java.util.UUID.randomUUID().toString(),
                quizId = quizId,
                attempts = 0,
                correctCount = 0
            )
        }
        questionDao.insertQuestions(duplicatedQuestions)
        return quizId
    }

    // Category Metadata
    fun getCategoryMetadata() = categoryMetadataDao.getAllMetadata()
    suspend fun updateCategoryMetadata(metadata: com.ahmedyejam.mks.data.local.entity.CategoryMetadataEntity) = 
        categoryMetadataDao.insertMetadata(metadata)

    fun getAllCategoriesWithMetadata(): Flow<List<CategoryWithMetadata>> = 
        combine(
            questionDao.getAllQuestionsFlow(),
            quizDao.getAllQuizzesFlow(),
            categoryMetadataDao.getAllMetadata()
        ) { allQuestions: List<QuestionEntity>, allQuizzes: List<QuizEntity>, metadataList: List<CategoryMetadataEntity> ->
            val fromQuestions = allQuestions.flatMap { it.categories }
            val fromQuizzes = allQuizzes.mapNotNull { it.category }
            val allNames = (fromQuestions + fromQuizzes).distinct().filter { it.isNotBlank() }
            
            allNames.map { name ->
                val qCount = allQuestions.count { it.categories.contains(name) || allQuizzes.find { quiz -> quiz.id == it.quizId }?.category == name }
                CategoryWithMetadata(
                    name = name,
                    questionCount = qCount,
                    metadata = metadataList.find { it.name == name }
                )
            }.sortedWith(compareByDescending<CategoryWithMetadata> { it.isPinned }.thenBy { it.name })
        }

    fun getCategoryQuestionCount(category: String): Flow<Int> = 
        questionDao.getQuestionsByCategoryFlow(category).map { it.size }


    suspend fun exportQuizToZip(quizId: Long, outputStream: java.io.OutputStream) {
        exportManager?.exportQuizToZip(quizId, outputStream)
    }

    suspend fun exportBundleToZip(bookId: Long, outputStream: java.io.OutputStream) {
        exportManager?.exportBundleToZip(bookId, outputStream)
    }

    suspend fun exportAllToZip(outputStream: java.io.OutputStream) {
        exportManager?.exportAllToZip(outputStream)
    }

    suspend fun getImportPreview(uri: android.net.Uri) = importManager?.getImportPreview(uri)

    suspend fun importFromUri(uri: android.net.Uri, onProgress: (Float, String) -> Unit = {_, _ -> }): com.ahmedyejam.mks.data.import.model.ImportResult? {
        return importManager?.import(uri = uri, onProgress = onProgress)
    }

    fun saveImage(uri: android.net.Uri): String? = fileManager.saveImage(uri)

    fun resolveImagePath(path: String?): String? {
        if (path.isNullOrBlank()) return null
        if (path.startsWith("/") || path.startsWith("http") || path.startsWith("content://") || path.startsWith("assets/")) return path
        // Assume relative to internal images directory
        return File(fileManager.getContext().filesDir, "images/$path").absolutePath
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

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getBookCompletion(bookId: Long): Flow<Float> = quizDao.getQuizzesByBookId(bookId)
        .flatMapLatest { quizzes ->
            if (quizzes.isEmpty()) return@flatMapLatest flowOf(0f)
            combine(quizzes.map { getQuizCompletion(it.id) }) { completions ->
                completions.average().toFloat()
            }
        }
}
