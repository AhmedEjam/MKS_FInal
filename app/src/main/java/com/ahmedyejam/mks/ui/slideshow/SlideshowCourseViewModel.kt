package com.ahmedyejam.mks.ui.slideshow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedyejam.mks.data.local.entity.CourseSlideEntity
import com.ahmedyejam.mks.data.local.entity.QuizEntity
import com.ahmedyejam.mks.data.local.entity.SlideshowCourseEntity
import com.ahmedyejam.mks.data.repository.MksRepository
import com.ahmedyejam.mks.data.repository.SortOption
import com.ahmedyejam.mks.data.model.SlideGenerationConfig
import com.ahmedyejam.mks.data.import.parser.TextSlideParser
import com.ahmedyejam.mks.data.import.parser.TextParseMode
import com.ahmedyejam.mks.di.AppModule
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

data class SlideshowCourseUiState(
    val course: SlideshowCourseEntity? = null,
    val slides: List<CourseSlideEntity> = emptyList(),
    val availableCourses: List<SlideshowCourseEntity> = emptyList(),
    val quizzes: List<QuizEntity> = emptyList(),
    val categories: List<String> = emptyList(),
    val currentIndex: Int = 0,
    val isLoading: Boolean = true,
    val isPresentationMode: Boolean = false,
    val selectedSlideIds: Set<Long> = emptySet(),
    val message: String? = null,
    val error: String? = null
) {
    val currentSlide: CourseSlideEntity?
        get() = slides.getOrNull(currentIndex.coerceIn(0, (slides.size - 1).coerceAtLeast(0)))
}

class SlideshowCourseViewModel(appModule: AppModule) : ViewModel() {
    private val repository: MksRepository = appModule.repository
    private val _uiState = MutableStateFlow(SlideshowCourseUiState())
    val uiState = _uiState.asStateFlow()
    private var courseId: Long? = null
    private var loadJob: Job? = null

