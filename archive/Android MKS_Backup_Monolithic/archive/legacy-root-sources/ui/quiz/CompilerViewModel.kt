package com.ahmedyejam.mks.ui.quiz

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedyejam.mks.data.import.detector.ImportFormatDetector
import com.ahmedyejam.mks.data.import.model.ImportFormat
import com.ahmedyejam.mks.data.import.model.ImportMode
import com.ahmedyejam.mks.data.import.model.ParsedQuestion
import com.ahmedyejam.mks.data.import.parser.GenericImageExtractor
import com.ahmedyejam.mks.data.import.parser.HtmlQuestionParser
import com.ahmedyejam.mks.data.import.parser.JsonQuestionParser
import com.ahmedyejam.mks.data.import.parser.SpreadsheetHeaderMapper
import com.ahmedyejam.mks.data.import.parser.SpreadsheetQuestionParser
import com.ahmedyejam.mks.data.import.parser.TextQuestionParser
import com.ahmedyejam.mks.data.import.xlsx.XlsxImageResolver
import com.ahmedyejam.mks.data.repository.MksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.FormulaEvaluator
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.ss.util.CellRangeAddress
import java.io.File
import java.util.UUID
import java.util.zip.ZipFile

data class CompilerUiState(
    val questions: List<ParsedQuestion> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val mode: ImportMode = ImportMode.AUTO,
    val detectedMode: ImportMode? = null,
    val sheetNames: List<String> = emptyList(),
    val selectedSheet: String? = null,
    val headerRow: Int = 0,
    val mapping: Map<String, Int> = emptyMap(),
    val optionColumns: List<Int> = emptyList(),
    val availableColumns: List<String> = emptyList()
)

