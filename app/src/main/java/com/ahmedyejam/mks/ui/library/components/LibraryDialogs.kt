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
    allowBlankTitle: Boolean = false,
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
                enabled = allowBlankTitle || titleText.isNotBlank()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuizDialog(
    quizzes: List<QuizEntity>,
    categories: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (title: String, description: String, coverImage: String?, sourceQuizIds: List<Long>, sourceCategories: List<String>) -> Unit
) {
    var titleText by remember { mutableStateOf("") }
    var descriptionText by remember { mutableStateOf("") }
    var coverImage by remember { mutableStateOf<String?>(null) }
    
    var isFromExisting by remember { mutableStateOf(false) }
    var selectedQuizIds by remember { mutableStateOf(setOf<Long>()) }
    var selectedCategories by remember { mutableStateOf(setOf<String>()) }
    
    val allQuizzesSelected = quizzes.isNotEmpty() && selectedQuizIds.size == quizzes.size
    val allCategoriesSelected = categories.isNotEmpty() && selectedCategories.size == categories.size

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { coverImage = it.toString() }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Quiz") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Image Picker
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
                    label = { Text("Quiz Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = descriptionText,
                    onValueChange = { descriptionText = it },
                    label = { Text("Description (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Mode Selection
                Text("Quiz Content", style = MaterialTheme.typography.titleMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = !isFromExisting, onClick = { isFromExisting = false })
                    Text("Empty Quiz", modifier = Modifier.clickable { isFromExisting = false })
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = isFromExisting, onClick = { isFromExisting = true })
                    Text("From Existing Quizzes & Categories", modifier = Modifier.clickable { isFromExisting = true })
                }

                if (isFromExisting) {
                    if (quizzes.isNotEmpty()) {
                        Text("Source Quizzes", style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(top = 8.dp))
                        Card(modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp)) {
                            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().clickable {
                                        selectedQuizIds = if (allQuizzesSelected) emptySet() else quizzes.map { it.id }.toSet()
                                    }.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(checked = allQuizzesSelected, onCheckedChange = { 
                                        selectedQuizIds = if (it) quizzes.map { q -> q.id }.toSet() else emptySet()
                                    })
                                    Text("Select All", style = MaterialTheme.typography.bodyMedium)
                                }
                                quizzes.forEach { quiz ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth().clickable {
                                            selectedQuizIds = if (selectedQuizIds.contains(quiz.id)) selectedQuizIds - quiz.id else selectedQuizIds + quiz.id
                                        }.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(checked = selectedQuizIds.contains(quiz.id), onCheckedChange = { checked ->
                                            selectedQuizIds = if (checked) selectedQuizIds + quiz.id else selectedQuizIds - quiz.id
                                        })
                                        Text(quiz.title, style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }
                    }

                    if (categories.isNotEmpty()) {
                        Text("Source Categories (Filters)", style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(top = 8.dp))
                        Card(modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp)) {
                            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().clickable {
                                        selectedCategories = if (allCategoriesSelected) emptySet() else categories.toSet()
                                    }.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(checked = allCategoriesSelected, onCheckedChange = { 
                                        selectedCategories = if (it) categories.toSet() else emptySet()
                                    })
                                    Text("Select All", style = MaterialTheme.typography.bodyMedium)
                                }
                                categories.forEach { cat ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth().clickable {
                                            selectedCategories = if (selectedCategories.contains(cat)) selectedCategories - cat else selectedCategories + cat
                                        }.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(checked = selectedCategories.contains(cat), onCheckedChange = { checked ->
                                            selectedCategories = if (checked) selectedCategories + cat else selectedCategories - cat
                                        })
                                        Text(cat, style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { 
                    onConfirm(
                        titleText, 
                        descriptionText, 
                        coverImage, 
                        if (isFromExisting) selectedQuizIds.toList() else emptyList(),
                        if (isFromExisting) selectedCategories.toList() else emptyList()
                    )
                },
                enabled = titleText.isNotBlank() && (!isFromExisting || (selectedQuizIds.isNotEmpty() || selectedCategories.isNotEmpty()))
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
