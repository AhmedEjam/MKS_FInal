package com.ahmedyejam.mks.ui.booktools

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmedyejam.mks.data.local.entity.*
import com.ahmedyejam.mks.data.repository.BookKnowledgeSummary
import com.ahmedyejam.mks.ui.review.MistakeCard
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.res.stringResource
import com.ahmedyejam.mks.R

enum class BookTab(val title: String, val icon: ImageVector) {
    DASHBOARD("Dashboard", Icons.Rounded.Dashboard),
    SLIDES("Slides", Icons.Rounded.Slideshow),
    QUIZZES("Quizzes", Icons.Rounded.FactCheck),
    NOTES("Articles & short notes", Icons.Rounded.NoteAlt),
    MISTAKES("Mistakes", Icons.Rounded.Error),
    FLASHCARDS("Cards", Icons.Rounded.ViewCarousel),
    PROMPTS("Prompts", Icons.Rounded.Psychology),
    SOURCES("Sources", Icons.Rounded.Source)
}

@Composable
fun DashboardTab(
    summary: BookKnowledgeSummary?,
    bookId: Long,
    viewModel: BookToolsViewModel
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { DashboardSummaryCard(summary) }
        item {
            MagicActionsSection(
                bookId = bookId,
                summary = summary,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun QuizzesTab(
    quizzes: List<QuizEntity>,
    onOpenQuiz: (Long) -> Unit,
    onQuizLongClick: (QuizEntity) -> Unit
) {
    if (quizzes.isEmpty()) {
        EmptyTabContent("No quizzes found", "Quizzes created for this book will appear here.", Icons.Rounded.FactCheck)
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(quizzes, key = { it.id }) { quiz ->
                BookToolListItem(
                    title = quiz.title,
                    subtitle = quiz.description ?: "Quiz",
                    icon = Icons.Rounded.FactCheck,
                    onClick = { onOpenQuiz(quiz.id) },
                    onDelete = { onQuizLongClick(quiz) }
                )
            }
        }
    }
}

@Composable
fun SlidesTab(
    courses: List<SlideshowCourseEntity>,
    onOpenCourse: (Long) -> Unit,
    onEdit: (SlideshowCourseEntity) -> Unit,
    onDelete: (SlideshowCourseEntity) -> Unit
) {
    if (courses.isEmpty()) {
        EmptyTabContent(stringResource(R.string.no_study_slides), stringResource(R.string.no_study_slides_body), Icons.Rounded.Slideshow)
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(courses, key = { it.id }) { course ->
                BookToolListItem(
                    title = course.title,
                    subtitle = course.description ?: "${course.slideCount} slides",
                    icon = Icons.Rounded.Slideshow,
                    onClick = { onOpenCourse(course.id) },
                    onEdit = { onEdit(course) },
                    onDelete = { onDelete(course) }
                )
            }
        }
    }
}

@Composable
fun NotesTab(
    notes: List<NoteBlueprintEntity>,
    onOpenNote: (Long) -> Unit,
    onEdit: (NoteBlueprintEntity) -> Unit,
    onDelete: (NoteBlueprintEntity) -> Unit
) {
    if (notes.isEmpty()) {
        EmptyTabContent("No articles or notes", "Articles and short notes for quick review.", Icons.Rounded.NoteAlt)
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(notes, key = { it.id }) { note ->
                BookToolListItem(
                    title = note.title,
                    subtitle = "${note.blueprintMode} - ${note.reviewStatus}",
                    icon = Icons.Rounded.NoteAlt,
                    onClick = { onOpenNote(note.id) },
                    onEdit = { onEdit(note) },
                    onDelete = { onDelete(note) }
                )
            }
        }
    }
}

@Composable
fun MistakesTab(
    mistakes: List<MistakeLogEntryEntity>,
    onToggleFixed: (Long) -> Unit,
    onDelete: (MistakeLogEntryEntity) -> Unit
) {
    if (mistakes.isEmpty()) {
        EmptyTabContent("No mistakes logged", "Mistakes from your quizzes will show up here.", Icons.Rounded.Error)
    } else {
        val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(mistakes, key = { it.id }) { mistake ->
                MistakeCard(
                    mistake = mistake,
                    dateFormat = dateFormat,
                    onToggleFixed = { onToggleFixed(mistake.id) },
                    onSnooze = { /* TODO if needed */ },
                    onDelete = { onDelete(mistake) }
                )
            }
        }
    }
}

@Composable
fun FlashcardsTab(
    decks: List<FlashcardDeckEntity>,
    onOpenDeck: (Long) -> Unit,
    onDelete: (FlashcardDeckEntity) -> Unit
) {
    if (decks.isEmpty()) {
        EmptyTabContent("No flashcards found", "Memorize key concepts with decks.", Icons.Rounded.ViewCarousel)
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(decks, key = { it.id }) { deck ->
                BookToolListItem(
                    title = deck.title,
                    subtitle = deck.description ?: "Flashcard deck",
                    icon = Icons.Rounded.ViewCarousel,
                    onClick = { onOpenDeck(deck.id) },
                    onDelete = { onDelete(deck) }
                )
            }
        }
    }
}

@Composable
fun PromptsTab(
    decks: List<PromptDeckEntity>,
    onOpenDeck: (Long) -> Unit,
    onEdit: (PromptDeckEntity) -> Unit,
    onDelete: (PromptDeckEntity) -> Unit
) {
    if (decks.isEmpty()) {
        EmptyTabContent("No prompt decks", "Reusable AI prompt templates.", Icons.Rounded.Psychology)
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(decks, key = { it.id }) { deck ->
                BookToolListItem(
                    title = deck.title,
                    subtitle = deck.description ?: "Prompt deck",
                    icon = Icons.Rounded.Psychology,
                    onClick = { onOpenDeck(deck.id) },
                    onEdit = { onEdit(deck) },
                    onDelete = { onDelete(deck) }
                )
            }
        }
    }
}

@Composable
fun SourcesTab(
    sources: List<SourceDocumentEntity>,
    onEdit: (SourceDocumentEntity) -> Unit,
    onDelete: (SourceDocumentEntity) -> Unit
) {
    if (sources.isEmpty()) {
        EmptyTabContent("No sources added", "Textbooks and guideline documents.", Icons.Rounded.Source)
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sources, key = { it.id }) { source ->
                BookToolListItem(
                    title = source.title,
                    subtitle = "${source.sourceType} - ${source.description.orEmpty()}",
                    icon = Icons.Rounded.Source,
                    onClick = { /* View source? */ },
                    onEdit = { onEdit(source) },
                    onDelete = { onDelete(source) }
                )
            }
        }
    }
}

@Composable
private fun EmptyTabContent(title: String, body: String, icon: ImageVector) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
        Spacer(Modifier.height(16.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(body, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
