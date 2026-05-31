package com.ahmedyejam.mks.ui.data

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ahmedyejam.mks.ui.components.ChangePreviewDialog
import com.ahmedyejam.mks.ui.components.EmptyStateCard
import com.ahmedyejam.mks.ui.components.LoadingErrorState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataToolsScreen(
    viewModel: DataToolsViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var importPath by remember { mutableStateOf("") }
    var saveMessage by remember { mutableStateOf<String?>(null) }
    var saveError by remember { mutableStateOf<String?>(null) }
    val importFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let(viewModel::previewImportUri)
    }
    val saveExportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/zip")
    ) { uri ->
        val file = state.lastExport?.file
        if (uri == null || file == null) return@rememberLauncherForActivityResult
        scope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    file.inputStream().use { input ->
                        context.contentResolver.openOutputStream(uri)?.use { output ->
                            input.copyTo(output)
                        } ?: error("Could not open selected destination.")
                    }
                }
            }.onSuccess {
                saveError = null
                saveMessage = "Export saved to selected file."
            }.onFailure { error ->
                saveMessage = null
                saveError = error.message ?: "Could not save export."
            }
        }
    }
    state.importPreview?.let { preview ->
        ChangePreviewDialog(
            result = preview,
            confirmLabel = "Preview only",
            onConfirm = viewModel::clearPreview,
            onDismiss = viewModel::clearPreview
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Import / Export") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") } }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Full Library Export", style = MaterialTheme.typography.titleMedium)
            Text("Creates a ZIP with manifest and JSON dumps of the main learning tables. This repair build exports structured data only; media files are not included yet.")
            Button(onClick = viewModel::exportFullLibrary, enabled = !state.isWorking) { Text("Export full library") }
            state.lastExport?.file?.takeIf { state.lastExport?.success == true }?.let { file ->
                Button(onClick = { saveExportLauncher.launch(file.name) }, enabled = !state.isWorking) {
                    Text("Save export file")
                }
            }
            LoadingErrorState(state.isWorking, state.error ?: saveError)
            state.message?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
            saveMessage?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
            state.lastExport?.warnings?.takeIf { it.isNotEmpty() }?.let { warnings ->
                EmptyStateCard("Export warnings", warnings.joinToString("\n"))
            }
            Text("Import Preview", style = MaterialTheme.typography.titleMedium)
            Text("Choose a ZIP file to preview before any destructive action. Full graph merge remains preview-first.")
            Button(
                onClick = { importFileLauncher.launch(arrayOf("application/zip", "application/octet-stream", "application/x-zip-compressed")) },
                enabled = !state.isWorking
            ) {
                Text("Choose ZIP and preview")
            }
            Text("Optional fallback: paste a local ZIP path if you are testing on an emulator or debug build.")
            OutlinedTextField(
                value = importPath,
                onValueChange = { importPath = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Local ZIP path") },
                singleLine = true
            )
            Button(onClick = { viewModel.previewImportPath(importPath) }, enabled = !state.isWorking && importPath.isNotBlank()) {
                Text("Preview pasted path")
            }
        }
    }
}
