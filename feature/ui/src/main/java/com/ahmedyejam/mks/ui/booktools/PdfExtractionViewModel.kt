package com.ahmedyejam.mks.ui.booktools

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedyejam.mks.data.local.entity.NoteBlueprintEntity
import com.ahmedyejam.mks.data.model.AiProviderConfig
import com.ahmedyejam.mks.data.network.OcrService
import com.ahmedyejam.mks.data.network.PdfRendererService
import com.ahmedyejam.mks.data.network.PdfTextExtractor
import com.ahmedyejam.mks.data.preferences.DataStoreManager
import com.ahmedyejam.mks.data.repository.WorkspaceRepository
import com.ahmedyejam.mks.data.network.AiClient
import com.ahmedyejam.mks.data.repository.OllamaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject
import android.util.Base64

enum class ExtractionStatus {
    IDLE, PROCESSING, DONE, ERROR
}

data class ExtractionBlock(
    val id: String = UUID.randomUUID().toString(),
    val text: String = "",
    val status: ExtractionStatus = ExtractionStatus.IDLE,
    val errorMessage: String? = null
)

@HiltViewModel
class PdfExtractionViewModel @Inject constructor(
    @dagger.hilt.android.qualifiers.ApplicationContext private val context: android.content.Context,
    private val assetRepository: com.ahmedyejam.mks.data.repository.AssetRepository,
    private val knowledgeRepository: com.ahmedyejam.mks.data.repository.KnowledgeRepository,
    private val pdfRendererService: PdfRendererService,
    private val pdfTextExtractor: PdfTextExtractor,
    private val ocrService: OcrService,
    val dataStoreManager: DataStoreManager,
    private val aiClient: AiClient,
    private val ollamaRepository: OllamaRepository
) : ViewModel() {

    private val _sourceType = MutableStateFlow<String>("")
    val sourceType = _sourceType.asStateFlow()

    private val _pdfUri = MutableStateFlow<Uri?>(null)
    val pdfUri = _pdfUri.asStateFlow()

    private val _totalPages = MutableStateFlow(0)
    val totalPages = _totalPages.asStateFlow()

    private val _selectedPages = MutableStateFlow<Set<Int>>(emptySet())
    val selectedPages = _selectedPages.asStateFlow()

    private val _blocks = MutableStateFlow<List<ExtractionBlock>>(emptyList())
    val blocks = _blocks.asStateFlow()

    private val _pdfImagesCache = mutableMapOf<Int, Bitmap>()

    fun loadSource(sourceId: Long) {
        viewModelScope.launch {
            val source = assetRepository.getSourceDocumentById(sourceId)
            _sourceType.value = source?.sourceType ?: ""
            val uriStr = source?.localPath ?: source?.externalUrl
            if (uriStr != null) {
                val uri = if (uriStr.startsWith("http") || uriStr.startsWith("content://")) {
                    Uri.parse(uriStr)
                } else {
                    Uri.fromFile(java.io.File(uriStr))
                }
                _pdfUri.value = uri
                
                when {
                    _sourceType.value.equals("IMAGE", ignoreCase = true) -> {
                        _totalPages.value = 1
                        _selectedPages.value = setOf(0)
                    }
                    _sourceType.value.equals("PDF", ignoreCase = true) -> {
                        _totalPages.value = pdfRendererService.getPageCount(uri)
                    }
                    else -> {
                        // AUDIO / VOICE / Other
                        _totalPages.value = 0
                    }
                }
            }
        }
    }

    suspend fun getPageBitmap(pageIndex: Int): Bitmap? {
        if (_pdfImagesCache.containsKey(pageIndex)) return _pdfImagesCache[pageIndex]
        val uri = _pdfUri.value ?: return null
        
        val bitmap = if (_sourceType.value.equals("IMAGE", ignoreCase = true)) {
            withContext(Dispatchers.IO) {
                try {
                    val options = android.graphics.BitmapFactory.Options().apply {
                        inSampleSize = 2 // Downscale by 2 for UI thumbnail to save memory
                    }
                    val inputStream = context.contentResolver.openInputStream(uri)
                    android.graphics.BitmapFactory.decodeStream(inputStream, null, options)
                } catch (e: Exception) {
                    null
                }
            }
        } else {
            pdfRendererService.renderPage(uri, pageIndex, densityDpi = 150) // low res for thumbnail
        }
        
        if (bitmap != null) {
            _pdfImagesCache[pageIndex] = bitmap
        }
        return bitmap
    }

    fun togglePageSelection(pageIndex: Int) {
        val current = _selectedPages.value.toMutableSet() // toMutableSet creates a LinkedHashSet, preserving insertion order
        if (current.contains(pageIndex)) current.remove(pageIndex) else current.add(pageIndex)
        _selectedPages.value = current
    }

    fun selectPageRange(pageIndex: Int) {
        val current = _selectedPages.value.toMutableSet()
        val lastSelected = current.lastOrNull()
        if (lastSelected == null) {
            current.add(pageIndex)
        } else {
            val start = minOf(lastSelected, pageIndex)
            val end = maxOf(lastSelected, pageIndex)
            for (i in start..end) {
                current.add(i)
            }
        }
        _selectedPages.value = current
    }

    fun selectAllPages() {
        val total = _totalPages.value
        _selectedPages.value = (0 until total).toSet()
    }

    fun clearSelection() {
        _selectedPages.value = emptySet()
    }

    fun extractViaVision(config: AiProviderConfig, prompt: String? = null) {
        if (_selectedPages.value.isEmpty()) return
        
        val blockId = UUID.randomUUID().toString()
        _blocks.value = _blocks.value + ExtractionBlock(id = blockId, status = ExtractionStatus.PROCESSING)
        
        viewModelScope.launch {
            try {
                val uri = _pdfUri.value ?: throw Exception("No source loaded")
                _blocks.value = _blocks.value.map {
                    if (it.id == blockId) it.copy(status = ExtractionStatus.PROCESSING) else it
                }
                
                try {
                    // Ensure no double prefix "data:image/jpeg;base64," is sent, as AiClient handles the prefix.
                    val base64Images = selectedPages.value.mapNotNull { pageNum ->
                        val bmp = if (_sourceType.value.equals("IMAGE", ignoreCase = true)) {
                            withContext(Dispatchers.IO) {
                                try {
                                    val inputStream = context.contentResolver.openInputStream(uri)
                                    android.graphics.BitmapFactory.decodeStream(inputStream) // No capping
                                } catch (e: Exception) {
                                    null
                                }
                            }
                        } else {
                            pdfRendererService.renderPage(uri, pageNum)
                        } ?: return@mapNotNull null
                        
                        val baos = ByteArrayOutputStream()
                        bmp.compress(Bitmap.CompressFormat.JPEG, 90, baos)
                        val bytes = baos.toByteArray()
                        Base64.encodeToString(bytes, Base64.NO_WRAP)
                    }

                    val text = ocrService.processPages(
                        base64Images = base64Images, 
                        config = config,
                        ocrPrompt = prompt ?: com.ahmedyejam.mks.data.model.McqPrompts.OCR_DEFAULT,
                        reviewPrompt = null // We can skip review prompt here if we just want raw extraction
                    )
                    updateBlock(blockId) { it.copy(text = text, status = ExtractionStatus.DONE) }
                } catch (e: Exception) {
                    updateBlock(blockId) { it.copy(status = ExtractionStatus.ERROR, errorMessage = e.message) }
                }
            } catch (e: Exception) {
                updateBlock(blockId) { it.copy(status = ExtractionStatus.ERROR, errorMessage = e.message) }
            }
        }
    }

    fun extractViaText() {
        if (_selectedPages.value.isEmpty()) return

        val blockId = UUID.randomUUID().toString()
        _blocks.value = _blocks.value + ExtractionBlock(id = blockId, status = ExtractionStatus.PROCESSING)

        viewModelScope.launch {
            try {
                if (!_sourceType.value.equals("PDF", ignoreCase = true)) {
                    throw Exception("Raw text extraction is only supported for PDFs. Please use AI extraction.")
                }
                
                val uri = _pdfUri.value ?: throw Exception("No source loaded")
                val rawText = pdfTextExtractor.extractTextFromPages(uri, _selectedPages.value)
                updateBlock(blockId) { it.copy(text = rawText, status = ExtractionStatus.DONE) }
            } catch (e: Exception) {
                updateBlock(blockId) { it.copy(status = ExtractionStatus.ERROR, errorMessage = e.message) }
            }
        }
    }

    fun reviewBlock(parentBlockId: String, config: AiProviderConfig, prompt: String) {
        val parentText = _blocks.value.find { it.id == parentBlockId }?.text ?: return
        
        val blockId = UUID.randomUUID().toString()
        // Insert new block directly after the parent block
        val currentBlocks = _blocks.value.toMutableList()
        val parentIndex = currentBlocks.indexOfFirst { it.id == parentBlockId }
        if (parentIndex != -1) {
            currentBlocks.add(parentIndex + 1, ExtractionBlock(id = blockId, status = ExtractionStatus.PROCESSING))
            _blocks.value = currentBlocks
        }

        viewModelScope.launch {
            try {
                val refined = ocrService.refineRawText(parentText, config, reviewPrompt = prompt)
                updateBlock(blockId) { it.copy(text = refined, status = ExtractionStatus.DONE) }
            } catch (e: Exception) {
                updateBlock(blockId) { it.copy(status = ExtractionStatus.ERROR, errorMessage = e.message) }
            }
        }
    }

    fun updateBlockText(blockId: String, newText: String) {
        updateBlock(blockId) { it.copy(text = newText) }
    }

    fun deleteBlock(blockId: String) {
        _blocks.value = _blocks.value.filter { it.id != blockId }
    }

    private fun updateBlock(id: String, update: (ExtractionBlock) -> ExtractionBlock) {
        _blocks.value = _blocks.value.map { if (it.id == id) update(it) else it }
    }

    suspend fun saveToNote(sourceId: Long, text: String) {
        val source = assetRepository.getSourceDocumentById(sourceId) ?: return
        val note = NoteBlueprintEntity(
            externalId = java.util.UUID.randomUUID().toString(),
            collectionId = knowledgeRepository.getOrCreateDefaultNoteCollection(source.bookId ?: return),
            title = "Extracted from ${source.title}",
            body = text,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        knowledgeRepository.insertNoteBlueprint(note)
    }

    override fun onCleared() {
        super.onCleared()
        _pdfImagesCache.values.forEach { it.recycle() }
        _pdfImagesCache.clear()
    }

    suspend fun pingProvider(config: AiProviderConfig): String = withContext(Dispatchers.IO) {
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

    suspend fun fetchModels(config: AiProviderConfig): List<String> = withContext(Dispatchers.IO) {
        try {
            aiClient.listModels(config.baseUrl, config.apiKey)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun testCall(config: AiProviderConfig, testMessage: String): String = withContext(Dispatchers.IO) {
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
