package com.ahmedyejam.mks.data.network

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import java.io.File

@Singleton
class PdfRendererService @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun getPageCount(pdfUri: Uri): Int = withContext(Dispatchers.IO) {
        var pfd: ParcelFileDescriptor? = null
        var renderer: PdfRenderer? = null
        try {
            pfd = context.contentResolver.openFileDescriptor(pdfUri, "r")
            if (pfd != null) {
                renderer = PdfRenderer(pfd)
                return@withContext renderer.pageCount
            }
            return@withContext 0
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext 0
        } finally {
            renderer?.close()
            pfd?.close()
        }
    }

    suspend fun renderPage(pdfUri: Uri, pageIndex: Int, densityDpi: Int = 300): Bitmap? = withContext(Dispatchers.IO) {
        var pfd: ParcelFileDescriptor? = null
        var renderer: PdfRenderer? = null
        var page: PdfRenderer.Page? = null
        try {
            pfd = context.contentResolver.openFileDescriptor(pdfUri, "r")
            if (pfd != null) {
                renderer = PdfRenderer(pfd)
                if (pageIndex in 0 until renderer.pageCount) {
                    page = renderer.openPage(pageIndex)
                    
                    // Default density mapping (72pt -> 300dpi scaling)
                    val scale = densityDpi / 72f
                    val width = (page.width * scale).toInt()
                    val height = (page.height * scale).toInt()
                    
                    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                    // Fill with white background
                    bitmap.eraseColor(android.graphics.Color.WHITE)
                    
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    return@withContext bitmap
                }
            }
            return@withContext null
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        } finally {
            page?.close()
            renderer?.close()
            pfd?.close()
        }
    }
}
