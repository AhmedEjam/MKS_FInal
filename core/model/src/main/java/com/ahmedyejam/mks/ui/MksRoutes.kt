package com.ahmedyejam.mks.ui

import kotlinx.serialization.Serializable

@Serializable data object WelcomeRoute
@Serializable data object LibraryRoute
@Serializable data object GlobalSearchRoute
@Serializable data class ReviewDashboardRoute(val mistakeId: Long? = null)
@Serializable data object DataToolsRoute
@Serializable data object SettingsRoute

// Quiz & Sessions
@Serializable data class QuizQuestionsRoute(val quizId: Long, val questionId: Long? = null)
@Serializable data class SessionsRoute(val quizId: Long)
@Serializable data class QuizRoute(val quizId: Long, val sessionId: Long? = null)
@Serializable data class SummaryRoute(val sessionId: Long)
@Serializable data class ScannerRoute(val quizId: Long)

// Categories
@Serializable data class CategoryRoute(val category: String)
@Serializable data class AdaptiveRoute(val type: String, val id: String)

// Knowledge Bank
@Serializable data class FlashcardsRoute(val deckId: Long, val cardId: Long? = null)
@Serializable data class SlideshowRoute(val courseId: Long, val slideId: Long? = null)
@Serializable data class BlueprintRoute(val noteId: Long)

// Book Tools
@Serializable data class BookDashboardRoute(val bookId: Long)
@Serializable data class BookSlideshowsRoute(val bookId: Long)
@Serializable data class BookBlueprintsRoute(val bookId: Long)
@Serializable data class BookSourcesRoute(val bookId: Long, val sourceId: Long? = null)
@Serializable data class BookNotesRoute(val bookId: Long)
@Serializable data class BookPromptsRoute(val bookId: Long)
@Serializable data class PromptDeckRoute(val promptId: Long, val cardId: Long? = null, val runId: Long? = null)
