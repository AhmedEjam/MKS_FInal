package com.ahmedyejam.mks.data.repository

import com.ahmedyejam.mks.data.local.FileManager
import com.ahmedyejam.mks.data.local.dao.AssetReferenceDao
import com.ahmedyejam.mks.data.local.dao.BookDao
import com.ahmedyejam.mks.data.local.dao.QuestionDao
import com.ahmedyejam.mks.data.local.dao.QuizDao
import com.ahmedyejam.mks.data.local.entity.BookEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

data class IntegrityReport(
    val orphanedImageFiles: Int = 0,
    val missingReferencedFiles: Int = 0,
    val orphanedReferences: Int = 0,
    val staleBookCounters: Int = 0,
    val staleQuizCounters: Int = 0,
    val totalImageFiles: Int = 0,
    val totalReferences: Int = 0,
    val totalBooks: Int = 0,
    val totalQuizzes: Int = 0,
    val orphanedFilePaths: List<String> = emptyList(),
) {
    val hasIssues: Boolean
        get() = orphanedImageFiles > 0 || missingReferencedFiles > 0 || orphanedReferences > 0 || staleBookCounters > 0 || staleQuizCounters > 0

    val totalIssues: Int
        get() = orphanedImageFiles + missingReferencedFiles + orphanedReferences + staleBookCounters + staleQuizCounters
}

@Singleton
class DataIntegrityService @Inject constructor(
    private val fileManager: FileManager,
    private val assetReferenceDao: AssetReferenceDao,
    private val bookDao: BookDao,
    private val quizDao: QuizDao,
    private val questionDao: QuestionDao,
) {
    suspend fun checkIntegrity(): IntegrityReport = withContext(Dispatchers.IO) {
        val imageFiles = fileManager.listImageFiles()
        val references = assetReferenceDao.getAllReferences()
        val books = bookDao.getAllBooksNow().filter { it.deletedAt == null }
        val quizzes = quizDao.getAllQuizzesNow().filter { it.deletedAt == null }

        // 1. Find referenced image paths
        val referencedPaths = references
            .map { it.path }
            .filter { it.isNotBlank() }
            .map { File(it).canonicalPath }
            .toMutableSet()

        // Also check book covers and quiz covers
        books.forEach { book ->
            if (!book.coverImage.isNullOrBlank()) {
                referencedPaths.add(File(book.coverImage).canonicalPath)
            }
        }
        quizzes.forEach { quiz ->
            if (!quiz.coverImage.isNullOrBlank()) {
                referencedPaths.add(File(quiz.coverImage).canonicalPath)
            }
        }

        // 2. Find orphaned image files (files on disk with no DB reference)
        val orphanedFiles = imageFiles.filter { file ->
            try {
                file.canonicalPath !in referencedPaths
            } catch (e: Exception) {
                false
            }
        }

        // 3. Find missing referenced files (DB references with no file on disk)
        var missingFiles = 0
        references.forEach { ref ->
            if (ref.path.isNotBlank() && !File(ref.path).exists()) {
                missingFiles++
            }
        }

        // 4. Find orphaned references (references whose owner is deleted/missing)
        val orphanedRefs = references.count { ref ->
            when (ref.ownerType) {
                "book" -> books.none { it.id == ref.ownerId }
                "quiz" -> quizzes.none { it.id == ref.ownerId }
                "question" -> questionDao.getQuestionById(ref.ownerId) == null
                else -> false
            }
        }

        // 5. Check stale book counters
        var staleBooks = 0
        books.forEach { book ->
            val actualQuestionCount = quizDao.getBookQuestionCountNow(book.id)
            if (book.questionCount != actualQuestionCount) {
                staleBooks++
            }
        }

        // 6. Check stale quiz counters
        var staleQuizzes = 0
        quizzes.forEach { quiz ->
            val actualQuestions = questionDao.getQuestionsByQuizIdNow(quiz.id).filter { !it.isDropped }
            if (quiz.questionCount != actualQuestions.size) {
                staleQuizzes++
            }
        }

        IntegrityReport(
            orphanedImageFiles = orphanedFiles.size,
            missingReferencedFiles = missingFiles,
            orphanedReferences = orphanedRefs,
            staleBookCounters = staleBooks,
            staleQuizCounters = staleQuizzes,
            totalImageFiles = imageFiles.size,
            totalReferences = references.size,
            totalBooks = books.size,
            totalQuizzes = quizzes.size,
            orphanedFilePaths = orphanedFiles.map { it.absolutePath }.take(50),
        )
    }

    suspend fun repairOrphanedFiles(report: IntegrityReport): Int = withContext(Dispatchers.IO) {
        var deleted = 0
        report.orphanedFilePaths.forEach { path ->
            if (fileManager.deleteImageFile(path)) deleted++
        }
        deleted
    }

    suspend fun refreshAllCounters() = withContext(Dispatchers.IO) {
        // This will be called from the ViewModel which has access to repositories
        // The service just marks what needs refreshing; actual refresh is done via repositories
    }
}
