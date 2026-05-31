package com.ahmedyejam.mks.ui.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedyejam.mks.data.local.entity.QuestionEntity
import com.ahmedyejam.mks.data.local.entity.SessionEntity
import com.ahmedyejam.mks.data.repository.MksRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class ReviewFilter {
    ALL, CORRECT, WRONG, UNANSWERED, DROPPED, WITH_EXPLANATION
}

enum class ReviewDetail {
    STEM, OPTIONS, HINT, HIGH_YIELD, REFERENCE, QUESTION_NUMBER, EXPLANATION
}

data class SummaryState(
    val session: SessionEntity? = null,
    val questions: List<QuestionEntity> = emptyList(),
    val isLoading: Boolean = true,
    val categoryPerformance: Map<String, CategoryStats> = emptyMap(),
    val filteredQuestions: List<QuestionEntity> = emptyList(),
    val maxStreak: Int = 0,
    val reviewFilter: ReviewFilter = ReviewFilter.ALL,
    val visibleDetails: Set<ReviewDetail> = setOf(
        ReviewDetail.STEM,
        ReviewDetail.OPTIONS,
        ReviewDetail.QUESTION_NUMBER,
        ReviewDetail.EXPLANATION
    )
)

data class CategoryStats(
    val correct: Int,
    val total: Int,
    val percentage: Int
)