    fun loadCourse(id: Long) {
        if (courseId == id && loadJob?.isActive == true) return
        courseId = id
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            combine(
                flowOf(repository.getSlideshowCourseById(id)), // getSlideshowCourseById is suspend in MksRepository. Wait, I should wrap it or adjust. Let me fix the loading block.
                repository.getSlidesByCourseId(id)
            ) { course, slides -> Pair(course, slides) }
            .flatMapLatest { pair ->
                val course = pair.first
                val slides = pair.second
                val coursesFlow = course?.bookId?.let { repository.getSlideshowCoursesByBookId(it) } ?: flowOf(emptyList())
                val quizzesFlow = course?.bookId?.let { repository.getQuizzesByBookId(it, SortOption.TITLE) } ?: flowOf(emptyList())
                val categoriesFlow = repository.getAllCategoriesWithMetadata()

                combine(coursesFlow, quizzesFlow, categoriesFlow) { allCourses, quizzes, cats ->
                    Triple(course, slides, Triple(allCourses, quizzes, cats))
                }
            }
            .collect { result ->
                val course = result.first
                val slides = result.second
                val extras = result.third
                val allCourses = extras.first
                val quizzes = extras.second
                val cats = extras.third
                val current = _uiState.value
                _uiState.value = current.copy(
                    course = course,
                    slides = slides,
                    availableCourses = allCourses.filter { it.id != id },
                    quizzes = quizzes,
                    categories = cats.map { it.name },
                    currentIndex = current.currentIndex.coerceIn(0, (slides.size - 1).coerceAtLeast(0)),
                    isLoading = false
                )
            }
        }
    }

    // A helper method since getSlideshowCourseById is a suspend function
    private suspend fun fetchCourseAndObserve(id: Long) {
        val course = repository.getSlideshowCourseById(id)
        repository.getSlidesByCourseId(id).flatMapLatest { slides ->
            val coursesFlow = course?.bookId?.let { repository.getSlideshowCoursesByBookId(it) } ?: flowOf(emptyList())
            val quizzesFlow = course?.bookId?.let { repository.getQuizzesByBookId(it, SortOption.TITLE) } ?: flowOf(emptyList())
            val categoriesFlow = repository.getAllCategoriesWithMetadata()

            combine(coursesFlow, quizzesFlow, categoriesFlow) { allCourses, quizzes, cats ->
                Triple(course, slides, Triple(allCourses, quizzes, cats))
            }
        }.collect { result ->
            val loadedCourse = result.first
            val slides = result.second
            val extras = result.third
            val allCourses = extras.first
            val quizzes = extras.second
            val cats = extras.third
            val current = _uiState.value
            _uiState.value = current.copy(
                course = loadedCourse,
                slides = slides,
                availableCourses = allCourses.filter { it.id != id },
                quizzes = quizzes,
                categories = cats.map { it.name },
                currentIndex = current.currentIndex.coerceIn(0, (slides.size - 1).coerceAtLeast(0)),
                isLoading = false
            )
        }
    }

    fun loadCourseSafe(id: Long) {
        if (courseId == id && loadJob?.isActive == true) return
        courseId = id
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            fetchCourseAndObserve(id)
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null, error = null)
    }

    fun setPresentationMode(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isPresentationMode = enabled, currentIndex = 0)
    }

    fun nextSlide() {
        val current = _uiState.value
        val slide = current.currentSlide
        if (slide != null && !slide.isCompleted) {
            viewModelScope.launch { repository.updateCourseSlide(slide.copy(isCompleted = true)) }
        }
        val next = if (current.slides.isEmpty()) 0 else (current.currentIndex + 1).coerceAtMost(current.slides.lastIndex)
        _uiState.value = current.copy(currentIndex = next)
    }

    fun previousSlide() {
        val current = _uiState.value
        val previous = (current.currentIndex - 1).coerceAtLeast(0)
        _uiState.value = current.copy(currentIndex = previous)
    }

    fun updateCourse(title: String, description: String?, coverImage: String?) {
        val course = _uiState.value.course ?: return
        viewModelScope.launch {
            repository.updateSlideshowCourse(course.copy(title = title, description = description, coverImage = coverImage))
            _uiState.value = _uiState.value.copy(message = "Course updated")
            // refresh course object since it's not fully observed
            val refreshed = repository.getSlideshowCourseById(course.id)
            _uiState.value = _uiState.value.copy(course = refreshed)
        }
    }

    fun addSlide(title: String, body: String, imagePath: String?) {
        val course = _uiState.value.course ?: return
        viewModelScope.launch {
            val order = _uiState.value.slides.size
            val now = System.currentTimeMillis()
            repository.insertCourseSlide(
                CourseSlideEntity(
                    externalId = java.util.UUID.randomUUID().toString(),
                    courseId = course.id,
                    title = title,
                    body = body,
                    imagePath = imagePath,
                    orderIndex = order,
                    createdAt = now,
                    updatedAt = now
                )
            )
            _uiState.value = _uiState.value.copy(message = "Slide added")
        }
    }

    fun updateSlide(slide: CourseSlideEntity, title: String, body: String, imagePath: String?) {
        viewModelScope.launch {
            repository.updateCourseSlide(
                slide.copy(
                    title = title,
                    body = body,
                    imagePath = imagePath
                )
            )
            _uiState.value = _uiState.value.copy(message = "Slide updated")
        }
    }

    fun deleteSlide(slide: CourseSlideEntity) {
        viewModelScope.launch {
            repository.deleteCourseSlide(slide)
            _uiState.value = _uiState.value.copy(message = "Slide deleted")
        }
    }

    fun moveSlide(slide: CourseSlideEntity, direction: Int) {
        val slides = _uiState.value.slides.toMutableList()
        val index = slides.indexOfFirst { it.id == slide.id }
        val target = index + direction
        if (index !in slides.indices || target !in slides.indices) return
        val moved = slides.removeAt(index)
        slides.add(target, moved)
        viewModelScope.launch { repository.reorderCourseSlides(slides) }
    }

    fun toggleSlideSelection(id: Long) {
        val current = _uiState.value.selectedSlideIds
        val next = if (current.contains(id)) current - id else current + id
        _uiState.value = _uiState.value.copy(selectedSlideIds = next)
    }

    fun clearSelection() {
        _uiState.value = _uiState.value.copy(selectedSlideIds = emptySet())
    }

    fun selectAllSlides() {
        val allIds = _uiState.value.slides.map { it.id }.toSet()
        _uiState.value = _uiState.value.copy(selectedSlideIds = allIds)
    }

    fun deleteSelectedSlides() {
        val ids = _uiState.value.selectedSlideIds
        if (ids.isEmpty()) return
        val slidesToDelete = _uiState.value.slides.filter { it.id in ids }
        viewModelScope.launch {
            slidesToDelete.forEach { repository.deleteCourseSlide(it) }
            _uiState.value = _uiState.value.copy(selectedSlideIds = emptySet(), message = "${ids.size} slides deleted")
        }
    }

    fun moveSelectedSlides(targetCourseId: Long) {
        val ids = _uiState.value.selectedSlideIds
        if (ids.isEmpty()) return
        viewModelScope.launch {
            repository.moveCourseSlides(ids.toList(), targetCourseId)
            _uiState.value = _uiState.value.copy(selectedSlideIds = emptySet(), message = "${ids.size} slides moved")
        }
    }

    fun copySelectedSlides(targetCourseId: Long) {
        val ids = _uiState.value.selectedSlideIds
        if (ids.isEmpty()) return
        viewModelScope.launch {
            repository.copyCourseSlides(ids.toList(), targetCourseId)
            _uiState.value = _uiState.value.copy(selectedSlideIds = emptySet(), message = "${ids.size} slides copied")
        }
    }

    fun importFromText(text: String, mode: TextParseMode) {
        val course = _uiState.value.course ?: return
        viewModelScope.launch {
            runCatching {
                val parser = TextSlideParser()
                val orderStart = _uiState.value.slides.size
                val slides = parser.parse(text, course.id, orderStart, mode)
                if (slides.isNotEmpty()) {
                    repository.insertCourseSlides(slides)
                }
                slides.size
            }.onSuccess { count ->
                _uiState.value = _uiState.value.copy(message = "$count slide(s) imported")
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(error = error.message ?: "Failed to import text")
            }
        }
    }

    fun generateFromMarked(config: SlideGenerationConfig, clearMarksAfter: Boolean) {
        val course = _uiState.value.course ?: return
        viewModelScope.launch {
            runCatching {
                val markedQuestions = repository.getBookStudyBundle(course.bookId)?.questions.orEmpty().filter { it.isMarked }
                val count = repository.addCourseSlidesFromQuestionsToCourse(course.id, markedQuestions.map { it.id }, config)
                if (clearMarksAfter) {
                    markedQuestions.forEach { question ->
                        repository.updateQuestion(question.copy(isMarked = false, updatedAt = System.currentTimeMillis()))
                    }
                }
                count
            }.onSuccess { count ->
                _uiState.value = _uiState.value.copy(message = "$count marked-question slide(s) added")
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(error = error.message ?: "Failed to generate slides")
            }
        }
    }

    fun generateFromMissed(config: SlideGenerationConfig) {
        val course = _uiState.value.course ?: return
        viewModelScope.launch {
            runCatching {
                val missedIds = repository.getBookStudyBundle(course.bookId)?.questions.orEmpty()
                    .filter { it.attempts > 0 && (it.correctCount < it.attempts || it.lastAttemptResult == false) }
                    .map { it.id }
                repository.addCourseSlidesFromQuestionsToCourse(course.id, missedIds, config)
            }.onSuccess { count ->
                _uiState.value = _uiState.value.copy(message = "$count missed-question slide(s) added")
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(error = error.message ?: "Failed to generate slides")
            }
        }
    }

    fun generateFromBook(config: SlideGenerationConfig, clearMarksAfter: Boolean) {
        val course = _uiState.value.course ?: return
        viewModelScope.launch {
            runCatching {
                val questions = repository.getBookStudyBundle(course.bookId)?.questions.orEmpty()
                val count = repository.addCourseSlidesFromQuestionsToCourse(course.id, questions.map { it.id }, config)
                if (clearMarksAfter) {
                    questions.filter { it.isMarked }.forEach { question ->
                        repository.updateQuestion(question.copy(isMarked = false, updatedAt = System.currentTimeMillis()))
                    }
                }
                count
            }.onSuccess { count ->
                _uiState.value = _uiState.value.copy(message = "$count slide(s) added from book")
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(error = error.message ?: "Failed to generate slides")
            }
        }
    }

    fun generateFromQuiz(quizId: Long, config: SlideGenerationConfig, clearMarksAfter: Boolean) {
        val course = _uiState.value.course ?: return
        viewModelScope.launch {
            runCatching {
                val bundle = repository.getBookStudyBundle(course.bookId)
                val questions = bundle?.questionsByQuiz?.get(quizId).orEmpty()
                val count = repository.addCourseSlidesFromQuestionsToCourse(course.id, questions.map { it.id }, config)
                if (clearMarksAfter) {
                    questions.filter { it.isMarked }.forEach { question ->
                        repository.updateQuestion(question.copy(isMarked = false, updatedAt = System.currentTimeMillis()))
                    }
                }
                count
            }.onSuccess { count ->
                _uiState.value = _uiState.value.copy(message = "$count slide(s) added from quiz")
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(error = error.message ?: "Failed to generate slides")
            }
        }
    }

    fun generateFromCategory(categoryName: String, config: SlideGenerationConfig, clearMarksAfter: Boolean) {
        val course = _uiState.value.course ?: return
        viewModelScope.launch {
            runCatching {
                val questions = repository.getBookStudyBundle(course.bookId)?.questions.orEmpty()
                    .filter { it.categories.contains(categoryName) }
                val count = repository.addCourseSlidesFromQuestionsToCourse(course.id, questions.map { it.id }, config)
                if (clearMarksAfter) {
                    questions.filter { it.isMarked }.forEach { question ->
                        repository.updateQuestion(question.copy(isMarked = false, updatedAt = System.currentTimeMillis()))
                    }
                }
                count
            }.onSuccess { count ->
                _uiState.value = _uiState.value.copy(message = "$count slide(s) added from category '$categoryName'")
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(error = error.message ?: "Failed to generate slides")
            }
        }
    }
}
