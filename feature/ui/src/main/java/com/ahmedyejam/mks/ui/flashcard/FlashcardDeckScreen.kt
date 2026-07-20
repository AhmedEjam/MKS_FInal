package com.ahmedyejam.mks.ui.flashcard

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DriveFileMove
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmedyejam.mks.data.local.entity.FlashcardDeckEntity
import com.ahmedyejam.mks.data.local.entity.FlashcardEntity
import com.ahmedyejam.mks.data.model.FlashcardGenerationConfig
import com.ahmedyejam.mks.core.ui.R
import com.ahmedyejam.mks.ui.components.FlipSurface
import com.ahmedyejam.mks.ui.components.StudyControlBar
import com.ahmedyejam.mks.ui.components.StudyEmptyState
import com.ahmedyejam.mks.ui.components.StudyFaceLabel
import com.ahmedyejam.mks.ui.components.StudyPlayerScaffold
import com.ahmedyejam.mks.ui.components.StudyPrimarySlot
import com.ahmedyejam.mks.ui.components.StudyRatingBar
import com.ahmedyejam.mks.ui.components.StudyStepDots
import com.ahmedyejam.mks.ui.quiz.CompilerDialog
import com.ahmedyejam.mks.ui.quiz.CompilerViewModel
import com.ahmedyejam.mks.ui.theme.LocalMksDesignTokens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardDeckScreen(
    deckId: Long,
    focusedCardId: Long? = null,
    onBack: () -> Unit
) {
    val tokens = LocalMksDesignTokens.current
    val vm: FlashcardDeckViewModel = hiltViewModel()

    val compilerViewModel: CompilerViewModel = hiltViewModel()

    val state by vm.uiState.collectAsStateWithLifecycle()
    val elapsed by vm.elapsedSeconds.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    BackHandler(enabled = state.isStudyMode) {
        vm.setStudyMode(false)
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, state.isStudyMode) {
        if (state.isStudyMode) {
            vm.startSessionTimer()
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> vm.startSessionTimer()
                    Lifecycle.Event.ON_PAUSE -> vm.pauseSessionTimer()
                    else -> {}
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
                vm.pauseSessionTimer()
            }
        } else {
            onDispose {}
        }
    }

    LaunchedEffect(focusedCardId, state.cards) {
        if (focusedCardId != null && state.cards.isNotEmpty()) {
            val index = state.cards.indexOfFirst { it.id == focusedCardId }
            if (index != -1) {
                if (tokens.isPlain) {
                    listState.scrollToItem(index + 1)
                } else {
                    listState.animateScrollToItem(index + 1)
                }
            }
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }
    var showCardEditor by remember { mutableStateOf<FlashcardEntity?>(null) }
    var showAddCard by rememberSaveable { mutableStateOf(false) }
    var showDeckEditor by rememberSaveable { mutableStateOf(false) }
    var showDeckSelectionForMove by rememberSaveable { mutableStateOf(false) }
    var showDeckSelectionForCopy by rememberSaveable { mutableStateOf(false) }
    var showPasteTextDialog by rememberSaveable { mutableStateOf(false) }
    var showCompilerDialog by rememberSaveable { mutableStateOf(false) }
    var showGeneratorDialog by rememberSaveable { mutableStateOf(false) }

    val filePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            compilerViewModel.onFileSelected(uri = uri, targetDeckId = deckId)
            showCompilerDialog = true
        }
    }

    LaunchedEffect(deckId) { vm.loadDeck(deckId) }
    LaunchedEffect(state.message, state.error) {
        val text = state.message ?: state.error
        if (text != null) {
            snackbarHostState.showSnackbar(text)
            vm.clearMessage()
        }
    }

    var showAttemptsChip by rememberSaveable { mutableStateOf(true) }
    var showCorrectChip by rememberSaveable { mutableStateOf(true) }
    var showSourceChip by rememberSaveable { mutableStateOf(true) }
    var showOptionsMenu by remember { mutableStateOf(false) }
    var showExplanation by rememberSaveable { mutableStateOf(false) }
    var showReference by rememberSaveable { mutableStateOf(false) }
    var showHint by rememberSaveable { mutableStateOf(false) }
    var showAdditionalInfo by rememberSaveable { mutableStateOf(false) }

    state.deck?.let { deck ->
        if (showDeckEditor) {
            com.ahmedyejam.mks.ui.components.EntityEditDialog(
                title = "Edit deck",
                initialName = deck.title,
                initialDescription = deck.description ?: "",
                initialImage = deck.coverImage ?: "",
                showImage = true,
                onDismiss = { showDeckEditor = false },
                onSave = { title, desc, image ->
                    vm.updateDeck(title, desc.ifBlank { null }, image.ifBlank { null })
                    showDeckEditor = false
                }
            )
        }
    }

    if (showAddCard || showCardEditor != null) {
        FlashcardEditorDialog(
            card = showCardEditor,
            onDismiss = {
                showAddCard = false
                showCardEditor = null
            },
            onSave = { front, back, hint, tags ->
                val card = showCardEditor
                if (card == null) vm.addCard(front, back, hint, tags) else vm.updateCard(card, front, back, hint, tags)
                showAddCard = false
                showCardEditor = null
            }
        )
    }

    if (showGeneratorDialog) {
        FlashcardGeneratorDialog(
            quizzes = state.quizzes,
            categories = state.categories,
            onDismiss = { showGeneratorDialog = false },
            onConfirm = { source, sourceId, config, clearMarks ->
                when (source) {
                    "ALL" -> vm.generateFromBook(clearMarks, config)
                    "MARKED" -> vm.generateFromMarked("Marked question flashcards", clearMarks, config)
                    "MISSED" -> vm.generateFromMissed(config)
                    "QUIZ" -> sourceId?.toLongOrNull()?.let { vm.generateFromQuiz(it, clearMarks, config) }
                    "CATEGORY" -> sourceId?.let { vm.generateFromCategory(it, clearMarks, config) }
                }
                showGeneratorDialog = false
            }
        )
    }

    if (showDeckSelectionForMove) {
        DeckSelectionDialog(
            title = "Move cards to deck",
            decks = state.availableDecks,
            onDismiss = { showDeckSelectionForMove = false },
            onDeckSelected = { targetDeckId ->
                vm.moveSelectedCards(targetDeckId)
                showDeckSelectionForMove = false
            }
        )
    }

    if (showDeckSelectionForCopy) {
        DeckSelectionDialog(
            title = "Copy cards to deck",
            decks = state.availableDecks,
            onDismiss = { showDeckSelectionForCopy = false },
            onDeckSelected = { targetDeckId ->
                vm.copySelectedCards(targetDeckId)
                showDeckSelectionForCopy = false
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (state.selectedCardIds.isNotEmpty()) {
                TopAppBar(
                    title = { Text(stringResource(R.string.flashcard_selected_count, state.selectedCardIds.size)) },
                    navigationIcon = { IconButton(onClick = { vm.clearSelection() }) { Icon(Icons.Default.Close, contentDescription = "Clear selection") } },
                    actions = {
                        IconButton(onClick = { vm.selectAllCards() }) {
                            Icon(Icons.Default.SelectAll, contentDescription = "Select All")
                        }
                        IconButton(onClick = { showDeckSelectionForMove = true }) {
                            Icon(
                                Icons.AutoMirrored.Filled.DriveFileMove,
                                contentDescription = "Move"
                            )
                        }
                        IconButton(onClick = { showDeckSelectionForCopy = true }) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy")
                        }
                        IconButton(onClick = { vm.deleteSelectedCards() }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                )
            } else {
                var subtitle: String? = null
                if (state.isStudyMode) {
                    val elapsed by vm.elapsedSeconds.collectAsState()
                    val minutes = elapsed / 60
                    val seconds = elapsed % 60
                    subtitle =
                        String.format(java.util.Locale.getDefault(), "%02d:%02d", minutes, seconds)
                }

                com.ahmedyejam.mks.ui.components.StudyTopAppBar(
                    title = state.deck?.title ?: "Flashcards",
                    subtitle = subtitle,
                    onNavigateBack = onBack,
                    actions = {
                        IconButton(onClick = { showOptionsMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Options")
                        }
                        DropdownMenu(expanded = showOptionsMenu, onDismissRequest = { showOptionsMenu = false }) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.player_show_attempts)) },
                                onClick = { showAttemptsChip = !showAttemptsChip },
                                trailingIcon = { if (showAttemptsChip) Icon(Icons.Default.Check, null) }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.player_show_correct)) },
                                onClick = { showCorrectChip = !showCorrectChip },
                                trailingIcon = { if (showCorrectChip) Icon(Icons.Default.Check, null) }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.player_show_source)) },
                                onClick = { showSourceChip = !showSourceChip },
                                trailingIcon = { if (showSourceChip) Icon(Icons.Default.Check, null) }
                            )
                            if (state.isStudyMode) {
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.player_show_explanation)) },
                                    onClick = { showExplanation = !showExplanation },
                                    trailingIcon = { if (showExplanation) Icon(Icons.Default.Check, null) }
                                )
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.player_show_reference)) },
                                    onClick = { showReference = !showReference },
                                    trailingIcon = { if (showReference) Icon(Icons.Default.Check, null) }
                                )
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.player_show_hint)) },
                                    onClick = { showHint = !showHint },
                                    trailingIcon = { if (showHint) Icon(Icons.Default.Check, null) }
                                )
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.player_show_additional_info)) },
                                    onClick = { showAdditionalInfo = !showAdditionalInfo },
                                    trailingIcon = { if (showAdditionalInfo) Icon(Icons.Default.Check, null) }
                                )
                            }
                        }
                        IconButton(onClick = { vm.setStudyMode(!state.isStudyMode) }) {
                            Icon(
                                if (state.isStudyMode) Icons.AutoMirrored.Filled.ViewList else Icons.Default.PlayArrow,
                                contentDescription = "Toggle study mode"
                            )
                        }
                        IconButton(onClick = { showDeckEditor = true }, enabled = state.deck != null) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit deck")
                        }
                    }
                )
            }
        }
    ) { padding ->
        Box(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                state.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                state.deck == null -> Text(
                    stringResource(R.string.flashcard_deck_not_found),
                    Modifier.align(Alignment.Center)
                )
                state.isStudyMode -> FlashcardStudyContent(
                    state = state, 
                    viewModel = vm,
                    showExplanation = showExplanation,
                    showReference = showReference,
                    showHint = showHint,
                    showAdditionalInfo = showAdditionalInfo
                )
                else -> FlashcardDeckDetailContent(
                    state = state,
                    showAttemptsChip = showAttemptsChip,
                    showCorrectChip = showCorrectChip,
                    showSourceChip = showSourceChip,
                    onAddCard = { showAddCard = true },
                    onEditCard = { showCardEditor = it },
                    onDeleteCard = { vm.deleteCard(it) },
                    onMoveCard = { card, direction -> vm.moveCard(card, direction) },
                    onGenerateCards = { showGeneratorDialog = true },
                    onStartStudy = { vm.setStudyMode(true) },
                    onToggleSelect = { vm.toggleCardSelection(it) },
                    onImportFile = { filePickerLauncher.launch("*/*") },
                    onPasteText = { showPasteTextDialog = true }
                )
            }
        }
    }

    if (showPasteTextDialog) {
        PasteTextDialog(
            onDismiss = { showPasteTextDialog = false },
            onImport = { text, mode ->
                vm.importFromText(text, mode)
                showPasteTextDialog = false
            }
        )
    }

    if (showCompilerDialog) {
        CompilerDialog(
            viewModel = compilerViewModel,
            books = emptyList(),
            quizzes = emptyList(),
            onDismiss = { showCompilerDialog = false }
        )
    }
}

