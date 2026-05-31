package com.ahmedyejam.mks.data.local

import android.content.Context
import android.net.Uri
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import java.util.UUID

class FileManager(private val context: Context) {

    fun getContext(): Context = context

    fun saveBase64AsImage(base64String: String): String? {
        if (base64String.isBlank()) return null
        return try {
            val cleaned = base64String.replace(Regex("\s+"), "")
            val extension = inferDataUrlExtension(cleaned)
            val pureBase64 = cleaned.substringAfter(',', cleaned)
            val imageBytes = Base64.decode(pureBase64, Base64.DEFAULT)
            saveImage(imageBytes, extension)
        } catch (_: Exception) {
            null
        }
    }

    fun saveImage(base64String: String?): String? {
        if (base64String.isNullOrBlank()) return null
        val trimmed = base64String.trim()
        if (trimmed.startsWith("data:image/", ignoreCase = true)) return saveBase64AsImage(trimmed)
        if (trimmed.startsWith("/")) return trimmed.takeIf { File(it).exists() }
        if (trimmed.startsWith("file://")) return Uri.parse(trimmed).path?.takeIf { File(it).exists() }
        return saveBase64AsImage(trimmed)
    }

    fun saveImage(bytes: ByteArray, extension: String = "webp"): String? {
        return try {
            val safeExtension = sanitizeExtension(extension)
            val file = File(getImagesDir(), "img_${UUID.randomUUID()}.$safeExtension")
            file.writeBytes(bytes)
            file.absolutePath
        } catch (_: Exception) {
            null
        }
    }

    suspend fun downloadAndSaveImage(url: String): String? {
        if (!url.startsWith("http")) return url
        return try {
            val client = okhttp3.OkHttpClient()
            val request = okhttp3.Request.Builder().url(url).build()
            val response = withContext(Dispatchers.IO) { client.newCall(request).execute() }
            if (!response.isSuccessful) return null
            response.body?.use { body ->
                val bytes = body.bytes()
                val extension = url.substringAfterLast('.', "webp").substringBefore('?')
                saveImage(bytes, extension)
            }
        } catch (_: Exception) {
            null
        }
    }

    fun saveImage(inputStream: InputStream, originalName: String? = null): String? {
        return try {
            val extension = sanitizeExtension(originalName?.substringAfterLast('.', "webp") ?: "webp")
            val file = File(getImagesDir(), "img_${UUID.randomUUID()}.$extension")
            file.outputStream().use { output -> inputStream.copyTo(output) }
            file.absolutePath
        } catch (_: Exception) {
            null
        }
    }

    fun saveImage(uri: Uri): String? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { input -> saveImage(input, uri.lastPathSegment) }
        } catch (_: Exception) {
            null
        }
    }

    private fun getImagesDir(): File {
        val dir = File(context.filesDir, "images")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    fun getFile(path: String?): File? {
        if (path.isNullOrBlank()) return null
        val file = File(path)
        return if (file.exists()) file else null
    }

    fun getBase64Image(path: String?): String {
        if (path.isNullOrBlank()) return ""
        return try {
            val file = File(path)
            if (!file.exists()) return ""
            val bytes = file.readBytes()
            val mimeType = when (file.extension.lowercase()) {
                "png" -> "image/png"
                "jpg", "jpeg" -> "image/jpeg"
                "webp" -> "image/webp"
                "gif" -> "image/gif"
                "bmp" -> "image/bmp"
                else -> "image/webp"
            }
            "data:$mimeType;base64," + Base64.encodeToString(bytes, Base64.NO_WRAP)
        } catch (_: Exception) {
            ""
        }
    }

    fun deleteImage(path: String?) {
        if (path.isNullOrBlank()) return
        try {
            val file = File(path)
            if (file.exists()) file.delete()
        } catch (_: Exception) {
        }
    }

    private fun inferDataUrlExtension(value: String): String {
        val mime = Regex("""^data:image/([a-zA-Z0-9.+-]+);base64,""", RegexOption.IGNORE_CASE).find(value)?.groupValues?.getOrNull(1)
        return when (mime?.lowercase()) {
            "jpeg", "jpg" -> "jpg"
            "png" -> "png"
            "gif" -> "gif"
            "bmp" -> "bmp"
            "webp" -> "webp"
            "svg+xml" -> "svg"
            else -> "webp"
        }
    }

    private fun sanitizeExtension(extension: String): String {
        val cleaned = extension.lowercase().replace(Regex("[^a-z0-9]+"), "")
        return cleaned.ifBlank { "webp" }
    }
}
