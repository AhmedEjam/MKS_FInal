package com.ahmedyejam.mks.ui.booktools

import androidx.compose.animation.*
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.rounded.Source
import androidx.compose.material3.AlertDialog
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
import com.ahmedyejam.mks.data.local.entity.BlueprintReviewStatus
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
    var showCreate by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf<SlideshowCourseEntity?>(null) }
    var deleting by remember { mutableStateOf<SlideshowCourseEntity?>(null) }

    LaunchedEffect(bookId) { viewModel.loadBook(bookId) }

    if (showCreate) {
        TitleBodyDialog(
            title = "Create slideshow course",
            titleLabel = "Course title",
            bodyLabel = "Description",
            confirmLabel = "Create",
            onDismiss = { showCreate = false },
            onConfirm = { title, body ->
                viewModel.createSlideshowCourse(bookId, title, body.takeIf { it.isNotBlank() })
                showCreate = false
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
            onDismiss = { editing = null },
            onConfirm = { title, body ->
                viewModel.updateSlideshowCourse(course.copy(title = title, description = body.takeIf { it.isNotBlank() }))
                editing = null
            }
        )
    }
    deleting?.let { course ->
        ConfirmDeleteDialog(
            title = "Delete slideshow?",
            body = "Delete '${course.title}' and its slides?",
            onDismiss = { deleting = null },
            onConfirm = { viewModel.deleteSlideshowCourse(course); deleting = null }
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
        floatingActionButton = { FloatingActionButton(onClick = { showCreate = true }) { Icon(Icons.Rounded.Add, null) } }
    ) { padding ->
        LazyColumn(Modifier.padding(padding).fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item { BookSummaryStrip(state.bookSummary) }
            items(state.allCourses, key = { it.id }) { course ->
                BookToolListItem(course.title, course.description ?: "${course.slideCount} slides", Icons.Rounded.PlayLesson, { onOpenCourse(course.id) }, { editing = course }, { deleting = course })
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
    var showCreate by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf<NoteBlueprintEntity?>(null) }
    var deleting by remember { mutableStateOf<NoteBlueprintEntity?>(null) }

    LaunchedEffect(bookId) { viewModel.loadBook(bookId) }

    if (showCreate) {
        BlueprintCreateDialog(
            onDismiss = { showCreate = false },
            onCreate = { title, body, mode -> viewModel.createNote(bookId, title, body, mode = mode); showCreate = false },
            onMarked = { viewModel.createBlueprintFromMarked(bookId, "Marked questions blueprint"); showCreate = false },
            onMissed = { viewModel.createBlueprintFromMissed(bookId, "Missed questions blueprint"); showCreate = false }
        )
    }
    editing?.let { note ->
        BlueprintCreateDialog(
            initialTitle = note.title,
            initialBody = note.body,
            initialMode = note.blueprintMode,
            onDismiss = { editing = null },
            onCreate = { title, body, mode -> viewModel.updateNote(note.copy(title = title, body = body, blueprintMode = mode)); editing = null },
            onMarked = null,
            onMissed = null
        )
    }
    deleting?.let { note ->
        ConfirmDeleteDialog("Delete blueprint?", "Delete '${note.title}'?", { deleting = null }, { viewModel.deleteNote(note); deleting = null })
    }

    BookToolListScaffold(
        title = "Blueprints",
        onBack = onBack,
        isLoading = state.isLoading,
        error = state.error,
        empty = state.allNotes.isEmpty(),
        emptyTitle = "No blueprints",
        emptyBody = "Create a disease, drug, concept, or mistake-review blueprint.",
        floatingActionButton = { FloatingActionButton(onClick = { showCreate = true }) { Icon(Icons.Rounded.Add, null) } }
    ) { padding ->
        LazyColumn(Modifier.padding(padding).fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item { BookSummaryStrip(state.bookSummary) }
            items(state.allNotes, key = { it.id }) { note ->
                BookToolListItem(
                    title = note.title,
                    subtitle = "${note.blueprintMode} - ${note.reviewStatus} - ${note.summary ?: note.body.take(120)}",
                    icon = Icons.Rounded.Description,
                    onClick = { onOpenNote(note.id) },
                    onEdit = { editing = note },
                    onDelete = { deleting = note }
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
    var showCreate by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf<SourceDocumentEntity?>(null) }
    var deleting by remember { mutableStateOf<SourceDocumentEntity?>(null) }

    LaunchedEffect(bookId) { viewModel.loadBook(bookId) }

    if (showCreate) {
        SourceDocumentDialog("Create source", "Create", onDismiss = { showCreate = false }) { title, type, details ->
            viewModel.createSource(bookId, title, type, details)
            showCreate = false
        }
    }
    editing?.let { source ->
        SourceDocumentDialog("Edit source", "Save", source.title, source.sourceType, source.description.orEmpty(), { editing = null }) { title, type, details ->
            viewModel.updateSource(source.copy(title = title, sourceType = type, description = details.takeIf { it.isNotBlank() }))
            editing = null
        }
    }
    deleting?.let { source ->
        ConfirmDeleteDialog("Delete source?", "Delete '${source.title}'? Citation assets may keep unresolved references.", { deleting = null }, { viewModel.deleteSource(source); deleting = null })
    }

    BookToolListScaffold(
        title = "Sources",
        onBack = onBack,
        isLoading = state.isLoading,
        error = state.error,
        empty = state.allSources.isEmpty(),
        emptyTitle = "No sources",
        emptyBody = "Add book-specific textbooks, PDFs, lectures, websites, or guidelines.",
        floatingActionButton = { FloatingActionButton(onClick = { showCreate = true }) { Icon(Icons.Rounded.Add, null) } }
    ) { padding ->
        LazyColumn(Modifier.padding(padding).fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item { BookSummaryStrip(state.bookSummary) }
            items(state.allSources, key = { it.id }) { source ->
                BookToolListItem(source.title, "${source.sourceType} - ${source.description.orEmpty()}", Icons.Rounded.Source, {}, { editing = source }, { deleting = source })
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
    var showCreate by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf<PromptDeckEntity?>(null) }
    var deleting by remember { mutableStateOf<PromptDeckEntity?>(null) }

    LaunchedEffect(bookId) { viewModel.loadBook(bookId) }

    if (showCreate) {
        TitleBodyDialog("Create prompt deck", "Deck title", "Description", "Create", onDismiss = { showCreate = false }) { title, body ->
            viewModel.createPromptDeck(bookId, title, body.takeIf { it.isNotBlank() })
            showCreate = false
        }
    }
    editing?.let { deck ->
        TitleBodyDialog("Edit prompt deck", "Deck title", "Description", "Save", deck.title, deck.description.orEmpty(), { editing = null }) { title, body ->
            viewModel.updatePromptDeck(deck.copy(title = title, description = body.takeIf { it.isNotBlank() }))
            editing = null
        }
    }
    deleting?.let { deck ->
        ConfirmDeleteDialog("Delete prompt deck?", "Delete '${deck.title}' and its cards/runs?", { deleting = null }, { viewModel.deletePromptDeck(deck); deleting = null })
    }

    BookToolListScaffold(
        title = "Prompt decks",
        onBack = onBack,
        isLoading = state.isLoading,
        error = state.error,
        empty = state.allPromptDecks.isEmpty(),
        emptyTitle = "No prompt decks",
        emptyBody = "Create reusable prompt decks for quiz generation, flashcards, and blueprints.",
        floatingActionButton = { FloatingActionButton(onClick = { showCreate = true }) { Icon(Icons.Rounded.Add, null) } }
    ) { padding ->
        LazyColumn(Modifier.padding(padding).fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item { BookSummaryStrip(state.bookSummary) }
            items(state.allPromptDecks, key = { it.id }) { deck ->
                BookToolListItem(deck.title, deck.description ?: "Reusable prompt deck", Icons.Rounded.SmartToy, { onOpenPrompt(deck.id) }, { editing = deck }, { deleting = deck })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlideshowCourseScreen(
    courseId: Long,
    focusedSlideId: Long? = null,
    viewModel: BookToolsViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var showCreate by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf<CourseSlideEntity?>(null) }
    var deleting by remember { mutableStateOf<CourseSlideEntity?>(null) }

    LaunchedEffect(courseId) { 
        viewModel.loadCourse(courseId)
    }

    // Auto-open/focus slide if provided
    LaunchedEffect(state.courseSlides, focusedSlideId) {
        if (focusedSlideId != null && state.courseSlides.isNotEmpty()) {
            val slide = state.courseSlides.find { it.id == focusedSlideId }
            if (slide != null) {
                // In this implementation, SlideshowCourseScreen is a list, 
                // so we might want to highlight or scroll to it.
                // For now, let's just make sure we are loaded.
            }
        }
    }

    if (showCreate) {
        TitleBodyDialog("Create slide", "Slide title", "Body", "Create", onDismiss = { showCreate = false }) { title, body ->
            viewModel.createCourseSlide(courseId, title, body)
            showCreate = false
        }
    }
    editing?.let { slide ->
        TitleBodyDialog("Edit slide", "Slide title", "Body", "Save", slide.title, slide.body, { editing = null }) { title, body ->
            viewModel.updateCourseSlide(slide.copy(title = title, body = body))
            editing = null
        }
    }
    deleting?.let { slide ->
        ConfirmDeleteDialog("Delete slide?", "Delete '${slide.title}'?", { deleting = null }, { viewModel.deleteCourseSlide(slide); deleting = null })
    }

    BookToolListScaffold(
        title = state.slideshowCourse?.title ?: "Slideshow",
        onBack = onBack,
        isLoading = state.isLoading,
        error = state.error,
        empty = state.courseSlides.isEmpty(),
        emptyTitle = "No slides",
        emptyBody = "Add slides to this course.",
        floatingActionButton = { FloatingActionButton(onClick = { showCreate = true }) { Icon(Icons.Rounded.Add, null) } }
    ) { padding ->
        LazyColumn(Modifier.padding(padding).fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(state.courseSlides, key = { it.id }) { slide ->
                BookToolListItem(slide.title, slide.body.take(160), Icons.Rounded.PlayLesson, {}, { editing = slide }, { deleting = slide })
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

    Scaffold(topBar = { ToolTopBar(note?.title ?: "Blueprint", onBack) }) { padding ->
        when {
            state.isLoading -> LoadingPane(Modifier.padding(padding))
            state.error != null -> MessagePane(Modifier.padding(padding), "Error", state.error ?: "Unknown error")
            note == null -> MessagePane(Modifier.padding(padding), "Blueprint", "Blueprint not found.")
            else -> LazyColumn(Modifier.padding(padding).fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                item {
                    val tokens = LocalMksDesignTokens.current
                    Card(
                        shape = RoundedCornerShape(tokens.cardRadius),
                        elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation)
                    ) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("${note.blueprintMode} - ${note.reviewStatus}", style = MaterialTheme.typography.bodyMedium)
                        Text("Reviewed ${note.reviewCount} time(s)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        OutlinedTextField(editedBody, { editedBody = it }, modifier = Modifier.fillMaxWidth(), minLines = 8, label = { Text("Blueprint body") })
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = { viewModel.updateNote(note.copy(body = editedBody)) }) { Icon(Icons.Rounded.Save, null); Spacer(Modifier.width(6.dp)); Text("Save") }
                            FilledTonalButton(onClick = { viewModel.recordNoteReview(note) }) { Text("Mark reviewed") }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
    BookToolListScaffold("Question notes", onBack, state.isLoading, state.error, state.questions.none { !it.notes.isNullOrBlank() }, "No question notes", "Question notes will appear here.") { padding ->
        LazyColumn(Modifier.padding(padding).fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(state.questions.filter { !it.notes.isNullOrBlank() }, key = { it.id }) { question ->
                val tokens = LocalMksDesignTokens.current
                Card(
                    shape = RoundedCornerShape(tokens.cardRadius),
                    elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation)
                ) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(question.text, fontWeight = FontWeight.Bold)
                    Text(question.notes.orEmpty())
                } }
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
    var showCardDialog by remember { mutableStateOf(false) }
    var editingCard by remember { mutableStateOf<PromptCardEntity?>(null) }
    var selectedCardId by remember { mutableStateOf<Long?>(null) }
    var outputText by remember { mutableStateOf("") }

    LaunchedEffect(promptId) { viewModel.loadPromptDeck(promptId) }

    // Use focused IDs if provided
    LaunchedEffect(state.promptCards, focusedCardId) {
        if (focusedCardId != null) {
            selectedCardId = focusedCardId
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
            onDismiss = { showCardDialog = false },
            onConfirm = { title, prompt, type ->
                state.promptDeck?.let { viewModel.createPromptCard(it.id, title, prompt, type) }
                showCardDialog = false
            }
        )
    }
    editingCard?.let { card ->
        PromptCardDialog(
            initialTitle = card.title,
            initialPrompt = card.promptText,
            initialType = card.outputType,
            onDismiss = { editingCard = null },
            onConfirm = { title, prompt, type ->
                viewModel.updatePromptCard(card.copy(title = title, promptText = prompt, outputType = type))
                editingCard = null
            }
        )
    }

    Scaffold(topBar = { ToolTopBar(state.promptDeck?.title ?: "Prompt deck", onBack) }, floatingActionButton = { FloatingActionButton(onClick = { showCardDialog = true }) { Icon(Icons.Rounded.Add, null) } }) { padding ->
        when {
            state.isLoading -> LoadingPane(Modifier.padding(padding))
            state.error != null -> MessagePane(Modifier.padding(padding), "Error", state.error ?: "Unknown error")
            state.promptDeck == null -> MessagePane(Modifier.padding(padding), "Prompt deck", "Prompt deck not found.")
            else -> LazyColumn(Modifier.padding(padding).fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
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
                                    IconButton(onClick = { editingCard = card }) { Icon(Icons.Rounded.Edit, "Edit") }
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
                                FilledTonalButton(onClick = { viewModel.savePromptOutputAsBlueprint(card, outputText, "${card.title} blueprint") }, enabled = outputText.isNotBlank()) { Text("To blueprint") }
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
private fun BlueprintCreateDialog(
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
        title = { Text("Blueprint") },
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
