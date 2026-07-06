package com.ahmedyejam.mks.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

object SourceDocumentTypes {
    const val BOOK = "Book"
    const val DOCUMENT = "Document"
    const val PDF = "PDF"
    const val IMAGE = "Image"
    const val VIDEO = "Video"
    const val AUDIO = "Audio"
    const val TABLESHEET = "Tablesheet"
    const val POWERPOINT = "Powerpoint"
    const val OTHERS = "Others"

    val all = listOf(BOOK, DOCUMENT, PDF, IMAGE, VIDEO, AUDIO, TABLESHEET, POWERPOINT, OTHERS)

    fun detectType(urlOrPath: String): String {
        val path = urlOrPath.lowercase()
        return when {
            path.endsWith(".pdf") -> PDF
            path.endsWith(".doc") || path.endsWith(".docx") || path.endsWith(".txt") -> DOCUMENT
            path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png") || path.endsWith(
                ".webp"
            ) || path.endsWith(".gif") -> IMAGE

            path.endsWith(".mp4") || path.endsWith(".mkv") || path.endsWith(".mov") || path.endsWith(
                ".avi"
            ) -> VIDEO

            path.endsWith(".mp3") || path.endsWith(".wav") || path.endsWith(".m4a") || path.endsWith(
                ".flac"
            ) -> AUDIO

            path.endsWith(".xls") || path.endsWith(".xlsx") || path.endsWith(".csv") -> TABLESHEET
            path.endsWith(".ppt") || path.endsWith(".pptx") -> POWERPOINT
            else -> OTHERS
        }
    }
}

@Entity(
    tableName = "source_documents",
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
        Index("title"),
        Index("sourceType"),
        Index("deletedAt")
    ]
)
@JsonClass(generateAdapter = true)
data class SourceDocumentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookId: Long? = null,
    val title: String,
    val sourceType: String = SourceDocumentTypes.OTHERS,
    val author: String? = null,
    val edition: String? = null,
    val year: String? = null,
    val publisher: String? = null,
    val localPath: String? = null,
    val externalUrl: String? = null,
    val description: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null
)