@Composable
private fun FlashcardDeckDetailContent(
    state: FlashcardDeckUiState,
    showAttemptsChip: Boolean,
    showCorrectChip: Boolean,
    showSourceChip: Boolean,
    onAddCard: () -> Unit,
    onEditCard: (FlashcardEntity) -> Unit,
    onDeleteCard: (FlashcardEntity) -> Unit,
    onMoveCard: (FlashcardEntity, Int) -> Unit,
    onGenerateCards: () -> Unit,
    onStartStudy: () -> Unit,
    onToggleSelect: (Long) -> Unit,
    onImportFile: () -> Unit,
    onPasteText: () -> Unit
) {
    val deck = state.deck ?: return
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            DeckStatsCard(deck = deck, cardCount = state.cards.size)
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                Button(onClick = onAddCard, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text(stringResource(R.string.flashcard_add_card))
                }
                OutlinedButton(onClick = onStartStudy, enabled = state.cards.isNotEmpty(), modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text(stringResource(R.string.flashcard_study))
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = onImportFile, modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.flashcard_import_file))
                }
                OutlinedButton(onClick = onPasteText, modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.flashcard_paste_text))
                }
            }
        }
        item {
            val tokens = LocalMksDesignTokens.current
            Button(
                onClick = onGenerateCards,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(tokens.cardRadius)
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.flashcard_generate), fontWeight = FontWeight.Bold)
            }
        }
        if (state.cards.isEmpty()) {
            item {
                val tokens = LocalMksDesignTokens.current
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(tokens.cardRadius),
                    elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation)
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(stringResource(R.string.flashcard_empty_title), fontWeight = FontWeight.SemiBold)
                        Text(stringResource(R.string.flashcard_empty_body))
                    }
                }
            }
        } else {
            items(state.cards, key = { it.id }) { card ->
                FlashcardListItem(
                    card = card,
                    showAttemptsChip = showAttemptsChip,
                    showCorrectChip = showCorrectChip,
                    showSourceChip = showSourceChip,
                    isSelected = state.selectedCardIds.contains(card.id),
                    isSelectionMode = state.selectedCardIds.isNotEmpty(),
                    onToggleSelect = { onToggleSelect(card.id) },
                    onEdit = { onEditCard(card) },
                    onDelete = { onDeleteCard(card) },
                    onMoveUp = { onMoveCard(card, -1) },
                    onMoveDown = { onMoveCard(card, 1) }
                )
            }
        }
    }
}

