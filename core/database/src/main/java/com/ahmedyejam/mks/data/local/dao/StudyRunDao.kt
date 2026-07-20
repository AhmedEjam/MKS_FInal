package com.ahmedyejam.mks.data.local.dao

import androidx.room.*
import com.ahmedyejam.mks.data.local.entity.StudyRunEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyRunDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(run: StudyRunEntity): Long

    @Update
    suspend fun update(run: StudyRunEntity)

    @Query("SELECT * FROM study_runs WHERE id = :id AND deletedAt IS NULL")
    suspend fun getById(id: Long): StudyRunEntity?

    @Query("SELECT * FROM study_runs WHERE contentType = :contentType AND contentId = :contentId AND isCompleted = 0 AND deletedAt IS NULL ORDER BY updatedAt DESC LIMIT 1")
    suspend fun getLatestIncomplete(contentType: String, contentId: Long): StudyRunEntity?

    @Query("SELECT * FROM study_runs WHERE isCompleted = 0 AND deletedAt IS NULL ORDER BY updatedAt DESC")
    suspend fun getAllIncomplete(): List<StudyRunEntity>

    @Query("UPDATE study_runs SET isCompleted = 1, completedAt = :completedAt, updatedAt = :completedAt WHERE id = :id")
    suspend fun markCompleted(id: Long, completedAt: Long = System.currentTimeMillis())

    @Query("UPDATE study_runs SET deletedAt = :deletedAt, updatedAt = :deletedAt WHERE id = :id")
    suspend fun softDelete(id: Long, deletedAt: Long = System.currentTimeMillis())

    @Query("SELECT * FROM study_runs WHERE deletedAt IS NOT NULL ORDER BY updatedAt DESC")
    fun getDeletedRuns(): Flow<List<StudyRunEntity>>
}
