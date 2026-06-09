package com.ahmedyejam.mks.ui.flashcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedyejam.mks.data.import.parser.TextFlashcardParser
import com.ahmedyejam.mks.data.local.entity.FlashcardDeckEntity
import com.ahmedyejam.mks.data.local.entity.FlashcardEntity
import com.ahmedyejam.mks.data.model.FlashcardGenerationConfig
import com.ahmedyejam.mks.data.repository.MksRepository
import com.ahmedyejam.mks.di.AppModule
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

const val FLASHCARD_RATING_AGAIN = "again"
const val FLASHCARD_RATING_GOOD = "good"
const val FLASHCARD_RATING_EASY = "easy"

data class FlashcardDeckUiState(
    val deck: FlashcardDeckEntity? = null,
    val cards: List<FlashcardEntity> = emptyList(),
    val availableDecks: List<FlashcardDeckEntity> = emptyList(),
    val quizzes: List<com.ahmedyejam.mks.data.local.entity.QuizEntity> = emptyList(),
    val categories: List<String> = emptyList(),
    val currentIndex: Int = 0,
    val isFlipped: Boolean = false,
    val isLoading: Boolean = true,
    val isStudyMode: Boolean = false,
    val selectedCardIds: Set<Long> = emptySet(),
    val message: String? = null,
    val error: String? = null
) {
    val currentCard: FlashcardEntity?
        get() = cards.getOrNull(currentIndex.coerceIn(0, (cards.size - 1).coerceAtLeast(0)))
}

class FlashcardDeckViewModel(appModule: AppModule) : ViewModel() {
    private val repository: MksRepository = appModule.repository
    private val _uiState = MutableStateFlow(FlashcardDeckUiState())
    val uiState = _uiState.asStateFlow()
    private var deckId: Long? = null
    private var loadJob: Job? = null

