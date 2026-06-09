package com.ahmedyejam.mks.ui.library.components

import androidx.compose.animation.*
import androidx.compose.animation.core.snap
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FactCheck
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmedyejam.mks.ui.theme.LocalMksDesignTokens
import com.ahmedyejam.mks.R
import com.ahmedyejam.mks.data.local.entity.BookEntity
import com.ahmedyejam.mks.data.local.entity.QuizEntity
import com.ahmedyejam.mks.data.local.entity.FlashcardDeckEntity
import com.ahmedyejam.mks.data.local.entity.SlideshowCourseEntity
import com.ahmedyejam.mks.data.local.entity.NoteBlueprintEntity
import com.ahmedyejam.mks.data.local.entity.PromptDeckEntity
import com.ahmedyejam.mks.data.repository.KnowledgeSummary
import com.ahmedyejam.mks.data.model.CategoryWithMetadata
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.AnimatedVisibility
import kotlinx.coroutines.delay

private const val KNOWLEDGE_SUMMARY_AUTO_HIDE_DELAY_MS = 5_000L

@Composable
fun LibraryContentGrid(
    gridState: LazyGridState,
    columns: GridCells,
    padding: PaddingValues,
    selectedBookId: Long,
    selectedCategory: String?,
    resumeQuiz: QuizEntity?,
    recentQuizzes: List<QuizEntity>,
    currentThemeMode: String,
    onQuizClick: (Long) -> Unit,
    onLinkClick: (String) -> Unit,
    categoriesExpanded: Boolean,
    previewCategories: List<CategoryWithMetadata>,
    onCategorySelected: (String) -> Unit,
    onCategoryLongClick: (CategoryWithMetadata) -> Unit,
    showCategoryBrowser: () -> Unit,
    books: List<BookEntity>,
    quizzes: List<QuizEntity>,
    flashcardDecks: List<FlashcardDeckEntity>,
    slideshowCourses: List<SlideshowCourseEntity>,
    noteBlueprints: List<NoteBlueprintEntity>,
    promptDecks: List<PromptDeckEntity>,
    currentViewMode: String,
    showCovers: Boolean,
    onBookClick: (BookEntity) -> Unit,
    onBookLongClick: (BookEntity) -> Unit,
    onQuizLongClick: (QuizEntity) -> Unit,
    onFlashcardDeckSelected: (Long) -> Unit,
    onFlashcardDeckLongClick: (FlashcardDeckEntity) -> Unit,
    onSlideshowSelected: (Long) -> Unit,
    onSlideshowLongClick: (SlideshowCourseEntity) -> Unit,
    onReviewBlueprintSelected: (Long) -> Unit,
    onReviewBlueprintLongClick: (NoteBlueprintEntity) -> Unit,
    onAiPromptDeckSelected: (Long) -> Unit,
    onAiPromptDeckLongClick: (PromptDeckEntity) -> Unit,
) {
    LazyVerticalGrid(
        state = gridState,
        columns = columns,
        modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if ((selectedBookId == -1L) && (selectedCategory == null)) {
            item(span = { GridItemSpan(maxLineSpan) }, key = "banner_header") {
                LibraryBanner(
                    resumeQuiz = resumeQuiz,
                    recentQuizzes = recentQuizzes,
                    currentThemeMode = currentThemeMode,
                    onQuizClick = { onQuizClick(it.id) },
                    onLinkClick = onLinkClick,
                )
            }



            item(span = { GridItemSpan(maxLineSpan) }, key = "categories_header") {
                CategoriesSection(
                    categoriesExpanded = categoriesExpanded,
                    previewCategories = previewCategories,
                    onCategorySelected = onCategorySelected,
                    onCategoryLongClick = onCategoryLongClick,
                    showCategoryBrowser = showCategoryBrowser,
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                SectionHeader(
                    title = stringResource(R.string.books_header),
                    subtitle = if (currentViewMode == "LIST") stringResource(R.string.list_view_label) else stringResource(R.string.grid_view_label),
                )
            }

            if (books.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }, key = "empty_books") {
                    LibraryEmptyStateCard(
                        icon = Icons.Rounded.MenuBook,
                        title = stringResource(R.string.empty_books_title),
                        body = stringResource(R.string.empty_books_body),
                    )
                }
            } else {
                items(books, key = { "book_${it.id}" }) { book ->
                    BookItem(
                        book = book,
                        viewMode = currentViewMode,
                        showCover = showCovers,
                        onClick = { onBookClick(book) },
                        onLongClick = { onBookLongClick(book) },
                    )
                }
            }
        } else {
            if (quizzes.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }, key = "empty_quizzes") {
                    LibraryEmptyStateCard(
                        icon = Icons.Rounded.FactCheck,
                        title = stringResource(R.string.empty_quizzes_title),
                        body = stringResource(R.string.empty_quizzes_body),
                    )
                }
            } else {
                items(quizzes, key = { "quiz_${it.id}" }) { quiz ->
                    QuizItem(
                        quiz = quiz,
                        viewMode = currentViewMode,
                        showCover = showCovers,
                        onClick = { onQuizClick(quiz.id) },
                        onLongClick = { onQuizLongClick(quiz) },
                    )
                }
            }
        }

        // Additional sections (Flashcards, Slideshows, etc.)
        if ((selectedBookId != -1L) && (selectedCategory == null)) {
            if (flashcardDecks.isNotEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SectionHeader(
                        title = stringResource(R.string.flashcards_title),
                        subtitle = if (currentViewMode == "LIST") stringResource(R.string.list_view_label) else stringResource(R.string.grid_view_label),
                    )
                }
                items(flashcardDecks, key = { "deck_${it.id}" }) { deck ->
                    QuizItem(
                        quiz = QuizEntity(
                            id = deck.id,
                            externalId = deck.externalId,
                            bookId = deck.bookId,
                            title = deck.title,
                            description = deck.description ?: "",
                            coverImage = deck.coverImage,
                            iconName = deck.iconName ?: "flashcard",
                            isPinned = deck.isPinned,
                        ),
                        viewMode = currentViewMode,
                        showCover = showCovers,
                        onClick = { onFlashcardDeckSelected(deck.id) },
                        onLongClick = { onFlashcardDeckLongClick(deck) },
                    )
                }
            }

            if (slideshowCourses.isNotEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SectionHeader(title = stringResource(R.string.slideshow_courses_title))
                }
                items(slideshowCourses, key = { "slideshow_${it.id}" }) { course ->
                    QuizItem(
                        quiz = QuizEntity(
                            id = course.id,
                            externalId = course.externalId,
                            bookId = course.bookId,
                            title = course.title,
                            description = course.description ?: "",
                            iconName = "slideshow",
                        ),
                        viewMode = currentViewMode,
                        showCover = showCovers,
                        onClick = { onSlideshowSelected(course.id) },
                        onLongClick = { onSlideshowLongClick(course) },
                    )
                }
            }

            if (noteBlueprints.isNotEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SectionHeader(title = stringResource(R.string.note_blueprints_title))
                }
                items(noteBlueprints, key = { "note_${it.id}" }) { note ->
                    QuizItem(
                        quiz = QuizEntity(
                            id = note.id,
                            externalId = note.externalId,
                            bookId = note.bookId,
                            title = note.title,
                            description = note.summary ?: "",
                            iconName = "note",
                        ),
                        viewMode = currentViewMode,
                        showCover = showCovers,
                        onClick = { onReviewBlueprintSelected(note.id) },
                        onLongClick = { onReviewBlueprintLongClick(note) },
                    )
                }
            }

            if (promptDecks.isNotEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SectionHeader(title = stringResource(R.string.prompts_title))
                }
                items(promptDecks, key = { "prompt_deck_${it.id}" }) { deck ->
                    QuizItem(
                        quiz = QuizEntity(
                            id = deck.id,
                            externalId = "prompt-deck-${deck.id}",
                            bookId = deck.bookId,
                            title = deck.title,
                            description = deck.description ?: "Reusable prompt deck",
                            iconName = "prompt",
                        ),
                        viewMode = currentViewMode,
                        showCover = showCovers,
                        onClick = { onAiPromptDeckSelected(deck.id) },
                        onLongClick = { onAiPromptDeckLongClick(deck) },
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, subtitle: String? = null) {
    Column(modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        subtitle?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CategoriesSection(
    categoriesExpanded: Boolean,
    previewCategories: List<CategoryWithMetadata>,
    onCategorySelected: (String) -> Unit,
    onCategoryLongClick: (CategoryWithMetadata) -> Unit,
    showCategoryBrowser: () -> Unit,
) {
    val tokens = LocalMksDesignTokens.current
    val haptic = LocalHapticFeedback.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.94f))
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
                RoundedCornerShape(26.dp),
            )
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = showCategoryBrowser,
                    onLongClick = {
                        if (!tokens.isPlain) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        showCategoryBrowser()
                    },
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    stringResource(R.string.categories_header),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = showCategoryBrowser) {
                    Text(stringResource(R.string.view_all))
                }
            }
        }

        val tokens = LocalMksDesignTokens.current
        AnimatedVisibility(
            visible = categoriesExpanded && previewCategories.isNotEmpty(),
            enter = if (tokens.isPlain) fadeIn(snap()) else fadeIn() + expandVertically(),
            exit = if (tokens.isPlain) fadeOut(snap()) else fadeOut() + shrinkVertically(),
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    previewCategories.forEach { category ->
                        CategoryPreviewCard(
                            category = category,
                            onCategorySelected = { onCategorySelected(it.name) },
                            onCategoryLongClick = { onCategoryLongClick(category) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LibraryEmptyStateCard(
    icon: ImageVector,
    title: String,
    body: String,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface.copy(alpha = 0.90f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.outline.copy(alpha = 0.14f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = colors.primary.copy(alpha = 0.10f),
                contentColor = colors.primary,
            ) {
                Icon(icon, contentDescription = null, modifier = Modifier.padding(14.dp).size(30.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurfaceVariant,
                )
            }
        }
    }
}