@Composable
private fun DeckStatsCard(deck: FlashcardDeckEntity, cardCount: Int) {
    val tokens = LocalMksDesignTokens.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(tokens.cardRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(deck.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            deck.description?.takeIf { it.isNotBlank() }?.let { Text(it) }
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, label = { Text(stringResource(R.string.flashcard_cards_count, cardCount)) })
                AssistChip(onClick = {}, label = { Text(stringResource(R.string.flashcard_studied_count, deck.studiedCount)) })
                AssistChip(onClick = {}, label = { Text(stringResource(R.string.flashcard_mastery, (deck.masteryPercentage * 100).toInt())) })
            }
            LinearProgressIndicator(progress = { deck.masteryPercentage.coerceIn(0f, 1f) }, modifier = Modifier.fillMaxWidth())
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FlashcardListItem(
    card: FlashcardEntity,
    showAttemptsChip: Boolean = true,
    showCorrectChip: Boolean = true,
    showSourceChip: Boolean = true,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    onToggleSelect: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    val tokens = LocalMksDesignTokens.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(tokens.cardRadius))
            .combinedClickable(
                onClick = { if (isSelectionMode) onToggleSelect() },
                onLongClick = onToggleSelect
            ),
        shape = RoundedCornerShape(tokens.cardRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation)
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isSelectionMode) {
                    Checkbox(checked = isSelected, onCheckedChange = { onToggleSelect() })
                } else {
                    Icon(Icons.Default.Style, contentDescription = null)
                }
                Spacer(Modifier.width(8.dp))
                Column(Modifier.weight(1f)) {
                    Text(card.frontText, fontWeight = FontWeight.SemiBold, maxLines = 2)
                    Text(card.backText, style = MaterialTheme.typography.bodySmall, maxLines = 2)
                }
                if (!isSelectionMode) {
                    IconButton(onClick = onMoveUp) { Icon(Icons.Default.ArrowUpward, contentDescription = "Move up") }
                    IconButton(onClick = onMoveDown) { Icon(Icons.Default.ArrowDownward, contentDescription = "Move down") }
                    IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = "Edit") }
                    IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Delete") }
                }
            }
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (showAttemptsChip) AssistChip(onClick = {}, label = { Text("Attempts ${card.attempts}") })
                if (showCorrectChip) AssistChip(onClick = {}, label = { Text("Correct ${card.correctCount}") })
                if (showSourceChip) card.sourceQuestionId?.let { AssistChip(onClick = {}, label = { Text("Source #$it") }) }
            }
        }
    }
}

