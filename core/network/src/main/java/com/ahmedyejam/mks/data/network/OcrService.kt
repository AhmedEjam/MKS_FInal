package com.ahmedyejam.mks.data.network

import android.util.Log
import com.ahmedyejam.mks.data.model.AiProviderConfig
import com.ahmedyejam.mks.data.model.McqPrompts
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.coroutineContext

private const val TAG = "OcrService"

/**
 * Orchestrates Vision-Language Model (VLM) OCR on a series of page images.
 *
 * 1. Takes base64-encoded page images.
 * 2. Feeds them to a vision model (e.g. GPT-4o, Gemini Flash) via [AiClient.generateWithImage].
 * 3. Applies an optional text-only review pass to clean up OCR artifacts.
 * 4. Combines the pages into a single chunk of text with "--- PAGE N ---" dividers.
 *
 * Port of `ocrService.js` from the DocQuiz AI desktop project.
 */
@Singleton
class OcrService @Inject constructor(private val aiClient: AiClient) {

    /**
     * Run the OCR pipeline on a list of page images.
     *
     * @param base64Images List of base64-encoded PNG/JPEG strings.
     * @param config       Provider config (MUST specify a vision-capable model).
     * @param ocrPrompt    System prompt for the vision pass (default: [McqPrompts.OCR_DEFAULT]).
     * @param reviewPrompt System prompt for the cleanup pass (default: [McqPrompts.OCR_REVIEW_DEFAULT]).
     * @param onProgress   Callback with `(pageIndex, totalPages)` for UI updates.
     * @return Extracted, cleaned, and combined text of all pages.
     */
    suspend fun processPages(
        base64Images: List<String>,
        config: AiProviderConfig,
        ocrPrompt: String = McqPrompts.OCR_DEFAULT,
        reviewPrompt: String? = McqPrompts.OCR_REVIEW_DEFAULT,
        onProgress: (suspend (Int, Int) -> Unit)? = null,
    ): String = withContext(Dispatchers.IO) {
        val total = base64Images.size
        val combinedText = StringBuilder()

        for ((index, b64) in base64Images.withIndex()) {
            if (!coroutineContext.isActive) throw CancellationException("OCR cancelled")

            // ── Pass 1: Vision Extraction ─────────────────────────────────────
            val rawOcr = runCatching {
                aiClient.generateWithImage(
                    config = config,
                    prompt = ocrPrompt,
                    images = listOf(b64)
                )
            }.getOrElse {
                Log.e(TAG, "Vision OCR failed on page ${index + 1}", it)
                "[OCR FAILED ON PAGE ${index + 1}: ${it.message}]"
            }

            // ── Pass 2: Text Cleanup (Optional) ───────────────────────────────
            var cleanedText = rawOcr
            if (!reviewPrompt.isNullOrBlank() && !rawOcr.startsWith("[OCR FAILED")) {
                cleanedText = runCatching {
                    aiClient.chatComplete(
                        config = config,
                        systemPrompt = reviewPrompt,
                        userMessage = rawOcr,
                        temperature = 0.0,
                        jsonMode = false,
                    )
                }.getOrElse {
                    Log.w(TAG, "OCR Review failed on page ${index + 1}, using raw OCR", it)
                    rawOcr
                }
            }

            combinedText.append("--- PAGE ${index + 1} ---\n\n")
            combinedText.append(cleanedText.trim())
            combinedText.append("\n\n")

            onProgress?.invoke(index + 1, total)
        }

        combinedText.toString().trim()
    }

    /**
     * Pipeline 2: Refines raw text extracted from a PDF via PdfBox (or another text extractor).
     * Bypasses the vision step and only runs the text cleanup model.
     *
     * @param rawText      The messy, raw text extracted from the PDF text layer.
     * @param config       Provider config for a fast text model.
     * @param reviewPrompt System prompt for the cleanup pass.
     * @return Cleaned, formatted text.
     */
    suspend fun refineRawText(
        rawText: String,
        config: AiProviderConfig,
        reviewPrompt: String = McqPrompts.OCR_REVIEW_DEFAULT
    ): String = withContext(Dispatchers.IO) {
        if (!coroutineContext.isActive) throw CancellationException("Refinement cancelled")

        runCatching {
            aiClient.chatComplete(
                config = config,
                systemPrompt = reviewPrompt,
                userMessage = rawText,
                temperature = 0.0, // Low temperature for factual cleanup
                jsonMode = false,
            )
        }.getOrElse {
            Log.e(TAG, "Raw text refinement failed", it)
            "[REFINEMENT FAILED: ${it.message}]\n\n$rawText"
        }
    }
}
