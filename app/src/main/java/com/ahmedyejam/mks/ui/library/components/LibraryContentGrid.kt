package com.ahmedyejam.mks.ui.library.components

import androidx.compose.animation.*
import androidx.compose.animation.core.snap
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.Quiz
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
import com.ahmedyejam.mks.data.local.entity.PromptEntity
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
    categories: List<CategoryWithMetadata>,
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
    prompts: List<PromptEntity>,
    promptDecks: List<PromptDeckEntity>,
    knowledgeSummary: KnowledgeSummary?,
    autoHideKnowledgeSummary: Boolean,
    currentViewMode: String,
    showCovers: Boolean,
    onBookClick: (BookEntity) -> Unit,
    onBookLongClick: (BookEntity) -> Unit,
    onQuizLongClick: (QuizEntity) -> Unit,
    onFlashcardDeckSelected: (Long) -> Unit,
    onSlideshowSelected: (Long) -> Unit,
    onReviewBlueprintSelected: (Long) -> Unit,
    onAiPromptDeckSelected: (Long) -> Unit
) {
    var showKnowledgeSummary by remember { mutableStateOf(false) }

    LaunchedEffect(knowledgeSummary, selectedBookId, selectedCategory, autoHideKnowledgeSummary) {
        if (knowledgeSummary != null && selectedBookId == -1L && selectedCategory == null) {
            showKnowledgeSummary = true
            if (autoHideKnowledgeSummary) {
                delay(KNOWLEDGE_SUMMARY_AUTO_HIDE_DELAY_MS)
                showKnowledgeSummary = false
            }
        } else {
            showKnowledgeSummary = false
        }
    }

    LazyVerticalGrid(
        state = gridState,
        columns = columns,
        modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (selectedBookId == -1L && selectedCategory == null) {
            item(span = { GridItemSpan(maxLineSpan) }, key = "banner_header") {
                LibraryBanner(
                    resumeQuiz = resumeQuiz,
                    recentQuizzes = recentQuizzes,
                    currentThemeMode = currentThemeMode,
                    onQuizClick = { onQuizClick(it.id) },
                    onLinkClick = onLinkClick
                )
            }

            if (showKnowledgeSummary) {
                knowledgeSummary?.let { summary ->
                    item(span = { GridItemSpan(maxLineSpan) }, key = "knowledge_summary") {
                        KnowledgeSummaryCard(summary)
                    }
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }, key = "categories_header") {
                CategoriesSection(
                    categories = categories,
                    categoriesExpanded = categoriesExpanded,
                    previewCategories = previewCategories,
                    onCategorySelected = onCategorySelected,
                    onCategoryLongClick = onCategoryLongClick,
                    showCategoryBrowser = showCategoryBrowser
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                SectionHeader(
                    title = stringResource(R.string.books_header),
                    subtitle = if (currentViewMode == "LIST") stringResource(R.string.list_view_label) else stringResource(R.string.grid_view_label)
                )
            }

            if (books.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }, key = "empty_books") {
                    LibraryEmptyStateCard(
                        icon = Icons.Rounded.Book,
                        title = stringResource(R.string.empty_books_title),
                        body = stringResource(R.string.empty_books_body)
                    )
                }
            } else {
                items(books, key = { "book_${it.id}" }) { book ->
                    BookItem(
                        book = book,
                        viewMode = currentViewMode,
                        showCover = showCovers,
                        onClick = { onBookClick(book) },
                        onLongClick = { onBookLongClick(book) }
                    )
                }
            }
        } else {
            if (quizzes.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }, key = "empty_quizzes") {
                    LibraryEmptyStateCard(
                        icon = Icons.Rounded.Quiz,
                        title = stringResource(R.string.empty_quizzes_title),
                        body = stringResource(R.string.empty_quizzes_body)
                    )
                }
            } else {
                items(quizzes, key = { "quiz_${it.id}" }) { quiz ->
                    QuizItem(
                        quiz = quiz,
                        viewMode = currentViewMode,
                        showCover = showCovers,
                        onClick = { onQuizClick(quiz.id) },
                        onLongClick = { onQuizLongClick(quiz) }
                    )
                }
            }
        }

        // Additional sections (Flashcards, Slideshows, etc.)
        if (selectedBookId != -1L && selectedCategory == null) {
            if (flashcardDecks.isNotEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SectionHeader(
                        title = stringResource(R.string.flashcards_title),
                        subtitle = if (currentViewMode == "LIST") stringResource(R.string.list_view_label) else stringResource(R.string.grid_view_label)
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
                            iconName = deck.iconName,
                            isPinned = deck.isPinned
                        ),
                        viewMode = currentViewMode,
                        showCover = showCovers,
                        onClick = { onFlashcardDeckSelected(deck.id) },
                        onLongClick = { }
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
                            description = course.description ?: ""
                        ),
                        viewMode = currentViewMode,
                        showCover = showCovers,
                        onClick = { onSlideshowSelected(course.id) },
                        onLongClick = { }
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
                            description = note.summary ?: ""
                        ),
                        viewMode = currentViewMode,
                        showCover = showCovers,
                        onClick = { onReviewBlueprintSelected(note.id) },
                        onLongClick = { }
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
                            description = deck.description ?: "Reusable prompt deck"
                        ),
                        viewMode = currentViewMode,
                        showCover = showCovers,
                        onClick = { onAiPromptDeckSelected(deck.id) },
                        onLongClick = { }
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
            fontWeight = FontWeight.Bold
        )
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CategoriesSection(
    categories: List<CategoryWithMetadata>,
    categoriesExpanded: Boolean,
    previewCategories: List<CategoryWithMetadata>,
    onCategorySelected: (String) -> Unit,
    onCategoryLongClick: (CategoryWithMetadata) -> Unit,
    showCategoryBrowser: () -> Unit
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
                RoundedCornerShape(26.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = showCategoryBrowser,
                    onLongClick = {
                        if (!tokens.isPlain) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        showCategoryBrowser()
                    }
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    stringResource(R.string.categories_header),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
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
            exit = if (tokens.isPlain) fadeOut(snap()) else fadeOut() + shrinkVertically()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    previewCategories.forEach { category ->
                        CategoryPreviewCard(
                            category = category,
                            onCategorySelected = { onCategorySelected(it.name) },
                            onCategoryLongClick = { onCategoryLongClick(category) }
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
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface.copy(alpha = 0.90f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.outline.copy(alpha = 0.14f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = colors.primary.copy(alpha = 0.10f),
                contentColor = colors.primary
            ) {
                Icon(icon, contentDescription = null, modifier = Modifier.padding(14.dp).size(30.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun KnowledgeSummaryCard(summary: KnowledgeSummary) {
    val tokens = LocalMksDesignTokens.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(tokens.cardRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Knowledge summary",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SummaryMiniStat("Books", summary.totalBooks, Modifier.weight(1f))
                SummaryMiniStat("Quizzes", summary.totalQuizzes, Modifier.weight(1f))
                SummaryMiniStat("Questions", summary.totalQuestions, Modifier.weight(1f))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SummaryMiniStat("Marked", summary.markedQuestions, Modifier.weight(1f))
                SummaryMiniStat("Assets", summary.questionsWithAssets, Modifier.weight(1f))
                SummaryMiniStat("Sources", summary.questionsWithSources, Modifier.weight(1f))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SummaryMiniStat("Flashcards", summary.totalFlashcards, Modifier.weight(1f))
                SummaryMiniStat("Blueprints", summary.totalBlueprints, Modifier.weight(1f))
                SummaryMiniStat("Prompts", summary.promptDecks, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun SummaryMiniStat(label: String, value: Int, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(value.toString(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
