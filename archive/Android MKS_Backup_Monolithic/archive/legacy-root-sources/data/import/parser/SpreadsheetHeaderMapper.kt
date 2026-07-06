package com.ahmedyejam.mks.data.import.parser

import java.util.Locale

class SpreadsheetHeaderMapper {

    private val aliases = mapOf(
        "question" to listOf(
            "question", "question text", "question stem", "question summary", "prompt", "stem", "rephrased stem", "text",
            "mcq", "query", "السؤال", "سؤال", "نص السؤال", "متن السؤال", "السوال"
        ),
        "answer" to listOf(
            "answer", "answers", "ans", "correct", "correct answer", "answer key", "key", "key answer",
            "الإجابة", "الاجابة", "الجواب", "الجواب الصحيح", "الإجابة الصحيحة", "مفتاح الإجابة", "مفتاح الاجابة"
        ),
        "explanation" to listOf(
            "explanation", "explanation summary", "rationale", "reason", "logic", "summary", "why",
            "الشرح", "تفسير", "التفسير", "التعليل", "السبب", "التوضيح"
        ),
        "hint" to listOf(
            "hint", "tip", "clue", "cue", "help", "تلميح", "ملمح", "اشارة", "إشارة"
        ),
        "reference" to listOf(
            "reference", "ref", "source", "chapter q", "ch q", "chapter_q", "ch_q", "question number", "q#", "qid",
            "مرجع", "المصدر", "reference text", "رقم السؤال", "الفصل"
        ),
        "additional" to listOf(
            "additional info", "additional", "info", "notes", "extra", "high yield", "high-yield", "facts", "fact", "idea",
            "معلومة", "ملاحظات", "معلومة اضافية", "معلومة إضافية", "فائدة", "هاي ييلد"
        ),
        "categories" to listOf(
            "categories", "category", "tags", "tag", "topic", "topics", "classification",
            "تصنيف", "فئة", "فئات", "وسوم", "موضوع", "مواضيع", "التصنيف"
        ),
        "image" to listOf(
            "image", "image url", "image source", "img", "picture", "media", "asset", "attachment",
            "صورة", "رابط الصورة", "مصدر الصورة", "وسائط"
        )
    )

    fun mapHeaders(headerRow: List<String>): Map<String, Int> {
        val result = linkedMapOf<String, Int>()
        headerRow.forEachIndexed { index, rawCell ->
            val cell = normalize(rawCell)
            if (cell.isBlank()) return@forEachIndexed
            for ((field, aliasList) in aliases) {
                if (field in result) continue
                if (aliasList.any { alias -> matchesAlias(cell, normalize(alias)) }) {
                    result[field] = index
                    break
                }
            }
        }
        return result
    }

    fun scoreHeaderRow(headerRow: List<String>): Int {
        val mapping = mapHeaders(headerRow)
        val optionColumns = guessOptionColumns(headerRow, mapping)
        var score = mapping.size
        if (mapping.containsKey("question")) score += 5
        if (mapping.containsKey("answer")) score += 4
        if (mapping.containsKey("explanation")) score += 2
        if (mapping.containsKey("hint")) score += 1
        score += optionColumns.size * 2
        return score
    }

    fun guessOptionColumns(headerRow: List<String>, mapped: Map<String, Int>): List<Int> {
        val mappedCols = mapped.values.toSet()
        val detected = mutableListOf<Int>()
        headerRow.forEachIndexed { index, rawCell ->
            if (index in mappedCols) return@forEachIndexed
            if (looksLikeOptionHeader(rawCell)) detected += index
        }
        if (detected.isNotEmpty()) return detected.distinct().sorted()

        val questionIndex = mapped["question"] ?: -1
        if (questionIndex == -1) return emptyList()
        val answerIndex = mapped["answer"] ?: headerRow.size
        val fallbackRange = if (answerIndex > questionIndex + 1) (questionIndex + 1) until answerIndex else (questionIndex + 1) until headerRow.size
        fallbackRange.forEach { index ->
            if (index in mappedCols) return@forEach
            val normalized = normalize(headerRow.getOrNull(index).orEmpty())
            if (normalized.isBlank()) return@forEach
            if (normalized.length <= 24 || normalized.contains("option") || normalized.contains("choice") || normalized.contains("خيار")) {
                detected += index
            }
        }
        return detected.distinct().sorted().take(26)
    }

    private fun looksLikeOptionHeader(rawCell: String): Boolean {
        val cell = normalize(rawCell)
        if (cell.isBlank()) return false
        val patterns = listOf(
            Regex("^(option|opt|choice|answer option)\s*([a-z]|\d{1,2})$"),
            Regex("^(mcq|option|choice)\s*(text\s*)?([a-z]|\d{1,2})$"),
            Regex("^(الخيار|اختيار|اجابة|إجابة)\s*([\u0621-\u064a]|\d{1,2})$"),
            Regex("^[a-z]$"),
            Regex("^\d{1,2}$")
        )
        return patterns.any { it.matches(cell) }
    }

    private fun matchesAlias(cell: String, alias: String): Boolean {
        if (cell == alias) return true
        if (alias.isBlank() || alias.length <= 2) return false
        return cell.startsWith("$alias ") || cell.endsWith(" $alias") || cell.contains(" $alias ")
    }

    private fun normalize(text: String): String {
        return text
            .lowercase(Locale.ROOT)
            .replace(Regex("[\u064B-\u065F\u0670]"), "")
            .replace("ـ", "")
            .replace('أ', 'ا')
            .replace('إ', 'ا')
            .replace('آ', 'ا')
            .replace('ى', 'ي')
            .replace('ؤ', 'و')
            .replace('ئ', 'ي')
            .replace(Regex("[_\-–—]+"), " ")
            .replace(Regex("[^\p{L}\p{N}]+"), " ")
            .replace(Regex("\s+"), " ")
            .trim()
    }
}
