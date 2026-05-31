package com.ahmedyejam.mks.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "course_slides",
    foreignKeys = [
        ForeignKey(
            entity = SlideshowCourseEntity::class,
            parentColumns = ["id"],
            childColumns = ["courseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("courseId"),
        Index("sourceQuestionId"),
        Index("deletedAt")
    ]
)
data class CourseSlideEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val externalId: String,
    val courseId: Long,
    val title: String,
    val body: String,
    val speakerNotes: String? = null,
    val imagePath: String? = null,
    val orderIndex: Int = 0,
    val isCompleted: Boolean = false,
    val createdAt: Long,
    val updatedAt: Long,
    val sourceQuestionId: Long? = null,
    val syncConfig: Map<String, Boolean> = emptyMap(),
    val deletedAt: Long? = null
)
