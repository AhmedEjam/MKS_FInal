package com.ahmedyejam.mks.data.import.xlsx

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.ahmedyejam.mks.data.import.dto.BookDto
import com.ahmedyejam.mks.data.import.dto.LibraryBundleDto
import com.ahmedyejam.mks.data.import.dto.OptionDto
import com.ahmedyejam.mks.data.import.dto.QuestionDto
import com.ahmedyejam.mks.data.import.dto.QuizDto
import com.ahmedyejam.mks.data.import.parser.GenericImageExtractor
import com.ahmedyejam.mks.data.import.parser.SpreadsheetHeaderMapper
import com.ahmedyejam.mks.data.import.parser.SpreadsheetQuestionParser
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.FormulaEvaluator
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.ss.util.CellRangeAddress
import java.io.File
import java.util.UUID
import java.util.zip.ZipFile

class XlsxLibraryCompiler(private val context: Context) {
    private val xlsxResolver = XlsxImageResolver()
    private val headerMapper = SpreadsheetHeaderMapper()
    private val imageExtractor = GenericImageExtractor()

    suspend fun compile(uri: Uri): LibraryBundleDto {
        val file = prepareTempFile(uri) ?: throw Exception("Could not create temporary file")
        val zipFile = ZipFile(file)
        val cellImagePathMap = xlsxResolver.getCellImagePathMap(zipFile)
        val displayName = getDisplayName(uri) ?: uri.lastPathSegment ?: "Imported XLSX"
        val bookId = UUID.randomUUID().toString()
        val quizzes = mutableListOf<QuizDto>()
        try {
            WorkbookFactory.create(file).use { workbook ->
                val formatter = DataFormatter()
                val evaluator = workbook.creationHelper.createFormulaEvaluator()
                for (sheetIndex in 0 until workbook.numberOfSheets) {
                    val sheet = workbook.getSheetAt(sheetIndex)
                    if (sheet.lastRowNum < 0) continue
                    val headerRowIndex = detectHeaderRow(sheet, formatter, evaluator)
                    val headerCells = readRow(sheet, headerRowIndex, formatter, evaluator)
                    val mapping = headerMapper.mapHeaders(headerCells)
                    val optionColumns = headerMapper.guessOptionColumns(headerCells, mapping)
                    if (mapping["question"] == null && optionColumns.isEmpty()) continue
                    val imageResolution = xlsxResolver.resolveSheetImages(zipFile, sheet.sheetName, cellImagePathMap)
                    val parser = SpreadsheetQuestionParser(mapping, optionColumns, imageResolution.addressImages, imageResolution.rowImages, imageExtractor)
                    val questionDtos = mutableListOf<QuestionDto>()
                    for (rowIndex in (headerRowIndex + 1)..sheet.lastRowNum) {
                        val cells = readRow(sheet, rowIndex, formatter, evaluator)
                        val parsed = parser.parseRow(cells, rowIndex + 1)
                        if (parser.shouldSkipQuestion(parsed)) continue
                        questionDtos += QuestionDto(
                            id = UUID.randomUUID().toString(),
                            stem = parsed.stem,
                            options = parsed.options.map { OptionDto(it.id, it.text) },
                            correct = parsed.correctAnswers,
                            explanation = parsed.explanation.orEmpty(),
                            hint = parsed.hint.orEmpty(),
                            reference = parsed.reference.orEmpty(),
                            imageDataUrl = parsed.imageDataUrl.orEmpty(),
                            imageSource = parsed.imageSource.orEmpty(),
                            categories = parsed.categories,
                            answerMode = if (parsed.correctAnswers.size > 1) "multiple" else "single",
                            additionalInfo = parsed.additionalInfo.orEmpty()
                        )
                    }
                    if (questionDtos.isEmpty()) continue
                    quizzes += QuizDto(id = UUID.randomUUID().toString(), bookId = bookId, title = sheet.sheetName.ifBlank { displayName }, questions = questionDtos)
                }
            }
        } finally {
            zipFile.close()
            file.delete()
        }
        return LibraryBundleDto(books = listOf(BookDto(id = bookId, title = displayName.substringBeforeLast('.', displayName))), quizzes = quizzes)
    }

    private fun prepareTempFile(uri: Uri): File? {
        val file = File(context.cacheDir, "import_lib_${System.currentTimeMillis()}.xlsx")
        return try {
            context.contentResolver.openInputStream(uri)?.use { input -> file.outputStream().use { output -> input.copyTo(output) } }
            file
        } catch (_: Exception) {
            null
        }
    }

    private fun getDisplayName(uri: Uri): String? = try {
        context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
            if (!cursor.moveToFirst()) return@use null
            val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (index >= 0) cursor.getString(index) else null
        }
    } catch (_: Exception) {
        uri.lastPathSegment
    }

    private fun detectHeaderRow(sheet: Sheet, formatter: DataFormatter, evaluator: FormulaEvaluator): Int {
        var bestRowIndex = 0
        var bestScore = Int.MIN_VALUE
        for (rowIndex in 0..minOf(sheet.lastRowNum, 25)) {
            val score = headerMapper.scoreHeaderRow(readRow(sheet, rowIndex, formatter, evaluator))
            if (score > bestScore) {
                bestScore = score
                bestRowIndex = rowIndex
            }
        }
        return bestRowIndex
    }

    private fun readRow(sheet: Sheet, rowIndex: Int, formatter: DataFormatter, evaluator: FormulaEvaluator): List<String> {
        val row = sheet.getRow(rowIndex) ?: return emptyList()
        val lastCellIndex = maxOf(row.lastCellNum.toInt(), 0)
        if (lastCellIndex <= 0) return emptyList()
        val values = MutableList(lastCellIndex) { columnIndex -> readCell(sheet, rowIndex, columnIndex, formatter, evaluator) }
        while (values.isNotEmpty() && values.last().isBlank()) values.removeAt(values.lastIndex)
        return values
    }

    private fun readCell(sheet: Sheet, rowIndex: Int, columnIndex: Int, formatter: DataFormatter, evaluator: FormulaEvaluator): String {
        val direct = readCellDirect(sheet, rowIndex, columnIndex, formatter, evaluator)
        if (direct.isNotBlank()) return direct
        val mergedRegion = findMergedRegion(sheet, rowIndex, columnIndex) ?: return direct
        return readCellDirect(sheet, mergedRegion.firstRow, mergedRegion.firstColumn, formatter, evaluator)
    }

    private fun readCellDirect(sheet: Sheet, rowIndex: Int, columnIndex: Int, formatter: DataFormatter, evaluator: FormulaEvaluator): String {
        val row = sheet.getRow(rowIndex) ?: return ""
        val cell = row.getCell(columnIndex) ?: return ""
        val displayed = formatter.formatCellValue(cell, evaluator).trim()
        if (displayed.isNotBlank()) return displayed
        return if (cell.cellType == CellType.FORMULA) cell.cellFormula?.trim().orEmpty() else ""
    }

    private fun findMergedRegion(sheet: Sheet, rowIndex: Int, columnIndex: Int): CellRangeAddress? {
        for (regionIndex in 0 until sheet.numMergedRegions) {
            val region = sheet.getMergedRegion(regionIndex)
            if (rowIndex in region.firstRow..region.lastRow && columnIndex in region.firstColumn..region.lastColumn) return region
        }
        return null
    }
}
