package com.ahmedyejam.mks.data.repository

import com.ahmedyejam.mks.data.local.MksDatabase
import com.ahmedyejam.mks.data.local.dao.*
import com.ahmedyejam.mks.data.local.entity.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ahmedyejam.mks.data.local.FileManager
import io.mockk.mockk
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class WorkspaceIsolationTest {

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
    fun booksInWorkspaceA_areNotReturnedForWorkspaceB() = runTest {
        val wsA = workspaceDao.insertWorkspace(WorkspaceEntity(externalId = "ws-a", name = "Workspace A", createdAt = System.currentTimeMillis()))
        val wsB = workspaceDao.insertWorkspace(WorkspaceEntity(externalId = "ws-b", name = "Workspace B", createdAt = System.currentTimeMillis()))

        bookDao.insertBook(BookEntity(workspaceId = wsA, title = "Book A", externalId = "book-a"))
        bookDao.insertBook(BookEntity(workspaceId = wsB, title = "Book B", externalId = "book-b"))

        val booksA = bookDao.getBooksByWorkspaceNow(wsA)
        val booksB = bookDao.getBooksByWorkspaceNow(wsB)

        assertEquals(1, booksA.size)
        assertEquals("Book A", booksA.first().title)
        assertEquals(1, booksB.size)
        assertEquals("Book B", booksB.first().title)
    }

    @Test
    fun deletedBooksInWorkspaceA_notReturnedForWorkspaceB() = runTest {
        val wsA = workspaceDao.insertWorkspace(WorkspaceEntity(externalId = "ws-a", name = "Workspace A", createdAt = System.currentTimeMillis()))
        val wsB = workspaceDao.insertWorkspace(WorkspaceEntity(externalId = "ws-b", name = "Workspace B", createdAt = System.currentTimeMillis()))

        val bookAId = bookDao.insertBook(BookEntity(workspaceId = wsA, title = "Book A", externalId = "book-a"))
        bookDao.insertBook(BookEntity(workspaceId = wsB, title = "Book B", externalId = "book-b"))

        bookDao.softDeleteBookById(bookAId, System.currentTimeMillis())

        val deletedA = bookDao.getDeletedBooksByWorkspaceFlow(wsA).first()
        val deletedB = bookDao.getDeletedBooksByWorkspaceFlow(wsB).first()

        assertEquals(1, deletedA.size)
        assertEquals(0, deletedB.size)
    }
}
