package com.ahmedyejam.mks.ui.library

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject



import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedyejam.mks.data.local.entity.BookEntity
import com.ahmedyejam.mks.data.local.entity.CategoryMetadataEntity
import com.ahmedyejam.mks.data.local.entity.FlashcardDeckEntity
import com.ahmedyejam.mks.data.local.entity.NoteBlueprintEntity
import com.ahmedyejam.mks.data.local.entity.PromptDeckEntity
import com.ahmedyejam.mks.data.local.entity.QuestionEntity
import com.ahmedyejam.mks.data.local.entity.QuizEntity
import com.ahmedyejam.mks.data.local.entity.SlideshowCourseEntity
import com.ahmedyejam.mks.data.local.entity.WorkspaceEntity
import com.ahmedyejam.mks.data.model.CategoryWithMetadata
import com.ahmedyejam.mks.data.model.ExportResult
import com.ahmedyejam.mks.data.preferences.DataStoreManager
import com.ahmedyejam.mks.data.repository.MksRepository
import com.ahmedyejam.mks.data.repository.SortOption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class LibraryUiEvent {
    data class ShowSnackbar(val message: String) : LibraryUiEvent()
}

private data class WorkspaceBookFilter(
    val workspaceId: Long,
    val sortBy: SortOption,
    val filterField: String?,
    val searchQuery: String,
)

