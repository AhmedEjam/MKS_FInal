package com.ahmedyejam.mks.ui.library.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ahmedyejam.mks.R

@Composable
fun LibraryFabMenu(
    fabExpanded: Boolean,
    onFabExpandedChange: (Boolean) -> Unit,
    selectedBookId: Long,
    selectedCategory: String?,
    onAdaptiveSelected: (String, String) -> Unit,
    onFlashcardDeckDialogShow: () -> Unit,
    onBookSlideshowSelected: (Long) -> Unit,
    onBookReviewBlueprintSelected: (Long) -> Unit,
    onBookSourcesSelected: (Long) -> Unit,
    onBookNotesSelected: (Long) -> Unit,
    onPromptEditDialogShow: () -> Unit,
    onQuizSelectionDialogShow: () -> Unit,
    onNewBookClick: () -> Unit,
    onImportClick: () -> Unit,
    onExportClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.End) {
        AnimatedVisibility(
            visible = fabExpanded,
            enter = fadeIn() + expandVertically(expandFrom = Alignment.Bottom),
            exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom)
        ) {
            Column(horizontalAlignment = Alignment.End) {
                SmallFloatingActionButton(
                    onClick = {
                        onFabExpandedChange(false)
                        val type = when {
                            selectedBookId != -1L -> "BOOK"
                            selectedCategory != null -> "CATEGORY"
                            else -> "ALL"
                        }
                        val id = when {
                            selectedBookId != -1L -> selectedBookId.toString()
                            selectedCategory != null -> selectedCategory
                            else -> "0"
                        }
                        onAdaptiveSelected(type, id ?: "0")
                    },
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    FabActionRow(Icons.Rounded.Psychology, stringResource(R.string.adaptive_training))
                }

                if (selectedBookId != -1L) {
                    SmallFloatingActionButton(
                        onClick = {
                            onFabExpandedChange(false)
                            onFlashcardDeckDialogShow()
                        },
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        FabActionRow(Icons.Rounded.Style, stringResource(R.string.flashcard_deck))
                    }
                    SmallFloatingActionButton(
                        onClick = {
                            onFabExpandedChange(false)
                            onBookSlideshowSelected(selectedBookId)
                        },
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        FabActionRow(Icons.Rounded.PlayLesson, stringResource(R.string.slideshow_course))
                    }
                    SmallFloatingActionButton(
                        onClick = {
                            onFabExpandedChange(false)
                            onBookReviewBlueprintSelected(selectedBookId)
                        },
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        FabActionRow(Icons.Rounded.Description, stringResource(R.string.review_blueprint))
                    }
                    SmallFloatingActionButton(
                        onClick = {
                            onFabExpandedChange(false)
                            onBookSourcesSelected(selectedBookId)
                        },
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        FabActionRow(Icons.Rounded.Book, "Sources")
                    }
                    SmallFloatingActionButton(
                        onClick = {
                            onFabExpandedChange(false)
                            onBookNotesSelected(selectedBookId)
                        },
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        FabActionRow(Icons.Rounded.NoteAlt, stringResource(R.string.book_notes))
                    }
                    SmallFloatingActionButton(
                        onClick = {
                            onFabExpandedChange(false)
                            onPromptEditDialogShow()
                        },
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        FabActionRow(Icons.Rounded.SmartToy, stringResource(R.string.ai_prompt_deck))
                    }
                    SmallFloatingActionButton(
                        onClick = {
                            onFabExpandedChange(false)
                            onQuizSelectionDialogShow()
                        },
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        FabActionRow(Icons.Rounded.Quiz, stringResource(R.string.new_quiz))
                    }
                } else if (selectedCategory == null) {
                    SmallFloatingActionButton(
                        onClick = {
                            onFabExpandedChange(false)
                            onNewBookClick()
                        },
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        FabActionRow(Icons.Rounded.Book, stringResource(R.string.new_book))
                    }
                }

                SmallFloatingActionButton(
                    onClick = {
                        onFabExpandedChange(false)
                        onImportClick()
                    },
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    FabActionRow(Icons.Rounded.FileDownload, stringResource(R.string.import_label))
                }

                SmallFloatingActionButton(
                    onClick = {
                        onFabExpandedChange(false)
                        onExportClick()
                    },
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    FabActionRow(Icons.Rounded.FileUpload, stringResource(R.string.export_label))
                }
            }
        }

        FloatingActionButton(
            onClick = { onFabExpandedChange(!fabExpanded) },
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            Icon(
                if (fabExpanded) Icons.Rounded.Close else Icons.Rounded.Add,
                contentDescription = stringResource(R.string.app_name)
            )
        }
    }
}

@Composable
private fun FabActionRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Row(
        modifier = Modifier.padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null)
        Spacer(Modifier.width(8.dp))
        Text(label)
    }
}