/**
 * Flashcard study player.
 *
 * Built on the shared player kit in `core/ui` so it matches the slideshow and article players.
 * The interaction model is deliberately single-track: swiping or the arrow controls move between
 * cards, and the centre slot carries whatever the current card state asks for — flip while face
 * down, rate while face up. The old screen had rating *and* a separate previous/next row competing
 * to advance the deck, with no indication which one was authoritative.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FlashcardStudyContent(
    state: FlashcardDeckUiState,
    viewModel: FlashcardDeckViewModel,
    showExplanation: Boolean,
    showReference: Boolean,
    showHint: Boolean,
    showAdditionalInfo: Boolean
) {
    val tokens = LocalMksDesignTokens.current
    if (state.cards.isEmpty() || state.currentCard == null) {
        StudyEmptyState(title = stringResource(R.string.flashcard_no_cards))
        return
    }

    val pagerState = rememberPagerState(
        initialPage = state.currentIndex,
        pageCount = { state.cards.size }
    )

    // Two-way sync between pager and ViewModel. setCurrentIndex no-ops when already current, so
    // these effects settle instead of ping-ponging.
    LaunchedEffect(pagerState.currentPage) { viewModel.setCurrentIndex(pagerState.currentPage) }
    LaunchedEffect(state.currentIndex) {
        if (pagerState.currentPage != state.currentIndex) {
            if (tokens.isPlain) {
                pagerState.scrollToPage(state.currentIndex)
            } else {
                pagerState.animateScrollToPage(state.currentIndex)
            }
        }
    }

    StudyPlayerScaffold(
        position = state.currentIndex,
        total = state.cards.size,
        progressLabel = stringResource(
            R.string.player_progress_format,
            state.currentIndex + 1,
            state.cards.size
        ),
        controls = {
            Column {
                StudyPrimarySlot(stateKey = state.isFlipped) { flipped ->
                    if (flipped) {
                        StudyRatingBar(
                            againLabel = stringResource(R.string.player_rating_again),
                            goodLabel = stringResource(R.string.player_rating_good),
                            easyLabel = stringResource(R.string.player_rating_easy),
                            onAgain = { viewModel.rateCurrentCard(FLASHCARD_RATING_AGAIN) },
                            onGood = { viewModel.rateCurrentCard(FLASHCARD_RATING_GOOD) },
                            onEasy = { viewModel.rateCurrentCard(FLASHCARD_RATING_EASY) }
                        )
                    } else {
                        Button(
                            onClick = { viewModel.flipCard() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                stringResource(R.string.player_flip),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
                StudyStepDots(
                    total = state.cards.size,
                    currentIndex = state.currentIndex,
                    onSelect = { viewModel.setCurrentIndex(it) }
                )
                StudyControlBar(
                    onPrevious = { viewModel.previousCard() },
                    onNext = { viewModel.nextCard() },
                    previousLabel = stringResource(R.string.player_previous),
                    nextLabel = stringResource(R.string.player_next),
                    previousEnabled = state.currentIndex > 0,
                    nextEnabled = state.currentIndex < state.cards.lastIndex
                )
            }
        }
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            beyondViewportPageCount = 1,
            pageSpacing = tokens.compactGap
        ) { page ->
            val pageCard = state.cards[page]
            // Only the card actually in view participates in the flip; neighbours stay face down
            // so a swipe never reveals a pre-flipped answer.
            val isPageFlipped = state.isFlipped && page == state.currentIndex
            FlipSurface(
                isFlipped = isPageFlipped,
                onFlip = { if (page == state.currentIndex) viewModel.flipCard() },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = tokens.pagePadding, vertical = tokens.compactGap),
                flipDescription = stringResource(R.string.player_flip_hint),
                front = {
                    FlashcardFace(
                        faceLabel = stringResource(R.string.player_face_front),
                        text = pageCard.frontText,
                        hint = pageCard.hint
                    )
                },
                back = {
                    FlashcardFace(
                        faceLabel = stringResource(R.string.player_face_back),
                        text = filterBackSections(
                            backText = pageCard.backText,
                            showExplanation = showExplanation,
                            showReference = showReference,
                            showHint = showHint,
                            showAdditionalInfo = showAdditionalInfo
                        ),
                        hint = null
                    )
                }
            )
        }
    }
}

/**
 * One face of a card.
 *
 * The primary text is sized by length rather than fixed at `headlineSmall`. A back face carrying
 * an explanation plus a reference plus additional info previously rendered every word at headline
 * size, which read as an undifferentiated wall of text.
 */
