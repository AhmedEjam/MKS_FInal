package com.ahmedyejam.mks.util

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset

class ImportSizeLimitExceededException(message: String) : IllegalStateException(message)

fun InputStream.copyToWithLimit(output: OutputStream, maxBytes: Long): Long {
    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
    var total = 0L
    while (true) {
        val read = read(buffer)
        if (read == -1) break
        total += read.toLong()
        if (total > maxBytes) {
            throw ImportSizeLimitExceededException("Input exceeds ${maxBytes / (1024 * 1024)} MB limit.")
        }
        output.write(buffer, 0, read)
    }
    return total
}

fun InputStream.readTextWithLimit(maxBytes: Long): String {
    val output = ByteArrayOutputStream()
    copyToWithLimit(output, maxBytes)
    val bytes = output.toByteArray()

    // Try UTF-8 first
    var text = String(bytes, Charsets.UTF_8)

    // If UTF-8 contains multiple replacement characters, it might be an Excel CSV saved in Windows-1256 (Arabic)
    if (text.count { it == '\uFFFD' } > 5) {
        try {
            text = String(bytes, Charset.forName("windows-1256"))
        } catch (e: Exception) {
            // Ignore and fallback to UTF-8
        }
    }

    // Strip BOM if present
    if (text.startsWith("\uFEFF")) {
        text = text.substring(1)
    }

    return text
}

fun File.readTextWithLimit(maxBytes: Long): String {
    inputStream().use { input -> return input.readTextWithLimit(maxBytes) }
}
