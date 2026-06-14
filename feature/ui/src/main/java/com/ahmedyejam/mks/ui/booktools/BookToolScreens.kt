package com.ahmedyejam.mks.ui.booktools

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.snap
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.NoteAlt
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material.icons.rounded.RecordVoiceOver
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Slideshow
import androidx.compose.material.icons.rounded.SmartToy
import androidx.compose.material.icons.rounded.Source
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ahmedyejam.mks.core.ui.R
import com.ahmedyejam.mks.data.local.entity.BlueprintMode
import com.ahmedyejam.mks.data.local.entity.NoteBlueprintEntity
import com.ahmedyejam.mks.data.local.entity.PromptDeckEntity
import com.ahmedyejam.mks.data.local.entity.PromptOutputType
import com.ahmedyejam.mks.data.local.entity.PromptRunEntity
import com.ahmedyejam.mks.data.local.entity.QuestionEntity
import com.ahmedyejam.mks.data.local.entity.SlideshowCourseEntity
import com.ahmedyejam.mks.data.local.entity.SourceDocumentEntity
import com.ahmedyejam.mks.data.local.entity.SourceDocumentTypes
import com.ahmedyejam.mks.data.repository.BookKnowledgeSummary
import com.ahmedyejam.mks.ui.components.EntityEditDialog
import com.ahmedyejam.mks.ui.theme.LocalMksDesignTokens

