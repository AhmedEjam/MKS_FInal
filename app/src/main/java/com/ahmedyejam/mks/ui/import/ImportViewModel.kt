package com.ahmedyejam.mks.ui.import

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedyejam.mks.data.import.model.ImportFormat
import com.ahmedyejam.mks.data.import.model.ImportResult
import com.ahmedyejam.mks.data.import.model.ImportPreviewDto
import com.ahmedyejam.mks.data.import.model.MergeStrategy
import com.ahmedyejam.mks.data.repository.MksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ImportViewModel(
    private val repository: MksRepository
) : ViewModel() {

    private val _importState = MutableStateFlow<ImportState>(ImportState.Idle)
    val importState: StateFlow<ImportState> = _importState

    fun detectFormat(uri: Uri): ImportFormat {
        return repository.detectFormat(uri)
    }

    fun getImportPreview(uri: Uri, targetBookId: Long? = null, targetQuizId: Long? = null) {
        viewModelScope.launch {
            _importState.value = ImportState.Loading(0.1f, "Preparing preview...")
            try {
                val preview = repository.getImportPreview(uri)
                    ?: throw Exception("Could not load preview")
                _importState.value = ImportState.Review(preview, uri, targetBookId, targetQuizId)
            } catch (e: Exception) {
                _importState.value = ImportState.Error(e.message ?: "Failed to load preview")
            }
        }
    }

    fun importLibrary(
        uri: Uri,
        strategy: MergeStrategy = MergeStrategy.SKIP_EXISTING,
        targetBookId: Long? = null,
        targetQuizId: Long? = null,
        allowInsecureRemoteImages: Boolean = false
    ) {
        viewModelScope.launch {
            _importState.value = ImportState.Loading(0f, "Starting import...")
            try {
                val result = repository.importFromUri(
                    uri = uri,
                    strategy = strategy,
                    targetBookId = targetBookId,
                    targetQuizId = targetQuizId,
                    allowInsecureRemoteImages = allowInsecureRemoteImages
                ) { progress, message ->
                    _importState.value = ImportState.Loading(progress, message)
                }
                if (result?.success == true) {
                    _importState.value = ImportState.Success(result)
                } else {
                    _importState.value = ImportState.Error(result?.errors?.firstOrNull()?.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                _importState.value = ImportState.Error(e.message ?: "Critical import failure")
            }
        }
    }

    fun resetState() {
        _importState.value = ImportState.Idle
    }

    sealed class ImportState {
        object Idle : ImportState()
        data class Loading(val progress: Float, val message: String) : ImportState()
        data class Review(
            val preview: ImportPreviewDto,
            val uri: Uri,
            val targetBookId: Long? = null,
            val targetQuizId: Long? = null
        ) : ImportState()
        data class Success(val result: ImportResult) : ImportState()
        data class Error(val message: String) : ImportState()
    }
}
