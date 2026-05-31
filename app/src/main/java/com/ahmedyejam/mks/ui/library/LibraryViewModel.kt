package com.ahmedyejam.mks.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.net.Uri
import com.ahmedyejam.mks.data.local.entity.*
import com.ahmedyejam.mks.data.model.CategoryWithMetadata
import com.ahmedyejam.mks.data.model.ExportResult
import com.ahmedyejam.mks.data.preferences.DataStoreManager
import com.ahmedyejam.mks.data.repository.MksRepository
import com.ahmedyejam.mks.data.repository.KnowledgeSummary
import com.ahmedyejam.mks.data.repository.SortOption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class LibraryUiEvent {
    data class ShowSnackbar(val message: String) : LibraryUiEvent()
}

private data class WorkspaceBookFilter(
    val workspaceId: Long,
    val sortBy: SortOption,
    val filterField: String?,
    val searchQuery: String
)

@OptIn(ExperimentalCoroutinesApi::class, kotlinx.coroutines.FlowPreview::class)
class LibraryViewModel(
    private val repository: MksRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _uiEvent = Channel<LibraryUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _knowledgeSummary = MutableStateFlow<KnowledgeSummary?>(null)
    val knowledgeSummary = _knowledgeSummary.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _globalSearchQuery = MutableStateFlow("")
    val globalSearchQuery = _globalSearchQuery.asStateFlow()

    private val _selectedBookId = MutableStateFlow(-1L)
    val selectedBookId = _selectedBookId.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _filterField = MutableStateFlow<String?>(null)
    val filterField = _filterField.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    val currentWorkspaceId = dataStoreManager.currentWorkspaceId
        .map { storedId -> storedId ?: repository.getOrCreateDefaultWorkspace().id }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0L)

    val sortBy = dataStoreManager.librarySortOption
        .map { runCatching { SortOption.valueOf(it) }.getOrDefault(SortOption.TITLE) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SortOption.TITLE)

    val bookSortBy = dataStoreManager.bookSortOption
        .map { runCatching { SortOption.valueOf(it) }.getOrDefault(SortOption.TITLE) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SortOption.TITLE)

    val libraryViewMode = dataStoreManager.libraryViewMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "LIST")

    val bookViewMode = dataStoreManager.bookViewMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "LIST")

    val showCovers = dataStoreManager.showCovers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val autoHideKnowledgeSummary = dataStoreManager.autoHideKnowledgeSummary
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    init {
        refreshKnowledgeSummary()
    }

    fun refreshKnowledgeSummary() {
        viewModelScope.launch {
            try {
                _knowledgeSummary.value = repository.getLibraryKnowledgeSummary()
            } catch (e: Exception) {
                _knowledgeSummary.value = null
                _uiEvent.trySend(
                    LibraryUiEvent.ShowSnackbar(
                        "Knowledge summary unavailable; main library can still be used. ${e.message.orEmpty()}".trim()
                    )
                )
            }
        }
    }

    val categories = combine(
        repository.getAllCategoriesWithMetadata(),
        sortBy
    ) { list, sortOption ->
        list.sortedWith(
            compareByDescending<CategoryWithMetadata> { it.isPinned }
                .thenByDescending { 
                    when (sortOption) {
                        SortOption.QUESTION_COUNT -> it.questionCount.toFloat()
                        SortOption.PROGRESS -> it.answeredCount.toFloat() / it.questionCount.coerceAtLeast(1)
                        SortOption.ACCURACY -> it.accuracyPercentage
                        SortOption.LAST_STUDIED -> 0f // Not tracked per category in metadata yet
                        SortOption.LAST_EDIT -> it.lastEditedAt.toFloat()
                        SortOption.TITLE -> 0f // Use ascending for title below
                        else -> 0f
                    }
                }
                .thenBy { if (sortOption == SortOption.TITLE) it.name else "" }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allFields = repository.getAllFields()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val globalSearchResults = _globalSearchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.length < 2) flowOf(emptyList())
            else repository.searchAllQuestions(query)
        }
        .map { list ->
            list.map { it.copy(imagePath = repository.resolveImagePath(it.imagePath)) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val books = combine(
        currentWorkspaceId,
        sortBy,
        _filterField,
        _searchQuery
    ) { workspaceId, sortBy, filterField, searchQuery ->
        WorkspaceBookFilter(workspaceId, sortBy, filterField, searchQuery)
    }.flatMapLatest { filter ->
        if (filter.workspaceId <= 0L) {
            flowOf(emptyList())
        } else repository.getBooksByWorkspace(filter.workspaceId, filter.sortBy).map { list ->
            list.filter { book ->
                (filter.filterField == null || book.fields.contains(filter.filterField)) &&
                (filter.searchQuery.isBlank() ||
                    book.title.contains(filter.searchQuery, ignoreCase = true) ||
                    book.description.contains(filter.searchQuery, ignoreCase = true) ||
                    book.fields.any { it.contains(filter.searchQuery, ignoreCase = true) }
                )
            }.map { it.copy(coverImage = repository.resolveImagePath(it.coverImage)) }
             .sortedByDescending { it.isPinned }
        }
    }
    .flowOn(Dispatchers.Default)
    .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val currentBook = combine(_selectedBookId, books) { id, list ->
        list.find { it.id == id }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val quizzes = combine(
        _selectedBookId,
        _selectedCategory,
        bookSortBy,
        _searchQuery
    ) { bookId, category, bookSortBy, searchQuery ->
        val baseFlow = when {
            bookId != -1L -> repository.getQuizzesByBookId(bookId, bookSortBy)
            category != null -> repository.getQuizzesByCategory(category, bookSortBy)
            else -> flowOf(emptyList())
        }
        baseFlow.map { list ->
            list.filter { quiz ->
                searchQuery.isBlank() || 
                quiz.title.contains(searchQuery, ignoreCase = true) ||
                quiz.description.contains(searchQuery, ignoreCase = true) ||
                (quiz.category?.contains(searchQuery, ignoreCase = true) ?: false)
            }.map { it.copy(coverImage = repository.resolveImagePath(it.coverImage)) }
             .sortedByDescending { it.isPinned }
        }
    }.flatMapLatest { it }
    .flowOn(Dispatchers.Default)
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val flashcardDecks = combine(
        _selectedBookId,
        bookSortBy,
        _searchQuery
    ) { bookId, bookSortBy, searchQuery ->
        if (bookId != -1L) {
            repository.getFlashcardDecksByBookId(bookId).map { list ->
                list.filter { deck ->
                    searchQuery.isBlank() ||
                    deck.title.contains(searchQuery, ignoreCase = true) ||
                    deck.description?.contains(searchQuery, ignoreCase = true) ?: false
                }.map { it.copy(coverImage = repository.resolveImagePath(it.coverImage)) }
                 .sortedByDescending { it.isPinned }
            }
        } else {
            flowOf(emptyList())
        }
    }.flatMapLatest { it }
    .flowOn(Dispatchers.Default)
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val slideshowCourses = combine(
        _selectedBookId,
        bookSortBy,
        _searchQuery
    ) { bookId, _, searchQuery ->
        if (bookId != -1L) {
            repository.getSlideshowCoursesByBookId(bookId).map { list ->
                list.filter { course ->
                    searchQuery.isBlank() ||
                    course.title.contains(searchQuery, ignoreCase = true) ||
                    course.description?.contains(searchQuery, ignoreCase = true) ?: false
                }.sortedByDescending { it.isPinned }
            }
        } else {
            flowOf(emptyList())
        }
    }.flatMapLatest { it }
    .flowOn(Dispatchers.Default)
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val noteBlueprints = combine(
        _selectedBookId,
        _searchQuery
    ) { bookId, searchQuery ->
        if (bookId != -1L) {
            repository.getNoteBlueprintsByBookId(bookId).map { list ->
                list.filter { note ->
                    searchQuery.isBlank() ||
                    note.title.contains(searchQuery, ignoreCase = true) ||
                    note.body.contains(searchQuery, ignoreCase = true)
                }
            }
        } else {
            flowOf(emptyList())
        }
    }.flatMapLatest { it }
    .flowOn(Dispatchers.Default)
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val prompts = combine(
        _selectedBookId,
        _searchQuery
    ) { bookId, searchQuery ->
        if (bookId != -1L) {
            repository.getPromptsByBookId(bookId).map { list ->
                list.filter { prompt ->
                    searchQuery.isBlank() ||
                    prompt.title.contains(searchQuery, ignoreCase = true) ||
                    prompt.stem.contains(searchQuery, ignoreCase = true)
                }
            }
        } else {
            flowOf(emptyList())
        }
    }.flatMapLatest { it }
    .flowOn(Dispatchers.Default)
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val promptDecks = combine(
        _selectedBookId,
        _searchQuery
    ) { bookId, searchQuery ->
        if (bookId != -1L) {
            repository.getPromptDecksByBookId(bookId).map { list ->
                list.filter { deck ->
                    searchQuery.isBlank() ||
                    deck.title.contains(searchQuery, ignoreCase = true) ||
                    (deck.description?.contains(searchQuery, ignoreCase = true) ?: false)
                }
            }
        } else {
            flowOf(emptyList())
        }
    }.flatMapLatest { it }
    .flowOn(Dispatchers.Default)
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentQuizzes = repository.getAllQuizzesFlow()
        .map { list ->
            list.sortedByDescending { it.updatedAt }
                .take(5)
                .map { it.copy(coverImage = repository.resolveImagePath(it.coverImage)) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val resumeQuiz = combine(
        dataStoreManager.lastSession,
        repository.getAllQuizzesFlow()
    ) { lastSession, list ->
        val lastQuizId = lastSession?.first
        val selected = lastQuizId?.let { id -> list.find { it.id == id } }
            ?: list.sortedByDescending { it.updatedAt }.firstOrNull()
        selected?.copy(coverImage = repository.resolveImagePath(selected.coverImage))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setGlobalSearchQuery(query: String) {
        _globalSearchQuery.value = query
    }

    fun selectBook(id: Long) {
        _selectedBookId.value = id
        _selectedCategory.value = null
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
        _selectedBookId.value = -1L
    }

    fun setFilterField(field: String?) {
        _filterField.value = field
    }

    fun setSearching(searching: Boolean) {
        _isSearching.value = searching
        if (!searching) _searchQuery.value = ""
    }

    fun resetToLibraryRoot() {
        _selectedBookId.value = -1L
        _selectedCategory.value = null
        _filterField.value = null
        _isSearching.value = false
        _searchQuery.value = ""
    }

    fun setSortBy(option: SortOption) {
        viewModelScope.launch {
            dataStoreManager.setLibrarySortOption(option.name)
        }
    }

    fun setBookSortBy(option: SortOption) {
        viewModelScope.launch {
            dataStoreManager.setBookSortOption(option.name)
        }
    }

    fun toggleLibraryViewMode() {
        viewModelScope.launch {
            val current = libraryViewMode.value
            dataStoreManager.setLibraryViewMode(if (current == "GRID") "LIST" else "GRID")
        }
    }

    fun toggleBookViewMode() {
        viewModelScope.launch {
            val current = bookViewMode.value
            dataStoreManager.setBookViewMode(if (current == "GRID") "LIST" else "GRID")
        }
    }

    fun toggleBookPinned(book: BookEntity) {
        viewModelScope.launch {
            repository.updateBook(book.copy(isPinned = !book.isPinned))
        }
    }

    fun toggleQuizPinned(quiz: QuizEntity) {
        viewModelScope.launch {
            repository.updateQuiz(quiz.copy(isPinned = !quiz.isPinned))
        }
    }

    fun toggleFlashcardDeckPinned(deck: FlashcardDeckEntity) {
        viewModelScope.launch {
            repository.updateFlashcardDeck(deck.copy(isPinned = !deck.isPinned))
        }
    }

    fun deleteBook(book: BookEntity) {
        viewModelScope.launch {
            try {
                repository.deleteBook(book)
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Book '${book.title}' deleted"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to delete book: ${e.message}"))
            }
        }
    }

    fun deleteQuiz(quiz: QuizEntity) {
        viewModelScope.launch {
            try {
                repository.deleteQuiz(quiz)
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Quiz '${quiz.title}' deleted"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to delete quiz: ${e.message}"))
            }
        }
    }

    fun deleteFlashcardDeck(deck: FlashcardDeckEntity) {
        viewModelScope.launch {
            try {
                repository.deleteFlashcardDeck(deck)
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Flashcard deck '${deck.title}' deleted"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to delete flashcard deck: ${e.message}"))
            }
        }
    }

    fun updateBook(book: BookEntity, newCoverUri: String? = null) {
        viewModelScope.launch {
            try {
                val finalBook = when {
                    newCoverUri?.startsWith("content://") == true -> {
                        val savedPath = withContext(Dispatchers.IO) {
                            repository.saveImage(Uri.parse(newCoverUri))
                        }
                        book.copy(coverImage = savedPath, updatedAt = System.currentTimeMillis())
                    }
                    newCoverUri != null -> {
                        book.copy(coverImage = newCoverUri, updatedAt = System.currentTimeMillis())
                    }
                    else -> book.copy(updatedAt = System.currentTimeMillis())
                }
                repository.updateBook(finalBook)
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Book updated"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to update book: ${e.message}"))
            }
        }
    }

    fun updateQuiz(quiz: QuizEntity, newCoverUri: String? = null) {
        viewModelScope.launch {
            try {
                val finalQuiz = when {
                    newCoverUri?.startsWith("content://") == true -> {
                        val savedPath = withContext(Dispatchers.IO) {
                            repository.saveImage(Uri.parse(newCoverUri))
                        }
                        quiz.copy(coverImage = savedPath, updatedAt = System.currentTimeMillis())
                    }
                    newCoverUri != null -> {
                        quiz.copy(coverImage = newCoverUri, updatedAt = System.currentTimeMillis())
                    }
                    else -> quiz.copy(updatedAt = System.currentTimeMillis())
                }
                repository.updateQuiz(finalQuiz)
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Quiz updated"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to update quiz: ${e.message}"))
            }
        }
    }

    fun updateFlashcardDeck(deck: FlashcardDeckEntity, newCoverUri: String? = null) {
        viewModelScope.launch {
            try {
                val finalDeck = when {
                    newCoverUri?.startsWith("content://") == true -> {
                        val savedPath = withContext(Dispatchers.IO) {
                            repository.saveImage(Uri.parse(newCoverUri))
                        }
                        deck.copy(coverImage = savedPath, updatedAt = System.currentTimeMillis())
                    }
                    newCoverUri != null -> {
                        deck.copy(coverImage = newCoverUri, updatedAt = System.currentTimeMillis())
                    }
                    else -> deck.copy(updatedAt = System.currentTimeMillis())
                }
                repository.updateFlashcardDeck(finalDeck)
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Flashcard deck updated"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to update flashcard deck: ${e.message}"))
            }
        }
    }

    fun insertBook(book: BookEntity, coverUri: String? = null) {
        viewModelScope.launch {
            try {
                val finalBook = if (coverUri?.startsWith("content://") == true) {
                    val savedPath = withContext(Dispatchers.IO) {
                        repository.saveImage(Uri.parse(coverUri))
                    }
                    book.copy(coverImage = savedPath)
                } else {
                    book
                }
                repository.insertBook(finalBook)
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Book created"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to create book: ${e.message}"))
            }
        }
    }

    fun insertQuiz(quiz: QuizEntity, coverUri: String? = null) {
        viewModelScope.launch {
            try {
                val finalQuiz = if (coverUri?.startsWith("content://") == true) {
                    val savedPath = withContext(Dispatchers.IO) {
                        repository.saveImage(Uri.parse(coverUri))
                    }
                    quiz.copy(coverImage = savedPath)
                } else {
                    quiz
                }
                repository.insertQuiz(finalQuiz)
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Quiz created"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to create quiz: ${e.message}"))
            }
        }
    }

    fun insertFlashcardDeck(deck: FlashcardDeckEntity, coverUri: String? = null) {
        viewModelScope.launch {
            try {
                val finalDeck = if (coverUri?.startsWith("content://") == true) {
                    val savedPath = withContext(Dispatchers.IO) {
                        repository.saveImage(Uri.parse(coverUri))
                    }
                    deck.copy(coverImage = savedPath)
                } else {
                    deck
                }
                repository.insertFlashcardDeck(finalDeck)
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Flashcard deck created"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to create flashcard deck: ${e.message}"))
            }
        }
    }

    fun createFlashcardDeckFromBook(
        bookId: Long,
        title: String,
        description: String = "",
        coverUri: String? = null,
        onCreated: (Long) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val coverImage = when {
                    coverUri?.startsWith("content://") == true -> withContext(Dispatchers.IO) {
                        repository.saveImage(Uri.parse(coverUri))
                    }
                    coverUri.isNullOrBlank() -> null
                    else -> coverUri
                }
                val deckId = repository.createFlashcardDeckFromBook(
                    bookId = bookId,
                    title = title,
                    description = description.ifBlank { null },
                    coverImage = coverImage
                )
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Flashcard deck created"))
                onCreated(deckId)
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to create flashcard deck: ${e.message}"))
            }
        }
    }

    suspend fun importFromUri(uri: android.net.Uri) = repository.importFromUri(uri)

    fun saveImage(uri: android.net.Uri): String? = repository.saveImage(uri)

    fun deleteCategory(category: String) {
        viewModelScope.launch { repository.deleteCategory(category) }
    }

    fun renameCategory(oldName: String, newName: String) {
        viewModelScope.launch { repository.renameCategory(oldName, newName) }
    }

    fun mergeCategory(source: String, target: String) {
        viewModelScope.launch { repository.mergeCategory(source, target) }
    }

    suspend fun getMergePreview(source: String, target: String): Int {
        return repository.getMergePreview(source, target)
    }

    fun createQuizFromCategory(category: String, title: String, bookId: Long) {
        viewModelScope.launch { repository.createQuizFromCategory(category, title, bookId) }
    }

    fun createCustomQuiz(bookId: Long, quizIds: List<Long>) {
        viewModelScope.launch {
            try {
                val bookQuizzes = repository.getQuizzesByBookId(bookId, SortOption.TITLE).first()
                val customQuizzesCount = bookQuizzes.count { it.title.lowercase().startsWith("custom quiz") }
                val nextNum = (customQuizzesCount + 1).toString().padStart(2, '0')
                val title = "Custom Quiz $nextNum"
                
                val selectedQuizzes = bookQuizzes.filter { it.id in quizIds }
                val quizTitles = selectedQuizzes.joinToString { it.title }
                val description = "Included quizzes: $quizTitles"

                val newQuizId = repository.insertQuiz(
                    QuizEntity(
                        externalId = java.util.UUID.randomUUID().toString(),
                        bookId = bookId,
                        title = title,
                        description = description,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                )

                val allQuestions = mutableListOf<QuestionEntity>()
                for (qId in quizIds) {
                    allQuestions.addAll(repository.getQuestionsByQuizId(qId).first())
                }

                val duplicatedQuestions = allQuestions.map { q ->
                    q.copy(
                        id = 0,
                        externalId = java.util.UUID.randomUUID().toString(),
                        quizId = newQuizId,
                        attempts = 0,
                        correctCount = 0,
                        isMarked = false,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                }
                repository.insertQuestions(duplicatedQuestions)
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Custom quiz '$title' created"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to create custom quiz: ${e.message}"))
            }
        }
    }

    fun updateCategoryMetadata(name: String, emoji: String?, color: Int?, isPinned: Boolean) {
        viewModelScope.launch {
            repository.updateCategoryMetadata(
                com.ahmedyejam.mks.data.local.entity.CategoryMetadataEntity(
                    name = name,
                    emoji = emoji,
                    color = color,
                    isPinned = isPinned
                )
            )
        }
    }

    fun insertPrompt(bookId: Long, title: String, stem: String) {
        viewModelScope.launch {
            try {
                repository.insertPrompt(
                    PromptEntity(
                        externalId = java.util.UUID.randomUUID().toString(),
                        bookId = bookId,
                        title = title,
                        stem = stem,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                )
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Prompt created"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to create prompt: ${e.message}"))
            }
        }
    }

    fun deletePrompt(prompt: PromptEntity) {
        viewModelScope.launch {
            try {
                repository.deletePrompt(prompt)
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Prompt deleted"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to delete prompt: ${e.message}"))
            }
        }
    }

    fun updatePrompt(prompt: PromptEntity) {
        viewModelScope.launch {
            try {
                repository.updatePrompt(prompt)
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Prompt updated"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to update prompt: ${e.message}"))
            }
        }
    }

    suspend fun exportQuiz(quizId: Long, outputStream: java.io.OutputStream): ExportResult {
        return repository.exportQuizToSchema7Zip(quizId, outputStream)
    }

    suspend fun exportBook(bookId: Long, outputStream: java.io.OutputStream): ExportResult {
        return repository.exportBundleToSchema7Zip(bookId, outputStream)
    }

    suspend fun exportAll(outputStream: java.io.OutputStream): ExportResult {
        return repository.exportAllToSchema7Zip(outputStream)
    }
}
