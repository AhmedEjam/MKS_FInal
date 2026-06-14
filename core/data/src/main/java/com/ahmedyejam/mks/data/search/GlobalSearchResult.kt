package com.ahmedyejam.mks.data.search

import com.ahmedyejam.mks.ui.*

data class GlobalSearchResult(
    val id: String,
    val type: GlobalSearchResultType,
    val title: String,
    val subtitle: String? = null,
    val snippet: String? = null,
    val bookId: Long? = null,
    val quizId: Long? = null,
    val questionId: Long? = null,
    val route: Any? = null,
    val updatedAt: Long? = null
)

fun GlobalSearchResultRow.toResult(): GlobalSearchResult {
    val resolvedType = runCatching { GlobalSearchResultType.valueOf(type) }.getOrDefault(GlobalSearchResultType.QUESTION)
    val route = when (resolvedType) {
        GlobalSearchResultType.BOOK -> LibraryRoute
        GlobalSearchResultType.QUIZ -> quizId?.let { SessionsRoute(it) }
        GlobalSearchResultType.QUESTION,
        GlobalSearchResultType.ANSWER,
        GlobalSearchResultType.EXPLANATION,
        GlobalSearchResultType.HINT,
        GlobalSearchResultType.NOTE -> quizId?.let { QuizQuestionsRoute(it) }
        GlobalSearchResultType.ASSET -> quizId?.let { QuizQuestionsRoute(it) }
        GlobalSearchResultType.SOURCE -> bookId?.let { BookSourcesRoute(it, id.toLongOrNull()) }
        GlobalSearchResultType.FLASHCARD -> parentId?.let { FlashcardsRoute(it, id.toLongOrNull()) }
        GlobalSearchResultType.BLUEPRINT -> id.toLongOrNull()?.let { BlueprintRoute(it) }
        GlobalSearchResultType.SLIDE -> parentId?.let { SlideshowRoute(it, id.toLongOrNull()) }
        GlobalSearchResultType.PROMPT_DECK -> id.toLongOrNull()?.let { PromptDeckRoute(it) }
        GlobalSearchResultType.PROMPT_CARD -> parentId?.let { PromptDeckRoute(it, cardId = id.toLongOrNull()) }
        GlobalSearchResultType.PROMPT_RUN -> parentId?.let { PromptDeckRoute(it, runId = id.toLongOrNull()) }
        GlobalSearchResultType.MISTAKE -> id.toLongOrNull()?.let { ReviewDashboardRoute(it) }
        GlobalSearchResultType.ANNOTATION -> bookId?.let { BookDashboardRoute(it) }
        GlobalSearchResultType.CATEGORY, GlobalSearchResultType.TAG -> title.takeIf { it.isNotBlank() }?.let { CategoryRoute(it) }
    }
    return GlobalSearchResult(id, resolvedType, title, subtitle, snippet, bookId, quizId, questionId, route, updatedAt)
}
