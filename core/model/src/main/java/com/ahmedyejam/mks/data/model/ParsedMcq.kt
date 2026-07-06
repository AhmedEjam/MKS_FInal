package com.ahmedyejam.mks.data.model

import com.ahmedyejam.mks.data.local.entity.QuestionEntity
import com.ahmedyejam.mks.data.local.entity.QuestionType
import org.json.JSONObject

/**
 * Domain model for a single AI-extracted or AI-generated MCQ.
 *
 * Port of `mcqSchema.js` from DocQuiz AI. All fields map 1-to-1 with the
 * JSON schema produced by [McqPrompts.EXT_DEFAULT] / [McqPrompts.GEN_DEFAULT].
 */
data class ParsedMcq(
    /** Compound identifier: "{chapter}-{questionNum}", e.g. "2-7". */
    val chQ: String = "",
    /** Full question stem. */
    val stem: String = "",
    /** ≤12-word summary of the concept tested (added by the review pass). */
    val stemBrief: String = "",
    /** Answer options keyed A–E. */
    val options: Map<String, String> = emptyMap(),
    /** Correct answer key, e.g. "B". Null if unknown. */
    val key: String? = null,
    /** Full explanation / rationale. */
    val explanation: String = "",
    /** ≤25-word explanation (added by the review pass). */
    val explanationBrief: String = "",
    /** Subtle reasoning clue that guides thinking without giving the answer away. */
    val hint: String = "",
    /** 2–3 high-yield facts or clinical pearls this question tests. */
    val highYield: String = "",
    /** True if this question was AI-generated (vs. extracted from existing text). */
    val generated: Boolean = false,
) {

    // ──────────────────────────────────────────────────────────────────────────
    // Conversion to MKS entities
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Convert this MCQ to a [QuestionEntity] ready for Room insertion.
     *
     * @param quizId  The target quiz's database ID.
     * @param order   1-based display order within the quiz.
     */
    fun toQuestionEntity(quizId: Long, order: Int): QuestionEntity {
        // Build the options list in A→E order
        val optionList = listOf("A", "B", "C", "D", "E")
            .mapNotNull { letter ->
                options[letter]?.takeIf { it.isNotBlank() }
                    ?.let { text -> "$letter. $text" }
            }

        // Resolve correct answer index (0-based) from the key letter
        val correctAnswers = key?.uppercase()?.let { k ->
            val idx = optionList.indexOfFirst { it.startsWith("$k.") }
            if (idx >= 0) listOf(idx) else emptyList()
        } ?: emptyList()

        // Build combined notes: hint + high-yield content
        val notes = buildString {
            if (hint.isNotBlank()) append("💡 Hint: $hint\n")
        }.trim()

        return QuestionEntity(
            quizId = quizId,
            externalId = "ai_${chQ.replace("-", "_")}_${System.nanoTime()}",
            text = stem.ifBlank { "MCQ $chQ" },
            type = QuestionType.SINGLE_CHOICE,
            options = optionList,
            correctAnswers = correctAnswers,
            explanation = explanation.ifBlank { explanationBrief }.ifBlank { null },
            hint = hint.ifBlank { null },
            notes = notes.ifBlank { null },
            additionalInfo = highYield.ifBlank { null },
            weight = 1,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
        )
    }

    // ──────────────────────────────────────────────────────────────────────────
    // JSON parsing (LLM output → ParsedMcq)
    // ──────────────────────────────────────────────────────────────────────────

    companion object {

        /**
         * Parse a single JSON object from the LLM into a [ParsedMcq].
         * Tolerates missing fields gracefully — no crash on partial output.
         */
        fun fromJson(obj: JSONObject): ParsedMcq? {
            val stem = obj.optString("stem").trim()
            if (stem.isBlank()) return null   // Skip malformed entries

            val optionsObj = obj.optJSONObject("options")
            val options = buildMap {
                listOf("A", "B", "C", "D", "E").forEach { letter ->
                    optionsObj?.optString(letter)
                        ?.takeIf { it.isNotBlank() }
                        ?.let { put(letter, it) }
                }
            }

            return ParsedMcq(
                chQ = obj.optString("ch_q"),
                stem = stem,
                stemBrief = obj.optString("stem_brief"),
                options = options,
                key = obj.optString("key").uppercase().takeIf { it.isNotBlank() && it != "NULL" },
                explanation = obj.optString("explanation"),
                explanationBrief = obj.optString("explanation_brief"),
                hint = obj.optString("hint"),
                highYield = obj.optString("high_yield"),
                generated = obj.optBoolean("generated", false),
            )
        }

        /**
         * Attempt to parse the LLM's raw text output as a JSON array of MCQs.
         * Strips markdown fences if the model didn't follow the "no fences" rule.
         *
         * @return Parsed list (may be empty on total failure — never throws).
         */
        fun parseJsonArray(raw: String): List<ParsedMcq> {
            return try {
                val cleaned = raw
                    .trim()
                    .removePrefix("```json").removePrefix("```")
                    .removeSuffix("```")
                    .trim()

                // Accept both a bare array and a wrapped {"questions":[...]} object
                val jsonArray = when {
                    cleaned.startsWith("[") -> org.json.JSONArray(cleaned)
                    cleaned.startsWith("{") -> {
                        val obj = JSONObject(cleaned)
                        obj.optJSONArray("questions")
                            ?: obj.optJSONArray("mcqs")
                            ?: obj.optJSONArray("data")
                            ?: org.json.JSONArray()
                    }
                    else -> org.json.JSONArray()
                }

                (0 until jsonArray.length())
                    .mapNotNull { i -> fromJson(jsonArray.optJSONObject(i) ?: return@mapNotNull null) }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}
