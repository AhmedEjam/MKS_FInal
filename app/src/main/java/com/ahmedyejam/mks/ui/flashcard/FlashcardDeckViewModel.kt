package com.ahmedyejam.mks.ui.flashcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedyejam.mks.data.local.entity.FlashcardDeckEntity
import com.ahmedyejam.mks.data.local.entity.FlashcardEntity
import com.ahmedyejam.mks.data.repository.MksRepository
import com.ahmedyejam.mks.di.AppModule
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

const val FLASHCARD_RATING_AGAIN = "again"
const val FLASHCARD_RATING_GOOD = "good"
const val FLASHCARD_RATING_EASY = "easy"

data class FlashcardDeckUiState(
    val deck: FlashcardDeckEntity? = null,
    val cards: List<FlashcardEntity> = emptyList(),
    val currentIndex: Int = 0,
    val isFlipped: Boolean = false,
    val isLoading: Boolean = true,
    val isStudyMode: Boolean = false,
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
            combine(
                repository.observeFlashcardDeckById(id),
                repository.getFlashcardsByDeckId(id)
            ) { deck, cards -> deck to cards }
                .collect { (deck, cards) ->
                    val current = _uiState.value
                    _uiState.value = current.copy(
                        deck = deck,
                        cards = cards,
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

    fun updateDeck(title: String, description: String?) {
        val deck = _uiState.value.deck ?: return
        viewModelScope.launch {
            repository.updateFlashcardDeck(deck.copy(title = title, description = description))
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

    fun rateCurrentCard(rating: String) {
        val card = _uiState.value.currentCard ?: return
        viewModelScope.launch {
            repository.rateFlashcard(card, rating)
            nextCard()
        }
    }

    fun generateFromMarked(title: String, clearMarksAfter: Boolean) {
        val deck = _uiState.value.deck ?: return
        viewModelScope.launch {
            runCatching {
                val markedQuestions = repository.getBookStudyBundle(deck.bookId)?.questions.orEmpty().filter { it.isMarked }
                val count = repository.addFlashcardsFromQuestionsToDeck(deck.id, markedQuestions.map { it.id })
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

    fun generateFromMissed() {
        val deck = _uiState.value.deck ?: return
        viewModelScope.launch {
            runCatching {
                val missedIds = repository.getBookStudyBundle(deck.bookId)?.questions.orEmpty()
                    .filter { it.attempts > 0 && (it.correctCount < it.attempts || it.lastAttemptResult == false) }
                    .map { it.id }
                repository.addFlashcardsFromQuestionsToDeck(deck.id, missedIds)
            }.onSuccess { count ->
                _uiState.value = _uiState.value.copy(message = "$count missed-question card(s) added")
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(error = error.message ?: "Failed to generate cards")
            }
        }
    }
}
