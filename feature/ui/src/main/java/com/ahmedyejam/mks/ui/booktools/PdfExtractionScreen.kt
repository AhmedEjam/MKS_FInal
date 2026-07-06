package com.ahmedyejam.mks.ui.booktools

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmedyejam.mks.core.ui.R
import com.ahmedyejam.mks.data.model.AiProviderConfig
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PdfExtractionScreen(
    sourceId: Long,
    viewModel: PdfExtractionViewModel,
    onBack: () -> Unit,
    onNavigateToMcqGenerator: () -> Unit
) {
    val sourceType by viewModel.sourceType.collectAsStateWithLifecycle()
    val totalPages by viewModel.totalPages.collectAsStateWithLifecycle()
    val selectedPages by viewModel.selectedPages.collectAsStateWithLifecycle()
    val blocks by viewModel.blocks.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var showConfigDialog by remember { mutableStateOf<Boolean>(false) }
    var extractionType by remember { mutableStateOf<String>("") }
    
    var reviewingBlockId by remember { mutableStateOf<String?>(null) }
    var showReviewConfigDialog by remember { mutableStateOf<Boolean>(false) }

    LaunchedEffect(sourceId) {
        viewModel.loadSource(sourceId)
    }

    val isPdf = sourceType.equals("PDF", ignoreCase = true)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    val titleText = when {
                        sourceType.equals("IMAGE", ignoreCase = true) -> "Image Extraction"
                        sourceType.equals("AUDIO", ignoreCase = true) -> "Audio Extraction"
                        sourceType.equals("VOICE", ignoreCase = true) -> "Voice Extraction"
                        else -> "PDF Extraction"
                    }
                    Text(titleText)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (isPdf) {
                        IconButton(onClick = viewModel::selectAllPages) {
                            Icon(Icons.Rounded.SelectAll, contentDescription = "Select All")
                        }
                        IconButton(onClick = viewModel::clearSelection) {
                            Icon(Icons.Rounded.ClearAll, contentDescription = "Clear Selection")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // PDF Pages Horizontal List
            if (totalPages > 0) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(totalPages) { pageIndex ->
                        var bitmap by remember { mutableStateOf<Bitmap?>(null) }
                        LaunchedEffect(pageIndex) {
                            bitmap = viewModel.getPageBitmap(pageIndex)
                        }
                        val isSelected = selectedPages.contains(pageIndex)
                        
                        Box(
                            modifier = Modifier
                                .height(160.dp)
                                .aspectRatio(0.7f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray)
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .combinedClickable(
                                    onClick = { viewModel.togglePageSelection(pageIndex) },
                                    onLongClick = { viewModel.selectPageRange(pageIndex) }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (bitmap != null) {
                                Image(
                                    bitmap = bitmap!!.asImageBitmap(),
                                    contentDescription = "Page ${pageIndex + 1}",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                CircularProgressIndicator()
                            }
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                                )
                                Icon(
                                    Icons.Rounded.CheckCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                        .background(Color.White, shape = androidx.compose.foundation.shape.CircleShape)
                                )
                            }
                            Text(
                                text = "${pageIndex + 1}",
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .background(Color.Black.copy(alpha = 0.5f))
                                    .padding(horizontal = 4.dp, vertical = 2.dp),
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            // Extraction Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isPdf) {
                    Button(
                        onClick = {
                            viewModel.extractViaText()
                        },
                        modifier = Modifier.weight(1f),
                        enabled = selectedPages.isNotEmpty()
                    ) {
                        Icon(Icons.Rounded.TextFields, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Extract Text")
                    }
                }
                
                Button(
                    onClick = {
                        extractionType = "vision"
                        showConfigDialog = true
                    },
                    modifier = Modifier.weight(1f),
                    enabled = selectedPages.isNotEmpty()
                ) {
                    Icon(Icons.Rounded.ImageSearch, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Vision AI")
                }
            }

            HorizontalDivider(Modifier.padding(vertical = 8.dp))

            // Extraction Blocks List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(blocks) { block ->
                    ExtractionBlockCard(
                        block = block,
                        onUpdateText = { viewModel.updateBlockText(block.id, it) },
                        onDelete = { viewModel.deleteBlock(block.id) },
                        onReview = { 
                            reviewingBlockId = block.id
                            showReviewConfigDialog = true
                        },
                        onSaveToNotes = {
                            scope.launch {
                                viewModel.saveToNote(sourceId, block.text)
                                Toast.makeText(context, "Saved to notes", Toast.LENGTH_SHORT).show()
                            }
                        },
                        onSendToQuizzes = {
                            // Copy to clipboard or pass via arguments for now
                            val clipboardManager = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                            val clip = android.content.ClipData.newPlainText("Extracted Text", block.text)
                            clipboardManager.setPrimaryClip(clip)
                            Toast.makeText(context, "Copied to clipboard. Navigating to generator...", Toast.LENGTH_SHORT).show()
                            onNavigateToMcqGenerator()
                        }
                    )
                }
            }
        }
    }

    val currentProviderId by viewModel.dataStoreManager.aiProviderId.collectAsState(initial = "ollama_local")
    val currentBaseUrl by viewModel.dataStoreManager.aiBaseUrl.collectAsState(initial = "")
    val currentApiKey by viewModel.dataStoreManager.aiApiKey.collectAsState(initial = "")
    val currentModel by viewModel.dataStoreManager.aiChatModel.collectAsState(initial = "")

    var visionPrompt by remember { mutableStateOf(com.ahmedyejam.mks.data.model.McqPrompts.OCR_DEFAULT) }
    var reviewPrompt by remember { mutableStateOf(com.ahmedyejam.mks.data.model.McqPrompts.OCR_REVIEW_DEFAULT) }

    if (showConfigDialog || showReviewConfigDialog) {
        val type = if (showReviewConfigDialog) "review" else extractionType
        val currentConfig = com.ahmedyejam.mks.data.model.AiProviderConfig(
            providerId = currentProviderId ?: "ollama",
            baseUrl = currentBaseUrl ?: "",
            apiKey = currentApiKey ?: "",
            model = currentModel ?: ""
        )
        
        com.ahmedyejam.mks.ui.settings.ProviderConfigDialog(
            providers = com.ahmedyejam.mks.data.model.AI_PROVIDERS,
            initialConfig = currentConfig,
            initialPrompt = if (type == "vision") visionPrompt else if (type == "review") reviewPrompt else null,
            defaultPrompt = if (type == "vision") com.ahmedyejam.mks.data.model.McqPrompts.OCR_DEFAULT else if (type == "review") com.ahmedyejam.mks.data.model.McqPrompts.OCR_REVIEW_DEFAULT else null,
            onPromptChange = { if (type == "vision") visionPrompt = it else if (type == "review") reviewPrompt = it },
            onDismiss = { 
                showConfigDialog = false 
                showReviewConfigDialog = false
            },
            onConfirm = { config ->
                showConfigDialog = false
                showReviewConfigDialog = false
                if (type == "vision") {
                    viewModel.extractViaVision(config, visionPrompt)
                } else if (type == "review" && reviewingBlockId != null) {
                    viewModel.reviewBlock(reviewingBlockId!!, config, reviewPrompt)
                }
            },
            onPing = { config -> viewModel.pingProvider(config) },
            onFetchModels = { config -> viewModel.fetchModels(config) },
            onTestCall = { config, message -> viewModel.testCall(config, message) }
        )
    }
}
@Composable
fun ExtractionBlockCard(
    block: ExtractionBlock,
    onUpdateText: (String) -> Unit,
    onDelete: () -> Unit,
    onReview: () -> Unit,
    onSaveToNotes: () -> Unit,
    onSendToQuizzes: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (block.status) {
                        ExtractionStatus.PROCESSING -> "Processing..."
                        ExtractionStatus.ERROR -> "Error"
                        else -> "Extracted Content"
                    },
                    style = MaterialTheme.typography.titleSmall,
                    color = if (block.status == ExtractionStatus.ERROR) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Rounded.Close, contentDescription = "Delete Block")
                }
            }

            Spacer(Modifier.height(8.dp))

            if (block.status == ExtractionStatus.PROCESSING) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (block.status == ExtractionStatus.ERROR) {
                Text(
                    text = block.errorMessage ?: "Unknown error",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                OutlinedTextField(
                    value = block.text,
                    onValueChange = onUpdateText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp, max = 300.dp),
                    textStyle = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(16.dp))
                
                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilledTonalButton(onClick = onSaveToNotes) {
                        Icon(Icons.Rounded.NoteAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Notes", maxLines = 1)
                    }
                    FilledTonalButton(onClick = onReview) {
                        Icon(Icons.Rounded.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Review", maxLines = 1)
                    }
                    Button(onClick = onSendToQuizzes) {
                        Icon(Icons.Rounded.Quiz, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("MCQs", maxLines = 1)
                    }
                }
            }
        }
    }
}
