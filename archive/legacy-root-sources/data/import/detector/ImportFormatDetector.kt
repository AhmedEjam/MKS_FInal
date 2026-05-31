package com.ahmedyejam.mks.data.import.detector

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.ahmedyejam.mks.data.import.model.ImportFormat
import java.io.BufferedInputStream
import java.io.InputStream
import java.nio.charset.Charset
import java.util.Locale

class ImportFormatDetector(private val context: Context) {

    fun detectFormat(uri: Uri): ImportFormat {
        val displayName = getDisplayName(uri)
        val extension = extractExtension(displayName)
            ?: extractExtension(uri.lastPathSegment)
            ?: extractExtension(uri.path)

        detectByExtension(extension)?.let { return it }
        detectByMimeType(context.contentResolver.getType(uri))?.let { return it }

        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                detectFromStream(inputStream)
            } ?: ImportFormat.UNKNOWN
        } catch (_: Exception) {
            ImportFormat.UNKNOWN
        }
    }

    private fun getDisplayName(uri: Uri): String? {
        if (uri.scheme != "content") return uri.lastPathSegment
        return try {
            context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
                if (!cursor.moveToFirst()) return@use null
                val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index >= 0) cursor.getString(index) else null
            }
        } catch (_: Exception) {
            uri.lastPathSegment
        }
    }

    private fun extractExtension(name: String?): String? {
        val safeName = name?.substringAfterLast('/')?.trim().orEmpty()
        if (!safeName.contains('.')) return null
        return safeName.substringAfterLast('.', "").lowercase(Locale.ROOT).takeIf { it.isNotBlank() }
    }

    private fun detectByExtension(extension: String?): ImportFormat? {
        return when (extension) {
            "json" -> ImportFormat.JSON
            "zip" -> ImportFormat.ZIP
            "xlsx", "xlsm", "xltx", "xltm", "xls" -> ImportFormat.XLSX
            "csv", "tsv" -> ImportFormat.CSV_TSV
            "txt" -> ImportFormat.TEXT
            "html", "htm" -> ImportFormat.HTML
            else -> null
        }
    }

    private fun detectByMimeType(mimeType: String?): ImportFormat? {
        val mime = mimeType?.lowercase(Locale.ROOT) ?: return null
        return when {
            "spreadsheetml" in mime || "ms-excel" in mime -> ImportFormat.XLSX
            mime == "text/csv" || mime == "text/tab-separated-values" || mime.contains("csv") -> ImportFormat.CSV_TSV
            mime.contains("json") -> ImportFormat.JSON
            mime.contains("zip") || mime.contains("compressed") -> ImportFormat.ZIP
            mime.contains("html") -> ImportFormat.HTML
            mime.startsWith("text/") -> ImportFormat.TEXT
            else -> null
        }
    }

    private fun detectFromStream(inputStream: InputStream): ImportFormat {
        val buffered = BufferedInputStream(inputStream)
        buffered.mark(4096)
        val header = ByteArray(4096)
        val bytesRead = buffered.read(header)
        if (bytesRead <= 0) return ImportFormat.UNKNOWN

        if (bytesRead >= 4 &&
            header[0] == 0x50.toByte() &&
            header[1] == 0x4B.toByte() &&
            header[2] == 0x03.toByte() &&
            header[3] == 0x04.toByte()) {
            val headerAscii = String(header, 0, bytesRead, Charset.forName("US-ASCII"))
            return if (headerAscii.contains("xl/") || headerAscii.contains("[Content_Types].xml")) {
                ImportFormat.XLSX
            } else {
                ImportFormat.ZIP
            }
        }

        if (bytesRead >= 8 &&
            header[0] == 0xD0.toByte() &&
            header[1] == 0xCF.toByte() &&
            header[2] == 0x11.toByte() &&
            header[3] == 0xE0.toByte()) {
            return ImportFormat.XLSX
        }

        val headerString = String(header, 0, bytesRead)
        val firstVisibleChar = headerString.firstOrNull { !it.isWhitespace() }
        if (firstVisibleChar == '{' || firstVisibleChar == '[') return ImportFormat.JSON

        val lowered = headerString.lowercase(Locale.ROOT)
        if (lowered.contains("<!doctype html") || lowered.contains("<html") || lowered.contains("<body")) {
            return ImportFormat.HTML
        }

        if (isLikelyTabular(headerString)) return ImportFormat.CSV_TSV
        return ImportFormat.TEXT
    }

    private fun isLikelyTabular(content: String): Boolean {
        val lines = content.lines().map { it.trim() }.filter { it.isNotEmpty() }.take(6)
        if (lines.size < 2) return false
        for (separator in listOf('	', ',', ';')) {
            val counts = lines.map { line -> line.count { it == separator } }
            if (counts.all { it > 0 } && counts.maxOrNull() == counts.minOrNull()) return true
        }
        return false
    }
}