    fun loadDeck(id: Long) {
        if (deckId == id && loadJob?.isActive == true) return
        deckId = id
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            kotlinx.coroutines.flow.combine(
                repository.observeFlashcardDeckById(id),
                repository.getFlashcardsByDeckId(id)
            ) { deck, cards -> Pair(deck, cards) }
            .flatMapLatest { pair ->
                val deck = pair.first
                val cards = pair.second
                val decksFlow = deck?.bookId?.let { repository.getFlashcardDecksByBookId(it) } ?: kotlinx.coroutines.flow.flowOf(emptyList<com.ahmedyejam.mks.data.local.entity.FlashcardDeckEntity>())
                val quizzesFlow = deck?.bookId?.let { repository.getQuizzesByBookId(it, com.ahmedyejam.mks.data.repository.SortOption.TITLE) } ?: kotlinx.coroutines.flow.flowOf(emptyList<com.ahmedyejam.mks.data.local.entity.QuizEntity>())
                val categoriesFlow = repository.getAllCategoriesWithMetadata()

                kotlinx.coroutines.flow.combine(decksFlow, quizzesFlow, categoriesFlow) { allDecks, quizzes, cats ->
                    Triple(deck, cards, Triple(allDecks, quizzes, cats))
                }
            }
            .collect { result ->
                val deck = result.first
                val cards = result.second
                val extras = result.third
                val allDecks = extras.first
                val quizzes = extras.second
                val cats = extras.third
                val current = _uiState.value
                _uiState.value = current.copy(
                    deck = deck,
                    cards = cards,
                    availableDecks = allDecks.filter { it.id != id },
                    quizzes = quizzes,
                    categories = cats.map { it.name },
                    currentIndex = current.currentIndex.coerceIn(0, (cards.size - 1).coerceAtLeast(0)),
                    isLoading = false
                )
            }
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null, error = null)
    }

    fun setStudyMode(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isStudyMode = enabled, isFlipped = false, currentIndex = 0)
    }

    fun flipCard() {
        _uiState.value = _uiState.value.copy(isFlipped = !_uiState.value.isFlipped)
    }

    fun nextCard() {
        val current = _uiState.value
        val next = if (current.cards.isEmpty()) 0 else (current.currentIndex + 1).coerceAtMost(current.cards.lastIndex)
        _uiState.value = current.copy(currentIndex = next, isFlipped = false)
    }

    fun previousCard() {
        val current = _uiState.value
        val previous = (current.currentIndex - 1).coerceAtLeast(0)
        _uiState.value = current.copy(currentIndex = previous, isFlipped = false)
    }

    fun updateDeck(title: String, description: String?, coverImage: String?) {
        val deck = _uiState.value.deck ?: return
        viewModelScope.launch {
            repository.updateFlashcardDeck(deck.copy(title = title, description = description, coverImage = coverImage))
            _uiState.value = _uiState.value.copy(message = "Deck updated")
        }
    }

    fun addCard(front: String, back: String, hint: String?, tags: List<String>) {
        val deck = _uiState.value.deck ?: return
        viewModelScope.launch {
            val order = _uiState.value.cards.size
            repository.insertFlashcard(
                FlashcardEntity(
                    externalId = java.util.UUID.randomUUID().toString(),
                    deckId = deck.id,
                    frontText = front,
                    backText = back,
                    hint = hint,
                    tags = tags,
                    orderIndex = order
                )
            )
            _uiState.value = _uiState.value.copy(message = "Card added")
        }
    }

    fun updateCard(card: FlashcardEntity, front: String, back: String, hint: String?, tags: List<String>) {
        viewModelScope.launch {
            repository.updateFlashcard(
                card.copy(
                    frontText = front,
                    backText = back,
                    hint = hint,
                    tags = tags
                )
            )
            _uiState.value = _uiState.value.copy(message = "Card updated")
        }
    }

    fun deleteCard(card: FlashcardEntity) {
        viewModelScope.launch {
            repository.deleteFlashcard(card)
            _uiState.value = _uiState.value.copy(message = "Card deleted")
        }
    }

    fun moveCard(card: FlashcardEntity, direction: Int) {
        val cards = _uiState.value.cards.toMutableList()
        val index = cards.indexOfFirst { it.id == card.id }
        val target = index + direction
        if (index !in cards.indices || target !in cards.indices) return
        val moved = cards.removeAt(index)
        cards.add(target, moved)
        viewModelScope.launch { repository.reorderFlashcards(cards) }
    }

    fun toggleCardSelection(id: Long) {
        val current = _uiState.value.selectedCardIds
        val next = if (current.contains(id)) current - id else current + id
        _uiState.value = _uiState.value.copy(selectedCardIds = next)
    }

    fun clearSelection() {
        _uiState.value = _uiState.value.copy(selectedCardIds = emptySet())
    }

    fun selectAllCards() {
        val allIds = _uiState.value.cards.map { it.id }.toSet()
        _uiState.value = _uiState.value.copy(selectedCardIds = allIds)
    }

    fun deleteSelectedCards() {
        val ids = _uiState.value.selectedCardIds
        if (ids.isEmpty()) return
        val cardsToDelete = _uiState.value.cards.filter { it.id in ids }
        viewModelScope.launch {
            cardsToDelete.forEach { repository.deleteFlashcard(it) }
            _uiState.value = _uiState.value.copy(selectedCardIds = emptySet(), message = "${ids.size} cards deleted")
        }
    }

    fun moveSelectedCards(targetDeckId: Long) {
        val ids = _uiState.value.selectedCardIds.toList()
        if (ids.isEmpty()) return
        viewModelScope.launch {
            repository.moveFlashcards(ids, targetDeckId)
            _uiState.value = _uiState.value.copy(selectedCardIds = emptySet(), message = "${ids.size} cards moved")
        }
    }

    fun copySelectedCards(targetDeckId: Long) {
        val ids = _uiState.value.selectedCardIds.toList()
        if (ids.isEmpty()) return
        viewModelScope.launch {
            repository.copyFlashcards(ids, targetDeckId)
            _uiState.value = _uiState.value.copy(selectedCardIds = emptySet(), message = "${ids.size} cards copied")
        }
    }

    fun rateCurrentCard(rating: String) {
        val card = _uiState.value.currentCard ?: return
        viewModelScope.launch {
            repository.rateFlashcard(card, rating)
            nextCard()
        }
    }

    fun generateFromMarked(title: String, clearMarksAfter: Boolean, config: FlashcardGenerationConfig) {
        val deck = _uiState.value.deck ?: return
        viewModelScope.launch {
            runCatching {
                val markedQuestions = repository.getBookStudyBundle(deck.bookId)?.questions.orEmpty().filter { it.isMarked }
                val count = repository.addFlashcardsFromQuestionsToDeck(deck.id, markedQuestions.map { it.id }, config)
                if (clearMarksAfter) {
                    markedQuestions.forEach { question ->
                        repository.updateQuestion(question.copy(isMarked = false, updatedAt = System.currentTimeMillis()))
                    }
                }
                count
            }.onSuccess { count ->
                _uiState.value = _uiState.value.copy(message = "$count marked-question card(s) added")
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(error = error.message ?: "Failed to generate cards")
            }
        }
    }

    fun generateFromMissed(config: FlashcardGenerationConfig) {
        val deck = _uiState.value.deck ?: return
        viewModelScope.launch {
            runCatching {
                val missedIds = repository.getBookStudyBundle(deck.bookId)?.questions.orEmpty()
                    .filter { it.attempts > 0 && (it.correctCount < it.attempts || it.lastAttemptResult == false) }
                    .map { it.id }
                repository.addFlashcardsFromQuestionsToDeck(deck.id, missedIds, config)
            }.onSuccess { count ->
                _uiState.value = _uiState.value.copy(message = "$count missed-question card(s) added")
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(error = error.message ?: "Failed to generate cards")
            }
        }
    }

    fun generateFromBook(clearMarksAfter: Boolean, config: FlashcardGenerationConfig) {
        val deck = _uiState.value.deck ?: return
        viewModelScope.launch {
            runCatching {
                val questions = repository.getBookStudyBundle(deck.bookId)?.questions.orEmpty()
                val count = repository.addFlashcardsFromQuestionsToDeck(deck.id, questions.map { it.id }, config)
                if (clearMarksAfter) {
                    questions.filter { it.isMarked }.forEach { question ->
                        repository.updateQuestion(question.copy(isMarked = false, updatedAt = System.currentTimeMillis()))
                    }
                }
                count
            }.onSuccess { count ->
                _uiState.value = _uiState.value.copy(message = "$count card(s) added from book")
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(error = error.message ?: "Failed to generate cards")
            }
        }
    }

    fun generateFromQuiz(quizId: Long, clearMarksAfter: Boolean, config: FlashcardGenerationConfig) {
        val deck = _uiState.value.deck ?: return
        viewModelScope.launch {
            runCatching {
                val bundle = repository.getBookStudyBundle(deck.bookId)
                val questions = bundle?.questionsByQuiz?.get(quizId).orEmpty()
                val count = repository.addFlashcardsFromQuestionsToDeck(deck.id, questions.map { it.id }, config)
                if (clearMarksAfter) {
                    questions.filter { it.isMarked }.forEach { question ->
                        repository.updateQuestion(question.copy(isMarked = false, updatedAt = System.currentTimeMillis()))
                    }
                }
                count
            }.onSuccess { count ->
                _uiState.value = _uiState.value.copy(message = "$count card(s) added from quiz")
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(error = error.message ?: "Failed to generate cards")
            }
        }
    }

    fun generateFromCategory(categoryName: String, clearMarksAfter: Boolean, config: FlashcardGenerationConfig) {
        val deck = _uiState.value.deck ?: return
        viewModelScope.launch {
            runCatching {
                val questions = repository.getBookStudyBundle(deck.bookId)?.questions.orEmpty()
                    .filter { it.categories.contains(categoryName) }
                val count = repository.addFlashcardsFromQuestionsToDeck(deck.id, questions.map { it.id }, config)
                if (clearMarksAfter) {
                    questions.filter { it.isMarked }.forEach { question ->
                        repository.updateQuestion(question.copy(isMarked = false, updatedAt = System.currentTimeMillis()))
                    }
                }
                count
            }.onSuccess { count ->
                _uiState.value = _uiState.value.copy(message = "$count card(s) added from category '$categoryName'")
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(error = error.message ?: "Failed to generate cards")
            }
        }
    }

    fun importFromText(text: String, mode: com.ahmedyejam.mks.data.import.parser.TextParseMode) {
        val deck = _uiState.value.deck ?: return
        viewModelScope.launch {
            runCatching {
                val currentOrder = _uiState.value.cards.size
                val parser = TextFlashcardParser()
                val flashcards = parser.parse(text, deck.id, currentOrder, mode)
                repository.insertFlashcards(flashcards)
                flashcards.size
            }.onSuccess { count ->
                _uiState.value = _uiState.value.copy(message = "$count card(s) imported")
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(error = error.message ?: "Failed to import cards")
            }
        }
    }
}
