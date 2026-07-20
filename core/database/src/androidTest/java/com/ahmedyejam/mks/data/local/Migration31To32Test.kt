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
 * v31 -> v32 creates the `study_runs` table plus its four indexes.
 *
 * Asserts the indexes explicitly, not just the table: `StudyRunDao.getLatestIncomplete` filters on
 * contentType + contentId + isCompleted, and losing those indexes degrades silently into a full
 * scan rather than failing outright.
 */
@RunWith(AndroidJUnit4::class)
class Migration31To32Test {
    private val testDb = "migration-31-32-test"

    @get:Rule
    val helper: MigrationTestHelper =
        MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            MksDatabase::class.java,
        )

    @Test
    @Throws(IOException::class)
    fun migrate31To32_createsStudyRunsTableWithIndexes() {
        helper.createDatabase(testDb, 31).close()

        val db = helper.runMigrationsAndValidate(testDb, 32, true, MksMigrations.MIGRATION_31_32)

        val columns = mutableSetOf<String>()
        db.query("PRAGMA table_info(study_runs)").use { cursor ->
            while (cursor.moveToNext()) {
                columns += cursor.getString(cursor.getColumnIndexOrThrow("name"))
            }
        }
        assertTrue("study_runs table should exist", columns.isNotEmpty())
        listOf(
            "id", "contentType", "contentId", "orderedItemIds", "currentIndex",
            "completedItemIds", "isCompleted", "startedAt", "updatedAt", "completedAt",
            "configurationJson", "stateJson", "deletedAt",
        ).forEach { expected ->
            assertTrue("study_runs should have $expected column", expected in columns)
        }

        val indexes = mutableSetOf<String>()
        db.query("PRAGMA index_list(study_runs)").use { cursor ->
            while (cursor.moveToNext()) {
                indexes += cursor.getString(cursor.getColumnIndexOrThrow("name"))
            }
        }
        listOf(
            "index_study_runs_contentType",
            "index_study_runs_contentId",
            "index_study_runs_isCompleted",
            "index_study_runs_deletedAt",
        ).forEach { expected ->
            assertTrue("study_runs should have $expected index", expected in indexes)
        }
    }

    @Test
    @Throws(IOException::class)
    fun migrate31To32_studyRunsAcceptsInsertAndDefaultsApply() {
        helper.createDatabase(testDb, 31).close()
        val db = helper.runMigrationsAndValidate(testDb, 32, true, MksMigrations.MIGRATION_31_32)

        // Insert using only the non-defaulted columns; the rest must fall back to their defaults.
        db.execSQL(
            "INSERT INTO study_runs (contentType, contentId) VALUES ('FLASHCARD_DECK', 42)",
        )

        db.query(
            "SELECT orderedItemIds, currentIndex, completedItemIds, isCompleted, deletedAt " +
                "FROM study_runs WHERE contentId = 42",
        ).use { cursor ->
            assertTrue("inserted study run should be readable", cursor.moveToFirst())
            assertEquals("orderedItemIds should default to an empty JSON array", "[]", cursor.getString(0))
            assertEquals("currentIndex should default to 0", 0, cursor.getInt(1))
            assertEquals("completedItemIds should default to an empty JSON array", "[]", cursor.getString(2))
            assertEquals("isCompleted should default to false", 0, cursor.getInt(3))
            assertTrue("deletedAt should be null for a live run", cursor.isNull(4))
        }
    }
}
