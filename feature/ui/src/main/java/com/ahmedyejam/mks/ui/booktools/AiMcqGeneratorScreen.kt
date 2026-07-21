package com.ahmedyejam.mks.ui.booktools

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmedyejam.mks.data.model.ParsedMcq

/**
 * Full 3-step AI MCQ Generator screen.
 *
 * **Step 1** — Paste/import the educational text + label the chapter/section.
 * **Step 2** — Configure the AI provider, models, and generation options.
 * **Step 3** — Trigger generation, watch progress, preview MCQs, save to book.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiMcqGeneratorScreen(
    bookId: Long,
    onNavigateUp: () -> Unit,
    onQuizSaved: (quizId: Long) -> Unit,
    viewModel: AiMcqGeneratorViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Initialise bookId on first composition
    LaunchedEffect(bookId) {
        viewModel.setBookId(bookId)
    }

    // Show errors/success as snackbars and navigate after save
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }
    LaunchedEffect(uiState.savedQuizId) {
        uiState.savedQuizId?.let { quizId ->
            snackbarHostState.showSnackbar(uiState.successMessage ?: "Quiz saved!")
            onQuizSaved(quizId)
        }
    }

    if (uiState.pendingPrivacyConsent) {
        AlertDialog(
            onDismissRequest = { viewModel.cancelPrivacyConsent() },
            title = { Text("Data will be sent to ${uiState.pendingProviderName}") },
            text = {
                Text(
                    "This text and any attached content will be sent to an external AI provider " +
                        "(${uiState.pendingProviderName}). Your data will be processed on their " +
                        "servers according to their privacy policy. Do you want to continue?"
                )
            },
            confirmButton = {
                Button(onClick = { viewModel.confirmPrivacyConsent() }) { Text("Send") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.cancelPrivacyConsent() }) { Text("Cancel") }
            },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("AI MCQ Generator", style = MaterialTheme.typography.titleMedium)
                        Text(
                            "Extract & generate questions from text",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // ── Step 1: Text Input ─────────────────────────────────────────────
            item {
                SectionCard(
                    step = "1",
                    title = "Educational Text",
                    subtitle = "Paste your section content below",
                ) {
                    OutlinedTextField(
                        value = uiState.inputText,
                        onValueChange = viewModel::updateInputText,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        placeholder = { Text("Paste OCR output, notes, or article text here…") },
                        enabled = !uiState.isRunning,
                        maxLines = Int.MAX_VALUE,
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = uiState.chapterNum,
                            onValueChange = viewModel::updateChapterNum,
                            label = { Text("Chapter #") },
                            modifier = Modifier.width(100.dp),
                            enabled = !uiState.isRunning,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        )
                        OutlinedTextField(
                            value = uiState.sectionName,
                            onValueChange = viewModel::updateSectionName,
                            label = { Text("Section Name") },
                            modifier = Modifier.weight(1f),
                            enabled = !uiState.isRunning,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    OutlinedTextField(
                        value = uiState.quizTitle,
                        onValueChange = viewModel::updateQuizTitle,
                        label = { Text("Quiz Title (optional)") },
                        placeholder = { Text("Auto: AI Quiz – ${uiState.sectionName}") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !uiState.isRunning,
                        singleLine = true,
                    )
                }
            }

            // ── Step 2: AI Provider Config ─────────────────────────────────────
            item {
                AiProviderConfigCard(viewModel = viewModel, enabled = !uiState.isRunning)
            }

            // ── Step 3: Generate button + Progress ────────────────────────────
            item {
                SectionCard(
                    step = "3",
                    title = "Generate",
                    subtitle = "Run the 3-pass AI pipeline",
                ) {
                    if (!uiState.isRunning) {
                        Button(
                            onClick = viewModel::startGeneration,
                            modifier = Modifier.fillMaxWidth(),
                            enabled = uiState.inputText.isNotBlank(),
                        ) {
                            Icon(Icons.Filled.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Generate MCQs")
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            val progressText = if (uiState.progressTotal > 0) {
                                "Chunk ${uiState.progressChunk} / ${uiState.progressTotal} · ${uiState.progressFound} found"
                            } else "Initialising…"
                            Text(progressText, style = MaterialTheme.typography.bodySmall)
                            LinearProgressIndicator(
                                progress = {
                                    if (uiState.progressTotal > 0)
                                        uiState.progressChunk.toFloat() / uiState.progressTotal
                                    else 0f
                                },
                                modifier = Modifier.fillMaxWidth(),
                            )
                            OutlinedButton(
                                onClick = viewModel::cancelGeneration,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                            ) {
                                Icon(Icons.Filled.Stop, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Cancel")
                            }
                        }
                    }
                }
            }

            // ── MCQ Preview (if any) ───────────────────────────────────────────
            if (uiState.previewMcqs.isNotEmpty()) {
                item {
                    Text(
                        "Preview — ${uiState.previewMcqs.size} MCQs",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                    )
                }
                itemsIndexed(uiState.previewMcqs) { index, mcq ->
                    McqPreviewCard(index = index + 1, mcq = mcq)
                }
            }
        }
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// Step card wrapper
// ──────────────────────────────────────────────────────────────────────────────

@Composable
private fun SectionCard(
    step: String,
    title: String,
    subtitle: String,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(32.dp)
                        .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small),
                ) {
                    Text(step, color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.labelLarge)
                }
                Column {
                    Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Text(subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            HorizontalDivider()
            content()
        }
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// AI Provider configuration card (Step 2)
// ──────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AiProviderConfigCard(
    viewModel: AiMcqGeneratorViewModel,
    enabled: Boolean,
) {
    val providerId by viewModel.dataStoreManager.aiProviderId.collectAsState(initial = "ollama")
    val baseUrl by viewModel.dataStoreManager.aiBaseUrl.collectAsState(initial = "http://10.0.2.2:11434/v1")
    val apiKey by viewModel.dataStoreManager.aiApiKey.collectAsState(initial = "")
    val chatModel by viewModel.dataStoreManager.aiChatModel.collectAsState(initial = "llama3.1:latest")
    val visionModel by viewModel.dataStoreManager.aiVisionModel.collectAsState(initial = "")
    val reviewEnabled by viewModel.dataStoreManager.aiMcqReviewEnabled.collectAsState(initial = true)
    val extractionMode by viewModel.dataStoreManager.aiMcqExtractionMode.collectAsState(initial = "full")

    // Local editable copies
    var localBaseUrl by rememberSaveable(baseUrl) { mutableStateOf(baseUrl) }
    var localApiKey by rememberSaveable(apiKey) { mutableStateOf(apiKey) }
    var localChatModel by rememberSaveable(chatModel) { mutableStateOf(chatModel) }
    var localVisionModel by rememberSaveable(visionModel) { mutableStateOf(visionModel) }
    var providerDropdownExpanded by remember { mutableStateOf(false) }
    var apiKeyVisible by remember { mutableStateOf(false) }
    var showAdvanced by remember { mutableStateOf(false) }

    val selectedProvider = viewModel.availableProviders.find { it.id == providerId }
        ?: viewModel.availableProviders.first()

    SectionCard(
        step = "2",
        title = "AI Provider",
        subtitle = "Configure model & endpoint",
    ) {
        // Provider selector
        ExposedDropdownMenuBox(
            expanded = providerDropdownExpanded,
            onExpandedChange = { if (enabled) providerDropdownExpanded = it },
        ) {
            OutlinedTextField(
                value = selectedProvider.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Provider") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(providerDropdownExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                enabled = enabled,
            )
            ExposedDropdownMenu(
                expanded = providerDropdownExpanded,
                onDismissRequest = { providerDropdownExpanded = false },
            ) {
                viewModel.availableProviders.forEach { provider ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(provider.name, style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    provider.defaultBaseUrl,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        },
                        onClick = {
                            providerDropdownExpanded = false
                            localBaseUrl = provider.defaultBaseUrl
                            localChatModel = provider.defaultChatModel
                            localVisionModel = provider.defaultVisionModel ?: ""
                            viewModel.saveAiProvider(provider.id, localBaseUrl, localApiKey, localChatModel, localVisionModel)
                        },
                    )
                }
            }
        }

        // Base URL
        OutlinedTextField(
            value = localBaseUrl,
            onValueChange = { localBaseUrl = it },
            label = { Text("Base URL") },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { if (!it.isFocused) viewModel.saveAiProvider(providerId, localBaseUrl, localApiKey, localChatModel, localVisionModel) },
            enabled = enabled,
            singleLine = true,
        )

        // API Key (hidden for local Ollama)
        if (selectedProvider.requiresKey) {
            OutlinedTextField(
                value = localApiKey,
                onValueChange = { localApiKey = it },
                label = { Text("API Key") },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { if (!it.isFocused) viewModel.saveAiProvider(providerId, localBaseUrl, localApiKey, localChatModel, localVisionModel) },
                enabled = enabled,
                singleLine = true,
                visualTransformation = if (apiKeyVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { apiKeyVisible = !apiKeyVisible }) {
                        Icon(
                            if (apiKeyVisible) Icons.Filled.CheckCircle else Icons.Filled.Settings,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                },
            )
        }

        // Chat model
        OutlinedTextField(
            value = localChatModel,
            onValueChange = { localChatModel = it },
            label = { Text("Chat Model (MCQ generation)") },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { if (!it.isFocused) viewModel.saveAiProvider(providerId, localBaseUrl, localApiKey, localChatModel, localVisionModel) },
            enabled = enabled,
            singleLine = true,
        )

        // Advanced toggle
        FilledTonalButton(
            onClick = { showAdvanced = !showAdvanced },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(if (showAdvanced) "Hide Advanced Options" else "Advanced Options")
        }

        AnimatedVisibility(
            visible = showAdvanced,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                HorizontalDivider()

                OutlinedTextField(
                    value = localVisionModel,
                    onValueChange = { localVisionModel = it },
                    label = { Text("Vision Model (OCR, optional)") },
                    placeholder = { Text("Falls back to chat model if blank") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { if (!it.isFocused) viewModel.saveAiProvider(providerId, localBaseUrl, localApiKey, localChatModel, localVisionModel) },
                    enabled = enabled,
                    singleLine = true,
                )

                // Review pass toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("MCQ Review Pass", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            "Enrich with hints, high-yield info & verify answers",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Switch(
                        checked = reviewEnabled,
                        onCheckedChange = viewModel::setReviewEnabled,
                        enabled = enabled,
                    )
                }

                // Extraction mode toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("Simple Extraction Mode", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            "Reduced schema for small local LLMs (7B–13B)",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Switch(
                        checked = extractionMode == "simple",
                        onCheckedChange = { viewModel.setExtractionMode(if (it) "simple" else "full") },
                        enabled = enabled,
                    )
                }
            }
        }
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// MCQ preview card
// ──────────────────────────────────────────────────────────────────────────────

@Composable
private fun McqPreviewCard(index: Int, mcq: ParsedMcq) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                "Q$index — ${mcq.chQ}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(mcq.stem, style = MaterialTheme.typography.bodyMedium)
            mcq.options.entries.sortedBy { it.key }.forEach { (letter, text) ->
                val isCorrect = letter == mcq.key
                Text(
                    "$letter. $text${if (isCorrect) " ✓" else ""}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isCorrect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (isCorrect) FontWeight.SemiBold else FontWeight.Normal,
                )
            }
        }
    }
}
