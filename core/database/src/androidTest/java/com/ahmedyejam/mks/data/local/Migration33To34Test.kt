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
 * v33 -> v34 adds the three FSRS scheduling columns to `flashcards`.
 *
 * All three are NOT NULL DEFAULT 0, so the risk is not that the ALTER fails but that pre-existing
 * cards end up with NULL — which would crash the scheduler the first time it reads them back. This
 * seeds a card at v33 and asserts it survives with the defaults applied.
 */
@RunWith(AndroidJUnit4::class)
class Migration33To34Test {
    private val testDb = "migration-33-34-test"

    @get:Rule
    val helper: MigrationTestHelper =
        MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            MksDatabase::class.java,
        )

    @Test
    @Throws(IOException::class)
    fun migrate33To34_addsFsrsColumnsAndBackfillsExistingCards() {
        var db = helper.createDatabase(testDb, 33)

        // A flashcard deck and one card must exist before the migration so we can prove the
        // pre-existing row is backfilled rather than left NULL.
        db.execSQL(
            """
            INSERT INTO flashcard_decks (
                externalId, bookId, title, cardCount, studiedCount, masteryPercentage,
                isSystem, isPinned, createdAt, updatedAt, lastStudiedAt, lastEditedAt
            ) VALUES ('d1', 1, 'Deck', 1, 0, 0.0, 0, 0, 1000, 1000, 0, 1000)
            """.trimIndent(),
        )
        db.execSQL(
            """
            INSERT INTO flashcards (
                externalId, deckId, frontText, backText, tags, orderIndex, attempts,
                correctCount, lastReviewedAt, createdAt, updatedAt, syncConfig
            ) VALUES ('c1', 1, 'front', 'back', '[]', 0, 0, 0, 0, 1000, 1000, '{}')
            """.trimIndent(),
        )
        db.close()

        db = helper.runMigrationsAndValidate(testDb, 34, true, MksMigrations.MIGRATION_33_34)

        val columns = mutableSetOf<String>()
        db.query("PRAGMA table_info(flashcards)").use { cursor ->
            while (cursor.moveToNext()) {
                columns += cursor.getString(cursor.getColumnIndexOrThrow("name"))
            }
        }
        listOf("fsrsStability", "fsrsDifficulty", "fsrsReps").forEach { expected ->
            assertTrue("flashcards should have $expected column", expected in columns)
        }

        db.query("SELECT fsrsStability, fsrsDifficulty, fsrsReps FROM flashcards WHERE externalId = 'c1'").use { cursor ->
            assertTrue("seeded card should survive the migration", cursor.moveToFirst())
            assertEquals("fsrsStability should default to 0", 0.0f, cursor.getFloat(0), 0.0001f)
            assertEquals("fsrsDifficulty should default to 0", 0.0f, cursor.getFloat(1), 0.0001f)
            assertEquals("fsrsReps should default to 0", 0, cursor.getInt(2))
        }
    }
}
