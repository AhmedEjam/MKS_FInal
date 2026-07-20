package com.ahmedyejam.mks.data.repository

import com.ahmedyejam.mks.data.local.MksDatabase
import com.ahmedyejam.mks.data.local.dao.*
import com.ahmedyejam.mks.data.local.entity.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SoftDeleteCascadeTest {

    private lateinit var database: MksDatabase
    private lateinit var bookDao: BookDao
    private lateinit var quizDao: QuizDao
    private lateinit var questionDao: QuestionDao
    private lateinit var workspaceDao: WorkspaceDao
    private lateinit var sessionDao: SessionDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        database = Room.inMemoryDatabaseBuilder(context, MksDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        bookDao = database.bookDao()
        quizDao = database.quizDao()
        questionDao = database.questionDao()
        workspaceDao = database.workspaceDao()
        sessionDao = database.sessionDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun deletingQuiz_softDeletesQuestionsAndSessions() = runTest {
        val wsId = workspaceDao.insertWorkspace(WorkspaceEntity(externalId = "ws", name = "WS", createdAt = System.currentTimeMillis()))
        val bookId = bookDao.insertBook(BookEntity(workspaceId = wsId, title = "Book", externalId = "book"))
        val quizId = quizDao.insertQuiz(QuizEntity(bookId = bookId, title = "Quiz", externalId = "quiz"))
        val qId = questionDao.insertQuestion(QuestionEntity(
            quizId = quizId, externalId = "q1", text = "Question 1",
            type = QuestionType.SINGLE_CHOICE, options = listOf("A", "B"), correctAnswers = listOf(0),
        ))
        val sId = sessionDao.insertSession(SessionEntity(quizId = quizId, label = "Session 1"))

        val now = System.currentTimeMillis()
        questionDao.softDeleteQuestionsByQuizId(quizId, now)
        sessionDao.softDeleteSessionsByQuizId(quizId, now)
        quizDao.softDeleteQuizById(quizId, now)

        val quiz = quizDao.getQuizByIdIncludingDeleted(quizId)
        val question = questionDao.getQuestionsByQuizIdIncludingDeleted(quizId)
        val session = sessionDao.getSessionById(sId)

        assertNotNull(quiz)
        assertTrue(quiz!!.deletedAt != null)
        assertEquals(1, question.size)
        assertTrue(question.first().deletedAt != null)
        // Session soft-deleted
        assertNull(session)
    }

    @Test
    fun restoringQuiz_doesNotResurrectPreviouslyDeletedQuestions() = runTest {
        val wsId = workspaceDao.insertWorkspace(WorkspaceEntity(externalId = "ws", name = "WS", createdAt = System.currentTimeMillis()))
        val bookId = bookDao.insertBook(BookEntity(workspaceId = wsId, title = "Book", externalId = "book"))
        val quizId = quizDao.insertQuiz(QuizEntity(bookId = bookId, title = "Quiz", externalId = "quiz"))
        val q1Id = questionDao.insertQuestion(QuestionEntity(
            quizId = quizId, externalId = "q1", text = "Q1",
            type = QuestionType.SINGLE_CHOICE, options = listOf("A"), correctAnswers = listOf(0),
        ))
        val q2Id = questionDao.insertQuestion(QuestionEntity(
            quizId = quizId, externalId = "q2", text = "Q2",
            type = QuestionType.SINGLE_CHOICE, options = listOf("B"), correctAnswers = listOf(0),
        ))

        // The cascade-restore discriminates by exact deletedAt timestamp, so the standalone
        // deletion and the cascade deletion must carry distinct times — which is what happens in
        // reality, where a user deletes a question and only later deletes the quiz. Using one
        // shared `now` for both makes the two cases indistinguishable and the assertion below
        // unsatisfiable against a correct implementation.
        val questionDeletedAt = System.currentTimeMillis()
        val quizDeletedAt = questionDeletedAt + 1_000L

        // Delete q1 on its own first, then delete the quiz (cascading the remaining questions).
        // softDeleteQuestionsByQuizId is guarded by `deletedAt IS NULL`, so q1 keeps its original
        // timestamp rather than being restamped by the cascade.
        questionDao.softDeleteQuestionById(q1Id, questionDeletedAt)
        questionDao.softDeleteQuestionsByQuizId(quizId, quizDeletedAt)
        quizDao.softDeleteQuizById(quizId, quizDeletedAt)

        // Restore quiz
        val restoreTime = quizDeletedAt + 1_000L
        quizDao.restoreQuizById(quizId, restoreTime)
        // Restore only the questions that were deleted as part of the quiz deletion
        questionDao.restoreQuestionsByQuizId(quizId, restoreTime, quizDeletedAt)

        val activeQuestions = questionDao.getQuestionsByQuizIdNow(quizId)
        // q1 was deleted BEFORE the quiz deletion, so it should NOT be restored
        assertEquals(1, activeQuestions.size)
        assertEquals("q2", activeQuestions.first().externalId)
    }

    @Test
    fun repeatedInserts_areIdempotent_byExternalId() = runTest {
        val wsId = workspaceDao.insertWorkspace(WorkspaceEntity(externalId = "ws", name = "WS", createdAt = System.currentTimeMillis()))
        val bookId = bookDao.insertBook(BookEntity(workspaceId = wsId, title = "Book", externalId = "book"))
        val quizId = quizDao.insertQuiz(QuizEntity(bookId = bookId, title = "Quiz", externalId = "quiz"))

        // Insert same question twice (REPLACE on conflict)
        val qId1 = questionDao.insertQuestion(QuestionEntity(
            quizId = quizId, externalId = "q1", text = "Original",
            type = QuestionType.SINGLE_CHOICE, options = listOf("A"), correctAnswers = listOf(0),
        ))
        val qId2 = questionDao.insertQuestion(QuestionEntity(
            id = qId1, quizId = quizId, externalId = "q1", text = "Updated",
            type = QuestionType.SINGLE_CHOICE, options = listOf("A"), correctAnswers = listOf(0),
        ))

        assertEquals(qId1, qId2)
        val questions = questionDao.getQuestionsByQuizIdNow(quizId)
        assertEquals(1, questions.size)
        assertEquals("Updated", questions.first().text)
    }
}
