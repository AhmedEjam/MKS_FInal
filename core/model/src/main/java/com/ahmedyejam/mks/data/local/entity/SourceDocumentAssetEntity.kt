package com.ahmedyejam.mks.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

object SourceAssetType {
    const val IMAGE = "image"
    const val PDF = "pdf"
    const val AUDIO = "audio"
    const val VIDEO = "video"
    const val TEXT_NOTE = "text_note"
    const val WEB_LINK = "web_link"
    const val OTHER = "other"
}

@Entity(
    tableName = "source_document_assets",
    foreignKeys = [
        ForeignKey(
            entity = SourceDocumentEntity::class,
            parentColumns = ["id"],
            childColumns = ["sourceDocumentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("sourceDocumentId"),
        Index("assetType"),
        Index("createdAt"),
        Index("deletedAt")
    ]
)
@JsonClass(generateAdapter = true)
data class SourceDocumentAssetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sourceDocumentId: Long,
    val assetType: String,
    val title: String,
    val description: String? = null,
    val localPath: String? = null,
    val externalUrl: String? = null,
    val mimeType: String? = null,
    val fileName: String? = null,
    val fileSizeBytes: Long? = null,
    val textContent: String? = null,
    @ColumnInfo(defaultValue = "0") val sortOrder: Int = 0,
    @ColumnInfo(defaultValue = "0") val isPinned: Boolean = false,
    @ColumnInfo(defaultValue = "0") val isPrimary: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null
)