@OptIn(ExperimentalCoroutinesApi::class, kotlinx.coroutines.FlowPreview::class)
@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val repository: MksRepository,
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {

    private val _uiEvent = Channel<LibraryUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _isSearching = MutableStateFlow(value = false)
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
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), initialValue = true)



    init {
    }

    val categories = combine(
        repository.getAllCategoriesWithMetadata(),
        sortBy,
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
                .thenBy { if (sortOption == SortOption.TITLE) it.name else "" },
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    val books = combine(
        currentWorkspaceId,
        sortBy,
        _searchQuery,
    ) { workspaceId, sortBy, searchQuery ->
        WorkspaceBookFilter(workspaceId, sortBy, null, searchQuery)
    }.flatMapLatest { filter ->
        if (filter.workspaceId <= 0L) {
            flowOf(emptyList())
        } else repository.getBooksByWorkspace(filter.workspaceId, filter.sortBy).map { list ->
            list.asSequence()
                .filter { book ->
                    ((filter.filterField == null) || book.fields.contains(filter.filterField)) &&
                    (filter.searchQuery.isBlank() ||
                        book.title.contains(filter.searchQuery, ignoreCase = true) ||
                        book.description.contains(filter.searchQuery, ignoreCase = true) ||
                        book.fields.any { it.contains(filter.searchQuery, ignoreCase = true) }
                    )
                }
                .map { it.copy(coverImage = repository.resolveImagePath(it.coverImage)) }
                .sortedByDescending { it.isPinned }
                .toList()
        }
    }
    .flowOn(Dispatchers.Default)
    .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val quizzes = combine(
        _selectedCategory,
        bookSortBy,
        _searchQuery,
    ) { category, bookSortBy, searchQuery ->
        val baseFlow = when {
            category != null -> repository.getQuizzesByCategory(category, bookSortBy)
            else -> flowOf(emptyList())
        }
        baseFlow.map { list ->
            list.asSequence()
                .filter { quiz ->
                    searchQuery.isBlank() ||
                    quiz.title.contains(searchQuery, ignoreCase = true) ||
                    quiz.description.contains(searchQuery, ignoreCase = true) ||
                    (quiz.category?.contains(searchQuery, ignoreCase = true) ?: false)
                }
                .map { it.copy(coverImage = repository.resolveImagePath(it.coverImage)) }
                .sortedByDescending { it.isPinned }
                .toList()
        }
    }.flatMapLatest { it }
    .flowOn(Dispatchers.Default)
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentQuizzes = repository.getAllQuizzesFlow()
        .map { list ->
            list.asSequence()
                .sortedByDescending { it.updatedAt }
                .take(5)
                .map { it.copy(coverImage = repository.resolveImagePath(it.coverImage)) }
                .toList()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val resumeQuiz = combine(
        dataStoreManager.lastSession,
        repository.getAllQuizzesFlow(),
    ) { lastSession, list ->
        val lastQuizId = lastSession?.first
        val selected = lastQuizId?.let { id -> list.find { it.id == id } }
            ?: list.maxByOrNull { it.updatedAt }
        selected?.copy(coverImage = repository.resolveImagePath(selected.coverImage))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(name: String) {
        _selectedCategory.value = name
    }

    fun setSearching(searching: Boolean) {
        _isSearching.value = searching
        if (!searching) _searchQuery.value = ""
    }

    fun resetToLibraryRoot() {
        _selectedCategory.value = null
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

    fun updateBook(book: BookEntity, newCoverUri: String? = null) {
        viewModelScope.launch {
            try {
                val finalBook = when {
                    newCoverUri?.startsWith("content://") == true -> {
                        val savedPath = withContext(Dispatchers.IO) {
                            repository.saveImage(newCoverUri.toUri())
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
                            repository.saveImage(newCoverUri.toUri())
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


    fun insertBook(book: BookEntity, coverUri: String? = null) {
        viewModelScope.launch {
            try {
                val finalBook = if (coverUri?.startsWith("content://") == true) {
                    val savedPath = withContext(Dispatchers.IO) {
                        repository.saveImage(coverUri.toUri())
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
                        repository.saveImage(coverUri.toUri())
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

    fun createNewQuiz(
        bookId: Long,
        title: String,
        description: String,
        coverImage: String?,
        sourceQuizIds: List<Long>,
        sourceCategories: List<String>
    ) {
        viewModelScope.launch {
            try {
                val newQuizId = repository.insertQuiz(
                    QuizEntity(
                        externalId = java.util.UUID.randomUUID().toString(),
                        bookId = bookId,
                        title = title.ifBlank { "New Quiz" },
                        description = description,
                        coverImage = coverImage,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis(),
                    ),
                )

                if (sourceQuizIds.isNotEmpty() || sourceCategories.isNotEmpty()) {
                    val bookQuizzes = repository.getQuizzesByBookId(bookId, SortOption.TITLE).first()
                    val allQuestions = mutableListOf<QuestionEntity>()

                    if (sourceQuizIds.isNotEmpty()) {
                        for (qId in sourceQuizIds) {
                            allQuestions.addAll(repository.getQuestionsByQuizId(qId).first())
                        }
                    } else if (sourceCategories.isNotEmpty()) {
                        for (quiz in bookQuizzes) {
                            allQuestions.addAll(repository.getQuestionsByQuizId(quiz.id).first())
                        }
                    }

                    val filteredQuestions = if (sourceCategories.isNotEmpty()) {
                        allQuestions.filter { q ->
                            q.categories.any { it in sourceCategories }
                        }
                    } else {
                        allQuestions
                    }

                    val duplicatedQuestions = filteredQuestions.map { q ->
                        q.copy(
                            id = 0,
                            externalId = java.util.UUID.randomUUID().toString(),
                            quizId = newQuizId,
                            attempts = 0,
                            correctCount = 0,
                            isMarked = false,
                            createdAt = System.currentTimeMillis(),
                            updatedAt = System.currentTimeMillis(),
                        )
                    }
                    
                    if (duplicatedQuestions.isNotEmpty()) {
                        repository.insertQuestions(duplicatedQuestions)
                    }
                }

                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Quiz '${title.ifBlank { "New Quiz" }}' created"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to create quiz: ${e.message}"))
            }
        }
    }

    fun updateCategoryMetadata(name: String, emoji: String?, color: Int?, isPinned: Boolean) {
        viewModelScope.launch {
            repository.updateCategoryMetadata(
                CategoryMetadataEntity(
                    name = name,
                    emoji = emoji,
                    color = color,
                    isPinned = isPinned,
                ),
            )
        }
    }

    val workspaces = repository.getAllWorkspaces()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val deletedWorkspaces = repository.getDeletedWorkspaces()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectWorkspace(workspaceId: Long) {
        viewModelScope.launch {
            dataStoreManager.setCurrentWorkspaceId(workspaceId)
            resetToLibraryRoot()
        }
    }

    fun insertWorkspace(name: String, description: String? = null) {
        viewModelScope.launch {
            try {
                repository.insertWorkspace(
                    WorkspaceEntity(
                        externalId = java.util.UUID.randomUUID().toString(),
                        name = name,
                        description = description
                    )
                )
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Workspace created"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to create workspace: ${e.message}"))
            }
        }
    }

    fun updateWorkspace(workspace: WorkspaceEntity) {
        viewModelScope.launch {
            try {
                repository.updateWorkspace(workspace)
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Workspace updated"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to update workspace: ${e.message}"))
            }
        }
    }

    fun deleteWorkspace(workspace: WorkspaceEntity) {
        viewModelScope.launch {
            try {
                repository.deleteWorkspace(workspace)
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Workspace deleted"))
                if (currentWorkspaceId.value == workspace.id) {
                    val defaultWs = repository.getDefaultWorkspace() ?: repository.getOrCreateDefaultWorkspace()
                    selectWorkspace(defaultWs.id)
                }
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to delete workspace: ${e.message}"))
            }
        }
    }

    fun restoreWorkspace(workspaceId: Long) {
        viewModelScope.launch {
            try {
                repository.restoreWorkspace(workspaceId)
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Workspace restored"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to restore workspace: ${e.message}"))
            }
        }
    }

    fun permanentlyDeleteWorkspace(workspace: WorkspaceEntity) {
        viewModelScope.launch {
            try {
                repository.permanentlyDeleteWorkspace(workspace)
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Workspace permanently deleted"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to permanently delete workspace: ${e.message}"))
            }
        }
    }

    suspend fun exportQuiz(quizId: Long, outputStream: java.io.OutputStream): ExportResult {
        return repository.exportQuizToZip(quizId, outputStream)
    }

    suspend fun exportBook(bookId: Long, outputStream: java.io.OutputStream): ExportResult {
        return repository.exportBundleToZip(bookId, outputStream)
    }

    suspend fun exportAll(outputStream: java.io.OutputStream): ExportResult {
        return repository.exportAllToZip(outputStream)
    }

    val deletedBooks = currentWorkspaceId.flatMapLatest { workspaceId ->
        if (workspaceId <= 0L) flowOf(emptyList()) else repository.getDeletedBooks(workspaceId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val deletedQuizzes = currentWorkspaceId.flatMapLatest { workspaceId ->
        if (workspaceId <= 0L) flowOf(emptyList()) else repository.getDeletedQuizzes(workspaceId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val deletedDecks = currentWorkspaceId.flatMapLatest { workspaceId ->
        if (workspaceId <= 0L) flowOf(emptyList()) else repository.getDeletedFlashcardDecks(workspaceId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val deletedSlideshows = currentWorkspaceId.flatMapLatest { workspaceId ->
        if (workspaceId <= 0L) flowOf(emptyList()) else repository.getDeletedSlideshowCourses(workspaceId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val deletedNotes = currentWorkspaceId.flatMapLatest { workspaceId ->
        if (workspaceId <= 0L) flowOf(emptyList()) else repository.getDeletedNoteBlueprints(workspaceId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val deletedPrompts = currentWorkspaceId.flatMapLatest { workspaceId ->
        if (workspaceId <= 0L) flowOf(emptyOf<PromptDeckEntity>()) else repository.getDeletedPromptDecks(
            workspaceId
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun restoreBook(bookId: Long) {
        viewModelScope.launch {
            try {
                repository.restoreBook(bookId)
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Book restored"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to restore book: ${e.message}"))
            }
        }
    }

    fun permanentlyDeleteBook(book: BookEntity) {
        viewModelScope.launch {
            try {
                repository.permanentlyDeleteBook(book)
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Book permanently deleted"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to permanently delete book: ${e.message}"))
            }
        }
    }

    fun restoreQuiz(quizId: Long) {
        viewModelScope.launch {
            try {
                repository.restoreQuiz(quizId)
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Quiz restored"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to restore quiz: ${e.message}"))
            }
        }
    }

    fun permanentlyDeleteQuiz(quiz: QuizEntity) {
        viewModelScope.launch {
            try {
                repository.permanentlyDeleteQuiz(quiz)
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Quiz permanently deleted"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to permanently delete quiz: ${e.message}"))
            }
        }
    }

    fun restoreFlashcardDeck(deckId: Long) {
        viewModelScope.launch {
            try {
                repository.restoreFlashcardDeck(deckId)
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Flashcard deck restored"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to restore flashcard deck: ${e.message}"))
            }
        }
    }

    fun permanentlyDeleteFlashcardDeck(deck: FlashcardDeckEntity) {
        viewModelScope.launch {
            try {
                repository.permanentlyDeleteFlashcardDeck(deck)
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Flashcard deck permanently deleted"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to permanently delete deck: ${e.message}"))
            }
        }
    }

    fun restoreSlideshowCourse(courseId: Long) {
        viewModelScope.launch {
            try {
                repository.restoreSlideshowCourse(courseId)
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Slideshow course restored"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to restore slideshow course: ${e.message}"))
            }
        }
    }

    fun permanentlyDeleteSlideshowCourse(course: SlideshowCourseEntity) {
        viewModelScope.launch {
            try {
                repository.permanentlyDeleteSlideshowCourse(course)
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Slideshow course permanently deleted"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to permanently delete slideshow course: ${e.message}"))
            }
        }
    }

    fun restoreNoteBlueprint(noteId: Long) {
        viewModelScope.launch {
            try {
                repository.restoreNoteBlueprint(noteId)
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Note blueprint restored"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to restore note blueprint: ${e.message}"))
            }
        }
    }

    fun permanentlyDeleteNoteBlueprint(note: NoteBlueprintEntity) {
        viewModelScope.launch {
            try {
                repository.permanentlyDeleteNoteBlueprint(note)
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Note blueprint permanently deleted"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to permanently delete note blueprint: ${e.message}"))
            }
        }
    }

    fun restorePromptDeck(deckId: Long) {
        viewModelScope.launch {
            try {
                repository.restorePromptDeck(deckId)
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Prompt deck restored"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to restore prompt deck: ${e.message}"))
            }
        }
    }

    fun permanentlyDeletePromptDeck(deck: PromptDeckEntity) {
        viewModelScope.launch {
            try {
                repository.permanentlyDeletePromptDeck(deck)
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Prompt deck permanently deleted"))
            } catch (e: Exception) {
                _uiEvent.send(LibraryUiEvent.ShowSnackbar("Failed to permanently delete prompt deck: ${e.message}"))
            }
        }
    }

    private fun <T> emptyOf(): List<T> = emptyList()
}
