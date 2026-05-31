package com.ahmedyejam.mks.ui.flashcard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ahmedyejam.mks.data.local.entity.FlashcardDeckEntity
import com.ahmedyejam.mks.data.local.entity.FlashcardEntity
import com.ahmedyejam.mks.di.AppModule

import com.ahmedyejam.mks.ui.theme.LocalMksDesignTokens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardDeckScreen(
    deckId: Long,
    focusedCardId: Long? = null,
    appModule: AppModule,
    onBack: () -> Unit
) {
    val tokens = LocalMksDesignTokens.current
    val vm: FlashcardDeckViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = FlashcardDeckViewModel(appModule) as T
        }
    )
    val state by vm.uiState.collectAsState()
    val listState = rememberLazyListState()

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
    var showMarkedGenerator by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(deckId) { vm.loadDeck(deckId) }
    LaunchedEffect(state.message, state.error) {
        val text = state.message ?: state.error
        if (text != null) {
            snackbarHostState.showSnackbar(text)
            vm.clearMessage()
        }
    }

    state.deck?.let { deck ->
        if (showDeckEditor) {
            DeckEditorDialog(
                deck = deck,
                onDismiss = { showDeckEditor = false },
                onSave = { title, description ->
                    vm.updateDeck(title, description)
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

    if (showMarkedGenerator) {
        MarkedFlashcardGeneratorDialog(
            onDismiss = { showMarkedGenerator = false },
            onConfirm = { clearMarks ->
                vm.generateFromMarked("Marked question flashcards", clearMarks)
                showMarkedGenerator = false
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(state.deck?.title ?: "Flashcards") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") } },
                actions = {
                    IconButton(onClick = { vm.setStudyMode(!state.isStudyMode) }) {
                        Icon(if (state.isStudyMode) Icons.Default.ViewList else Icons.Default.PlayArrow, contentDescription = "Toggle study mode")
                    }
                    IconButton(onClick = { showDeckEditor = true }, enabled = state.deck != null) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit deck")
                    }
                }
            )
        }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when {
                state.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                state.deck == null -> Text("Deck not found", Modifier.align(Alignment.Center))
                state.isStudyMode -> FlashcardStudyContent(state = state, viewModel = vm)
                else -> FlashcardDeckDetailContent(
                    state = state,
                    onAddCard = { showAddCard = true },
                    onEditCard = { showCardEditor = it },
                    onDeleteCard = { vm.deleteCard(it) },
                    onMoveCard = { card, direction -> vm.moveCard(card, direction) },
                    onGenerateMarked = { showMarkedGenerator = true },
                    onGenerateMissed = { vm.generateFromMissed() },
                    onStartStudy = { vm.setStudyMode(true) }
                )
            }
        }
    }
}

@Composable
private fun FlashcardDeckDetailContent(
    state: FlashcardDeckUiState,
    onAddCard: () -> Unit,
    onEditCard: (FlashcardEntity) -> Unit,
    onDeleteCard: (FlashcardEntity) -> Unit,
    onMoveCard: (FlashcardEntity, Int) -> Unit,
    onGenerateMarked: () -> Unit,
    onGenerateMissed: () -> Unit,
    onStartStudy: () -> Unit
) {
    val deck = state.deck ?: return
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
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
                    Text("Add card")
                }
                OutlinedButton(onClick = onStartStudy, enabled = state.cards.isNotEmpty(), modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Study")
                }
            }
        }
        item {
            val tokens = LocalMksDesignTokens.current
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(tokens.cardRadius),
                elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation)
            ) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Generate from questions", fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = onGenerateMarked, modifier = Modifier.weight(1f)) { Text("Marked") }
                        OutlinedButton(onClick = onGenerateMissed, modifier = Modifier.weight(1f)) { Text("Missed") }
                    }
                    Text(
                        "Generated cards are detached copies with source question links. They will not silently live-sync.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
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
                        Text("No flashcards yet", fontWeight = FontWeight.SemiBold)
                        Text("Create cards manually or generate them from marked/missed questions.")
                    }
                }
            }
        } else {
            items(state.cards, key = { it.id }) { card ->
                FlashcardListItem(
                    card = card,
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
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, label = { Text("$cardCount cards") })
                AssistChip(onClick = {}, label = { Text("${deck.studiedCount} studied") })
                AssistChip(onClick = {}, label = { Text("${(deck.masteryPercentage * 100).toInt()}% mastery") })
            }
            LinearProgressIndicator(progress = { deck.masteryPercentage.coerceIn(0f, 1f) }, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun FlashcardListItem(
    card: FlashcardEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    val tokens = LocalMksDesignTokens.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(tokens.cardRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation)
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Style, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Column(Modifier.weight(1f)) {
                    Text(card.frontText, fontWeight = FontWeight.SemiBold, maxLines = 2)
                    Text(card.backText, style = MaterialTheme.typography.bodySmall, maxLines = 2)
                }
                IconButton(onClick = onMoveUp) { Icon(Icons.Default.ArrowUpward, contentDescription = "Move up") }
                IconButton(onClick = onMoveDown) { Icon(Icons.Default.ArrowDownward, contentDescription = "Move down") }
                IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = "Edit") }
                IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Delete") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, label = { Text("Attempts ${card.attempts}") })
                AssistChip(onClick = {}, label = { Text("Correct ${card.correctCount}") })
                card.sourceQuestionId?.let { AssistChip(onClick = {}, label = { Text("Source #$it") }) }
            }
        }
    }
}

