package com.ahmedyejam.mks.data.search

import com.ahmedyejam.mks.data.local.MksDatabase
import com.ahmedyejam.mks.data.local.dao.BookDao
import com.ahmedyejam.mks.data.local.dao.CourseSlideDao
import com.ahmedyejam.mks.data.local.dao.SearchIndexDao
import com.ahmedyejam.mks.data.local.dao.SlideshowCourseDao
import com.ahmedyejam.mks.data.local.dao.SourceDocumentDao
import com.ahmedyejam.mks.data.local.dao.AnnotationDao
import com.ahmedyejam.mks.data.local.dao.PromptDeckDao
import com.ahmedyejam.mks.data.local.dao.QuestionDao
import com.ahmedyejam.mks.data.local.dao.QuizDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchIndexManager @Inject constructor(
    private val database: MksDatabase,
    private val searchIndexDao: SearchIndexDao,
    private val bookDao: BookDao,
    private val quizDao: QuizDao,
    private val questionDao: QuestionDao,
    private val courseSlideDao: CourseSlideDao,
    private val annotationDao: AnnotationDao,
    private val slideshowCourseDao: SlideshowCourseDao,
    private val sourceDocumentDao: SourceDocumentDao,
    private val promptDeckDao: PromptDeckDao,
) {
    /** Rebuilds the entire FTS index from scratch. Call after import or bulk edits. */
    suspend fun rebuildIndex() = withContext(Dispatchers.IO) {
        searchIndexDao.clearAll()
        val books = bookDao.getAllBooksNow().filter { it.deletedAt == null }
        val activeBookIds = books.map { it.id }.toSet()

        // Books
        books.forEach { book ->
            execInsert(book.title, book.description ?: "", "", "BOOK", book.id.toString(), book.workspaceId, book.id, 0, 0, book.updatedAt)
        }

        // Quizzes
        quizDao.getAllQuizzesNow().filter { it.deletedAt == null && it.bookId in activeBookIds }.forEach { quiz ->
            val book = books.find { it.id == quiz.bookId } ?: return@forEach
            execInsert(quiz.title, quiz.description ?: "", quiz.category ?: "", "QUIZ", quiz.id.toString(), book.workspaceId, book.id, quiz.id, 0, quiz.updatedAt)
        }

        // Questions
        questionDao.getAllQuestionsNow().filter { !it.isDropped && it.deletedAt == null }.forEach { q ->
            val quiz = quizDao.getQuizById(q.quizId) ?: return@forEach
            if (quiz.deletedAt != null || quiz.bookId !in activeBookIds) return@forEach
            val book = books.find { it.id == quiz.bookId } ?: return@forEach
            val contentText = listOfNotNull(q.explanation, q.notes, q.additionalInfo, q.hint, q.reference).joinToString(" ")
            execInsert(q.text, quiz.title, contentText, "QUESTION", q.id.toString(), book.workspaceId, book.id, q.quizId, 0, q.updatedAt)
        }

        // Annotations
        annotationDao.getRecentAnnotations(500).forEach { ann ->
            val book = books.find { it.id == ann.bookId } ?: return@forEach
            execInsert(ann.noteBody ?: ann.selectedText ?: "Annotation", ann.ownerType, ann.selectedText ?: "", "ANNOTATION", ann.id.toString(), book.workspaceId, ann.bookId, 0, ann.ownerId, ann.updatedAt)
        }

        // Slides
        courseSlideDao.getUnfinishedSlides(500).forEach { slide ->
            val course = slideshowCourseDao.getCourseById(slide.courseId) ?: return@forEach
            if (course.deletedAt != null || course.bookId !in activeBookIds) return@forEach
            val book = books.find { it.id == course.bookId } ?: return@forEach
            execInsert(slide.title, course.title, slide.body ?: "", "SLIDE", slide.id.toString(), book.workspaceId, book.id, 0, course.id, slide.updatedAt)
        }

        // Source documents + Prompt decks (per book)
        books.forEach { book ->
            sourceDocumentDao.getSourcesByBookIdNow(book.id).filter { it.deletedAt == null }.forEach { src ->
                execInsert(src.title, src.author ?: "", src.description ?: "", "SOURCE", src.id.toString(), book.workspaceId, book.id, 0, 0, src.updatedAt)
            }
            promptDeckDao.getDecksByBookIdNow(book.id).filter { it.deletedAt == null }.forEach { deck ->
                execInsert(deck.title, deck.description ?: "", deck.tags.joinToString(" "), "PROMPT_DECK", deck.id.toString(), book.workspaceId, book.id, 0, 0, deck.updatedAt)
            }
        }
    }

    private fun execInsert(
        title: String, subtitle: String, content: String,
        entityType: String, entityId: String, workspaceId: Long,
        bookId: Long, quizId: Long, parentId: Long, updatedAt: Long,
    ) {
        val sql = "INSERT INTO search_index (title, subtitle, content, entityType, entityId, workspaceId, bookId, quizId, parentId, updatedAt) " +
            "VALUES ('${escape(title)}', '${escape(subtitle)}', '${escape(content)}', '${escape(entityType)}', '${escape(entityId)}', $workspaceId, $bookId, $quizId, $parentId, $updatedAt)"
        database.openHelper.writableDatabase.execSQL(sql)
    }

    private fun escape(value: String): String = value.replace("'", "''")

    /** Transforms a user query into FTS4 MATCH syntax with prefix matching. */
    fun prepareQuery(query: String): String {
        val cleaned = query.trim()
        if (cleaned.isEmpty()) return ""
        return cleaned.split(Regex("\\s+"))
            .filter { it.isNotBlank() }
            .joinToString(" ") { word ->
                val escaped = word.replace(Regex("[\"*:]"), "")
                if (escaped.isNotEmpty()) "$escaped*" else ""
            }
            .trim()
    }
}
