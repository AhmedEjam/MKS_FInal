package com.ahmedyejam.mks.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "study_runs",
    indices = [
        Index("contentType"),
        Index("contentId"),
        Index("isCompleted"),
        Index("deletedAt"),
    ],
)
data class StudyRunEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val contentType: String,
    val contentId: Long,
    val orderedItemIds: List<Long> = emptyList(),
    val currentIndex: Int = 0,
    val completedItemIds: List<Long> = emptyList(),
    val isCompleted: Boolean = false,
    val startedAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val completedAt: Long = 0,
    val configurationJson: String? = null,
    val stateJson: String? = null,
    val deletedAt: Long? = null,
)
