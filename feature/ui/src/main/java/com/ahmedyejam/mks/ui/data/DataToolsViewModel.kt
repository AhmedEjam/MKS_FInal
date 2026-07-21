package com.ahmedyejam.mks.ui.data

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedyejam.mks.data.preferences.DataStoreManager
import com.ahmedyejam.mks.data.repository.BookRepository
import com.ahmedyejam.mks.data.repository.DataIntegrityService
import com.ahmedyejam.mks.data.repository.ExportManager
import com.ahmedyejam.mks.data.repository.IntegrityReport
import com.ahmedyejam.mks.data.repository.QuizRepository
import com.ahmedyejam.mks.data.repository.WorkspaceRepository
import com.ahmedyejam.mks.data.repository.SortOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DataToolsUiState(
    val isWorking: Boolean = false,
    val message: String? = null,
    val error: String? = null,
    val lastExportResult: ExportSummary? = null,
    val libraryStats: LibraryStats = LibraryStats(),
    val showResetConfirm: Boolean = false,
    val resetConfirmText: String = "",
    val integrityReport: IntegrityReport? = null,
    val isCheckingIntegrity: Boolean = false,
)

data class ExportSummary(
    val success: Boolean,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
)

data class LibraryStats(
    val bookCount: Int = 0,
    val quizCount: Int = 0,
)

@HiltViewModel
class DataToolsViewModel @Inject constructor(
    private val exportManager: ExportManager,
    private val bookRepository: BookRepository,
    private val quizRepository: QuizRepository,
    private val workspaceRepository: WorkspaceRepository,
    private val dataStoreManager: DataStoreManager,
    private val dataIntegrityService: DataIntegrityService,
) : ViewModel() {
    private val _state = MutableStateFlow(DataToolsUiState())
    val state: StateFlow<DataToolsUiState> = _state.asStateFlow()

    init { refreshStats() }

    fun refreshStats() {
        viewModelScope.launch {
            try {
                val wsId = dataStoreManager.currentWorkspaceId.first()
                    ?: workspaceRepository.getOrCreateDefaultWorkspace().id
                val books = bookRepository.getBooksByWorkspace(wsId, SortOption.TITLE).first()
                val quizzes = quizRepository.getAllQuizzesFlow().first()
                _state.update { it.copy(libraryStats = LibraryStats(
                    bookCount = books.size,
                    quizCount = quizzes.size,
                )) }
            } catch (e: Exception) {
                // Stats are best-effort
            }
        }
    }

    fun exportFullLibrary(outputStream: java.io.OutputStream) {
        viewModelScope.launch {
            _state.update { it.copy(isWorking = true, error = null, message = null) }
            val result = exportManager.exportAllToZip(outputStream)
            val summary = if (result.success) {
                ExportSummary(true, "Export completed successfully.")
            } else {
                ExportSummary(false, result.errorMessage ?: "Export failed")
            }
            _state.update {
                it.copy(
                    isWorking = false,
                    message = if (result.success) summary.message else null,
                    error = if (!result.success) summary.message else null,
                    lastExportResult = summary,
                )
            }
            refreshStats()
        }
    }

    fun exportBook(bookId: Long, outputStream: java.io.OutputStream) {
        viewModelScope.launch {
            _state.update { it.copy(isWorking = true, error = null, message = null) }
            val result = exportManager.exportBundleToZip(bookId, outputStream)
            _state.update {
                it.copy(
                    isWorking = false,
                    message = if (result.success) "Book exported successfully." else null,
                    error = if (!result.success) result.errorMessage else null,
                    lastExportResult = if (result.success) ExportSummary(true, "Book exported.") else null,
                )
            }
        }
    }

    fun showResetConfirm() {
        _state.update { it.copy(showResetConfirm = true, resetConfirmText = "") }
    }

    fun dismissResetConfirm() {
        _state.update { it.copy(showResetConfirm = false, resetConfirmText = "") }
    }

    fun updateResetConfirmText(text: String) {
        _state.update { it.copy(resetConfirmText = text) }
    }

    fun resetDatabase() {
        viewModelScope.launch {
            _state.update { it.copy(isWorking = true) }
            try {
                workspaceRepository.resetDatabase()
                _state.update { it.copy(isWorking = false, showResetConfirm = false, message = "Database reset complete. Sample data reloaded.") }
                refreshStats()
            } catch (e: Exception) {
                _state.update { it.copy(isWorking = false, error = "Reset failed: ${e.message}") }
            }
        }
    }

    fun clearMessage() {
        _state.update { it.copy(message = null, error = null) }
    }

    fun checkIntegrity() {
        viewModelScope.launch {
            _state.update { it.copy(isCheckingIntegrity = true) }
            try {
                val report = dataIntegrityService.checkIntegrity()
                _state.update { it.copy(integrityReport = report, isCheckingIntegrity = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isCheckingIntegrity = false, error = "Integrity check failed: ${e.message}") }
            }
        }
    }

    fun repairOrphanedFiles() {
        viewModelScope.launch {
            val report = _state.value.integrityReport ?: return@launch
            _state.update { it.copy(isWorking = true) }
            try {
                val deleted = dataIntegrityService.repairOrphanedFiles(report)
                _state.update { it.copy(isWorking = false, message = "Deleted $deleted orphaned file(s).") }
                checkIntegrity()
            } catch (e: Exception) {
                _state.update { it.copy(isWorking = false, error = "Repair failed: ${e.message}") }
            }
        }
    }
}
