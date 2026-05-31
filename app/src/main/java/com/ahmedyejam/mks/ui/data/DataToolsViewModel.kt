package com.ahmedyejam.mks.ui.data

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedyejam.mks.data.exportfull.MksFullExportResult
import com.ahmedyejam.mks.data.exportfull.MksFullImportExportService
import com.ahmedyejam.mks.data.simulation.ChangeSimulationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

data class DataToolsUiState(
    val isWorking: Boolean = false,
    val lastExport: MksFullExportResult? = null,
    val importPreview: ChangeSimulationResult? = null,
    val message: String? = null,
    val error: String? = null
)

class DataToolsViewModel(private val service: MksFullImportExportService) : ViewModel() {
    private val _state = MutableStateFlow(DataToolsUiState())
    val state: StateFlow<DataToolsUiState> = _state.asStateFlow()

    fun exportFullLibrary() {
        viewModelScope.launch {
            _state.update { it.copy(isWorking = true, error = null, message = null) }
            val result = service.exportFullLibrary()
            _state.update {
                it.copy(
                    isWorking = false,
                    lastExport = result,
                    message = if (result.success) "Export created: ${result.file?.name}. Tap Save export file to choose where to store it." else null,
                    error = result.error
                )
            }
        }
    }

    fun previewImportPath(path: String) {
        viewModelScope.launch {
            _state.update { it.copy(isWorking = true, error = null) }
            val preview = service.simulateImportBundle(File(path))
            _state.update { it.copy(isWorking = false, importPreview = preview) }
        }
    }

    fun previewImportUri(uri: Uri) {
        viewModelScope.launch {
            _state.update { it.copy(isWorking = true, error = null) }
            val preview = service.simulateImportBundle(uri)
            _state.update { it.copy(isWorking = false, importPreview = preview) }
        }
    }

    fun clearPreview() { _state.update { it.copy(importPreview = null) } }
}
