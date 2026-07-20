package com.ahmedyejam.mks.data.local

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * v32 -> v33 creates the `search_index` FTS4 table and backfills it from existing content.
 *
 * This is the most data-sensitive migration in the set: it reads every book, quiz, question,
 * flashcard, mistake, annotation and blueprint at upgrade time. Two failure modes matter and
 * neither surfaces as a crash — soft-deleted content leaking into search results, and live content
 * silently missing from the index. Both are asserted here.
 */
@RunWith(AndroidJUnit4::class)
class Migration32To33Test {
    private val testDb = "migration-32-33-test"

    @get:Rule
    val helper: MigrationTestHelper =
        MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            MksDatabase::class.java,
        )

    private fun seedV32(db: androidx.sqlite.db.SupportSQLiteDatabase) {
        db.execSQL(
            "INSERT INTO workspaces (externalId, name, isDefault, createdAt, updatedAt) " +
                "VALUES ('w1', 'Workspace One', 1, 1000, 1000)",
        )
        // Live book (id 1) and a soft-deleted book (id 2).
        db.execSQL(
            """
            INSERT INTO books (
                workspaceId, externalId, title, description, createdAt, updatedAt, contentUpdatedAt,
                lastStudiedAt, lastEditedAt, isPinned, isSystem, fields, questionCount,
                answeredCount, totalAttempts, completionPercentage, accuracyPercentage
            ) VALUES (1, 'b1', 'Pharmacology', 'Live book', 1000, 1000, 1000, 0, 1000, 0, 0, '[]', 0, 0, 0, 0.0, 0.0)
            """.trimIndent(),
        )
        db.execSQL(
            """
            INSERT INTO books (
                workspaceId, externalId, title, description, createdAt, updatedAt, contentUpdatedAt,
                lastStudiedAt, lastEditedAt, isPinned, isSystem, fields, questionCount,
                answeredCount, totalAttempts, completionPercentage, accuracyPercentage, deletedAt
            ) VALUES (1, 'b2', 'DeletedBookTitle', 'Trashed book', 1000, 1000, 1000, 0, 1000, 0, 0, '[]', 0, 0, 0, 0.0, 0.0, 5000)
            """.trimIndent(),
        )
        db.execSQL(
            """
            INSERT INTO quizzes (
                externalId, bookId, title, description, createdAt, updatedAt, contentUpdatedAt,
                lastStudiedAt, lastEditedAt, isPinned, isSystem, questionCount, answeredCount,
                totalAttempts, completionPercentage, accuracyPercentage
            ) VALUES ('q1', 1, 'Antibiotics', 'Quiz description', 1000, 1000, 1000, 0, 1000, 0, 0, 0, 0, 0, 0.0, 0.0)
            """.trimIndent(),
        )
        db.execSQL(
            """
            INSERT INTO questions (
                externalId, quizId, text, type, options, correctAnswers, weight, attempts,
                correctCount, isDropped, isMarked, categories, createdAt, updatedAt, lastStudiedAt,
                lastEditedAt, timeSpentMs, consecutiveCorrect, explanation
            ) VALUES ('qq1', 1, 'Which drug inhibits cell wall synthesis?', 'SINGLE_CHOICE', '[]', '[]',
                      1, 0, 0, 0, 0, '[]', 1000, 1000, 0, 1000, 0, 0, 'Penicillin explanation')
            """.trimIndent(),
        )
    }

    @Test
    @Throws(IOException::class)
    fun migrate32To33_createsAndBackfillsSearchIndex() {
        var db = helper.createDatabase(testDb, 32)
        seedV32(db)
        db.close()

        db = helper.runMigrationsAndValidate(testDb, 33, true, MksMigrations.MIGRATION_32_33)

        db.query("SELECT COUNT(*) FROM search_index").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertTrue("search_index should be populated by the backfill", cursor.getInt(0) > 0)
        }

        // The live book must be indexed with its workspace attached — GlobalSearchRepository
        // post-filters on workspaceId, so an unset value would make the row unreachable.
        db.query(
            "SELECT workspaceId, bookId FROM search_index WHERE entityType = 'BOOK' AND title = 'Pharmacology'",
        ).use { cursor ->
            assertTrue("live book should be indexed", cursor.moveToFirst())
            assertEquals("indexed book should carry its workspaceId", "1", cursor.getString(0))
            assertEquals("indexed book should carry its bookId", "1", cursor.getString(1))
        }

        db.query("SELECT COUNT(*) FROM search_index WHERE entityType = 'QUIZ'").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals("the single live quiz should be indexed", 1, cursor.getInt(0))
        }

        db.query("SELECT COUNT(*) FROM search_index WHERE entityType = 'QUESTION'").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals("the single live question should be indexed", 1, cursor.getInt(0))
        }
    }

    @Test
    @Throws(IOException::class)
    fun migrate32To33_excludesSoftDeletedContent() {
        var db = helper.createDatabase(testDb, 32)
        seedV32(db)
        db.close()

        db = helper.runMigrationsAndValidate(testDb, 33, true, MksMigrations.MIGRATION_32_33)

        db.query("SELECT COUNT(*) FROM search_index WHERE title = 'DeletedBookTitle'").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals(
                "soft-deleted content must not be backfilled into the search index",
                0,
                cursor.getInt(0),
            )
        }
    }

    @Test
    @Throws(IOException::class)
    fun migrate32To33_indexIsSearchableViaMatch() {
        var db = helper.createDatabase(testDb, 32)
        seedV32(db)
        db.close()

        db = helper.runMigrationsAndValidate(testDb, 33, true, MksMigrations.MIGRATION_32_33)

        // The whole point of the FTS4 table is MATCH, so exercise it rather than only SELECTing.
        db.query("SELECT title FROM search_index WHERE search_index MATCH 'Antibiotics'").use { cursor ->
            assertTrue("MATCH should find the indexed quiz", cursor.moveToFirst())
            assertEquals("Antibiotics", cursor.getString(0))
        }

        // Content column is tokenized too, not just title.
        db.query("SELECT COUNT(*) FROM search_index WHERE search_index MATCH 'Penicillin'").use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertTrue("explanation text should be searchable via the content column", cursor.getInt(0) > 0)
        }
    }
}