enum class QuestionComponent(val label: String) {
    OPTIONS("Options"),
    EXPLANATION("Explanation"),
    HINT("Hint"),
    REFERENCE("Reference"),
    IMAGE("Image"),
    CATEGORIES("Categories")
}

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
        EntityEditDialog(
            title = stringResource(R.string.create_study_slides),
            titleLabel = "Course title",
            descriptionLabel = "Description",
            onDismiss = { showCreateState.value = false },
            onSave = { title, body, _ ->
                viewModel.createSlideshowCourse(bookId, title, body.takeIf { it.isNotBlank() })
                showCreateState.value = false
            }
        )
    }
    editing?.let { course ->
        EntityEditDialog(
            title = stringResource(R.string.edit_slide),
            initialName = course.title,
            initialDescription = course.description.orEmpty(),
            titleLabel = "Course title",
            descriptionLabel = "Description",
            onDismiss = { editingState.value = null },
            onSave = { title, body, _ ->
                viewModel.updateSlideshowCourse(course.copy(title = title, description = body.takeIf { it.isNotBlank() }))
                editingState.value = null
            }
        )
    }
    deleting?.let { course ->
        ConfirmDeleteDialog(
            title = "Delete study slides?",
            body = "Delete '${course.title}' and its slides?",
            onDismiss = { deletingState.value = null },
            onConfirm = { viewModel.deleteSlideshowCourse(course); deletingState.value = null }
        )
    }

    BookToolListScaffold(
        title = stringResource(R.string.slideshow_courses_title),
        onBack = onBack,
        isLoading = state.isLoading,
        error = state.error,
        empty = state.allCourses.isEmpty(),
        emptyTitle = stringResource(R.string.no_study_slides),
        emptyBody = stringResource(R.string.no_study_slides_body),
        floatingActionButton = { FloatingActionButton(onClick = { showCreateState.value = true }) { Icon(Icons.Rounded.Add, null) } }
    ) { padding ->
        LazyColumn(Modifier
            .padding(padding)
            .fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
        title = "Articles and short notes",
        onBack = onBack,
        isLoading = state.isLoading,
        error = state.error,
        empty = state.allNotes.isEmpty(),
        emptyTitle = "No articles or notes",
        emptyBody = "Create a disease, drug, concept, or mistake-review article.",
        floatingActionButton = { FloatingActionButton(onClick = { showCreateState.value = true }) { Icon(Icons.Rounded.Add, null) } }
    ) { padding ->
        LazyColumn(Modifier
            .padding(padding)
            .fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
    val focusedSourceState = remember { mutableStateOf<SourceDocumentEntity?>(null) }
    var focusedSource by focusedSourceState

    LaunchedEffect(bookId) { viewModel.loadBook(bookId) }

    LaunchedEffect(focusedSource?.id) {
        focusedSource?.id?.let { viewModel.loadSourceAssets(it) }
    }

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

    focusedSource?.let { source ->
        SourceDocumentAssetsDialog(
            sourceDocument = source,
            assets = state.sourceAssets,
            onDismiss = { focusedSourceState.value = null },
            onAddAsset = { viewModel.addSourceAsset(it) },
            onUpdateAsset = { viewModel.updateSourceAsset(it) },
            onDeleteAsset = { viewModel.deleteSourceAsset(it) }
        )
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
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { BookSummaryStrip(state.bookSummary) }
            items(state.allSources, key = { it.id }) { source ->
                BookToolListItem(source.title, "${source.sourceType} - ${source.description.orEmpty()}", Icons.Rounded.Source, { focusedSourceState.value = source }, { editingState.value = source }, { deletingState.value = source })
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
        EntityEditDialog(
            title = "Create prompt deck",
            titleLabel = "Deck title",
            descriptionLabel = "Description",
            onDismiss = { showCreateState.value = false },
            onSave = { title, body, _ ->
                viewModel.createPromptDeck(bookId, title, body.takeIf { it.isNotBlank() })
                showCreateState.value = false
            }
        )
    }
    editing?.let { deck ->
        EntityEditDialog(
            title = "Edit prompt deck",
            titleLabel = "Deck title",
            descriptionLabel = "Description",
            initialName = deck.title,
            initialDescription = deck.description.orEmpty(),
            onDismiss = { editingState.value = null },
            onSave = { title, body, _ ->
                viewModel.updatePromptDeck(
                    deck.copy(
                        title = title,
                        description = body.takeIf { it.isNotBlank() })
                )
                editingState.value = null
            }
        )
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
        LazyColumn(Modifier
            .padding(padding)
            .fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
    val note = state.noteBlueprint
    
    var isPlayerMode by remember { mutableStateOf(true) }
    var controlsVisible by remember { mutableStateOf(true) }
    
    val view = androidx.compose.ui.platform.LocalView.current
    val context = androidx.compose.ui.platform.LocalContext.current
    val window = (context as? android.app.Activity)?.window

    LaunchedEffect(isPlayerMode, controlsVisible) {
        window?.let {
            val insetsController = androidx.core.view.WindowCompat.getInsetsController(it, view)
            if (isPlayerMode && !controlsVisible) {
                insetsController.hide(androidx.core.view.WindowInsetsCompat.Type.systemBars())
                insetsController.systemBarsBehavior = androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            } else {
                insetsController.show(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            }
        }
    }
    
    DisposableEffect(Unit) {
        onDispose {
            window?.let {
                val insetsController = androidx.core.view.WindowCompat.getInsetsController(it, view)
                insetsController.show(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            }
        }
    }

    LaunchedEffect(noteId) { viewModel.loadNote(noteId) }

    var editedBody by remember(note?.id) { mutableStateOf(note?.body.orEmpty()) }
    var isTitlePinned by remember { mutableStateOf(false) }

    val ttsManager = remember { com.ahmedyejam.mks.ui.utils.TtsManager(context) }
    DisposableEffect(ttsManager) { onDispose { ttsManager.shutdown() } }

    var ttsRate by remember { androidx.compose.runtime.mutableFloatStateOf(1f) }
    var ttsPitch by remember { androidx.compose.runtime.mutableFloatStateOf(1f) }
    var isTtsPlaying by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    var isAutoScrolling by remember { mutableStateOf(false) }
    var scrollSpeed by remember { androidx.compose.runtime.mutableFloatStateOf(50f) }

    LaunchedEffect(isAutoScrolling, scrollSpeed, scrollState.maxValue) {
        if (isAutoScrolling && scrollState.value < scrollState.maxValue) {
            val remainingScroll = scrollState.maxValue - scrollState.value
            val duration = (remainingScroll / scrollSpeed * 1000).toInt()
            if (duration > 0) {
                scrollState.animateScrollTo(scrollState.maxValue, animationSpec = androidx.compose.animation.core.tween(duration, easing = androidx.compose.animation.core.LinearEasing))
                isAutoScrolling = false
            }
        }
    }

    if (note == null && !state.isLoading) {
        Scaffold(topBar = { ToolTopBar("Article", onBack) }) { padding ->
            MessagePane(Modifier.padding(padding), "Article", "Article not found.")
        }
        return
    }

    Scaffold(
        topBar = {
            AnimatedVisibility(visible = controlsVisible || !isPlayerMode, enter = androidx.compose.animation.slideInVertically { -it }, exit = androidx.compose.animation.slideOutVertically { -it }) {
                ToolTopBar(note?.title ?: "Article", onBack) {
                    IconButton(onClick = {
                        isPlayerMode = !isPlayerMode
                        if (!isPlayerMode) controlsVisible = true
                    }) {
                        Icon(if (isPlayerMode) Icons.Rounded.Edit else Icons.Rounded.PlayArrow, if (isPlayerMode) "Edit Mode" else "Player Mode")
                    }
                }
            }
        },
        bottomBar = {
            AnimatedVisibility(visible = isPlayerMode && controlsVisible, enter = androidx.compose.animation.slideInVertically { it }, exit = androidx.compose.animation.slideOutVertically { it }) {
                BottomAppBar {
                    IconButton(onClick = {
                        if (isTtsPlaying) {
                            ttsManager.stop()
                            isTtsPlaying = false
                        } else {
                            ttsManager.play(note?.body ?: "", ttsPitch, ttsRate)
                            isTtsPlaying = true
                        }
                    }) {
                        Icon(if (isTtsPlaying) Icons.Rounded.Stop else Icons.Rounded.RecordVoiceOver, "TTS Play/Stop")
                    }
                    Spacer(Modifier.width(8.dp))
                    IconButton(onClick = { isTitlePinned = !isTitlePinned }) {
                        Icon(if (isTitlePinned) Icons.Rounded.PushPin else Icons.Outlined.PushPin, "Pin Title")
                    }
                    Spacer(Modifier.width(8.dp))
                    IconButton(onClick = { isAutoScrolling = !isAutoScrolling }) {
                        Icon(if (isAutoScrolling) Icons.Rounded.Pause else Icons.Rounded.ArrowDownward, "Autoscroll")
                    }
                    Spacer(Modifier.weight(1f))
                    var showSettings by remember { mutableStateOf(false) }
                    IconButton(onClick = { showSettings = true }) {
                        Icon(Icons.Rounded.Settings, "Player Settings")
                    }

                    if (showSettings) {
                        ModalBottomSheet(onDismissRequest = { showSettings = false }) {
                            Column(Modifier
                                .padding(16.dp)
                                .fillMaxWidth()) {
                                Text("Player Settings", style = MaterialTheme.typography.titleLarge)
                                Spacer(Modifier.height(16.dp))
                                Text("Auto-scroll speed: ${scrollSpeed.toInt()} px/s")
                                Slider(value = scrollSpeed, onValueChange = { scrollSpeed = it }, valueRange = 10f..200f)
                                Text(
                                    "TTS Rate: ${
                                        String.format(
                                            java.util.Locale.getDefault(),
                                            "%.1f",
                                            ttsRate
                                        )
                                    }x"
                                )
                                Slider(value = ttsRate, onValueChange = { ttsRate = it }, valueRange = 0.5f..2.0f)
                                Text(
                                    "TTS Pitch: ${
                                        String.format(
                                            java.util.Locale.getDefault(),
                                            "%.1f",
                                            ttsPitch
                                        )
                                    }"
                                )
                                Slider(value = ttsPitch, onValueChange = { ttsPitch = it }, valueRange = 0.5f..2.0f)
                                Spacer(Modifier.height(32.dp))
                            }
                        }
                    }
                }
            }
        }
    ) { padding ->
        if (state.isLoading) {
            LoadingPane(Modifier.padding(padding))
        } else if (isPlayerMode) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .clickable(
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                        indication = null
                    ) { controlsVisible = !controlsVisible }
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    if (isTitlePinned) {
                        Text(
                            text = note?.title ?: "",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        )
                        androidx.compose.material3.HorizontalDivider()
                    }
                    
                    Column(
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(16.dp)
                    ) {
                        if (!isTitlePinned) {
                            Text(
                                text = note?.title ?: "",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(bottom = 16.dp)
                                    .fillMaxWidth()
                            )
                        }
                        Text(
                            text = note?.body ?: "",
                            style = MaterialTheme.typography.bodyLarge.copy(lineHeight = androidx.compose.ui.unit.TextUnit(28f, androidx.compose.ui.unit.TextUnitType.Sp)),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(100.dp))
                    }
                }
            }
        } else {
            LazyColumn(Modifier
                .padding(padding)
                .fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                item {
                    val tokens = LocalMksDesignTokens.current
                    Card(
                        shape = RoundedCornerShape(tokens.cardRadius),
                        elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation)
                    ) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("${note?.blueprintMode} - ${note?.reviewStatus}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(editedBody, { editedBody = it }, modifier = Modifier.fillMaxWidth(), minLines = 8, label = { Text("Article body") })
                        Spacer(Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            if (note != null) {
                                FilledTonalButton(onClick = { viewModel.updateNote(note.copy(body = editedBody)) }) { Text("Save") }
                                FilledTonalButton(onClick = { viewModel.recordNoteReview(note) }) { Text("Mark reviewed") }
                                FilledTonalButton(onClick = { viewModel.createFlashcardsFromBlueprint(note.id) }) { Text("To flashcards") }
                                FilledTonalButton(onClick = { viewModel.appendBlueprintToQuestionNote(note.id) }) { Text("Append to question note") }
                            }
                        }
                    } }
                }
            }
        }
    }
}

