package com.ahmedyejam.mks.ui.library.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ahmedyejam.mks.R
import com.ahmedyejam.mks.data.import.model.ImportResult
import com.ahmedyejam.mks.data.import.model.MergeStrategy
import com.ahmedyejam.mks.data.import.model.ImportPreviewDto
import com.ahmedyejam.mks.data.local.entity.QuizEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEntityDialog(
    title: String,
    initialTitle: String,
    initialDescription: String,
    initialCoverImage: String?,
    titleLabel: String,
    descriptionLabel: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String?) -> Unit
) {
    var titleText by remember { mutableStateOf(initialTitle) }
    var descriptionText by remember { mutableStateOf(initialDescription) }
    var coverImage by remember { mutableStateOf(initialCoverImage) }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { coverImage = it.toString() }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { imageLauncher.launch(arrayOf("image/*")) },
                    contentAlignment = Alignment.Center
                ) {
                    if (!coverImage.isNullOrBlank()) {
                        AsyncImage(
                            model = coverImage,
                            contentDescription = "Cover Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.AddPhotoAlternate, null, modifier = Modifier.size(32.dp))
                            Text(stringResource(R.string.show_cover_images), style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }

                OutlinedTextField(
                    value = titleText,
                    onValueChange = { titleText = it },
                    label = { Text(titleLabel) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = descriptionText,
                    onValueChange = { descriptionText = it },
                    label = { Text(descriptionLabel) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(titleText, descriptionText, coverImage) },
                enabled = titleText.isNotBlank()
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
fun DeleteConfirmDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(stringResource(R.string.delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
fun ImportReviewDialog(
    preview: ImportPreviewDto,
    onDismiss: () -> Unit,
    onConfirm: (MergeStrategy, Boolean) -> Unit
) {
    var strategy by remember { mutableStateOf(MergeStrategy.MERGE_ONLY) }
    var allowInsecureHttpImages by remember { mutableStateOf(false) }
    val hasPlainHttpImageWarning = preview.warnings.any { it.message.contains("plain HTTP", ignoreCase = true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.import_review_title)) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("Stats", style = MaterialTheme.typography.titleSmall)
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(stringResource(R.string.books_header) + ":")
                            Text("${preview.booksToCreate.size + preview.booksToUpdate.size}")
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(stringResource(R.string.quizzes_title) + ":")
                            Text("${preview.quizzesToCreate.size + preview.quizzesToUpdate.size}")
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(stringResource(R.string.import_review_questions))
                            Text("${preview.totalQuestions}")
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(stringResource(R.string.import_review_categories))
                            Text("${preview.totalCategories}")
                        }
                        if (preview.totalSessions > 0) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(stringResource(R.string.import_review_sessions))
                                Text("${preview.totalSessions}")
                            }
                        }
                        if (preview.hasAssets) {
                            Text("• " + stringResource(R.string.import_review_assets), color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }

                Text("Merge Options", style = MaterialTheme.typography.titleMedium)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = strategy == MergeStrategy.MERGE_ONLY,
                            onClick = { strategy = MergeStrategy.MERGE_ONLY }
                        )
                        Column(Modifier.clickable { strategy = MergeStrategy.MERGE_ONLY }) {
                            Text("Merge Only", style = MaterialTheme.typography.bodyMedium)
                            Text("Ignore existing records, only add new ones.", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = strategy == MergeStrategy.MERGE_AND_UPDATE,
                            onClick = { strategy = MergeStrategy.MERGE_AND_UPDATE }
                        )
                        Column(Modifier.clickable { strategy = MergeStrategy.MERGE_AND_UPDATE }) {
                            Text("Merge & Update", style = MaterialTheme.typography.bodyMedium)
                            Text("Update existing records and add new ones.", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }


                if (preview.skippedRecordsCount > 0) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Text(
                            text = "${preview.skippedRecordsCount} invalid question(s) will be skipped. Review the warnings below for line/row and reason details.",
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }

                if (hasPlainHttpImageWarning) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { allowInsecureHttpImages = !allowInsecureHttpImages }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Checkbox(
                                checked = allowInsecureHttpImages,
                                onCheckedChange = { allowInsecureHttpImages = it }
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("Allow plain HTTP image downloads", style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    "Only enable this for trusted imports. Plain HTTP images can be observed or modified by the network.",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }
                        }
                    }
                }
                if (preview.errors.isNotEmpty()) {
                    Text("Errors", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.error)
                    preview.errors.forEach { 
                        Text("• ${it.message}", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall) 
                    }
                }
                
                if (preview.warnings.isNotEmpty()) {
                    Text("Warnings", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.tertiary)
                    preview.warnings.forEach {
                        Text("• ${it.message}", color = MaterialTheme.colorScheme.tertiary, style = MaterialTheme.typography.labelSmall)
                        it.details?.takeIf { details -> details.isNotBlank() }?.let { details ->
                            Text("  $details", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }

                if (preview.booksToCreate.isNotEmpty() || preview.booksToUpdate.isNotEmpty()) {
                    Text(stringResource(R.string.books_header) + " Detail", style = MaterialTheme.typography.titleMedium)
                    preview.booksToCreate.forEach { 
                        Text("+ $it", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodySmall) 
                    }
                    preview.booksToUpdate.forEach { 
                        Text("↻ $it", color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.bodySmall) 
                    }
                }

                if (preview.quizzesToCreate.isNotEmpty() || preview.quizzesToUpdate.isNotEmpty()) {
                    Text(stringResource(R.string.quizzes_title) + " Detail", style = MaterialTheme.typography.titleMedium)
                    preview.quizzesToCreate.forEach { 
                        Text("+ $it", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodySmall) 
                    }
                    preview.quizzesToUpdate.forEach { 
                        Text("↻ $it", color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.bodySmall) 
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(strategy, allowInsecureHttpImages) }) {
                Text(stringResource(R.string.import_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.import_cancel))
            }
        }
    )
}

@Composable
fun ImportWarningsDialog(
    result: ImportResult,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.import_result_warnings_title)) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    stringResource(
                        R.string.import_result_warnings_summary,
                        result.warnings.size
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )

                if (result.skippedRecordsCount > 0) {
                    Text(
                        stringResource(
                            R.string.import_result_skipped_summary,
                            result.skippedRecordsCount
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

                result.warnings.forEach { warning ->
                    Text(
                        text = "• ${warning.message}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    warning.details?.takeIf { it.isNotBlank() }?.let { details ->
                        Text(
                            text = "  $details",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.ok))
            }
        }
    )
}

@Composable
fun QuizSelectionDialog(
    quizzes: List<QuizEntity>,
    onDismiss: () -> Unit,
    onConfirm: (List<Long>) -> Unit
) {
    var selectedIds by remember { mutableStateOf(setOf<Long>()) }
    val allSelected = quizzes.isNotEmpty() && selectedIds.size == quizzes.size

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.select_quizzes_custom)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedIds = if (allSelected) emptySet() else quizzes.map { it.id }.toSet()
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = allSelected,
                        onCheckedChange = { 
                            selectedIds = if (it) quizzes.map { it.id }.toSet() else emptySet()
                        }
                    )
                    Text(stringResource(R.string.select_all), style = MaterialTheme.typography.titleSmall)
                }

                HorizontalDivider()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                ) {
                    items(quizzes, key = { it.id }) { quiz ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedIds = if (selectedIds.contains(quiz.id)) {
                                        selectedIds - quiz.id
                                    } else {
                                        selectedIds + quiz.id
                                    }
                                }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = selectedIds.contains(quiz.id),
                                onCheckedChange = { checked ->
                                    selectedIds = if (checked) selectedIds + quiz.id else selectedIds - quiz.id
                                }
                            )
                            Text(quiz.title, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(selectedIds.toList()) },
                enabled = selectedIds.isNotEmpty()
            ) {
                Text(stringResource(R.string.create))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
