package com.ahmedyejam.mks.ui.navigation

import com.ahmedyejam.mks.ui.MksRoutes

/**
 * Higher-level builders for MKS navigation.
 */
object MksRouteBuilders {
    fun welcome() = MksRoutes.WELCOME
    fun library() = MksRoutes.LIBRARY
    fun globalSearch() = MksRoutes.GLOBAL_SEARCH
    fun reviewDashboard(mistakeId: Long? = null) = MksRoutes.reviewDashboard(mistakeId)
    fun dataTools() = MksRoutes.DATA_TOOLS
    fun settings() = MksRoutes.SETTINGS

    fun quizQuestions(quizId: Long, questionId: Long? = null) = MksRoutes.quizQuestions(quizId, questionId)
    fun sessions(quizId: Long) = MksRoutes.sessions(quizId)
    fun quiz(quizId: Long, sessionId: Long? = null) = MksRoutes.quiz(quizId, sessionId)
    fun summary(sessionId: Long) = MksRoutes.summary(sessionId)
    fun scanner(quizId: Long) = MksRoutes.scanner(quizId)
    
    fun category(category: String) = MksRoutes.category(category)
    fun adaptive(type: String, id: String) = MksRoutes.adaptive(type, id)

    fun flashcards(deckId: Long, cardId: Long? = null) = MksRoutes.flashcards(deckId, cardId)
    fun slideshow(courseId: Long, slideId: Long? = null) = MksRoutes.slideshow(courseId, slideId)
    fun blueprint(noteId: Long) = MksRoutes.blueprint(noteId)
    
    fun bookSlideshows(bookId: Long) = MksRoutes.bookSlideshows(bookId)
    fun bookBlueprints(bookId: Long) = MksRoutes.bookBlueprints(bookId)
    fun bookSources(bookId: Long, sourceId: Long? = null) = MksRoutes.bookSources(bookId, sourceId)
    fun bookNotes(bookId: Long) = MksRoutes.bookNotes(bookId)
    fun bookPrompts(bookId: Long) = MksRoutes.bookPrompts(bookId)
    fun promptDeck(promptId: Long, cardId: Long? = null, runId: Long? = null) = MksRoutes.promptDeck(promptId, cardId, runId)
}
