package com.ahmedyejam.mks.data.network

import android.util.Log
import com.ahmedyejam.mks.data.model.AiProviderConfig
import com.ahmedyejam.mks.data.model.McqPrompts
import com.ahmedyejam.mks.data.model.ParsedMcq
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.coroutineContext

private const val TAG = "McqService"

/**
 * Configuration for a single MCQ generation run.
 *
 * @param provider          AI provider config (model + baseUrl + key).
 * @param chapterNum        Chapter/section number label, e.g. "2".
 * @param sectionName       Human-readable section title.
 * @param extractionPrompt  System prompt for the extraction pass (default: [McqPrompts.EXT_DEFAULT]).
 * @param generationPrompt  System prompt for the generation fallback (default: [McqPrompts.GEN_DEFAULT]).
 * @param reviewPrompt      System prompt for the enrichment review (null = skip review pass).
 * @param reviewEnabled     Whether to run the review/enrichment pass.
 * @param chunkSizeChars    Max characters per chunk sent to the LLM (default ≈6 000 ≈ 1 500 tokens).
 */
data class McqRunConfig(
    val provider: AiProviderConfig,
    val chapterNum: String = "1",
    val sectionName: String = "Section",
    val extractionPrompt: String = McqPrompts.EXT_DEFAULT,
    val generationPrompt: String = McqPrompts.GEN_DEFAULT,
    val reviewPrompt: String? = McqPrompts.REV_DEFAULT,
    val reviewEnabled: Boolean = true,
    val chunkSizeChars: Int = 6_000,
)

/** Progress event emitted during MCQ generation. */
data class McqProgress(
    val chunk: Int,
    val totalChunks: Int,
    val foundSoFar: Int,
)

/**
 * Orchestrates the 3-pass MCQ pipeline:
 *
 * 1. **Extraction** — tries to find existing MCQs in the text.
 * 2. **Generation** (fallback) — if extraction returns nothing, generates original MCQs.
 * 3. **Review** (optional) — enriches each MCQ with hint, high-yield, brief fields,
 *    and verifies the answer key.
 *
 * Port of `mcqService.js` from the DocQuiz AI desktop project.
 */
@Singleton
class McqService @Inject constructor(private val aiClient: AiClient) {

