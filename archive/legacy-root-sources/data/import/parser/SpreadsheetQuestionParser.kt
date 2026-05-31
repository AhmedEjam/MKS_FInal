package com.ahmedyejam.mks.data.import.parser

import com.ahmedyejam.mks.data.import.model.ParsedOption
import com.ahmedyejam.mks.data.import.model.ParsedQuestion
import com.ahmedyejam.mks.data.import.model.ResolvedImage
import com.ahmedyejam.mks.data.local.entity.QuestionType
import java.util.Locale

class SpreadsheetQuestionParser(
    private val mapping: Map<String, Int>,
    private val optionCols: List<Int>,
    private val sheetAddressImages: Map<String, String>,
    private val sheetRowImages: Map<Int, String>,
    private val imageExtractor: GenericImageExtractor
) {

    fun parseRow(row: List<String>, rowNumberOneBased: Int): ParsedQuestion {
        val rawStem = get(row, "question").orEmpty().trim()
        val stem = if (isDispImgOnly(rawStem)) "" else rawStem
        val options = optionCols.mapIndexedNotNull { index, columnIndex ->
            val text = row.getOrNull(columnIndex).orEmpty().trim()
            if (text.isBlank()) null else ParsedOption("opt_${('A'.code + index).toChar()}", text, isMarked(text))
        }
        val answerRaw = get(row, "answer").orEmpty().trim()
        val correctAnswers = resolveCorrectAnswers(answerRaw, options)
        val resolvedImage = resolveImage(row, rowNumberOneBased, mapping["question"] ?: -1, mapping["image"] ?: -1, optionCols)
        val categories = get(row, "categories")?.split(Regex("[,،;؛/|]+"))?.map { it.trim() }?.filter { it.isNotEmpty() }?.distinct() ?: emptyList()
        val type = if (correctAnswers.size > 1) QuestionType.MULTIPLE_CHOICE else QuestionType.SINGLE_CHOICE
        return ParsedQuestion(
            stem = stem,
            options = options,
            correctAnswers = correctAnswers,
            explanation = get(row, "explanation")?.trim(),
            hint = get(row, "hint")?.trim(),
            reference = get(row, "reference")?.trim(),
            additionalInfo = get(row, "additional")?.trim(),
            categories = categories,
            imageDataUrl = resolvedImage.imageDataUrl,
            imageSource = resolvedImage.imageSource,
            derivedFlags = listOfNotNull(resolvedImage.via?.let { "Image resolved via $it" }),
            sourceLine = rowNumberOneBased,
            type = type
        )
    }

    fun shouldSkipQuestion(question: ParsedQuestion): Boolean {
        val hasImage = !question.imageDataUrl.isNullOrBlank() || !question.imageSource.isNullOrBlank()
        val hasMetadata = !question.explanation.isNullOrBlank() || !question.hint.isNullOrBlank() || !question.reference.isNullOrBlank() || !question.additionalInfo.isNullOrBlank() || question.categories.isNotEmpty()
        return question.stem.isBlank() && question.options.isEmpty() && !hasImage && !hasMetadata
    }

    private fun resolveImage(row: List<String>, rowNumber: Int, questionColumnIndex: Int, imageColumnIndex: Int, optionColumnIndices: List<Int>): ResolvedImage {
        if (imageColumnIndex >= 0) resolveFromCell(row.getOrNull(imageColumnIndex), cellRef(imageColumnIndex, rowNumber))?.let { return it }
        if (questionColumnIndex >= 0) resolveFromCell(row.getOrNull(questionColumnIndex), cellRef(questionColumnIndex, rowNumber))?.let { return it }
        optionColumnIndices.forEach { index -> resolveFromCell(row.getOrNull(index), cellRef(index, rowNumber))?.let { return it } }
        val rowFallback = sheetRowImages[rowNumber]
        if (!rowFallback.isNullOrBlank()) return ResolvedImage(imageDataUrl = rowFallback, via = "row-fallback")
        return ResolvedImage()
    }

    private fun resolveFromCell(value: String?, address: String): ResolvedImage? {
        val direct = imageExtractor.extractFromText(value) ?: imageExtractor.extractFromFields(value)
        if (direct != null) return direct
        val addressImage = sheetAddressImages[address]
        if (!addressImage.isNullOrBlank()) return ResolvedImage(imageDataUrl = addressImage, via = "exact-cell")
        return null
    }

    private fun resolveCorrectAnswers(answerRaw: String, options: List<ParsedOption>): List<String> {
        val answerIds = linkedSetOf<String>()
        val normalizedAnswer = normalizeAnswer(answerRaw)
        normalizedAnswer.replace(Regex("[()\[\]{}]"), " ")
            .split(Regex("[,،;؛/|+&\s]+"))
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .forEach { token ->
                when {
                    token.length == 1 && token[0] in 'A'..'Z' -> options.getOrNull(token[0] - 'A')?.let { answerIds += it.id }
                    token.all { it in 'A'..'Z' } && token.length > 1 -> token.forEach { ch -> options.getOrNull(ch - 'A')?.let { answerIds += it.id } }
                    token.toIntOrNull() != null -> options.getOrNull(token.toInt() - 1)?.let { answerIds += it.id }
                    else -> options.firstOrNull { normalizeText(it.text) == normalizeText(token) }?.let { answerIds += it.id }
                }
            }
        if (answerIds.isEmpty() && answerRaw.isNotBlank()) options.firstOrNull { normalizeText(it.text) == normalizeText(answerRaw) }?.let { answerIds += it.id }
        options.filter { it.marked }.forEach { answerIds += it.id }
        return answerIds.toList()
    }

    private fun get(row: List<String>, field: String): String? = mapping[field]?.let { row.getOrNull(it) }

    private fun isMarked(text: String): Boolean = text.contains(Regex("""[*✓✔☑✅]"""))

    private fun isDispImgOnly(text: String): Boolean = Regex("""^(?:=\s*)?(?:_xlfn\.)?DISPIMG\(.*\)$""", RegexOption.IGNORE_CASE).matches(text.trim())

    private fun cellRef(columnIndex: Int, rowNumberOneBased: Int): String {
        var value = columnIndex + 1
        var letters = ""
        while (value > 0) {
            val remainder = (value - 1) % 26
            letters = ('A'.code + remainder).toChar() + letters
            value = (value - 1) / 26
        }
        return letters + rowNumberOneBased
    }

    private fun normalizeAnswer(text: String): String = text.uppercase(Locale.ROOT).replace(Regex("[^\p{L}\p{N},،;؛/|+&\s]"), " ").replace(Regex("\s+"), " ").trim()

    private fun normalizeText(text: String): String = text.lowercase(Locale.ROOT)
        .replace(Regex("[\u064B-\u065F\u0670]"), "")
        .replace("ـ", "")
        .replace('أ', 'ا').replace('إ', 'ا').replace('آ', 'ا').replace('ى', 'ي').replace('ؤ', 'و').replace('ئ', 'ي')
        .replace(Regex("[^\p{L}\p{N}]+"), " ")
        .replace(Regex("\s+"), " ")
        .trim()
}