@Composable
private fun FlashcardStudyContent(state: FlashcardDeckUiState, viewModel: FlashcardDeckViewModel) {
    val tokens = LocalMksDesignTokens.current
    val card = state.currentCard
    if (card == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No cards to study")
        }
        return
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("${state.currentIndex + 1} / ${state.cards.size}", style = MaterialTheme.typography.labelLarge)
        Surface(
            modifier = Modifier.weight(1f).fillMaxWidth().clickable { viewModel.flipCard() },
            shape = RoundedCornerShape(tokens.cardRadius),
            tonalElevation = 2.dp,
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = if (state.isFlipped) card.backText else card.frontText,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
        if (!state.isFlipped) {
            Button(onClick = { viewModel.flipCard() }, modifier = Modifier.fillMaxWidth()) { Text("Flip") }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { viewModel.rateCurrentCard(FLASHCARD_RATING_AGAIN) }, modifier = Modifier.weight(1f)) { Text("Again") }
                Button(onClick = { viewModel.rateCurrentCard(FLASHCARD_RATING_GOOD) }, modifier = Modifier.weight(1f)) { Text("Good") }
                Button(onClick = { viewModel.rateCurrentCard(FLASHCARD_RATING_EASY) }, modifier = Modifier.weight(1f)) { Text("Easy") }
            }
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            TextButton(onClick = { viewModel.previousCard() }) { Text("Previous") }
            TextButton(onClick = { viewModel.nextCard() }) { Text("Next") }
        }
    }
}

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

@Composable
private fun DeckEditorDialog(deck: FlashcardDeckEntity, onDismiss: () -> Unit, onSave: (String, String?) -> Unit) {
    var title by rememberSaveable(deck.id) { mutableStateOf(deck.title) }
    var description by rememberSaveable(deck.id) { mutableStateOf(deck.description ?: "") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit deck") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(title, { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(description, { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
            }
        },
        confirmButton = { Button(enabled = title.isNotBlank(), onClick = { onSave(title.trim(), description.trim().ifBlank { null }) }) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun MarkedFlashcardGeneratorDialog(onDismiss: () -> Unit, onConfirm: (clearMarks: Boolean) -> Unit) {
    var clearMarks by rememberSaveable { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Generate from marked questions") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("This adds detached flashcard copies from all marked questions in this deck's book.")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = clearMarks, onCheckedChange = { clearMarks = it })
                    Text("Clear marks after conversion")
                }
            }
        },
        confirmButton = { Button(onClick = { onConfirm(clearMarks) }) { Text("Generate") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