@Composable
private fun FlashcardFace(
    faceLabel: String,
    text: String,
    hint: String?
) {
    val tokens = LocalMksDesignTokens.current
    val textStyle = when {
        text.length > 320 -> MaterialTheme.typography.bodyLarge
        text.length > 120 -> MaterialTheme.typography.titleMedium
        else -> MaterialTheme.typography.headlineSmall
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(tokens.pagePadding)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(tokens.compactGap)
    ) {
        StudyFaceLabel(faceLabel)
        Spacer(Modifier.weight(1f))
        Text(
            text = text,
            style = textStyle,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        if (!hint.isNullOrBlank()) {
            Text(
                text = stringResource(R.string.flashcard_hint_label) + ": " + hint,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(Modifier.weight(1f))
    }
}

/**
 * Applies the top-bar visibility toggles to a generated back face.
 *
 * Generated cards join their sections with a blank line and prefix each with a bare label line.
 * This mirrors the format written by `KnowledgeRepository`'s flashcard generator — a hand-authored
 * card has no such sections and passes through untouched.
 */
private fun filterBackSections(
    backText: String,
    showExplanation: Boolean,
    showReference: Boolean,
    showHint: Boolean,
    showAdditionalInfo: Boolean
): String = backText
    .split("\n\n")
    .filter { section ->
        when {
            section.startsWith("Explanation\n") -> showExplanation
            section.startsWith("Reference\n") -> showReference
            section.startsWith("Hint\n") -> showHint
            section.startsWith("Additional Info\n") -> showAdditionalInfo
            else -> true
        }
    }
    .joinToString("\n\n")

@Composable
private fun FlashcardEditorDialog(
    card: FlashcardEntity?,
    onDismiss: () -> Unit,
    onSave: (front: String, back: String, hint: String?, tags: List<String>) -> Unit
) {
    var front by rememberSaveable(card?.id) { mutableStateOf(card?.frontText ?: "") }
    var back by rememberSaveable(card?.id) { mutableStateOf(card?.backText ?: "") }
    var hint by rememberSaveable(card?.id) { mutableStateOf(card?.hint ?: "") }
    var tags by rememberSaveable(card?.id) { mutableStateOf(card?.tags?.joinToString(", ") ?: "") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (card == null) "Add flashcard" else "Edit flashcard") },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(front, { front = it }, label = { Text("Front") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
                OutlinedTextField(back, { back = it }, label = { Text("Back") }, modifier = Modifier.fillMaxWidth(), minLines = 4)
                OutlinedTextField(hint, { hint = it }, label = { Text("Hint") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(tags, { tags = it }, label = { Text("Tags, comma separated") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(enabled = front.isNotBlank() && back.isNotBlank(), onClick = {
                onSave(
                    front.trim(),
                    back.trim(),
                    hint.trim().ifBlank { null },
                    tags.split(',').map { it.trim() }.filter { it.isNotBlank() }.distinct()
                )
            }) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FlashcardGeneratorDialog(
    quizzes: List<com.ahmedyejam.mks.data.local.entity.QuizEntity>,
    categories: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (source: String, sourceId: String?, config: FlashcardGenerationConfig, clearMarks: Boolean) -> Unit
) {
    var source by rememberSaveable { mutableStateOf("ALL") } // ALL, MARKED, MISSED, QUIZ, CATEGORY
    var sourceId by rememberSaveable { mutableStateOf<String?>(null) }
    
    var clearMarks by rememberSaveable { mutableStateOf(false) }
    
    var includeStemInFront by rememberSaveable { mutableStateOf(true) }
    var includeOptionsInFront by rememberSaveable { mutableStateOf(false) }
    var includeImageInFront by rememberSaveable { mutableStateOf(false) }
    
    var includeAnswerInBack by rememberSaveable { mutableStateOf(true) }
    var includeExplanationInBack by rememberSaveable { mutableStateOf(true) }
    var includeHintInBack by rememberSaveable { mutableStateOf(false) }
    var includeReferenceInBack by rememberSaveable { mutableStateOf(false) }
    var includeAdditionalInfoInBack by rememberSaveable { mutableStateOf(false) }
    var includeImageInBack by rememberSaveable { mutableStateOf(false) }

    var expandedSource by remember { mutableStateOf(false) }
    var expandedSubSource by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Generate from questions") },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                
                ExposedDropdownMenuBox(
                    expanded = expandedSource,
                    onExpandedChange = { expandedSource = it }
                ) {
                    OutlinedTextField(
                        value = when (source) {
                            "ALL" -> "All Book Questions"
                            "MARKED" -> "Marked Questions"
                            "MISSED" -> "Missed Questions"
                            "QUIZ" -> "Specific Quiz"
                            "CATEGORY" -> "Category"
                            else -> ""
                        },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Source") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSource) },
                        modifier = Modifier
                            .menuAnchor(androidx.compose.material3.MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedSource,
                        onDismissRequest = { expandedSource = false }
                    ) {
                        DropdownMenuItem(text = { Text("All Book Questions") }, onClick = { source = "ALL"; sourceId = null; expandedSource = false })
                        DropdownMenuItem(text = { Text("Marked Questions") }, onClick = { source = "MARKED"; sourceId = null; expandedSource = false })
                        DropdownMenuItem(text = { Text("Missed Questions") }, onClick = { source = "MISSED"; sourceId = null; expandedSource = false })
                        if (quizzes.isNotEmpty()) {
                            DropdownMenuItem(text = { Text("Specific Quiz") }, onClick = { source = "QUIZ"; sourceId = quizzes.firstOrNull()?.id?.toString(); expandedSource = false })
                        }
                        if (categories.isNotEmpty()) {
                            DropdownMenuItem(text = { Text("Category") }, onClick = { source = "CATEGORY"; sourceId = categories.firstOrNull(); expandedSource = false })
                        }
                    }
                }

                if (source == "QUIZ") {
                    ExposedDropdownMenuBox(
                        expanded = expandedSubSource,
                        onExpandedChange = { expandedSubSource = it }
                    ) {
                        OutlinedTextField(
                            value = quizzes.find { it.id.toString() == sourceId }?.title ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select Quiz") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSubSource) },
                            modifier = Modifier
                                .menuAnchor(androidx.compose.material3.MenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedSubSource,
                            onDismissRequest = { expandedSubSource = false }
                        ) {
                            quizzes.forEach { quiz ->
                                DropdownMenuItem(text = { Text(quiz.title) }, onClick = { sourceId = quiz.id.toString(); expandedSubSource = false })
                            }
                        }
                    }
                } else if (source == "CATEGORY") {
                    ExposedDropdownMenuBox(
                        expanded = expandedSubSource,
                        onExpandedChange = { expandedSubSource = it }
                    ) {
                        OutlinedTextField(
                            value = sourceId ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select Category") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSubSource) },
                            modifier = Modifier
                                .menuAnchor(androidx.compose.material3.MenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedSubSource,
                            onDismissRequest = { expandedSubSource = false }
                        ) {
                            categories.forEach { cat ->
                                DropdownMenuItem(text = { Text(cat) }, onClick = { sourceId = cat; expandedSubSource = false })
                            }
                        }
                    }
                }

                if (source != "MISSED") {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = clearMarks, onCheckedChange = { clearMarks = it })
                        Text("Clear marks after conversion")
                    }
                    Spacer(Modifier.height(8.dp))
                }
                
                Text("Front of card", fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) { Checkbox(includeStemInFront, { includeStemInFront = it }); Text("Question Stem") }
                Row(verticalAlignment = Alignment.CenterVertically) { Checkbox(includeOptionsInFront, { includeOptionsInFront = it }); Text("Options") }
                Row(verticalAlignment = Alignment.CenterVertically) { Checkbox(includeImageInFront, { includeImageInFront = it }); Text("Image") }
                
                Spacer(Modifier.height(8.dp))
                Text("Back of card", fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) { Checkbox(includeAnswerInBack, { includeAnswerInBack = it }); Text("Answer") }
                Row(verticalAlignment = Alignment.CenterVertically) { Checkbox(includeExplanationInBack, { includeExplanationInBack = it }); Text("Explanation") }
                Row(verticalAlignment = Alignment.CenterVertically) { Checkbox(includeHintInBack, { includeHintInBack = it }); Text("Hint") }
                Row(verticalAlignment = Alignment.CenterVertically) { Checkbox(includeReferenceInBack, { includeReferenceInBack = it }); Text("Reference") }
                Row(verticalAlignment = Alignment.CenterVertically) { Checkbox(includeAdditionalInfoInBack, { includeAdditionalInfoInBack = it }); Text("Additional Info") }
                Row(verticalAlignment = Alignment.CenterVertically) { Checkbox(includeImageInBack, { includeImageInBack = it }); Text("Image") }
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(
                    source,
                    sourceId,
                    FlashcardGenerationConfig(
                        includeStemInFront, includeOptionsInFront, includeImageInFront,
                        includeAnswerInBack, includeExplanationInBack, includeHintInBack, includeReferenceInBack, includeAdditionalInfoInBack, includeImageInBack
                    ),
                    clearMarks
                )
            }) { Text("Generate") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun DeckSelectionDialog(
    title: String,
    decks: List<FlashcardDeckEntity>,
    onDismiss: () -> Unit,
    onDeckSelected: (Long) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            if (decks.isEmpty()) {
                Text("No other decks available in this book.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(decks, key = { it.id }) { deck ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onDeckSelected(deck.id) },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(deck.title, fontWeight = FontWeight.SemiBold)
                                if (!deck.description.isNullOrBlank()) {
                                    Text(deck.description!!, style = MaterialTheme.typography.bodySmall, maxLines = 1)
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun PasteTextDialog(
    onDismiss: () -> Unit,
    onImport: (String, com.ahmedyejam.mks.data.importer.parser.TextParseMode) -> Unit
) {
    var text by rememberSaveable { mutableStateOf("") }
    var mode by rememberSaveable { mutableStateOf(com.ahmedyejam.mks.data.importer.parser.TextParseMode.ALTERNATING_PARAGRAPHS) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Paste text to import") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            mode =
                                com.ahmedyejam.mks.data.importer.parser.TextParseMode.ALTERNATING_PARAGRAPHS
                        }) {
                    androidx.compose.material3.RadioButton(
                        selected = mode == com.ahmedyejam.mks.data.importer.parser.TextParseMode.ALTERNATING_PARAGRAPHS,
                        onClick = { mode = com.ahmedyejam.mks.data.importer.parser.TextParseMode.ALTERNATING_PARAGRAPHS }
                    )
                    Text("Alternating Paragraphs (odd front, even back)", style = MaterialTheme.typography.bodySmall)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            mode =
                                com.ahmedyejam.mks.data.importer.parser.TextParseMode.EXPLICIT_LABELS
                        }) {
                    androidx.compose.material3.RadioButton(
                        selected = mode == com.ahmedyejam.mks.data.importer.parser.TextParseMode.EXPLICIT_LABELS,
                        onClick = { mode = com.ahmedyejam.mks.data.importer.parser.TextParseMode.EXPLICIT_LABELS }
                    )
                    Text("Explicit Labels (Front: ..., Back: ...)", style = MaterialTheme.typography.bodySmall)
                }
                
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Flashcards text") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    maxLines = 10
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onImport(text, mode) },
                enabled = text.isNotBlank()
            ) {
                Text("Import")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
