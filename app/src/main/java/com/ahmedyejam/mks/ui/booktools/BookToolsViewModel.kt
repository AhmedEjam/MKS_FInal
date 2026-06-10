package com.ahmedyejam.mks.ui.booktools

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedyejam.mks.data.local.entity.*
import com.ahmedyejam.mks.data.repository.BookKnowledgeSummary
import com.ahmedyejam.mks.data.repository.MksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private fun String.escapeJsonValue(): String = buildString {
    this@escapeJsonValue.forEach { ch ->
        when (ch) {
            '\\' -> append("\\\\")
            '"' -> append("\\\"")
            '\n' -> append("\\n")
            '\r' -> append("\\r")
            '\t' -> append("\\t")
            else -> append(ch)
        }
    }
}

private fun Map<String, String>.toSimpleJson(): String = entries.joinToString(prefix = "{", postfix = "}") { (key, value) ->
    "\"${key.escapeJsonValue()}\":\"${value.escapeJsonValue()}\""
}

data class BookToolsUiState(
    val book: BookEntity? = null,
    val quizzes: List<QuizEntity> = emptyList(),
    val questions: List<QuestionEntity> = emptyList(),
    val questionsByQuiz: Map<Long, List<QuestionEntity>> = emptyMap(),
    val flashcardDecks: List<FlashcardDeckEntity> = emptyList(),
    val allCourses: List<SlideshowCourseEntity> = emptyList(),
    val allNotes: List<NoteBlueprintEntity> = emptyList(),
    val allSources: List<SourceDocumentEntity> = emptyList(),
    val allPromptDecks: List<PromptDeckEntity> = emptyList(),
    val noteBlueprint: NoteBlueprintEntity? = null,
    val promptDeck: PromptDeckEntity? = null,
    val promptCards: List<PromptCardEntity> = emptyList(),
    val promptRuns: List<PromptRunEntity> = emptyList(),
    val slideshowCourse: SlideshowCourseEntity? = null,
    val courseSlides: List<CourseSlideEntity> = emptyList(),
    val mistakes: List<MistakeLogEntryEntity> = emptyList(),
    val bookSummary: BookKnowledgeSummary? = null,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

class BookToolsViewModel(
    private val repository: MksRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookToolsUiState())
    val uiState = _uiState.asStateFlow()

    fun loadBook(bookId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val bundle = repository.getBookStudyBundle(bookId)
                if (bundle == null) {
                    _uiState.value = BookToolsUiState(isLoading = false, error = "Book not found")
                    return@launch
                }
                _uiState.value = BookToolsUiState(
                    book = bundle.book.copy(coverImage = repository.resolveImagePath(bundle.book.coverImage)),
                    quizzes = bundle.quizzes.map { it.copy(coverImage = repository.resolveImagePath(it.coverImage)) },
                    questions = bundle.questions.map { it.copy(imagePath = repository.resolveImagePath(it.imagePath)) },
                    questionsByQuiz = bundle.questionsByQuiz.mapValues { (_, questions) ->
                        questions.map { it.copy(imagePath = repository.resolveImagePath(it.imagePath)) }
                    },
                    flashcardDecks = bundle.flashcardDecks.map { it.copy(coverImage = repository.resolveImagePath(it.coverImage)) },
                    allCourses = bundle.slideshowCourses.map { it.copy(coverImage = repository.resolveImagePath(it.coverImage)) },
                    allNotes = bundle.noteBlueprints,
                    allSources = bundle.sourceDocuments,
                    allPromptDecks = bundle.promptDecks,
                    mistakes = bundle.mistakes,
                    bookSummary = repository.getBookKnowledgeSummary(bookId),
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed to load book")
            }
        }
    }



    fun loadNote(noteId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val note = repository.getNoteBlueprintById(noteId)
            _uiState.value = if (note == null) {
                _uiState.value.copy(isLoading = false, error = "Blueprint not found")
            } else {
                _uiState.value.copy(noteBlueprint = note, isLoading = false)
            }
        }
    }

    fun updateQuestionNote(questionId: Long, note: String) {
        val question = _uiState.value.questions.firstOrNull { it.id == questionId } ?: return
        viewModelScope.launch {
            repository.updateQuestion(question.copy(notes = note))
            _uiState.value = _uiState.value.copy(
                questions = _uiState.value.questions.map { if (it.id == questionId) it.copy(notes = note) else it },
                questionsByQuiz = _uiState.value.questionsByQuiz.mapValues { (_, questions) ->
                    questions.map { if (it.id == questionId) it.copy(notes = note) else it }
                },
                successMessage = "Question note updated"
            )
        }
    }

    fun createNote(bookId: Long, title: String, body: String, sourceQuestionId: Long? = null, mode: String = BlueprintMode.SIMPLE_NOTE) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isSaving = true, error = null)
                val note = NoteBlueprintEntity(
                    externalId = java.util.UUID.randomUUID().toString(),
                    bookId = bookId,
                    title = title.ifBlank { "Untitled blueprint" },
                    body = body,
                    bulletPoints = emptyList(),
                    tags = emptyList(),
                    blueprintMode = mode,
                    sourceQuestionId = sourceQuestionId,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
                val id = repository.insertNoteBlueprint(note)
                val saved = repository.getNoteBlueprintById(id) ?: note.copy(id = id)
                _uiState.value = _uiState.value.copy(
                    noteBlueprint = saved,
                    allNotes = (_uiState.value.allNotes.filter { it.id != id } + saved).sortedByDescending { it.updatedAt },
                    isSaving = false,
                    successMessage = "Blueprint created"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isSaving = false, error = "Failed to create blueprint: ${e.message}")
            }
        }
    }

    fun updateNote(note: NoteBlueprintEntity) {
        viewModelScope.launch {
            try {
                val updated = note.copy(updatedAt = System.currentTimeMillis())
                repository.updateNoteBlueprint(updated)
                _uiState.value = _uiState.value.copy(
                    noteBlueprint = updated,
                    allNotes = _uiState.value.allNotes.map { if (it.id == updated.id) updated else it },
                    successMessage = "Blueprint updated"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to update blueprint: ${e.message}")
            }
        }
    }

    fun deleteNote(note: NoteBlueprintEntity) {
        viewModelScope.launch {
            try {
                repository.deleteNoteBlueprint(note)
                _uiState.value = _uiState.value.copy(
                    noteBlueprint = null,
                    allNotes = _uiState.value.allNotes.filter { it.id != note.id },
                    successMessage = "Blueprint deleted"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to delete blueprint: ${e.message}")
            }
        }
    }

    fun recordNoteReview(note: NoteBlueprintEntity) {
        viewModelScope.launch {
            val updated = note.copy(
                reviewCount = note.reviewCount + 1,
                lastReviewedAt = System.currentTimeMillis(),
                reviewStatus = BlueprintReviewStatus.REVIEWED,
                updatedAt = System.currentTimeMillis()
            )
            repository.updateNoteBlueprint(updated)
            _uiState.value = _uiState.value.copy(
                noteBlueprint = updated,
                allNotes = _uiState.value.allNotes.map { if (it.id == updated.id) updated else it },
                successMessage = "Blueprint reviewed"
            )
        }
    }

    fun createSource(bookId: Long, title: String, type: String, details: String = "") {
        viewModelScope.launch {
            try {
                val source = SourceDocumentEntity(
                    bookId = bookId,
                    title = title.trim().ifBlank { "Untitled source" },
                    sourceType = type.ifBlank { SourceDocumentTypes.OTHER },
                    description = details.takeIf { it.isNotBlank() }
                )
                val id = repository.insertSourceDocument(source)
                val saved = repository.getSourceDocumentById(id) ?: source.copy(id = id)
                _uiState.value = _uiState.value.copy(
                    allSources = (_uiState.value.allSources.filter { it.id != id } + saved).sortedByDescending { it.updatedAt },
                    successMessage = "Source created"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to create source: ${e.message}")
            }
        }
    }

    fun updateSource(source: SourceDocumentEntity) {
        viewModelScope.launch {
            try {
                val updated = source.copy(updatedAt = System.currentTimeMillis())
                repository.updateSourceDocument(updated)
                _uiState.value = _uiState.value.copy(
                    allSources = _uiState.value.allSources.map { if (it.id == updated.id) updated else it },
                    successMessage = "Source updated"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to update source: ${e.message}")
            }
        }
    }

    fun deleteSource(source: SourceDocumentEntity) {
        viewModelScope.launch {
            try {
                repository.deleteSourceDocument(source)
                _uiState.value = _uiState.value.copy(
                    allSources = _uiState.value.allSources.filter { it.id != source.id },
                    successMessage = "Source deleted"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to delete source: ${e.message}")
            }
        }
    }

    fun generateArticlesFromQuestions(questionIds: List<Long>, config: com.ahmedyejam.mks.data.model.ArticleGenerationConfig) {
        val bookId = _uiState.value.book?.id ?: return
        viewModelScope.launch {
            try {
                val newIds = repository.addArticlesFromQuestions(bookId, questionIds, config)
                _uiState.value = _uiState.value.copy(
                    allNotes = repository.getNoteBlueprintsByBookId(bookId).first(),
                    successMessage = "Generated ${newIds.size} Articles"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to generate articles: ${e.message}")
            }
        }
    }

    fun generateArticlesFromMarked(config: com.ahmedyejam.mks.data.model.ArticleGenerationConfig = com.ahmedyejam.mks.data.model.ArticleGenerationConfig.DEFAULT) {
        val questions = _uiState.value.questions.filter { it.isMarked }
        generateArticlesFromQuestions(questions.map { it.id }, config)
    }

    fun generateArticlesFromMissed(config: com.ahmedyejam.mks.data.model.ArticleGenerationConfig = com.ahmedyejam.mks.data.model.ArticleGenerationConfig.DEFAULT) {
        val questions = _uiState.value.questions.filter { it.attempts > 0 && (it.correctCount < it.attempts || it.lastAttemptResult == false) }
        generateArticlesFromQuestions(questions.map { it.id }, config)
    }
    
    fun generateArticlesFromQuiz(quizId: Long, config: com.ahmedyejam.mks.data.model.ArticleGenerationConfig = com.ahmedyejam.mks.data.model.ArticleGenerationConfig.DEFAULT) {
        val questions = _uiState.value.questionsByQuiz[quizId] ?: return
        generateArticlesFromQuestions(questions.map { it.id }, config)
    }
    
    fun generateArticlesFromCategory(categoryId: Long, config: com.ahmedyejam.mks.data.model.ArticleGenerationConfig = com.ahmedyejam.mks.data.model.ArticleGenerationConfig.DEFAULT) {
        val questions = _uiState.value.questions.filter { categoryId.toString() in it.categories }
        generateArticlesFromQuestions(questions.map { it.id }, config)
    }
    
    fun importArticlesFromText(text: String, mode: com.ahmedyejam.mks.data.import.parser.TextArticleParseMode = com.ahmedyejam.mks.data.import.parser.TextArticleParseMode.BASIC) {
        val bookId = _uiState.value.book?.id ?: return
        viewModelScope.launch {
            try {
                val parser = com.ahmedyejam.mks.data.import.parser.TextArticleParser()
                val parsedNotes = parser.parse(text, bookId, mode)
                
                if (parsedNotes.isEmpty()) {
                    _uiState.value = _uiState.value.copy(error = "No valid articles found in text")
                    return@launch
                }
                
                parsedNotes.forEach { note ->
                    repository.insertNoteBlueprint(note)
                }
                
                _uiState.value = _uiState.value.copy(
                    allNotes = repository.getNoteBlueprintsByBookId(bookId).first(),
                    successMessage = "Imported ${parsedNotes.size} Articles"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to import articles: ${e.message}")
            }
        }
    }

    fun createBlueprintFromQuestion(bookId: Long, questionId: Long, mode: String = BlueprintMode.CONCEPT_TEMPLATE) {
        viewModelScope.launch {
            try {
                val id = repository.createBlueprintFromQuestion(bookId, questionId, mode)
                val saved = repository.getNoteBlueprintById(id)
                _uiState.value = _uiState.value.copy(
                    allNotes = saved?.let { (_uiState.value.allNotes.filter { note -> note.id != id } + it).sortedByDescending { note -> note.updatedAt } } ?: _uiState.value.allNotes,
                    successMessage = "Article created from question"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to create article: ${e.message}")
            }
        }
    }

    fun createBlueprintFromMarked(bookId: Long, title: String) {
        viewModelScope.launch {
            try {
                val id = repository.createBlueprintFromMarkedQuestions(bookId, title)
                val saved = repository.getNoteBlueprintById(id)
                _uiState.value = _uiState.value.copy(
                    allNotes = saved?.let { (_uiState.value.allNotes.filter { note -> note.id != id } + it).sortedByDescending { note -> note.updatedAt } } ?: _uiState.value.allNotes,
                    successMessage = "Article created from marked questions"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to create marked article: ${e.message}")
            }
        }
    }

    fun createBlueprintFromMissed(bookId: Long, title: String) {
        viewModelScope.launch {
            try {
                val id = repository.createBlueprintFromMissedQuestions(bookId, title)
                val saved = repository.getNoteBlueprintById(id)
                _uiState.value = _uiState.value.copy(
                    allNotes = saved?.let { (_uiState.value.allNotes.filter { note -> note.id != id } + it).sortedByDescending { note -> note.updatedAt } } ?: _uiState.value.allNotes,
                    successMessage = "Article created from missed questions"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to create missed article: ${e.message}")
            }
        }
    }

    fun createFlashcardsFromBlueprint(noteId: Long) {
        viewModelScope.launch {
            try {
                repository.createFlashcardDeckFromBlueprint(noteId)
                _uiState.value = _uiState.value.copy(successMessage = "Flashcards created from blueprint")
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to create flashcards: ${e.message}")
            }
        }
    }

    fun appendBlueprintToQuestionNote(noteId: Long) {
        viewModelScope.launch {
            try {
                repository.appendBlueprintToSourceQuestionNote(noteId)
                _uiState.value = _uiState.value.copy(successMessage = "Blueprint appended to question note")
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to append blueprint: ${e.message}")
            }
        }
    }

    fun createSlideshowCourse(bookId: Long, title: String, description: String? = null) {
        viewModelScope.launch {
            try {
                val course = SlideshowCourseEntity(
                    externalId = java.util.UUID.randomUUID().toString(),
                    bookId = bookId,
                    title = title.ifBlank { "Untitled slideshow" },
                    description = description,
                    coverImage = null,
                    slideCount = 0,
                    studiedSlideCount = 0,
                    progress = 0f,
                    isSystem = false,
                    isPinned = false,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    lastStudiedAt = 0,
                    lastEditedAt = System.currentTimeMillis(),
                    isDerived = false,
                    sourceQuizId = null
                )
                val id = repository.insertSlideshowCourse(course)
                val saved = repository.getSlideshowCourseById(id) ?: course.copy(id = id)
                _uiState.value = _uiState.value.copy(
                    slideshowCourse = saved,
                    allCourses = (_uiState.value.allCourses.filter { it.id != id } + saved).sortedByDescending { it.lastEditedAt },
                    successMessage = "Slideshow created"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to create slideshow: ${e.message}")
            }
        }
    }

    fun updateSlideshowCourse(course: SlideshowCourseEntity) {
        viewModelScope.launch {
            try {
                val updated = course.copy(updatedAt = System.currentTimeMillis(), lastEditedAt = System.currentTimeMillis())
                repository.updateSlideshowCourse(updated)
                _uiState.value = _uiState.value.copy(
                    slideshowCourse = updated,
                    allCourses = _uiState.value.allCourses.map { if (it.id == updated.id) updated else it },
                    successMessage = "Slideshow updated"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to update slideshow: ${e.message}")
            }
        }
    }

    fun deleteSlideshowCourse(course: SlideshowCourseEntity) {
        viewModelScope.launch {
            try {
                repository.deleteSlideshowCourse(course)
                _uiState.value = _uiState.value.copy(
                    slideshowCourse = null,
                    courseSlides = emptyList(),
                    allCourses = _uiState.value.allCourses.filter { it.id != course.id },
                    successMessage = "Slideshow deleted"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to delete slideshow: ${e.message}")
            }
        }
    }


    fun loadPromptDeck(deckId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val deck = repository.getPromptDeckById(deckId)
            if (deck == null) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Prompt deck not found")
                return@launch
            }
            _uiState.value = _uiState.value.copy(
                promptDeck = deck,
                promptCards = repository.getPromptCardsByDeckIdNow(deckId),
                promptRuns = repository.getPromptRunsByDeckIdNow(deckId),
                isLoading = false
            )
        }
    }

    fun createPromptDeck(bookId: Long, title: String, description: String? = null) {
        viewModelScope.launch {
            try {
                val id = repository.createDefaultPromptDeck(bookId, title, description)
                val saved = repository.getPromptDeckById(id)
                _uiState.value = _uiState.value.copy(
                    allPromptDecks = saved?.let { (_uiState.value.allPromptDecks.filter { deck -> deck.id != id } + it).sortedByDescending { deck -> deck.updatedAt } } ?: _uiState.value.allPromptDecks,
                    successMessage = "Prompt deck created"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to create prompt deck: ${e.message}")
            }
        }
    }

    fun updatePromptDeck(deck: PromptDeckEntity) {
        viewModelScope.launch {
            try {
                val updated = deck.copy(updatedAt = System.currentTimeMillis())
                repository.updatePromptDeck(updated)
                _uiState.value = _uiState.value.copy(
                    promptDeck = updated,
                    allPromptDecks = _uiState.value.allPromptDecks.map { if (it.id == updated.id) updated else it },
                    successMessage = "Prompt deck updated"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to update prompt deck: ${e.message}")
            }
        }
    }

    fun deletePromptDeck(deck: PromptDeckEntity) {
        viewModelScope.launch {
            try {
                repository.deletePromptDeck(deck)
                _uiState.value = _uiState.value.copy(
                    promptDeck = null,
                    promptCards = emptyList(),
                    promptRuns = emptyList(),
                    allPromptDecks = _uiState.value.allPromptDecks.filter { it.id != deck.id },
                    successMessage = "Prompt deck deleted"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to delete prompt deck: ${e.message}")
            }
        }
    }

    fun createPromptCard(deckId: Long, title: String, promptText: String, outputType: String = PromptOutputType.OTHER) {
        viewModelScope.launch {
            try {
                val variablesJson = extractVariables(promptText).joinToString(prefix = "[", postfix = "]") { "\"${it.escapeJsonValue()}\"" }
                val id = repository.insertPromptCard(
                    PromptCardEntity(
                        deckId = deckId,
                        title = title.ifBlank { "Untitled prompt" },
                        promptText = promptText,
                        variablesJson = variablesJson,
                        outputType = outputType,
                        sortOrder = _uiState.value.promptCards.size
                    )
                )
                val saved = repository.getPromptCardsByDeckIdNow(deckId).firstOrNull { it.id == id }
                _uiState.value = _uiState.value.copy(
                    promptCards = repository.getPromptCardsByDeckIdNow(deckId),
                    successMessage = if (saved != null) "Prompt card created" else "Prompt card created"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to create prompt card: ${e.message}")
            }
        }
    }

    fun updatePromptCard(card: PromptCardEntity) {
        viewModelScope.launch {
            try {
                val updated = card.copy(
                    variablesJson = extractVariables(card.promptText).joinToString(prefix = "[", postfix = "]") { "\"${it.escapeJsonValue()}\"" },
                    updatedAt = System.currentTimeMillis()
                )
                repository.updatePromptCard(updated)
                _uiState.value = _uiState.value.copy(
                    promptCards = _uiState.value.promptCards.map { if (it.id == updated.id) updated else it },
                    successMessage = "Prompt card updated"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to update prompt card: ${e.message}")
            }
        }
    }

    fun deletePromptCard(card: PromptCardEntity) {
        viewModelScope.launch {
            try {
                repository.deletePromptCard(card)
                _uiState.value = _uiState.value.copy(
                    promptCards = _uiState.value.promptCards.filter { it.id != card.id },
                    successMessage = "Prompt card deleted"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to delete prompt card: ${e.message}")
            }
        }
    }

    fun recordPromptCardRun(card: PromptCardEntity, inputValues: Map<String, String>, renderedPrompt: String, outputText: String? = null) {
        viewModelScope.launch {
            try {
                repository.recordPromptRun(card.id, inputValues.toSimpleJson(), renderedPrompt, outputText)
                _uiState.value = _uiState.value.copy(
                    promptCards = repository.getPromptCardsByDeckIdNow(card.deckId),
                    promptRuns = repository.getPromptRunsByDeckIdNow(card.deckId),
                    successMessage = if (outputText.isNullOrBlank()) "Prompt run recorded" else "Prompt output saved"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to record prompt run: ${e.message}")
            }
        }
    }

    fun savePromptOutputAsNote(card: PromptCardEntity, outputText: String, title: String) {
        val bookId = _uiState.value.promptDeck?.bookId ?: return
        viewModelScope.launch {
            try {
                val id = repository.convertPromptOutputToNote(bookId, title, outputText, card.id)
                _uiState.value = _uiState.value.copy(
                    allNotes = repository.getNoteBlueprintsByBookId(bookId).first(),
                    promptRuns = repository.getPromptRunsByDeckIdNow(card.deckId),
                    successMessage = "Prompt output saved as note #$id"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to save note: ${e.message}")
            }
        }
    }

    fun savePromptOutputAsBlueprint(card: PromptCardEntity, outputText: String, title: String) {
        val bookId = _uiState.value.promptDeck?.bookId ?: return
        viewModelScope.launch {
            try {
                val id = repository.convertPromptOutputToBlueprint(bookId, title, outputText, card.id)
                _uiState.value = _uiState.value.copy(
                    allNotes = repository.getNoteBlueprintsByBookId(bookId).first(),
                    promptRuns = repository.getPromptRunsByDeckIdNow(card.deckId),
                    successMessage = "Prompt output saved as blueprint #$id"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to save blueprint: ${e.message}")
            }
        }
    }

    fun savePromptOutputAsFlashcards(card: PromptCardEntity, outputText: String, title: String) {
        val bookId = _uiState.value.promptDeck?.bookId ?: return
        viewModelScope.launch {
            try {
                val id = repository.convertPromptOutputToFlashcards(bookId, title, outputText, card.id)
                _uiState.value = _uiState.value.copy(
                    flashcardDecks = repository.getFlashcardDecksByBookId(bookId).first(),
                    promptRuns = repository.getPromptRunsByDeckIdNow(card.deckId),
                    successMessage = "Prompt output saved as flashcards deck #$id"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to save flashcards: ${e.message}")
            }
        }
    }

    fun extractVariables(text: String): List<String> =
        Regex("""\{[^}]+\}|\[[^\]]+\]|\([^)]+\)|<[^>]+>""").findAll(text).map { it.value }.distinct().toList()

    fun createFlashcardDeckFromMarkedQuestions(bookId: Long, title: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, error = null)
            runCatching {
                repository.createFlashcardDeckFromMarkedQuestions(bookId, title, "Generated from marked questions")
            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    successMessage = "Flashcard deck created from marked questions"
                )
                loadBook(bookId)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(isSaving = false, error = "Failed to create flashcard deck: ${e.message}")
            }
        }
    }

    fun createFlashcardDeckFromMissedQuestions(bookId: Long, title: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, error = null)
            runCatching {
                repository.createFlashcardDeckFromMissedQuestions(bookId, title, "Generated from missed questions")
            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    successMessage = "Flashcard deck created from missed questions"
                )
                loadBook(bookId)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(isSaving = false, error = "Failed to create flashcard deck: ${e.message}")
            }
        }
    }

    fun createSlideshowCourseFromQuestions(bookId: Long, title: String, questionIds: List<Long>) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, error = null)
            runCatching {
                repository.createSlideshowCourseFromQuestions(bookId, title, "Generated from questions", questionIds)
            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    successMessage = "Slideshow course created from questions"
                )
                loadBook(bookId)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(isSaving = false, error = "Failed to create slideshow course: ${e.message}")
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(error = null, successMessage = null)
    }
}
