package com.ahmedyejam.mks.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ahmedyejam.mks.data.local.entity.NoteBlueprintEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteBlueprintDao {
    @Query("SELECT * FROM note_blueprints WHERE bookId = :bookId AND deletedAt IS NULL ORDER BY updatedAt DESC")
    fun getNotesByBookId(bookId: Long): Flow<List<NoteBlueprintEntity>>

    @Query("SELECT * FROM note_blueprints WHERE bookId = :bookId AND deletedAt IS NULL ORDER BY updatedAt DESC")
    suspend fun getNotesByBookIdNow(bookId: Long): List<NoteBlueprintEntity>

    @Query("SELECT * FROM note_blueprints WHERE id = :id AND deletedAt IS NULL")
    suspend fun getNoteById(id: Long): NoteBlueprintEntity?

    @Query("SELECT * FROM note_blueprints WHERE deletedAt IS NULL AND (sourceQuestionId = :questionId OR linkedQuestionsJson LIKE '%' || :questionId || '%') ORDER BY updatedAt DESC")
    suspend fun getNotesBySourceQuestionId(questionId: Long): List<NoteBlueprintEntity>

    @Query("SELECT * FROM note_blueprints WHERE bookId = :bookId AND deletedAt IS NULL AND (sourceQuestionId = :questionId OR linkedQuestionsJson LIKE '%' || :questionId || '%') ORDER BY updatedAt DESC")
    fun getNotesByLinkedQuestion(bookId: Long, questionId: Long): Flow<List<NoteBlueprintEntity>>


    @Query("SELECT COUNT(*) FROM note_blueprints WHERE deletedAt IS NULL")
    suspend fun countAll(): Int

    @Query("SELECT COUNT(*) FROM note_blueprints WHERE deletedAt IS NULL AND (sourceQuestionId IS NOT NULL OR linkedQuestionsJson != '[]' OR linkedAssetsJson != '[]')")
    suspend fun countLinkedBlueprints(): Int

    @Query("SELECT * FROM note_blueprints WHERE deletedAt IS NULL AND reviewStatus != 'REVIEWED' AND updatedAt <= :now ORDER BY lastReviewedAt ASC, updatedAt DESC LIMIT :limit")
    suspend fun getDueBlueprints(now: Long, limit: Int = 50): List<NoteBlueprintEntity>

    @Query("SELECT COUNT(*) FROM note_blueprints WHERE deletedAt IS NULL AND reviewStatus != 'REVIEWED' AND updatedAt <= :now")
    suspend fun countDueBlueprints(now: Long): Int

    @Query("UPDATE note_blueprints SET reviewStatus = 'REVIEWED', reviewCount = reviewCount + 1, lastReviewedAt = :reviewedAt, updatedAt = :reviewedAt WHERE id = :id")
    suspend fun markReviewed(id: Long, reviewedAt: Long)

    @Query("UPDATE note_blueprints SET reviewStatus = 'REVIEWING', updatedAt = :nextReviewAt WHERE id = :id")
    suspend fun snoozeBlueprint(id: Long, nextReviewAt: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteBlueprintEntity): Long

    @Update
    suspend fun updateNote(note: NoteBlueprintEntity)

    @Query("UPDATE note_blueprints SET deletedAt = :deletedAt, updatedAt = :deletedAt WHERE id = :noteId")
    suspend fun softDeleteNoteById(noteId: Long, deletedAt: Long)

    @Query("UPDATE note_blueprints SET deletedAt = NULL, updatedAt = :updatedAt WHERE id = :noteId")
    suspend fun restoreNoteById(noteId: Long, updatedAt: Long)

    @Delete
    suspend fun hardDeleteNote(note: NoteBlueprintEntity)
}
