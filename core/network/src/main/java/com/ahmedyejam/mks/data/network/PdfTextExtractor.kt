package com.ahmedyejam.mks.data.network

import android.content.Context
import android.net.Uri
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PdfTextExtractor @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun extractTextFromPages(pdfUri: Uri, pageNumbers: Set<Int>): String = withContext(Dispatchers.IO) {
        if (pageNumbers.isEmpty()) return@withContext ""

        var document: PDDocument? = null
        try {
            context.contentResolver.openInputStream(pdfUri)?.use { inputStream ->
                // Use a temporary file to avoid OutOfMemoryError on large books
                document = PDDocument.load(inputStream, com.tom_roush.pdfbox.io.MemoryUsageSetting.setupTempFileOnly())
                val stripper = PDFTextStripper()
                
                val builder = StringBuilder()
                // Sort pages so we extract in order
                val sortedPages = pageNumbers.sorted()
                
                for (page in sortedPages) {
                    // PDFTextStripper is 1-indexed
                    stripper.startPage = page + 1
                    stripper.endPage = page + 1
                    val text = stripper.getText(document)
                    builder.append(text).append("\n\n")
                }
                
                return@withContext builder.toString().trim()
            }
            return@withContext ""
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext "Error extracting text: ${e.message}"
        } finally {
            document?.close()
        }
    }
}