@Composable
fun QuestionNoteItem(
    question: QuestionEntity,
    visibleComponents: Set<QuestionComponent>,
    onSaveNote: (String) -> Unit
) {
    val tokens = LocalMksDesignTokens.current
    var noteText by remember(question.id, question.notes) { mutableStateOf(question.notes.orEmpty()) }

    Card(
        shape = RoundedCornerShape(tokens.cardRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(question.text, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)

            if (QuestionComponent.IMAGE in visibleComponents && !question.imagePath.isNullOrBlank()) {
                AsyncImage(
                    model = question.imagePath,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 240.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Fit
                )
            }

            if (QuestionComponent.OPTIONS in visibleComponents && question.options.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Options:", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                    question.options.forEachIndexed { index, option ->
                        val isCorrect = question.correctAnswers.contains(index)
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(
                                if (isCorrect) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (isCorrect) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(option, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            if (QuestionComponent.EXPLANATION in visibleComponents && !question.explanation.isNullOrBlank()) {
                DetailSection("Explanation", question.explanation!!)
            }
            if (QuestionComponent.HINT in visibleComponents && !question.hint.isNullOrBlank()) {
                DetailSection("Hint", question.hint!!)
            }
            if (QuestionComponent.REFERENCE in visibleComponents && !question.reference.isNullOrBlank()) {
                DetailSection("Reference", question.reference!!)
            }
            if (QuestionComponent.CATEGORIES in visibleComponents && question.categories.isNotEmpty()) {
                DetailSection("Categories", question.categories.joinToString(", "))
            }

            Spacer(Modifier.height(4.dp))

            OutlinedTextField(
                value = noteText,
                onValueChange = { noteText = it },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                label = { Text("Question Note") },
                placeholder = { Text("Add a note...") },
                trailingIcon = {
                    if (noteText != question.notes.orEmpty()) {
                        IconButton(onClick = { onSaveNote(noteText) }) {
                            Icon(Icons.Rounded.Save, "Save")
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun DetailSection(label: String, content: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Text(content, style = MaterialTheme.typography.bodySmall)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookNotesScreen(bookId: Long, viewModel: BookToolsViewModel, onBack: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(bookId) { viewModel.loadBook(bookId) }
    
    var selectedQuizId by remember { mutableStateOf<Long?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var visibleComponents by remember { mutableStateOf(setOf<QuestionComponent>()) }

    BookToolListScaffold("Question notes", onBack, state.isLoading, state.error, state.questions.none { !it.notes.isNullOrBlank() } && selectedQuizId == null, "No question notes", "Question notes will appear here.") { padding ->
        Column(Modifier
            .padding(padding)
            .fillMaxSize()) {
            if (state.quizzes.isNotEmpty()) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    val selectedQuiz = state.quizzes.find { it.id == selectedQuizId }
                    OutlinedTextField(
                        value = selectedQuiz?.title ?: "All existing notes",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor(androidx.compose.material3.MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
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

            if (selectedQuizId != null) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(end = 16.dp)
                ) {
                    items(QuestionComponent.entries.toTypedArray()) { component ->
                        FilterChip(
                            selected = component in visibleComponents,
                            onClick = {
                                visibleComponents = if (component in visibleComponents) {
                                    visibleComponents - component
                                } else {
                                    visibleComponents + component
                                }
                            },
                            label = { Text(component.label) }
                        )
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
                        QuestionNoteItem(
                            question = question,
                            visibleComponents = visibleComponents,
                            onSaveNote = { note -> viewModel.updateQuestionNote(question.id, note) }
                        )
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
    @Suppress("UNUSED_PARAMETER") focusedRunId: Long? = null,
    seededQuestionId: Long? = null,
    viewModel: BookToolsViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val clipboard = LocalClipboardManager.current
    val context = androidx.compose.ui.platform.LocalContext.current
    var selectedCardId by remember { mutableStateOf<Long?>(null) }
    var outputText by remember { mutableStateOf("") }

    LaunchedEffect(promptId) { viewModel.loadPromptDeck(promptId) }

    val listState = rememberLazyListState()

    LaunchedEffect(state.promptCards, focusedCardId, seededQuestionId) {
        if (focusedCardId != null) {
            selectedCardId = focusedCardId
        } else if (seededQuestionId != null && state.promptCards.isNotEmpty() && selectedCardId == null) {
            val explainCard = state.promptCards.find { it.title.contains("Explain", ignoreCase = true) || it.title.contains("Ask AI", ignoreCase = true) }
            if (explainCard != null) {
                selectedCardId = explainCard.id
            }
        }
    }

    val handleBack = {
        if (selectedCardId != null) {
            selectedCardId = null
        } else {
            onBack()
        }
    }

    val selectedCard = remember(state.promptCards, selectedCardId) {
        state.promptCards.firstOrNull { it.id == selectedCardId }
    }

    var editTitle by remember(selectedCard?.id) { mutableStateOf(selectedCard?.title ?: "") }
    var editBody by remember(selectedCard?.id) { mutableStateOf(selectedCard?.promptText ?: "") }

    val variables = remember(editBody) { viewModel.extractVariables(editBody) }
    val values = remember(selectedCard?.id) { mutableStateMapOf<String, String>() }
    val imageValues = remember(selectedCard?.id) { mutableStateMapOf<String, List<String>>() }
    var showEntitySelector by remember { mutableStateOf<String?>(null) }
    
    LaunchedEffect(variables) {
        variables.forEach { if (!values.containsKey(it)) values[it] = "" }
        val currentKeys = values.keys.toList()
        currentKeys.forEach { if (it !in variables) values.remove(it) }
    }

    LaunchedEffect(selectedCardId, seededQuestionId, variables, state.questions) {
        if (selectedCardId != null && seededQuestionId != null && variables.isNotEmpty() && state.questions.isNotEmpty()) {
            val q = state.questions.find { it.id == seededQuestionId }
            if (q != null) {
                val qText = buildString {
                    appendLine("Question: ${q.text}")
                    if (q.options.isNotEmpty()) {
                        appendLine("Options:")
                        q.options.forEach { appendLine("- $it") }
                    }
                    if (!q.explanation.isNullOrBlank()) {
                        appendLine("Explanation: ${q.explanation}")
                    }
                }
                
                val varName = variables.firstOrNull { it.contains("question", ignoreCase = true) || it.contains("content", ignoreCase = true) || it.contains("text", ignoreCase = true) }
                if (varName != null && values[varName].isNullOrBlank()) {
                    values[varName] = qText
                }
                
                val partName = variables.firstOrNull { it.contains("part_to_explain", ignoreCase = true) || it.contains("part", ignoreCase = true) }
                if (partName != null && values[partName].isNullOrBlank()) {
                    values[partName] = "the entire question, options, and explanation"
                }
            }
        }
    }

    val renderedPrompt by remember(editBody, values.toMap()) {
        derivedStateOf {
            var text = editBody
            values.forEach { (key, value) -> text = text.replace(key, value.ifBlank { key }) }
            text
        }
    }

    Scaffold(
        topBar = { ToolTopBar(state.promptDeck?.title ?: "Prompt deck", handleBack) },
        floatingActionButton = {
            if (selectedCardId == null) {
                var showCreateOptions by remember { mutableStateOf(false) }
                Box {
                    FloatingActionButton(onClick = { showCreateOptions = true }) { 
                        Icon(Icons.Rounded.Add, null) 
                    }
                    DropdownMenu(
                        expanded = showCreateOptions,
                        onDismissRequest = { showCreateOptions = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Create empty prompt") },
                            onClick = {
                                state.promptDeck?.let { viewModel.createPromptCard(it.id, "New Prompt", "") }
                                showCreateOptions = false
                            }
                        )
                        androidx.compose.material3.HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Template: Quiz generator") },
                            onClick = {
                                state.promptDeck?.let { viewModel.createTemplatePromptCard(it.id, "QUIZ") }
                                showCreateOptions = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Template: Flashcard generator") },
                            onClick = {
                                state.promptDeck?.let { viewModel.createTemplatePromptCard(it.id, "FLASHCARDS") }
                                showCreateOptions = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Template: Blueprint maker") },
                            onClick = {
                                state.promptDeck?.let { viewModel.createTemplatePromptCard(it.id, "BLUEPRINT") }
                                showCreateOptions = false
                            }
                        )
                    }
                }
            }
        }
    ) { padding ->
        when {
            state.isLoading -> LoadingPane(Modifier.padding(padding))
            state.error != null -> MessagePane(Modifier.padding(padding), "Error", state.error ?: "Unknown error")
            state.promptDeck == null -> MessagePane(Modifier.padding(padding), "Prompt deck", "Prompt deck not found.")
            selectedCardId == null -> {
                // List View
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
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
                            Text(state.promptDeck?.description ?: "Prompt templates. Select one to open the live editor.")
                        } }
                    }
                    if (state.promptCards.isEmpty()) {
                        item { MessagePane(Modifier.fillMaxWidth(), "No prompt cards", "Create a card to start designing your prompts.") }
                    } else {
                        items(state.promptCards, key = { "card_${it.id}" }) { card ->
                            val tokens = LocalMksDesignTokens.current
                            Card(
                                modifier = Modifier.clickable { selectedCardId = card.id },
                                shape = RoundedCornerShape(tokens.cardRadius),
                                elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation)
                            ) {
                                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                        Column(Modifier.weight(1f)) {
                                            Text(card.title, fontWeight = FontWeight.Bold)
                                            Text("Used ${card.usageCount} time(s)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                        Row {
                                            TextButton(onClick = { viewModel.movePromptCard(card, true) }) { Text("Up") }
                                            TextButton(onClick = { viewModel.movePromptCard(card, false) }) { Text("Down") }
                                            IconButton(onClick = { viewModel.deletePromptCard(card) }) { Icon(Icons.Rounded.Delete, "Delete") }
                                        }
                                    }
                                    Text(card.promptText.take(180).ifBlank { "Empty prompt" }, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            }
            selectedCard != null -> {
                // Editor View
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        val tokens = LocalMksDesignTokens.current
                        Card(
                            shape = RoundedCornerShape(tokens.cardRadius),
                            elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation)
                        ) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedTextField(
                                value = editTitle,
                                onValueChange = { editTitle = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Prompt Title") },
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = editBody,
                                onValueChange = { editBody = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Prompt Body (Use {}, [], or () for variables)") },
                                minLines = 4
                            )
                            Button(onClick = {
                                viewModel.updatePromptCard(selectedCard.copy(title = editTitle, promptText = editBody))
                            }, modifier = Modifier.align(Alignment.End)) {
                                Text("Save Prompt Template")
                            }
                        } }
                    }

                    if (variables.isNotEmpty()) {
                        item { Text("Variables", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
                        items(variables) { variable ->
                            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = values[variable].orEmpty(),
                                    onValueChange = { values[variable] = it },
                                    modifier = Modifier.weight(1f),
                                    label = { Text(variable) }
                                )
                                IconButton(onClick = { showEntitySelector = variable }) {
                                    Icon(Icons.Rounded.Add, "Inject Entity")
                                }
                            }
                        }
                    }

                    item {
                        val tokens = LocalMksDesignTokens.current
                        Card(
                            shape = RoundedCornerShape(tokens.cardRadius),
                            elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                        ) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("Live Rendered Prompt", fontWeight = FontWeight.Bold)
                                Row {
                                    IconButton(onClick = { clipboard.setText(AnnotatedString(renderedPrompt)) }) { Icon(Icons.Rounded.ContentCopy, "Copy") }
                                    IconButton(onClick = {
                                        val sendIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                            type = "text/plain"
                                            putExtra(android.content.Intent.EXTRA_TEXT, renderedPrompt)
                                        }
                                        context.startActivity(android.content.Intent.createChooser(sendIntent, "Send prompt to..."))
                                    }) { Icon(Icons.Rounded.Share, "Share") }
                                }
                            }
                            Text(renderedPrompt)
                        } }
                    }

                    item {
                        val tokens = LocalMksDesignTokens.current
                        Card(
                            shape = RoundedCornerShape(tokens.cardRadius),
                            elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation)
                        ) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("AI Output Review", fontWeight = FontWeight.Bold)
                                if (state.isGenerating) {
                                    FilledTonalButton(
                                        onClick = { viewModel.cancelGeneration() },
                                        colors = androidx.compose.material3.ButtonDefaults.filledTonalButtonColors(
                                            containerColor = MaterialTheme.colorScheme.errorContainer,
                                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                                        )
                                    ) {
                                        Icon(Icons.Rounded.Stop, null, modifier = Modifier.size(16.dp))
                                        Spacer(Modifier.width(8.dp))
                                        Text("Stop")
                                    }
                                } else {
                                    FilledTonalButton(
                                        onClick = { 
                                            outputText = ""
                                            val images = imageValues.values.flatten().toList()
                                            viewModel.generateWithOllamaStream(renderedPrompt, images) { chunk ->
                                                outputText = chunk
                                            }
                                        }, 
                                        enabled = renderedPrompt.isNotBlank()
                                    ) {
                                        Icon(Icons.Rounded.SmartToy, null, modifier = Modifier.size(16.dp))
                                        Spacer(Modifier.width(8.dp))
                                        Text("Run with Ollama")
                                    }
                                }
                            }
                            OutlinedTextField(
                                value = outputText,
                                onValueChange = { outputText = it },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 5,
                                label = { Text("AI generated output") }
                            )
                            @OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
                            androidx.compose.foundation.layout.FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                FilledTonalButton(onClick = { viewModel.recordPromptCardRun(selectedCard, values.toMap(), renderedPrompt, outputText.takeIf { it.isNotBlank() }) }) { Text("Save run") }
                                FilledTonalButton(onClick = { viewModel.savePromptOutputAsNote(selectedCard, outputText, "$editTitle note") }, enabled = outputText.isNotBlank()) { Text("To note") }
                            }
                            @OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
                            androidx.compose.foundation.layout.FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                FilledTonalButton(onClick = { viewModel.savePromptOutputAsBlueprint(selectedCard, outputText, "$editTitle article") }, enabled = outputText.isNotBlank()) { Text("To article") }
                                FilledTonalButton(onClick = { viewModel.savePromptOutputAsFlashcards(selectedCard, outputText, "$editTitle flashcards") }, enabled = outputText.isNotBlank()) { Text("To flashcards") }
                                FilledTonalButton(onClick = { viewModel.savePromptOutputAsQuiz(selectedCard, outputText, "$editTitle quiz") }, enabled = outputText.isNotBlank()) { Text("To quiz") }
                            }
                        } }
                    }
                    if (state.promptRuns.filter { it.promptCardId == selectedCard.id }.isNotEmpty()) {
                        item { Text("Run history", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
                        items(state.promptRuns.filter { it.promptCardId == selectedCard.id }.take(10), key = { "run_${it.id}" }) { run -> PromptRunItem(run) }
                    }
                }
            }
        }
    }

    showEntitySelector?.let { variableName ->
        EntitySelectorDialog(
            state = state,
            onDismiss = { showEntitySelector = null },
            onEntitySelected = { injectedData, sourceEntity ->
                values[variableName] = injectedData
                if (sourceEntity != null) {
                    viewModel.getImagesForSource(sourceEntity.id) { imgs ->
                        if (imgs.isNotEmpty()) {
                            imageValues[variableName] = imgs
                        }
                    }
                } else {
                    imageValues.remove(variableName)
                }
                showEntitySelector = null
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ToolTopBar(title: String, onBack: () -> Unit, actions: @Composable RowScope.() -> Unit = {}) {
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
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
            }
        },
        actions = actions,
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
fun BookToolListItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    onEdit: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null
) {
    val tokens = LocalMksDesignTokens.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(tokens.cardRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation)
    ) {
        Row(Modifier
            .fillMaxWidth()
            .padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
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
    Column(modifier
        .fillMaxSize()
        .padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(Icons.Rounded.Folder, null)
        Spacer(Modifier.height(12.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))
        Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
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
fun ArticleCreateDialog(
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
fun SourceDocumentDialog(
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

@Suppress("unused")
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

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class, androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun EntitySelectorDialog(
    state: BookToolsUiState,
    onDismiss: () -> Unit,
    onEntitySelected: (String, com.ahmedyejam.mks.data.local.entity.SourceDocumentEntity?) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Notes", "Sources", "Questions")
    
    var selectedQuizId by remember { mutableStateOf<Long?>(null) }
    val componentOptions = listOf("Stem", "Options", "Explanation", "Hint", "Reference", "Categories")
    val selectedComponents = remember { mutableStateMapOf<String, Boolean>().apply {
        componentOptions.forEach { put(it, it == "Stem" || it == "Options") }
    } }
    var quizExpanded by remember { mutableStateOf(false) }

    val selectedItems = remember { mutableStateMapOf<String, Pair<String, com.ahmedyejam.mks.data.local.entity.SourceDocumentEntity?>>() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Data to Inject") },
        text = {
            Column(Modifier.fillMaxHeight(0.8f)) {
                androidx.compose.material3.TabRow(selectedTabIndex = selectedTab) {
                    tabs.forEachIndexed { index, title ->
                        androidx.compose.material3.Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) }
                        )
                    }
                }
                LazyColumn(
                    modifier = Modifier.weight(1f).padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    when (selectedTab) {
                        0 -> {
                            if (state.allNotes.isEmpty()) {
                                item { Text("No notes available.") }
                            } else {
                                items(state.allNotes) { note ->
                                    val previewText = "Title: ${note.title}\n\n${note.body}"
                                    val itemId = "Note_${note.id}"
                                    val isSelected = selectedItems.containsKey(itemId)
                                    Card(
                                        modifier = Modifier.fillMaxWidth().clickable {
                                            if (isSelected) selectedItems.remove(itemId) else selectedItems[itemId] = previewText to null
                                        },
                                        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)
                                    ) {
                                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                            androidx.compose.material3.Checkbox(checked = isSelected, onCheckedChange = { if (it) selectedItems[itemId] = previewText to null else selectedItems.remove(itemId) })
                                            Column {
                                                Text(note.title, fontWeight = FontWeight.Bold)
                                                Text(note.body.take(100), style = MaterialTheme.typography.bodySmall, maxLines = 2)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        1 -> {
                            if (state.allSources.isEmpty()) {
                                item { Text("No sources available.") }
                            } else {
                                items(state.allSources) { source ->
                                    val previewText = "Title: ${source.title}\nType: ${source.sourceType}\nDetails:\n${source.description.orEmpty()}"
                                    val itemId = "Source_${source.id}"
                                    val isSelected = selectedItems.containsKey(itemId)
                                    Card(
                                        modifier = Modifier.fillMaxWidth().clickable {
                                            if (isSelected) selectedItems.remove(itemId) else selectedItems[itemId] = previewText to source
                                        },
                                        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)
                                    ) {
                                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                            androidx.compose.material3.Checkbox(checked = isSelected, onCheckedChange = { if (it) selectedItems[itemId] = previewText to source else selectedItems.remove(itemId) })
                                            Column {
                                                Text(source.title, fontWeight = FontWeight.Bold)
                                                Text(source.description.orEmpty().take(100), style = MaterialTheme.typography.bodySmall, maxLines = 2)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        2 -> {
                            item {
                                androidx.compose.material3.ExposedDropdownMenuBox(
                                    expanded = quizExpanded,
                                    onExpandedChange = { quizExpanded = it }
                                ) {
                                    OutlinedTextField(
                                        value = state.quizzes.find { it.id == selectedQuizId }?.title ?: "All Quizzes",
                                        onValueChange = {},
                                        readOnly = true,
                                        trailingIcon = { androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon(expanded = quizExpanded) },
                                        colors = androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                                        label = { Text("Select Quiz") }
                                    )
                                    ExposedDropdownMenu(
                                        expanded = quizExpanded,
                                        onDismissRequest = { quizExpanded = false }
                                    ) {
                                        androidx.compose.material3.DropdownMenuItem(
                                            text = { Text("All Quizzes") },
                                            onClick = { selectedQuizId = null; quizExpanded = false }
                                        )
                                        state.quizzes.forEach { quiz ->
                                            androidx.compose.material3.DropdownMenuItem(
                                                text = { Text(quiz.title) },
                                                onClick = { selectedQuizId = quiz.id; quizExpanded = false }
                                            )
                                        }
                                    }
                                }
                            }
                            item {
                                androidx.compose.foundation.layout.FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    componentOptions.forEach { comp ->
                                        androidx.compose.material3.FilterChip(
                                            selected = selectedComponents[comp] == true,
                                            onClick = { selectedComponents[comp] = !(selectedComponents[comp] ?: false) },
                                            label = { Text(comp) }
                                        )
                                    }
                                }
                            }
                            
                            val filteredQuestions = if (selectedQuizId == null) state.questions else state.questions.filter { it.quizId == selectedQuizId }

                            if (filteredQuestions.isEmpty()) {
                                item { Text("No questions available.") }
                            } else {
                                items(filteredQuestions) { question ->
                                    val builder = StringBuilder()
                                    if (selectedComponents["Stem"] == true) builder.append("Question: ${question.text}\n")
                                    if (selectedComponents["Options"] == true && question.options.isNotEmpty()) {
                                        builder.append("Options:\n")
                                        question.options.forEach { builder.append("- $it\n") }
                                    }
                                    if (selectedComponents["Explanation"] == true && !question.explanation.isNullOrBlank()) {
                                        builder.append("Explanation: ${question.explanation}\n")
                                    }
                                    if (selectedComponents["Hint"] == true && !question.hint.isNullOrBlank()) {
                                        builder.append("Hint: ${question.hint}\n")
                                    }
                                    if (selectedComponents["Reference"] == true && !question.reference.isNullOrBlank()) {
                                        builder.append("Reference: ${question.reference}\n")
                                    }
                                    if (selectedComponents["Categories"] == true && question.categories.isNotEmpty()) {
                                        builder.append("Categories: ${question.categories.joinToString(", ")}\n")
                                    }
                                    val previewText = builder.toString().trim()
                                    
                                    if (previewText.isNotBlank()) {
                                        val itemId = "Question_${question.id}"
                                        val isSelected = selectedItems.containsKey(itemId)
                                        Card(
                                            modifier = Modifier.fillMaxWidth().clickable {
                                                if (isSelected) selectedItems.remove(itemId) else selectedItems[itemId] = previewText to null
                                            },
                                            colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)
                                        ) {
                                            Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                                androidx.compose.material3.Checkbox(checked = isSelected, onCheckedChange = { if (it) selectedItems[itemId] = previewText to null else selectedItems.remove(itemId) })
                                                Column {
                                                    Text(previewText.take(150), maxLines = 4, style = MaterialTheme.typography.bodySmall)
                                                }
                                            }
                                        }
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
                    val joined = selectedItems.values.joinToString("\n\n---\n\n") { it.first }
                    val source = selectedItems.values.mapNotNull { it.second }.firstOrNull()
                    onEntitySelected(joined, source) 
                },
                enabled = selectedItems.isNotEmpty()
            ) {
                Text("Inject Selected (${selectedItems.size})")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
