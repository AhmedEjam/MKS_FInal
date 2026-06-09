package com.ahmedyejam.mks.ui.booktools

import androidx.compose.animation.*
import androidx.compose.animation.core.snap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.PlayLesson
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.SmartToy
import androidx.compose.material.icons.rounded.NoteAlt
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Slideshow
import androidx.compose.material.icons.rounded.Source
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmedyejam.mks.R
import com.ahmedyejam.mks.ui.theme.LocalMksDesignTokens
import com.ahmedyejam.mks.data.local.entity.BlueprintMode
import com.ahmedyejam.mks.data.local.entity.CourseSlideEntity
import com.ahmedyejam.mks.data.local.entity.NoteBlueprintEntity
import com.ahmedyejam.mks.data.local.entity.PromptCardEntity
import com.ahmedyejam.mks.data.local.entity.PromptDeckEntity
import com.ahmedyejam.mks.data.local.entity.PromptOutputType
import com.ahmedyejam.mks.data.local.entity.PromptRunEntity
import com.ahmedyejam.mks.data.local.entity.SlideshowCourseEntity
import com.ahmedyejam.mks.data.local.entity.SourceDocumentEntity
import com.ahmedyejam.mks.data.local.entity.SourceDocumentTypes
import com.ahmedyejam.mks.data.repository.BookKnowledgeSummary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlideshowCourseListScreen(
    bookId: Long,
    viewModel: BookToolsViewModel,
    onBack: () -> Unit,
    onOpenCourse: (Long) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val showCreateState = remember { mutableStateOf(false) }
    var showCreate by showCreateState
    val editingState = remember { mutableStateOf<SlideshowCourseEntity?>(null) }
    var editing by editingState
    val deletingState = remember { mutableStateOf<SlideshowCourseEntity?>(null) }
    var deleting by deletingState

    LaunchedEffect(bookId) { viewModel.loadBook(bookId) }

    if (showCreate) {
        TitleBodyDialog(
            title = "Create slideshow course",
            titleLabel = "Course title",
            bodyLabel = "Description",
            confirmLabel = "Create",
            onDismiss = { showCreateState.value = false },
            onConfirm = { title, body ->
                viewModel.createSlideshowCourse(bookId, title, body.takeIf { it.isNotBlank() })
                showCreateState.value = false
            }
        )
    }
    editing?.let { course ->
        TitleBodyDialog(
            title = "Edit slideshow course",
            titleValue = course.title,
            bodyValue = course.description.orEmpty(),
            titleLabel = "Course title",
            bodyLabel = "Description",
            confirmLabel = "Save",
            onDismiss = { editingState.value = null },
            onConfirm = { title, body ->
                viewModel.updateSlideshowCourse(course.copy(title = title, description = body.takeIf { it.isNotBlank() }))
                editingState.value = null
            }
        )
    }
    deleting?.let { course ->
        ConfirmDeleteDialog(
            title = "Delete slideshow?",
            body = "Delete '${course.title}' and its slides?",
            onDismiss = { deletingState.value = null },
            onConfirm = { viewModel.deleteSlideshowCourse(course); deletingState.value = null }
        )
    }

    BookToolListScaffold(
        title = "Slideshow courses",
        onBack = onBack,
        isLoading = state.isLoading,
        error = state.error,
        empty = state.allCourses.isEmpty(),
        emptyTitle = "No slideshow courses",
        emptyBody = "Create a lightweight slideshow course from this book when needed.",
        floatingActionButton = { FloatingActionButton(onClick = { showCreateState.value = true }) { Icon(Icons.Rounded.Add, null) } }
    ) { padding ->
        LazyColumn(Modifier.padding(padding).fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item { BookSummaryStrip(state.bookSummary) }
            items(state.allCourses, key = { it.id }) { course ->
                BookToolListItem(course.title, course.description ?: "${course.slideCount} slides", Icons.Rounded.Slideshow, { onOpenCourse(course.id) }, { editingState.value = course }, { deletingState.value = course })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewBlueprintListScreen(
    bookId: Long,
    viewModel: BookToolsViewModel,
    onBack: () -> Unit,
    onOpenNote: (Long) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val showCreateState = remember { mutableStateOf(false) }
    var showCreate by showCreateState
    val editingState = remember { mutableStateOf<NoteBlueprintEntity?>(null) }
    var editing by editingState
    val deletingState = remember { mutableStateOf<NoteBlueprintEntity?>(null) }
    var deleting by deletingState

    LaunchedEffect(bookId) { viewModel.loadBook(bookId) }

    if (showCreate) {
        ArticleCreateDialog(
            onDismiss = { showCreateState.value = false },
            onCreate = { title, body, mode ->
                viewModel.createNote(bookId, title, body, mode = mode)
                showCreateState.value = false
            },
            onMarked = {
                viewModel.createBlueprintFromMarked(bookId, "Marked questions article")
                showCreateState.value = false
            },
            onMissed = {
                viewModel.createBlueprintFromMissed(bookId, "Missed questions article")
                showCreateState.value = false
            }
        )
    }
    editing?.let { note ->
        ArticleCreateDialog(
            initialTitle = note.title,
            initialBody = note.body,
            initialMode = note.blueprintMode,
            onDismiss = { editingState.value = null },
            onCreate = { title, body, mode -> viewModel.updateNote(note.copy(title = title, body = body, blueprintMode = mode)); editingState.value = null },
            onMarked = null,
            onMissed = null
        )
    }
    deleting?.let { note ->
        ConfirmDeleteDialog("Delete article?", "Delete '${note.title}'?", { deletingState.value = null }, { viewModel.deleteNote(note); deletingState.value = null })
    }

    BookToolListScaffold(
        title = "Articles",
        onBack = onBack,
        isLoading = state.isLoading,
        error = state.error,
        empty = state.allNotes.isEmpty(),
        emptyTitle = "No articles",
        emptyBody = "Create a disease, drug, concept, or mistake-review article.",
        floatingActionButton = { FloatingActionButton(onClick = { showCreateState.value = true }) { Icon(Icons.Rounded.Add, null) } }
    ) { padding ->
        LazyColumn(Modifier.padding(padding).fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item { BookSummaryStrip(state.bookSummary) }
            items(state.allNotes, key = { it.id }) { note ->
                BookToolListItem(
                    title = note.title,
                    subtitle = "${note.blueprintMode} - ${note.reviewStatus} - ${note.summary ?: note.body.take(120)}",
                    icon = Icons.Rounded.NoteAlt,
                    onClick = { onOpenNote(note.id) },
                    onEdit = { editingState.value = note },
                    onDelete = { deletingState.value = note }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SourceDocumentListScreen(
    bookId: Long,
    focusedSourceId: Long? = null,
    viewModel: BookToolsViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val showCreateState = remember { mutableStateOf(false) }
    var showCreate by showCreateState
    val editingState = remember { mutableStateOf<SourceDocumentEntity?>(null) }
    var editing by editingState
    val deletingState = remember { mutableStateOf<SourceDocumentEntity?>(null) }
    var deleting by deletingState

    LaunchedEffect(bookId) { viewModel.loadBook(bookId) }

    val listState = rememberLazyListState()
    LaunchedEffect(state.allSources, focusedSourceId) {
        focusedSourceId?.let { id ->
            val index = state.allSources.indexOfFirst { it.id == id }
            if (index >= 0) listState.animateScrollToItem(index + 1)
        }
    }

    if (showCreate) {
        SourceDocumentDialog("Create source", "Create", onDismiss = { showCreateState.value = false }) { title, type, details ->
            viewModel.createSource(bookId, title, type, details)
            showCreateState.value = false
        }
    }
    editing?.let { source ->
        SourceDocumentDialog("Edit source", "Save", source.title, source.sourceType, source.description.orEmpty(), { editingState.value = null }) { title, type, details ->
            viewModel.updateSource(source.copy(title = title, sourceType = type, description = details.takeIf { it.isNotBlank() }))
            editingState.value = null
        }
    }
    deleting?.let { source ->
        ConfirmDeleteDialog("Delete source?", "Delete '${source.title}'? Citation assets may keep unresolved references.", { deletingState.value = null }, { viewModel.deleteSource(source); deletingState.value = null })
    }

    BookToolListScaffold(
        title = "Sources",
        onBack = onBack,
        isLoading = state.isLoading,
        error = state.error,
        empty = state.allSources.isEmpty(),
        emptyTitle = "No sources",
        emptyBody = "Add book-specific textbooks, PDFs, lectures, websites, or guidelines.",
        floatingActionButton = { FloatingActionButton(onClick = { showCreateState.value = true }) { Icon(Icons.Rounded.Add, null) } }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { BookSummaryStrip(state.bookSummary) }
            items(state.allSources, key = { it.id }) { source ->
                BookToolListItem(source.title, "${source.sourceType} - ${source.description.orEmpty()}", Icons.Rounded.Source, {}, { editingState.value = source }, { deletingState.value = source })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiPromptDeckListScreen(
    bookId: Long,
    viewModel: BookToolsViewModel,
    onBack: () -> Unit,
    onOpenPrompt: (Long) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val showCreateState = remember { mutableStateOf(false) }
    var showCreate by showCreateState
    val editingState = remember { mutableStateOf<PromptDeckEntity?>(null) }
    var editing by editingState
    val deletingState = remember { mutableStateOf<PromptDeckEntity?>(null) }
    var deleting by deletingState

    LaunchedEffect(bookId) { viewModel.loadBook(bookId) }

    if (showCreate) {
        TitleBodyDialog("Create prompt deck", "Deck title", "Description", "Create", onDismiss = { showCreateState.value = false }) { title, body ->
            viewModel.createPromptDeck(bookId, title, body.takeIf { it.isNotBlank() })
            showCreateState.value = false
        }
    }
    editing?.let { deck ->
        TitleBodyDialog("Edit prompt deck", "Deck title", "Description", "Save", deck.title, deck.description.orEmpty(), { editingState.value = null }) { title, body ->
            viewModel.updatePromptDeck(deck.copy(title = title, description = body.takeIf { it.isNotBlank() }))
            editingState.value = null
        }
    }
    deleting?.let { deck ->
        ConfirmDeleteDialog("Delete prompt deck?", "Delete '${deck.title}' and its cards/runs?", { deletingState.value = null }, { viewModel.deletePromptDeck(deck); deletingState.value = null })
    }

    BookToolListScaffold(
        title = "Prompt decks",
        onBack = onBack,
        isLoading = state.isLoading,
        error = state.error,
        empty = state.allPromptDecks.isEmpty(),
        emptyTitle = "No prompt decks",
        emptyBody = "Create reusable prompt decks for quiz generation, flashcards, and articles.",
        floatingActionButton = { FloatingActionButton(onClick = { showCreateState.value = true }) { Icon(Icons.Rounded.Add, null) } }
    ) { padding ->
        LazyColumn(Modifier.padding(padding).fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item { BookSummaryStrip(state.bookSummary) }
            items(state.allPromptDecks, key = { it.id }) { deck ->
                BookToolListItem(deck.title, deck.description ?: "Reusable prompt deck", Icons.Rounded.Psychology, { onOpenPrompt(deck.id) }, { editingState.value = deck }, { deletingState.value = deck })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewBlueprintScreen(noteId: Long, viewModel: BookToolsViewModel, onBack: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    var editedBody by remember { mutableStateOf("") }
    LaunchedEffect(noteId) { viewModel.loadNote(noteId) }
    LaunchedEffect(state.noteBlueprint?.id) { editedBody = state.noteBlueprint?.body.orEmpty() }
    val note = state.noteBlueprint

    Scaffold(topBar = { ToolTopBar(note?.title ?: "Article", onBack) }) { padding ->
        when {
            state.isLoading -> LoadingPane(Modifier.padding(padding))
            state.error != null -> MessagePane(Modifier.padding(padding), "Error", state.error ?: "Unknown error")
            note == null -> MessagePane(Modifier.padding(padding), "Article", "Article not found.")
            else -> LazyColumn(Modifier.padding(padding).fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                item {
                    val tokens = LocalMksDesignTokens.current
                    Card(
                        shape = RoundedCornerShape(tokens.cardRadius),
                        elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation)
                    ) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("${note.blueprintMode} - ${note.reviewStatus}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(editedBody, { editedBody = it }, modifier = Modifier.fillMaxWidth(), minLines = 8, label = { Text("Article body") })
                        Spacer(Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilledTonalButton(onClick = { viewModel.updateNote(note.copy(body = editedBody)) }) { Text("Save") }
                            FilledTonalButton(onClick = { viewModel.recordNoteReview(note) }) { Text("Mark reviewed") }
                            FilledTonalButton(onClick = { viewModel.createFlashcardsFromBlueprint(note.id) }) { Text("To flashcards") }
                            FilledTonalButton(onClick = { viewModel.appendBlueprintToQuestionNote(note.id) }) { Text("Append to question note") }
                        }
                    } }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookNotesScreen(bookId: Long, viewModel: BookToolsViewModel, onBack: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(bookId) { viewModel.loadBook(bookId) }
    
    var selectedQuizId by remember { mutableStateOf<Long?>(null) }
    var expanded by remember { mutableStateOf(false) }

    BookToolListScaffold("Question notes", onBack, state.isLoading, state.error, state.questions.none { !it.notes.isNullOrBlank() } && selectedQuizId == null, "No question notes", "Question notes will appear here.") { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            if (state.quizzes.isNotEmpty()) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    val selectedQuiz = state.quizzes.find { it.id == selectedQuizId }
                    OutlinedTextField(
                        value = selectedQuiz?.title ?: "All existing notes",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("All existing notes") },
                            onClick = { selectedQuizId = null; expanded = false }
                        )
                        state.quizzes.forEach { quiz ->
                            DropdownMenuItem(
                                text = { Text(quiz.title) },
                                onClick = { selectedQuizId = quiz.id; expanded = false }
                            )
                        }
                    }
                }
            }

            LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (selectedQuizId == null) {
                    items(state.questions.filter { !it.notes.isNullOrBlank() }, key = { "note_${it.id}" }) { question ->
                        val tokens = LocalMksDesignTokens.current
                        Card(
                            shape = RoundedCornerShape(tokens.cardRadius),
                            elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation)
                        ) {
                            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(question.text, fontWeight = FontWeight.Bold)
                                Text(question.notes.orEmpty())
                            }
                        }
                    }
                } else {
                    val questions = state.questionsByQuiz[selectedQuizId] ?: emptyList()
                    items(questions, key = { "edit_${it.id}" }) { question ->
                        val tokens = LocalMksDesignTokens.current
                        var noteText by remember(question.id, question.notes) { mutableStateOf(question.notes.orEmpty()) }
                        
                        Card(
                            shape = RoundedCornerShape(tokens.cardRadius),
                            elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation)
                        ) {
                            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(question.text, fontWeight = FontWeight.Bold)
                                OutlinedTextField(
                                    value = noteText,
                                    onValueChange = { noteText = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    minLines = 2,
                                    label = { Text("Note") },
                                    trailingIcon = {
                                        if (noteText != question.notes.orEmpty()) {
                                            IconButton(onClick = { viewModel.updateQuestionNote(question.id, noteText) }) {
                                                Icon(Icons.Rounded.Save, "Save")
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiPromptDeckScreen(
    promptId: Long,
    focusedCardId: Long? = null,
    focusedRunId: Long? = null,
    viewModel: BookToolsViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val clipboard = LocalClipboardManager.current
    val showCardDialogState = remember { mutableStateOf(false) }
    var showCardDialog by showCardDialogState
    val editingCardState = remember { mutableStateOf<PromptCardEntity?>(null) }
    var editingCard by editingCardState
    var selectedCardId by remember { mutableStateOf<Long?>(null) }
    var outputText by remember { mutableStateOf("") }

    LaunchedEffect(promptId) { viewModel.loadPromptDeck(promptId) }

    val listState = rememberLazyListState()

    // Use focused IDs if provided
    LaunchedEffect(state.promptCards, focusedCardId) {
        if (focusedCardId != null) {
            selectedCardId = focusedCardId
        }
    }

    LaunchedEffect(state.promptRuns, focusedRunId) {
        focusedRunId?.let {
            // Optional: scroll to run history or highlight
        }
    }

    val selectedCard = remember(state.promptCards, selectedCardId) {
        state.promptCards.firstOrNull { it.id == selectedCardId } ?: state.promptCards.firstOrNull()
    }
    val variables = remember(selectedCard?.promptText) { selectedCard?.let { viewModel.extractVariables(it.promptText) }.orEmpty() }
    val values = remember(selectedCard?.id, variables) {
        mutableStateMapOf<String, String>().apply { variables.forEach { put(it, "") } }
    }
    val renderedPrompt by remember(selectedCard?.promptText, values.toMap()) {
        derivedStateOf {
            var text = selectedCard?.promptText.orEmpty()
            values.forEach { (key, value) -> text = text.replace("{$key}", value.ifBlank { "{$key}" }) }
            text
        }
    }

    if (showCardDialog) {
        PromptCardDialog(
            onDismiss = { showCardDialogState.value = false },
            onConfirm = { title, prompt, type ->
                state.promptDeck?.let { viewModel.createPromptCard(it.id, title, prompt, type) }
                showCardDialogState.value = false
            }
        )
    }
    editingCard?.let { card ->
        PromptCardDialog(
            initialTitle = card.title,
            initialPrompt = card.promptText,
            initialType = card.outputType,
            onDismiss = { editingCardState.value = null },
            onConfirm = { title, prompt, type ->
                viewModel.updatePromptCard(card.copy(title = title, promptText = prompt, outputType = type))
                editingCardState.value = null
            }
        )
    }

    Scaffold(topBar = { ToolTopBar(state.promptDeck?.title ?: "Prompt deck", onBack) }, floatingActionButton = { FloatingActionButton(onClick = { showCardDialogState.value = true }) { Icon(Icons.Rounded.Add, null) } }) { padding ->
        when {
            state.isLoading -> LoadingPane(Modifier.padding(padding))
            state.error != null -> MessagePane(Modifier.padding(padding), "Error", state.error ?: "Unknown error")
            state.promptDeck == null -> MessagePane(Modifier.padding(padding), "Prompt deck", "Prompt deck not found.")
            else -> LazyColumn(
                modifier = Modifier.padding(padding).fillMaxSize(),
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    val tokens = LocalMksDesignTokens.current
                    Card(
                        shape = RoundedCornerShape(tokens.cardRadius),
                        elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation)
                    ) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(state.promptDeck?.title.orEmpty(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(state.promptDeck?.description ?: "Prompt cards are reusable templates. Copy the rendered prompt, paste it into any AI tool, then optionally save the output back into MKS.")
                    } }
                }
                item { Text("Prompt cards", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
                if (state.promptCards.isEmpty()) {
                    item { MessagePane(Modifier.fillMaxWidth(), "No prompt cards", "Create a card or make a new deck to get the default templates.") }
                } else {
                    items(state.promptCards, key = { "card_${it.id}" }) { card ->
                        val selected = card.id == selectedCard?.id
                        val tokens = LocalMksDesignTokens.current
                        Card(
                            shape = RoundedCornerShape(tokens.cardRadius),
                            elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation),
                            colors = CardDefaults.cardColors(containerColor = if (selected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface)
                        ) {
                            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Column(Modifier.weight(1f)) {
                                        Text(card.title, fontWeight = FontWeight.Bold)
                                        Text("${card.outputType} - used ${card.usageCount} time(s)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    IconButton(onClick = { editingCardState.value = card }) { Icon(Icons.Rounded.Edit, "Edit") }
                                    IconButton(onClick = { viewModel.deletePromptCard(card) }) { Icon(Icons.Rounded.Delete, "Delete") }
                                }
                                Text(card.promptText.take(180), style = MaterialTheme.typography.bodySmall)
                                Button(onClick = { selectedCardId = card.id; outputText = "" }) { Text(if (selected) "Selected" else "Use this card") }
                            }
                        }
                    }
                }
                selectedCard?.let { card ->
                    item { Text("Run prompt", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
                    items(variables) { variable ->
                        OutlinedTextField(values[variable].orEmpty(), { values[variable] = it }, modifier = Modifier.fillMaxWidth(), label = { Text(variable) })
                    }
                    item {
                        val tokens = LocalMksDesignTokens.current
                        Card(
                            shape = RoundedCornerShape(tokens.cardRadius),
                            elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation)
                        ) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("Rendered prompt", fontWeight = FontWeight.Bold)
                                IconButton(onClick = { clipboard.setText(AnnotatedString(renderedPrompt)); viewModel.recordPromptCardRun(card, values.toMap(), renderedPrompt, null) }) { Icon(Icons.Rounded.ContentCopy, "Copy") }
                            }
                            Text(renderedPrompt)
                            OutlinedTextField(outputText, { outputText = it }, modifier = Modifier.fillMaxWidth(), minLines = 5, label = { Text("Optional AI output to save") })
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                FilledTonalButton(onClick = { viewModel.recordPromptCardRun(card, values.toMap(), renderedPrompt, outputText.takeIf { it.isNotBlank() }) }) { Text("Save run") }
                                FilledTonalButton(onClick = { viewModel.savePromptOutputAsNote(card, outputText, "${card.title} note") }, enabled = outputText.isNotBlank()) { Text("To note") }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                FilledTonalButton(onClick = { viewModel.savePromptOutputAsBlueprint(card, outputText, "${card.title} article") }, enabled = outputText.isNotBlank()) { Text("To article") }
                                FilledTonalButton(onClick = { viewModel.savePromptOutputAsFlashcards(card, outputText, "${card.title} flashcards") }, enabled = outputText.isNotBlank()) { Text("To flashcards") }
                            }
                        } }
                    }
                }
                if (state.promptRuns.isNotEmpty()) {
                    item { Text("Run history", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
                    items(state.promptRuns.take(10), key = { "run_${it.id}" }) { run -> PromptRunItem(run) }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ToolTopBar(title: String, onBack: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookToolListScaffold(
    title: String,
    onBack: () -> Unit,
    isLoading: Boolean,
    error: String?,
    empty: Boolean,
    emptyTitle: String,
    emptyBody: String,
    floatingActionButton: @Composable (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = { ToolTopBar(title, onBack) },
        floatingActionButton = { floatingActionButton?.invoke() },
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            val tokens = LocalMksDesignTokens.current
            Crossfade(targetState = isLoading, animationSpec = tokens.animationSpec()) { loading ->
                if (loading) {
                    LoadingPane(Modifier.padding(paddingValues))
                } else {
                    if (error != null) {
                        MessagePane(
                            modifier = Modifier.padding(paddingValues),
                            title = stringResource(R.string.error_title),
                            body = error
                        )
                    } else if (empty) {
                        AnimatedVisibility(
                            visible = true,
                            enter = if (tokens.isPlain) fadeIn(snap()) else fadeIn() + expandVertically(),
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            MessagePane(title = emptyTitle, body = emptyBody)
                        }
                    } else {
                        content(paddingValues)
                    }
                }
            }
        }
    }
}

@Composable
private fun BookToolListItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    onEdit: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null
) {
    val tokens = LocalMksDesignTokens.current
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(tokens.cardRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation)
    ) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(icon, contentDescription = null)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(title, fontWeight = FontWeight.Bold)
                Text(subtitle, maxLines = 3, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            onEdit?.let { IconButton(onClick = it) { Icon(Icons.Rounded.Edit, "Edit") } }
            onDelete?.let { IconButton(onClick = it) { Icon(Icons.Rounded.Delete, "Delete") } }
        }
    }
}

@Composable
private fun BookSummaryStrip(summary: BookKnowledgeSummary?) {
    summary ?: return
    val tokens = LocalMksDesignTokens.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(tokens.cardRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Book summary", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                SummaryChip("Questions", summary.totalQuestions)
                SummaryChip("Marked", summary.markedQuestions)
                SummaryChip("Assets", summary.questionsWithAssets)
                SummaryChip("Prompts", summary.promptDecks)
            }
            LinearProgressIndicator(progress = { if (summary.totalQuestions == 0) 0f else (summary.totalQuestions - summary.unansweredQuestions).toFloat() / summary.totalQuestions }, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun SummaryChip(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value.toString(), fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun LoadingPane(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun MessagePane(modifier: Modifier = Modifier, title: String, body: String) {
    Column(modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(Icons.Rounded.Folder, null)
        Spacer(Modifier.height(12.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))
        Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun TitleBodyDialog(
    title: String,
    titleLabel: String,
    bodyLabel: String,
    confirmLabel: String,
    titleValue: String = "",
    bodyValue: String = "",
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var currentTitle by remember { mutableStateOf(titleValue) }
    var currentBody by remember { mutableStateOf(bodyValue) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(currentTitle, { currentTitle = it }, label = { Text(titleLabel) }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(currentBody, { currentBody = it }, label = { Text(bodyLabel) }, modifier = Modifier.fillMaxWidth(), minLines = 3)
            }
        },
        confirmButton = { Button(onClick = { onConfirm(currentTitle, currentBody) }, enabled = currentTitle.isNotBlank()) { Text(confirmLabel) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun ConfirmDeleteDialog(title: String, body: String, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(body) },
        confirmButton = { Button(onClick = onConfirm) { Text("Delete") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun ArticleCreateDialog(
    initialTitle: String = "",
    initialBody: String = "",
    initialMode: String = BlueprintMode.CONCEPT_TEMPLATE,
    onDismiss: () -> Unit,
    onCreate: (String, String, String) -> Unit,
    onMarked: (() -> Unit)?,
    onMissed: (() -> Unit)?
) {
    var title by remember { mutableStateOf(initialTitle) }
    var body by remember { mutableStateOf(initialBody) }
    var mode by remember { mutableStateOf(initialMode) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Article") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(title, { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(body, { body = it }, label = { Text("Body") }, modifier = Modifier.fillMaxWidth(), minLines = 4)
                OutlinedTextField(mode, { mode = it }, label = { Text("Mode") }, modifier = Modifier.fillMaxWidth())
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    onMarked?.let { FilledTonalButton(onClick = it) { Text("From marked") } }
                    onMissed?.let { FilledTonalButton(onClick = it) { Text("From missed") } }
                }
            }
        },
        confirmButton = { Button(onClick = { onCreate(title, body, mode) }, enabled = title.isNotBlank()) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun SourceDocumentDialog(
    title: String,
    confirmLabel: String,
    initialTitle: String = "",
    initialType: String = SourceDocumentTypes.OTHER,
    initialDetails: String = "",
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var sourceTitle by remember { mutableStateOf(initialTitle) }
    var sourceType by remember { mutableStateOf(initialType) }
    var details by remember { mutableStateOf(initialDetails) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(sourceTitle, { sourceTitle = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(sourceType, { sourceType = it }, label = { Text("Type") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(details, { details = it }, label = { Text("Details") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
            }
        },
        confirmButton = { Button(onClick = { onConfirm(sourceTitle, sourceType, details) }, enabled = sourceTitle.isNotBlank()) { Text(confirmLabel) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun PromptCardDialog(
    initialTitle: String = "",
    initialPrompt: String = "",
    initialType: String = PromptOutputType.OTHER,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf(initialTitle) }
    var prompt by remember { mutableStateOf(initialPrompt) }
    var type by remember { mutableStateOf(initialType) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Prompt card") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(title, { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(prompt, { prompt = it }, label = { Text("Prompt text, use {variables}") }, modifier = Modifier.fillMaxWidth(), minLines = 5)
                OutlinedTextField(type, { type = it }, label = { Text("Output type") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = { Button(onClick = { onConfirm(title, prompt, type) }, enabled = title.isNotBlank() && prompt.isNotBlank()) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun PromptRunItem(run: PromptRunEntity) {
    val tokens = LocalMksDesignTokens.current
    Card(
        shape = RoundedCornerShape(tokens.cardRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation)
    ) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Run #${run.id}", fontWeight = FontWeight.Bold)
        Text(run.renderedPrompt.take(220), style = MaterialTheme.typography.bodySmall)
        run.outputText?.takeIf { it.isNotBlank() }?.let { Text("Output: ${it.take(160)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
    } }
}
