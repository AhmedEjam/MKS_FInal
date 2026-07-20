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
 * v30 -> v31 adds `sessions.draftAnswersByIndex`.
 *
 * The column is NOT NULL with a `'{}'` default, so the risk is not that the ALTER fails but that
 * pre-existing session rows end up with NULL or an empty string, either of which would blow up the
 * Moshi map converter the first time a user resumes an old session.
 */
@RunWith(AndroidJUnit4::class)
class Migration30To31Test {
    private val testDb = "migration-30-31-test"

    @get:Rule
    val helper: MigrationTestHelper =
        MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            MksDatabase::class.java,
        )

    @Test
    @Throws(IOException::class)
    fun migrate30To31_addsDraftAnswersColumnAndBackfillsExistingRows() {
        var db = helper.createDatabase(testDb, 30)

        db.execSQL(
            """
            INSERT INTO sessions (
                quizId, label, currentQuestionIndex, score, incorrectCount, answers, answersByIndex,
                isCompleted, createdAt, updatedAt, lastModifiedAt, lastStudiedAt, lastEditedAt,
                questionIds, originalQuestionCount, shuffleQuestions, shuffleOptions, rapidMode,
                repeatWrong, quizTimerSeconds, questionTimerSeconds, rangeFrom, rangeTo,
                includeFilters, droppedOptions, droppedOptionsByIndex, visibleOptionsCount,
                visibleOptionsCountByIndex, currentStreak, maxStreak, resultTaxonomy
            ) VALUES (
                1, 'Session in flight', 3, 2, 1, '{}', '{}',
                0, 1000, 1000, 1000, 1000, 1000,
                '[]', 10, 0, 0, 0,
                0, 0, 0, 0, 0,
                '[]', '{}', '{}', '{}',
                '{}', 0, 0, '{}'
            )
            """.trimIndent(),
        )
        db.close()

        db = helper.runMigrationsAndValidate(testDb, 31, true, MksMigrations.MIGRATION_30_31)

        db.query("PRAGMA table_info(sessions)").use { cursor ->
            var found = false
            var notNull = false
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndexOrThrow("name")) == "draftAnswersByIndex") {
                    found = true
                    notNull = cursor.getInt(cursor.getColumnIndexOrThrow("notnull")) == 1
                }
            }
            assertTrue("sessions should have draftAnswersByIndex column", found)
            assertTrue("draftAnswersByIndex should be NOT NULL", notNull)
        }

        // The row that existed before the migration must carry the default, not NULL.
        db.query("SELECT draftAnswersByIndex FROM sessions").use { cursor ->
            assertTrue("seeded session should survive the migration", cursor.moveToFirst())
            assertEquals(
                "pre-existing session should be backfilled with an empty JSON object",
                "{}",
                cursor.getString(0),
            )
        }
    }
}
