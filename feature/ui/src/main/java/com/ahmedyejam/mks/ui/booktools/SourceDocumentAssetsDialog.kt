package com.ahmedyejam.mks.ui.booktools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmedyejam.mks.data.local.entity.SourceDocumentAssetEntity
import com.ahmedyejam.mks.data.local.entity.SourceAssetType
import com.ahmedyejam.mks.data.local.entity.SourceDocumentEntity
import com.ahmedyejam.mks.ui.theme.LocalMksDesignTokens

@Composable
fun SourceDocumentAssetsDialog(
    sourceDocument: SourceDocumentEntity,
    assets: List<SourceDocumentAssetEntity>,
    onDismiss: () -> Unit,
    onAddAsset: (SourceDocumentAssetEntity) -> Unit,
    onUpdateAsset: (SourceDocumentAssetEntity) -> Unit,
    onDeleteAsset: (SourceDocumentAssetEntity) -> Unit
) {
    var editingAsset by remember { mutableStateOf<SourceDocumentAssetEntity?>(null) }
    var showAddForm by rememberSaveable { mutableStateOf(false) }

    if (showAddForm || editingAsset != null) {
        SourceAssetFormDialog(
            sourceId = sourceDocument.id,
            existing = editingAsset,
            onDismiss = {
                editingAsset = null
                showAddForm = false
            },
            onSave = { asset ->
                if (asset.id == 0L) onAddAsset(asset) else onUpdateAsset(asset)
                editingAsset = null
                showAddForm = false
            }
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Source attachments") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = sourceDocument.title,
                    maxLines = 3,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (assets.isEmpty()) {
                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("No attachments yet", fontWeight = FontWeight.SemiBold)
                            Text("Add an image, PDF, text note, or web link to enrich this source.")
                        }
                    }
                } else {
                    assets.forEach { asset ->
                        SourceAssetCard(
                            asset = asset,
                            onEdit = { editingAsset = asset },
                            onDelete = { onDeleteAsset(asset) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { showAddForm = true }) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Add")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Close") } }
    )
}

@Composable
private fun SourceAssetCard(
    asset: SourceDocumentAssetEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    showActions: Boolean = true
) {
    val tokens = LocalMksDesignTokens.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(tokens.cardRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation)
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AttachFile, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Column(Modifier.weight(1f)) {
                    Text(asset.title, fontWeight = FontWeight.SemiBold)
                    Text(asset.assetType.replace('_', ' '), style = MaterialTheme.typography.labelSmall)
                }
                if (asset.isPinned) {
                    Icon(Icons.Default.PushPin, contentDescription = "Pinned", tint = MaterialTheme.colorScheme.primary)
                }
                if (showActions) {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit asset")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete asset")
                    }
                }
            }
            asset.description?.takeIf { it.isNotBlank() }?.let { Text(it) }
            asset.externalUrl?.takeIf { it.isNotBlank() }?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
            asset.localPath?.takeIf { it.isNotBlank() }?.let { Text("Local: $it", style = MaterialTheme.typography.labelSmall) }
            asset.textContent?.takeIf { it.isNotBlank() }?.let { Text(it, maxLines = 4) }
        }
    }
}

@Composable
private fun SourceAssetFormDialog(
    sourceId: Long,
    existing: SourceDocumentAssetEntity?,
    onDismiss: () -> Unit,
    onSave: (SourceDocumentAssetEntity) -> Unit
) {
    val availableTypes = remember { 
        listOf(
            SourceAssetType.IMAGE,
            SourceAssetType.PDF,
            SourceAssetType.AUDIO,
            SourceAssetType.VIDEO,
            SourceAssetType.TEXT_NOTE,
            SourceAssetType.WEB_LINK,
            SourceAssetType.OTHER
        )
    }
    var assetType by rememberSaveable(existing?.id) { mutableStateOf(existing?.assetType ?: SourceAssetType.IMAGE) }
    var title by rememberSaveable(existing?.id) { mutableStateOf(existing?.title ?: "") }
    var description by rememberSaveable(existing?.id) { mutableStateOf(existing?.description ?: "") }
    var localPath by rememberSaveable(existing?.id) { mutableStateOf(existing?.localPath ?: "") }
    var externalUrl by rememberSaveable(existing?.id) { mutableStateOf(existing?.externalUrl ?: "") }
    var textContent by rememberSaveable(existing?.id) { mutableStateOf(existing?.textContent ?: "") }
    var isPinned by rememberSaveable(existing?.id) { mutableStateOf(existing?.isPinned ?: false) }
    var isPrimary by rememberSaveable(existing?.id) { mutableStateOf(existing?.isPrimary ?: false) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { localPath = it.toString() }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (existing == null) "Add attachment" else "Edit attachment") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Type", style = MaterialTheme.typography.labelMedium)
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    availableTypes.forEach { type ->
                        FilterChip(
                            selected = assetType == type,
                            onClick = { assetType = type },
                            label = { Text(type.replace('_', ' ').capitalize()) }
                        )
                    }
                }
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
                when (assetType) {
                    SourceAssetType.WEB_LINK -> {
                        OutlinedTextField(
                            value = externalUrl,
                            onValueChange = { externalUrl = it },
                            label = { Text("Web link URL") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    SourceAssetType.TEXT_NOTE -> {
                        OutlinedTextField(
                            value = textContent,
                            onValueChange = { textContent = it },
                            label = { Text("Text note") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 4
                        )
                    }
                    else -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = localPath,
                                onValueChange = { localPath = it },
                                label = { Text("Local path / content URI") },
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { filePickerLauncher.launch("*/*") }) {
                                Icon(Icons.Default.FolderOpen, contentDescription = "Pick file")
                            }
                        }
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FilterChip(selected = isPinned, onClick = { isPinned = !isPinned }, label = { Text("Pinned") })
                    Spacer(Modifier.width(8.dp))
                    FilterChip(selected = isPrimary, onClick = { isPrimary = !isPrimary }, label = { Text("Primary") })
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val effectiveTitle = title.ifBlank { assetType.replace('_', ' ') }
                    onSave(
                        SourceDocumentAssetEntity(
                            id = existing?.id ?: 0L,
                            sourceDocumentId = sourceId,
                            assetType = assetType,
                            title = effectiveTitle,
                            description = description.takeIf { it.isNotBlank() },
                            localPath = localPath.takeIf { it.isNotBlank() },
                            externalUrl = externalUrl.takeIf { it.isNotBlank() },
                            textContent = textContent.takeIf { it.isNotBlank() },
                            sortOrder = existing?.sortOrder ?: 0,
                            isPinned = isPinned,
                            isPrimary = isPrimary,
                            createdAt = existing?.createdAt ?: System.currentTimeMillis(),
                            updatedAt = System.currentTimeMillis()
                        )
                    )
                },
                enabled = title.isNotBlank() || assetType == SourceAssetType.TEXT_NOTE
            ) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
