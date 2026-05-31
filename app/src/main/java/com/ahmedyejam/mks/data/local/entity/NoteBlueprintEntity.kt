package com.ahmedyejam.mks.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

object BlueprintMode {
    const val SIMPLE_NOTE = "SIMPLE_NOTE"
    const val OUTLINE = "OUTLINE"
    const val CHECKLIST = "CHECKLIST"
    const val ALGORITHM = "ALGORITHM"
    const val DISEASE_TEMPLATE = "DISEASE_TEMPLATE"
    const val DRUG_TEMPLATE = "DRUG_TEMPLATE"
    const val CONCEPT_TEMPLATE = "CONCEPT_TEMPLATE"
    const val MISTAKE_REVIEW = "MISTAKE_REVIEW"
    const val CUSTOM = "CUSTOM"

    val firstTemplates = listOf(DISEASE_TEMPLATE, DRUG_TEMPLATE, CONCEPT_TEMPLATE, MISTAKE_REVIEW)
}

object BlueprintReviewStatus {
    const val NEW = "NEW"
    const val REVIEWING = "REVIEWING"
    const val REVIEWED = "REVIEWED"
    const val NEEDS_UPDATE = "NEEDS_UPDATE"
}

@Entity(
    tableName = "note_blueprints",
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
        Index("sourceQuestionId"),
        Index("blueprintMode"),
        Index("reviewStatus"),
        Index("deletedAt")
    ]
)
data class NoteBlueprintEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val externalId: String,
    val bookId: Long,
    val title: String,
    val summary: String? = null,
    val body: String,
    @ColumnInfo(defaultValue = "'[]'") val bulletPoints: List<String> = emptyList(),
    @ColumnInfo(defaultValue = "'[]'") val tags: List<String> = emptyList(),
    @ColumnInfo(defaultValue = "'SIMPLE_NOTE'") val blueprintMode: String = BlueprintMode.SIMPLE_NOTE,
    @ColumnInfo(defaultValue = "'[]'") val linkedQuestionsJson: String = "[]",
    @ColumnInfo(defaultValue = "'[]'") val linkedAssetsJson: String = "[]",
    @ColumnInfo(defaultValue = "'NEW'") val reviewStatus: String = BlueprintReviewStatus.NEW,
    @ColumnInfo(defaultValue = "0") val reviewCount: Int = 0,
    @ColumnInfo(defaultValue = "0") val lastReviewedAt: Long = 0,
    val createdAt: Long,
    val updatedAt: Long,
    val sourceQuestionId: Long? = null,
    val deletedAt: Long? = null
)
