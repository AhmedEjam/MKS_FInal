package com.ahmedyejam.mks.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "slideshow_courses",
    foreignKeys = [
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("bookId"),
        Index("deletedAt")
    ]
)
data class SlideshowCourseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val externalId: String,
    val bookId: Long,
    val title: String,
    val description: String? = null,
    val coverImage: String? = null,
    val slideCount: Int = 0,
    val studiedSlideCount: Int = 0,
    val progress: Float = 0f,
    val isSystem: Boolean = false,
    val isPinned: Boolean = false,
    val createdAt: Long,
    val updatedAt: Long,
    val lastStudiedAt: Long = 0,
    val lastEditedAt: Long,
    val isDerived: Boolean = false,
    val sourceQuizId: Long? = null,
    val deletedAt: Long? = null
)