class SummaryViewModel(
    private val repository: MksRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SummaryState())
    val uiState: StateFlow<SummaryState> = _uiState.asStateFlow()

    fun loadSummary(sessionId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val session = repository.getSessionById(sessionId)
            if (session != null) {
                // Get all questions in the sequence (including repeats)
                val allQuestionsInQuiz = repository.getQuestionsByQuizId(session.quizId).first()
                
                val sessionQuestions = session.questionIds.map { id -> 
                    allQuestionsInQuiz.find { it.id == id } ?: QuestionEntity(
                        id = id,
                        externalId = "missing",
                        quizId = session.quizId,
                        text = "[Missing Question Data]",
                        type = com.ahmedyejam.mks.data.local.entity.QuestionType.SINGLE_CHOICE,
                        options = emptyList(),
                        correctAnswers = emptyList()
                    )
                }
                
                // Calculate category performance using full history (answersByIndex)
                val catStats = mutableMapOf<String, Pair<Int, Int>>()
                session.questionIds.forEachIndexed { index, id ->
                    val q = allQuestionsInQuiz.find { it.id == id } ?: return@forEachIndexed
                    val userAnswers = session.answersByIndex[index]
                    if (userAnswers != null) {
                        val isCorrect = q.correctAnswers.toSet() == userAnswers.toSet()
                        q.categories.forEach { cat ->
                            val current = catStats.getOrDefault(cat, 0 to 0)
                            catStats[cat] = (current.first + (if (isCorrect) 1 else 0)) to (current.second + 1)
                        }
                    }
                }
                
                val categoryPerformance = catStats.mapValues { (_, stats) ->
                    CategoryStats(stats.first, stats.second, if (stats.second > 0) (stats.first * 100 / stats.second) else 0)
                }

                _uiState.update { 
                    it.copy(
                        session = session,
                        questions = sessionQuestions,
                        isLoading = false,
                        categoryPerformance = categoryPerformance,
                        maxStreak = session.maxStreak
                    )
                }
                updateFilteredQuestions()
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun setReviewFilter(filter: ReviewFilter) {
        _uiState.update { it.copy(reviewFilter = filter) }
        updateFilteredQuestions()
    }

    fun toggleReviewDetail(detail: ReviewDetail) {
        _uiState.update { state ->
            val newDetails = if (state.visibleDetails.contains(detail)) {
                state.visibleDetails - detail
            } else {
                state.visibleDetails + detail
            }
            state.copy(visibleDetails = newDetails)
        }
    }

    private fun updateFilteredQuestions() {
        val state = _uiState.value
        val session = state.session ?: return
        
        // Match state.questions (full sequence) with session.answersByIndex
        val filtered = state.questions.filterIndexed { index, q ->
            val userAnswers = session.answersByIndex[index]
            val isCorrect = userAnswers != null && q.correctAnswers.toSet() == userAnswers.toSet()
            
            when (state.reviewFilter) {
                ReviewFilter.ALL -> true
                ReviewFilter.CORRECT -> isCorrect
                ReviewFilter.WRONG -> !isCorrect && userAnswers != null && !q.isDropped
                ReviewFilter.UNANSWERED -> userAnswers == null && !q.isDropped
                ReviewFilter.DROPPED -> q.isDropped
                ReviewFilter.WITH_EXPLANATION -> !q.explanation.isNullOrBlank()
            }
        }
        
        _uiState.update { it.copy(filteredQuestions = filtered) }
    }

    fun getExportText(): String {
        val state = _uiState.value
        val session = state.session ?: return ""
        val details = state.visibleDetails

        val filteredIndexedQuestions = state.questions.mapIndexed { index, question -> index to question }
            .filter { (sequenceIndex, question) ->
                val userAnswers = session.answersByIndex[sequenceIndex]
                val isCorrect = userAnswers != null && question.correctAnswers.toSet() == userAnswers.toSet()

                when (state.reviewFilter) {
                    ReviewFilter.ALL -> true
                    ReviewFilter.CORRECT -> isCorrect
                    ReviewFilter.WRONG -> !isCorrect && userAnswers != null && !question.isDropped
                    ReviewFilter.UNANSWERED -> userAnswers == null && !question.isDropped
                    ReviewFilter.DROPPED -> question.isDropped
                    ReviewFilter.WITH_EXPLANATION -> !question.explanation.isNullOrBlank()
                }
            }
        
        return buildString {
            appendLine("MKS Session Review: ${session.label}")
            appendLine("Score: ${session.score} / ${session.originalQuestionCount}")
            appendLine("Filter: ${state.reviewFilter}")
            appendLine("-".repeat(20))
            appendLine()

            filteredIndexedQuestions.forEachIndexed { exportIndex, (sequenceIndex, q) ->
                val userAnswers = session.answersByIndex[sequenceIndex]
                val isCorrect = userAnswers != null && q.correctAnswers.toSet() == userAnswers.toSet()
                
                if (details.contains(ReviewDetail.QUESTION_NUMBER)) {
                    append("Q${exportIndex + 1}: ")
                }
                
                val statusText = when {
                    q.isDropped -> "[DROPPED]"
                    userAnswers == null -> "[UNANSWERED]"
                    isCorrect -> "[CORRECT]"
                    else -> "[WRONG]"
                }
                appendLine(statusText)

                if (details.contains(ReviewDetail.STEM)) {
                    appendLine(q.text)
                }

                if (details.contains(ReviewDetail.OPTIONS)) {
                    q.options.forEachIndexed { optIdx, opt ->
                        val prefix = when {
                            q.correctAnswers.contains(optIdx) -> "✓ "
                            userAnswers?.contains(optIdx) == true -> "✗ "
                            else -> "  "
                        }
                        appendLine("$prefix$opt")
                    }
                }

                if (details.contains(ReviewDetail.EXPLANATION) && !q.explanation.isNullOrBlank()) {
                    appendLine("Explanation: ${q.explanation}")
                }

                if (details.contains(ReviewDetail.HINT) && !q.hint.isNullOrBlank()) {
                    appendLine("Hint: ${q.hint}")
                }

                if (details.contains(ReviewDetail.HIGH_YIELD) && !q.additionalInfo.isNullOrBlank()) {
                    appendLine("High Yield: ${q.additionalInfo}")
                }

                if (details.contains(ReviewDetail.REFERENCE) && !q.reference.isNullOrBlank()) {
                    appendLine("Reference: ${q.reference}")
                }

                appendLine()
            }
        }
    }

}