    /**
     * Run the full MCQ pipeline on [sectionText].
     *
     * @param sectionText Raw text of the section to process.
     * @param config      Run configuration.
     * @param onProgress  Optional progress callback (called after each chunk).
     * @return Deduplicated, renumbered list of [ParsedMcq].
     */
    suspend fun processMCQ(
        sectionText: String,
        config: McqRunConfig,
        onProgress: (suspend (McqProgress) -> Unit)? = null,
    ): List<ParsedMcq> = withContext(Dispatchers.IO) {
        val chunks = chunkText(sectionText, config.chunkSizeChars)
        val all = mutableListOf<ParsedMcq>()
        var qNum = 1

        for ((index, chunk) in chunks.withIndex()) {
            // Respect coroutine cancellation
            if (!coroutineContext.isActive) throw CancellationException("MCQ generation cancelled")

            val extractSys = McqPrompts.render(
                config.extractionPrompt,
                config.chapterNum,
                config.sectionName,
                qNum,
            )

            // ── Pass 1: Extraction ────────────────────────────────────────────
            var parsed = runCatching {
                val raw = aiClient.chatComplete(
                    config.provider,
                    extractSys,
                    chunk,
                    temperature = 0.1,
                    jsonMode = true,
                )
                ParsedMcq.parseJsonArray(raw)
            }.getOrElse {
                Log.w(TAG, "Extraction failed on chunk ${index + 1}: ${it.message}")
                emptyList()
            }

            // ── Pass 2: Generation fallback ───────────────────────────────────
            if (parsed.isEmpty()) {
                val genSys = McqPrompts.render(
                    config.generationPrompt,
                    config.chapterNum,
                    config.sectionName,
                    qNum,
                )
                parsed = runCatching {
                    val raw = aiClient.chatComplete(
                        config.provider,
                        genSys,
                        chunk,
                        temperature = 0.5,
                        jsonMode = true,
                    )
                    ParsedMcq.parseJsonArray(raw)
                }.getOrElse {
                    Log.w(TAG, "Generation fallback failed on chunk ${index + 1}: ${it.message}")
                    emptyList()
                }
            }

            // ── Pass 3: Review / Enrichment (optional) ────────────────────────
            val reviewPrompt = config.reviewPrompt
            if (config.reviewEnabled && !reviewPrompt.isNullOrBlank() && parsed.isNotEmpty()) {
                parsed = runCatching {
                    val reviewed = aiClient.chatComplete(
                        config.provider,
                        reviewPrompt,
                        org.json.JSONArray(parsed.map { it.toRawJson() }).toString(),
                        temperature = 0.1,
                        jsonMode = true,
                    )
                    val reviewedList = ParsedMcq.parseJsonArray(reviewed)
                    // Keep the original if review returns an empty or mismatched list
                    if (reviewedList.isNotEmpty()) reviewedList else parsed
                }.getOrElse {
                    Log.w(TAG, "Review pass failed on chunk ${index + 1}: ${it.message}")
                    parsed
                }
            }

            // Stamp the ch_q field sequentially
            val stamped = parsed.mapIndexed { i, mcq ->
                mcq.copy(chQ = "${config.chapterNum}-${qNum + i}")
            }
            all.addAll(stamped)
            qNum += stamped.size

            onProgress?.invoke(
                McqProgress(chunk = index + 1, totalChunks = chunks.size, foundSoFar = all.size)
            )
        }

        // ── Deduplication via Jaccard similarity on stem words ─────────────────
        deduplicate(all).also {
            Log.d(TAG, "Pipeline complete: ${all.size} raw → ${it.size} deduplicated MCQs")
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Helpers
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Split [text] into chunks of at most [maxCharsPerChunk] characters,
     * preferring to break at the page dividers emitted by the OCR service
     * ("--- PAGE N ---") or at paragraph boundaries.
     */
    internal fun chunkText(text: String, maxCharsPerChunk: Int = 6_000): List<String> {
        if (text.length <= maxCharsPerChunk) return listOf(text)

        // Try splitting on page dividers first
        val pageDivider = Regex("""---\s*PAGE\s*\d+\s*---""")
        val pages = pageDivider.split(text).filter { it.isNotBlank() }

        val chunks = mutableListOf<String>()
        val current = StringBuilder()

        for (page in pages) {
            if (current.length + page.length > maxCharsPerChunk && current.isNotEmpty()) {
                chunks.add(current.toString().trim())
                current.clear()
            }
            if (page.length > maxCharsPerChunk) {
                // Single page too large — split at paragraph boundaries
                val paragraphs = page.split("\n\n")
                for (para in paragraphs) {
                    if (current.length + para.length > maxCharsPerChunk && current.isNotEmpty()) {
                        chunks.add(current.toString().trim())
                        current.clear()
                    }
                    current.append(para).append("\n\n")
                }
            } else {
                current.append(page).append("\n\n")
            }
        }
        if (current.isNotEmpty()) chunks.add(current.toString().trim())
        return chunks.ifEmpty { listOf(text) }
    }

    /** Fuzzy deduplication using Jaccard similarity on stem word sets. */
    private fun deduplicate(mcqs: List<ParsedMcq>, threshold: Double = 0.85): List<ParsedMcq> {
        val unique = mutableListOf<ParsedMcq>()
        for (mcq in mcqs) {
            if (mcq.stem.isBlank()) continue
            val words = tokenize(mcq.stem)
            val isDuplicate = unique.any { existing ->
                jaccard(words, tokenize(existing.stem)) > threshold
            }
            if (!isDuplicate) unique.add(mcq)
        }
        // Renumber to keep sequence gap-free
        return unique.mapIndexed { i, mcq ->
            val chapter = mcq.chQ.substringBefore("-").ifBlank { "1" }
            mcq.copy(chQ = "$chapter-${i + 1}")
        }
    }

    private fun tokenize(s: String): Set<String> =
        s.lowercase().replace(Regex("[^a-z0-9\\s]"), "").split(Regex("\\s+")).filter { it.isNotBlank() }.toSet()

    private fun jaccard(a: Set<String>, b: Set<String>): Double {
        if (a.isEmpty() && b.isEmpty()) return 1.0
        val intersection = a.intersect(b).size
        val union = a.size + b.size - intersection
        return if (union == 0) 0.0 else intersection.toDouble() / union
    }
}

/** Convert a [ParsedMcq] back to a plain JSON object (for the review pass). */
private fun ParsedMcq.toRawJson(): org.json.JSONObject = org.json.JSONObject().apply {
    put("ch_q", chQ)
    put("stem", stem)
    if (stemBrief.isNotBlank()) put("stem_brief", stemBrief)
    put("options", org.json.JSONObject(options))
    key?.let { put("key", it) }
    if (explanation.isNotBlank()) put("explanation", explanation)
    if (explanationBrief.isNotBlank()) put("explanation_brief", explanationBrief)
    if (hint.isNotBlank()) put("hint", hint)
    if (highYield.isNotBlank()) put("high_yield", highYield)
    put("generated", generated)
}
