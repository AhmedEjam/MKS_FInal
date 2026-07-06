package com.ahmedyejam.mks.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedyejam.mks.data.focus.FocusManager
import com.ahmedyejam.mks.data.preferences.DataStoreManager
import com.ahmedyejam.mks.data.network.AiClient
import com.ahmedyejam.mks.data.network.AiHttpError
import com.ahmedyejam.mks.data.repository.AssetRepository
import com.ahmedyejam.mks.data.repository.ExportManager
import com.ahmedyejam.mks.data.repository.OllamaRepository
import com.ahmedyejam.mks.data.repository.QuizRepository
import com.ahmedyejam.mks.data.repository.WorkspaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.OutputStream
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val exportManager: ExportManager,
    private val workspaceRepository: WorkspaceRepository,
    private val quizRepository: QuizRepository,
    private val assetRepository: AssetRepository,
    val ollamaRepository: OllamaRepository,
    val dataStoreManager: DataStoreManager,
    val focusManager: FocusManager,
    private val aiClient: AiClient
) : ViewModel() {

    private val _isExporting = MutableStateFlow(false)
    val isExporting = _isExporting.asStateFlow()

    private val _exportMessage = MutableStateFlow<String?>(null)
    val exportMessage = _exportMessage.asStateFlow()
    
    private val _isResetting = MutableStateFlow(false)
    val isResetting = _isResetting.asStateFlow()

    fun clearExportMessage() {
        _exportMessage.value = null
    }

    fun exportAllData(stream: OutputStream) {
        viewModelScope.launch(Dispatchers.IO) {
            _isExporting.value = true
            _exportMessage.value = "Exporting data... please wait."
            try {
                val result = exportManager.exportAllToZip(stream)
                _exportMessage.value = if (result.success) {
                    "Export completed successfully."
                } else {
                    "Export failed: ${result.errorMessage}"
                }
            } catch (e: Exception) {
                _exportMessage.value = "Error during export: ${e.message}"
            } finally {
                _isExporting.value = false
                stream.close()
            }
        }
    }

    fun resetDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            _isResetting.value = true
            try {
                workspaceRepository.resetDatabase()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isResetting.value = false
            }
        }
    }

    fun clearCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            quizRepository.clearAllCategories()
        }
    }

    fun rebuildDerivedIndexes() {
        viewModelScope.launch(Dispatchers.IO) {
            assetRepository.rebuildDerivedIndexes()
        }
    }
    
    fun updateAiProvider(config: com.ahmedyejam.mks.data.model.AiProviderConfig) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManager.setAiProviderId(config.providerId)
            dataStoreManager.setAiBaseUrl(config.baseUrl)
            dataStoreManager.setAiApiKey(config.apiKey)
            dataStoreManager.setAiChatModel(config.model)
        }
    }

    suspend fun pingProvider(config: com.ahmedyejam.mks.data.model.AiProviderConfig): String = withContext(Dispatchers.IO) {
        try {
            if (config.providerId.startsWith("ollama")) {
                val result = ollamaRepository.testConnection(config.baseUrl, config.model, config.apiKey)
                if (result.success) {
                    "Connection successful! Models found: ${result.models.size}"
                } else {
                    "Connection failed: ${result.message}"
                }
            } else {
                val models = aiClient.listModels(config.baseUrl, config.apiKey)
                "Connection successful! ${models.size} models available."
            }
        } catch (e: Exception) {
            "Ping failed: ${e.message}"
        }
    }

    suspend fun fetchModels(config: com.ahmedyejam.mks.data.model.AiProviderConfig): List<String> = withContext(Dispatchers.IO) {
        try {
            aiClient.listModels(config.baseUrl, config.apiKey)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun testCall(config: com.ahmedyejam.mks.data.model.AiProviderConfig, testMessage: String): String = withContext(Dispatchers.IO) {
        try {
            val response = aiClient.chatComplete(
                config = config,
                systemPrompt = "You are a helpful test assistant.",
                userMessage = testMessage,
                maxTokens = 200
            )
            response.ifBlank { "Test call successful, but empty response." }
        } catch (e: Exception) {
            "Test call failed: ${e.message}"
        }
    }
}