class CompilerViewModel(
    private val context: Context,
    private val repository: MksRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompilerUiState())
    val uiState = _uiState.asStateFlow()

    private val importFormatDetector = ImportFormatDetector(context)
    private val imageExtractor = GenericImageExtractor()
    private val xlsxResolver = XlsxImageResolver()
    private val jsonParser = JsonQuestionParser(imageExtractor)
    private val htmlParser = HtmlQuestionParser(jsonParser)
    private val textParser = TextQuestionParser(imageExtractor)
    private val headerMapper = SpreadsheetHeaderMapper()

    private var currentUri: Uri? = null
    private var currentFormat: ImportFormat = ImportFormat.UNKNOWN
    private var tempFile: File? = null
    private var currentDisplayName: String = "Imported File"
    private var currentDelimitedRows: List<List<String>> = emptyList()
    private var cellImagePathMap: Map<String, String> = emptyMap()

    override fun onCleared() {
        tempFile?.delete()
        super.onCleared()
    }

    fun onFileSelected(uri: Uri) {
        currentUri = uri
        viewModelScope.launch {
            _uiState.value = CompilerUiState(isLoading = true)
            try {
                currentDisplayName = getDisplayName(uri) ?: uri.lastPathSegment ?: "Imported File"
                currentFormat = withContext(Dispatchers.IO) { importFormatDetector.detectFormat(uri) }
                val detectedMode = when (currentFormat) {
                    ImportFormat.XLSX, ImportFormat.CSV_TSV -> ImportMode.SPREADSHEET
                    ImportFormat.JSON -> ImportMode.JSON
                    ImportFormat.HTML -> ImportMode.HTML
                    else -> ImportMode.TEXT
                }
                _uiState.value = _uiState.value.copy(detectedMode = detectedMode)
                when (currentFormat) {
                    ImportFormat.XLSX -> loadWorkbookSpreadsheet(uri)
                    ImportFormat.CSV_TSV -> loadDelimitedSpreadsheet(uri)
                    else -> loadNonSpreadsheet(uri, detectedMode)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message ?: "Failed to open file", isLoading = false)
            }
        }
    }

    fun onSheetSelected(sheetName: String?) {
        if (sheetName.isNullOrBlank()) return
        _uiState.value = _uiState.value.copy(selectedSheet = sheetName, isLoading = true, error = null)
        when (currentFormat) {
            ImportFormat.CSV_TSV -> applyDelimitedGuess(sheetName)
            ImportFormat.XLSX -> loadWorkbookSheet(sheetName)
            else -> _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun updateMapping(mapping: Map<String, Int>, optionCols: List<Int>) {
        _uiState.value = _uiState.value.copy(mapping = mapping, optionColumns = optionCols, isLoading = true, error = null)
        when (currentFormat) {
            ImportFormat.CSV_TSV -> viewModelScope.launch(Dispatchers.IO) { performDelimitedParse(currentDelimitedRows, mapping, optionCols, _uiState.value.headerRow) }
            ImportFormat.XLSX -> {
                val file = tempFile ?: return
                val sheetName = _uiState.value.selectedSheet ?: return
                viewModelScope.launch(Dispatchers.IO) { performWorkbookParse(file, sheetName, mapping, optionCols, _uiState.value.headerRow) }
            }
            else -> _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun updateHeaderRow(index: Int) {
        if (index < 0) return
        _uiState.value = _uiState.value.copy(headerRow = index, isLoading = true, error = null)
        when (currentFormat) {
            ImportFormat.CSV_TSV -> {
                val headerRow = currentDelimitedRows.getOrNull(index).orEmpty()
                val mapping = headerMapper.mapHeaders(headerRow)
                val optionCols = headerMapper.guessOptionColumns(headerRow, mapping)
                _uiState.value = _uiState.value.copy(mapping = mapping, optionColumns = optionCols, availableColumns = headerRow)
                viewModelScope.launch(Dispatchers.IO) { performDelimitedParse(currentDelimitedRows, mapping, optionCols, index) }
            }
            ImportFormat.XLSX -> {
                val file = tempFile ?: return
                val sheetName = _uiState.value.selectedSheet ?: return
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        WorkbookFactory.create(file).use { workbook ->
                            val sheet = workbook.getSheet(sheetName) ?: throw IllegalStateException("Sheet not found")
                            val formatter = DataFormatter()
                            val evaluator = workbook.creationHelper.createFormulaEvaluator()
                            val headerRow = readRow(sheet, index, formatter, evaluator)
                            val mapping = headerMapper.mapHeaders(headerRow)
                            val optionCols = headerMapper.guessOptionColumns(headerRow, mapping)
                            _uiState.value = _uiState.value.copy(mapping = mapping, optionColumns = optionCols, availableColumns = headerRow)
                            performWorkbookParse(file, sheetName, mapping, optionCols, index)
                        }
                    } catch (e: Exception) {
                        _uiState.value = _uiState.value.copy(error = e.message ?: "Failed to update header row", isLoading = false)
                    }
                }
            }
            else -> _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun saveParsedQuestions(title: String, bookId: Long?) {
        val questions = _uiState.value.questions
        if (questions.isEmpty()) return
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                repository.importParsedQuestions(
                    title = title,
                    description = "Imported from ${currentDisplayName}",
                    targetBookId = bookId,
                    parsedQuestions = questions
                )
                _uiState.value = _uiState.value.copy(isLoading = false, questions = emptyList())
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message ?: "Failed to save imported questions", isLoading = false)
            }
        }
    }

    private suspend fun loadNonSpreadsheet(uri: Uri, detectedMode: ImportMode) = withContext(Dispatchers.IO) {
        val content = context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
            ?: throw IllegalStateException("Could not read file")
        val questions = when (detectedMode) {
            ImportMode.JSON -> jsonParser.parse(content)
            ImportMode.HTML -> htmlParser.parse(content)
            else -> textParser.parse(content)
        }
        _uiState.value = _uiState.value.copy(questions = questions, isLoading = false, error = null, sheetNames = emptyList(), selectedSheet = null)
    }

    private suspend fun loadWorkbookSpreadsheet(uri: Uri) = withContext(Dispatchers.IO) {
        val file = prepareTempFile(uri) ?: throw IllegalStateException("Could not create temporary file")
        cellImagePathMap = ZipFile(file).use { zip -> xlsxResolver.getCellImagePathMap(zip) }
        WorkbookFactory.create(file).use { workbook ->
            val sheetNames = (0 until workbook.numberOfSheets).map { workbook.getSheetAt(it).sheetName }
            val firstSheet = sheetNames.firstOrNull()
            _uiState.value = _uiState.value.copy(sheetNames = sheetNames, selectedSheet = firstSheet, isLoading = false, error = null)
            if (firstSheet != null) onSheetSelected(firstSheet) else _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    private suspend fun loadDelimitedSpreadsheet(uri: Uri) = withContext(Dispatchers.IO) {
        currentDelimitedRows = context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { reader ->
            parseDelimited(reader.readText())
        } ?: emptyList()
        val sheetName = currentDisplayName.substringBeforeLast('.', currentDisplayName)
        _uiState.value = _uiState.value.copy(sheetNames = listOf(sheetName), selectedSheet = sheetName, isLoading = false, error = null)
        applyDelimitedGuess(sheetName)
    }

    private fun applyDelimitedGuess(sheetName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val headerRowIndex = detectHeaderRow(currentDelimitedRows)
                val headerRow = currentDelimitedRows.getOrNull(headerRowIndex).orEmpty()
                val mapping = headerMapper.mapHeaders(headerRow)
                val optionCols = headerMapper.guessOptionColumns(headerRow, mapping)
                _uiState.value = _uiState.value.copy(selectedSheet = sheetName, headerRow = headerRowIndex, mapping = mapping, optionColumns = optionCols, availableColumns = headerRow, isLoading = true, error = null)
                performDelimitedParse(currentDelimitedRows, mapping, optionCols, headerRowIndex)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message ?: "Failed to parse delimited sheet", isLoading = false)
            }
        }
    }

    private fun loadWorkbookSheet(sheetName: String) {
        val file = tempFile ?: return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                WorkbookFactory.create(file).use { workbook ->
                    val sheet = workbook.getSheet(sheetName) ?: throw IllegalStateException("Sheet not found")
                    val formatter = DataFormatter()
                    val evaluator = workbook.creationHelper.createFormulaEvaluator()
                    val headerRowIndex = detectHeaderRow(sheet, formatter, evaluator)
                    val headerRow = readRow(sheet, headerRowIndex, formatter, evaluator)
                    val mapping = headerMapper.mapHeaders(headerRow)
                    val optionCols = headerMapper.guessOptionColumns(headerRow, mapping)
                    _uiState.value = _uiState.value.copy(headerRow = headerRowIndex, mapping = mapping, optionColumns = optionCols, availableColumns = headerRow)
                    performWorkbookParse(file, sheetName, mapping, optionCols, headerRowIndex)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message ?: "Failed to parse workbook sheet", isLoading = false)
            }
        }
    }

    private suspend fun performDelimitedParse(rows: List<List<String>>, mapping: Map<String, Int>, optionCols: List<Int>, headerIdx: Int) = withContext(Dispatchers.IO) {
        try {
            val parser = SpreadsheetQuestionParser(mapping, optionCols, emptyMap(), emptyMap(), imageExtractor)
            val questions = mutableListOf<ParsedQuestion>()
            for (rowIndex in (headerIdx + 1) until rows.size) {
                val parsed = parser.parseRow(rows[rowIndex], rowIndex + 1)
                if (parser.shouldSkipQuestion(parsed)) continue
                questions += parsed
            }
            _uiState.value = _uiState.value.copy(questions = questions, isLoading = false, error = null)
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(error = e.message ?: "Failed to parse spreadsheet rows", isLoading = false)
        }
    }

    private suspend fun performWorkbookParse(file: File, sheetName: String, mapping: Map<String, Int>, optionCols: List<Int>, headerIdx: Int) = withContext(Dispatchers.IO) {
        try {
            WorkbookFactory.create(file).use { workbook ->
                val sheet = workbook.getSheet(sheetName) ?: throw IllegalStateException("Sheet not found")
                val formatter = DataFormatter()
                val evaluator = workbook.creationHelper.createFormulaEvaluator()
                val imageResolution = ZipFile(file).use { zip -> xlsxResolver.resolveSheetImages(zip, sheetName, cellImagePathMap) }
                val parser = SpreadsheetQuestionParser(mapping, optionCols, imageResolution.addressImages, imageResolution.rowImages, imageExtractor)
                val questions = mutableListOf<ParsedQuestion>()
                for (rowIndex in (headerIdx + 1)..sheet.lastRowNum) {
                    val parsed = parser.parseRow(readRow(sheet, rowIndex, formatter, evaluator), rowIndex + 1)
                    if (parser.shouldSkipQuestion(parsed)) continue
                    questions += parsed
                }
                _uiState.value = _uiState.value.copy(questions = questions, isLoading = false, error = null)
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(error = e.message ?: "Failed to parse workbook", isLoading = false)
        }
    }

    private fun prepareTempFile(uri: Uri): File? {
        tempFile?.delete()
        val file = File(context.cacheDir, "import_${System.currentTimeMillis()}.tmp")
        return try {
            context.contentResolver.openInputStream(uri)?.use { input -> file.outputStream().use { output -> input.copyTo(output) } }
            tempFile = file
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

    private fun detectHeaderRow(rows: List<List<String>>): Int {
        var bestRowIndex = 0
        var bestScore = Int.MIN_VALUE
        rows.take(25).forEachIndexed { index, row ->
            val score = headerMapper.scoreHeaderRow(row)
            if (score > bestScore) {
                bestScore = score
                bestRowIndex = index
            }
        }
        return bestRowIndex
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

    private fun parseDelimited(content: String): List<List<String>> {
        val lines = content.replace("
", "
").replace('
', '
').split('
').filter { it.isNotEmpty() }
        if (lines.isEmpty()) return emptyList()
        val delimiter = inferDelimiter(lines.take(10))
        return lines.map { parseDelimitedLine(it, delimiter) }
    }

    private fun inferDelimiter(lines: List<String>): Char {
        val candidates = listOf(',', '	', ';')
        return candidates.maxByOrNull { delimiter -> lines.map { parseDelimitedLine(it, delimiter).size }.average() } ?: ','
    }

    private fun parseDelimitedLine(line: String, delimiter: Char): List<String> {
        val cells = mutableListOf<String>()
        val current = StringBuilder()
        var inQuotes = false
        var i = 0
        while (i < line.length) {
            val char = line[i]
            when {
                char == '"' -> {
                    if (inQuotes && i + 1 < line.length && line[i + 1] == '"') {
                        current.append('"')
                        i++
                    } else {
                        inQuotes = !inQuotes
                    }
                }
                char == delimiter && !inQuotes -> {
                    cells += current.toString().trim()
                    current.clear()
                }
                else -> current.append(char)
            }
            i++
        }
        cells += current.toString().trim()
        return cells
    }
}
