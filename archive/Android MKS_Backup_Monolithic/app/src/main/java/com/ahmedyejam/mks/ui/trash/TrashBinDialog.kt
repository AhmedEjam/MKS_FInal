package com.ahmedyejam.mks.ui.trash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ahmedyejam.mks.data.local.entity.*
import com.ahmedyejam.mks.ui.library.LibraryViewModel

enum class TrashTab(val title: String) {
    BOOKS("Books"),
    QUIZZES("Quizzes"),
    FLASHCARDS("Flashcards"),
    SLIDESHOWS("Slideshows"),
    NOTES("Notes"),
    PROMPTS("Prompts")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrashBinDialog(
    viewModel: LibraryViewModel,
    onDismiss: () -> Unit
) {
    val deletedBooks by viewModel.deletedBooks.collectAsState(initial = emptyList())
    val deletedQuizzes by viewModel.deletedQuizzes.collectAsState(initial = emptyList())
    val deletedDecks by viewModel.deletedDecks.collectAsState(initial = emptyList())
    val deletedSlideshows by viewModel.deletedSlideshows.collectAsState(initial = emptyList())
    val deletedNotes by viewModel.deletedNotes.collectAsState(initial = emptyList())
    val deletedPrompts by viewModel.deletedPrompts.collectAsState(initial = emptyList())

    var selectedTab by remember { mutableStateOf(TrashTab.BOOKS) }
    var itemToPurge by remember { mutableStateOf<Any?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Trash Bin") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ScrollableTabRow(
                    selectedTabIndex = selectedTab.ordinal,
                    edgePadding = 0.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TrashTab.values().forEach { tab ->
                        Tab(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            text = { Text(tab.title) }
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    when (selectedTab) {
                        TrashTab.BOOKS -> {
                            if (deletedBooks.isEmpty()) {
                                EmptyTrashPlaceholder()
                            } else {
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(deletedBooks) { book ->
                                        TrashItemRow(
                                            title = book.title,
                                            description = book.description,
                                            onRestore = { viewModel.restoreBook(book.id) },
                                            onDelete = { itemToPurge = book }
                                        )
                                    }
                                }
                            }
                        }
                        TrashTab.QUIZZES -> {
                            if (deletedQuizzes.isEmpty()) {
                                EmptyTrashPlaceholder()
                            } else {
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(deletedQuizzes) { quiz ->
                                        TrashItemRow(
                                            title = quiz.title,
                                            description = quiz.description,
                                            onRestore = { viewModel.restoreQuiz(quiz.id) },
                                            onDelete = { itemToPurge = quiz }
                                        )
                                    }
                                }
                            }
                        }
                        TrashTab.FLASHCARDS -> {
                            if (deletedDecks.isEmpty()) {
                                EmptyTrashPlaceholder()
                            } else {
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(deletedDecks) { deck ->
                                        TrashItemRow(
                                            title = deck.title,
                                            description = deck.description,
                                            onRestore = { viewModel.restoreFlashcardDeck(deck.id) },
                                            onDelete = { itemToPurge = deck }
                                        )
                                    }
                                }
                            }
                        }
                        TrashTab.SLIDESHOWS -> {
                            if (deletedSlideshows.isEmpty()) {
                                EmptyTrashPlaceholder()
                            } else {
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(deletedSlideshows) { course ->
                                        TrashItemRow(
                                            title = course.title,
                                            description = course.description,
                                            onRestore = { viewModel.restoreSlideshowCourse(course.id) },
                                            onDelete = { itemToPurge = course }
                                        )
                                    }
                                }
                            }
                        }
                        TrashTab.NOTES -> {
                            if (deletedNotes.isEmpty()) {
                                EmptyTrashPlaceholder()
                            } else {
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(deletedNotes) { note ->
                                        TrashItemRow(
                                            title = note.title,
                                            description = note.summary,
                                            onRestore = { viewModel.restoreNoteBlueprint(note.id) },
                                            onDelete = { itemToPurge = note }
                                        )
                                    }
                                }
                            }
                        }
                        TrashTab.PROMPTS -> {
                            if (deletedPrompts.isEmpty()) {
                                EmptyTrashPlaceholder()
                            } else {
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(deletedPrompts) { deck ->
                                        TrashItemRow(
                                            title = deck.title,
                                            description = deck.description,
                                            onRestore = { viewModel.restorePromptDeck(deck.id) },
                                            onDelete = { itemToPurge = deck }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )

    // Confirmation dialog for permanent deletion
    itemToPurge?.let { item ->
        val title = when (item) {
            is BookEntity -> item.title
            is QuizEntity -> item.title
            is FlashcardDeckEntity -> item.title
            is SlideshowCourseEntity -> item.title
            is NoteBlueprintEntity -> item.title
            is PromptDeckEntity -> item.title
            else -> ""
        }

        AlertDialog(
            onDismissRequest = { itemToPurge = null },
            title = { Text("Permanently Delete?") },
            text = {
                Text("Are you sure you want to permanently delete \"$title\"? This action is irreversible and all nested files or assets will be lost.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        when (item) {
                            is BookEntity -> viewModel.permanentlyDeleteBook(item)
                            is QuizEntity -> viewModel.permanentlyDeleteQuiz(item)
                            is FlashcardDeckEntity -> viewModel.permanentlyDeleteFlashcardDeck(item)
                            is SlideshowCourseEntity -> viewModel.permanentlyDeleteSlideshowCourse(item)
                            is NoteBlueprintEntity -> viewModel.permanentlyDeleteNoteBlueprint(item)
                            is PromptDeckEntity -> viewModel.permanentlyDeletePromptDeck(item)
                        }
                        itemToPurge = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToPurge = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun TrashItemRow(
    title: String,
    description: String?,
    onRestore: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            if (!description.isNullOrBlank()) {
                Text(
                    description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            IconButton(onClick = onRestore) {
                Icon(
                    Icons.Default.Restore,
                    contentDescription = "Restore",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.DeleteForever,
                    contentDescription = "Permanently Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun EmptyTrashPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Trash is empty",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
