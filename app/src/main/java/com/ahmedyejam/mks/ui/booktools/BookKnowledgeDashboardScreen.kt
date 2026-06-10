package com.ahmedyejam.mks.ui.booktools

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmedyejam.mks.R
import com.ahmedyejam.mks.data.repository.BookKnowledgeSummary
import com.ahmedyejam.mks.ui.theme.LocalMksDesignTokens
import kotlinx.coroutines.launch
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookKnowledgeDashboardScreen(
    bookId: Long,
    viewModel: BookToolsViewModel,
    onBack: () -> Unit,
    onOpenQuiz: (Long) -> Unit,
    onOpenFlashcard: (Long) -> Unit,
    onOpenSlideshow: (Long) -> Unit,
    onOpenNote: (Long) -> Unit,
    onOpenPrompt: (Long) -> Unit,
    onOpenSource: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    
    val tabs = BookTab.entries.toTypedArray()
    val pagerState = rememberPagerState(initialPage = 2) { tabs.size }

    LaunchedEffect(bookId) {
        viewModel.loadBook(bookId)
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(uiState.book?.title ?: "Book") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                        }
                    }
                )
                ScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    edgePadding = 16.dp,
                    divider = {},
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                    indicator = { tabPositions ->
                        if (pagerState.currentPage < tabPositions.size) {
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                ) {
                    tabs.forEachIndexed { index, tab ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                            text = { Text(tab.title) },
                            icon = { Icon(tab.icon, null, Modifier.size(20.dp)) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize().padding(padding),
                beyondViewportPageCount = 1
            ) { page ->
                when (tabs[page]) {
                    BookTab.DASHBOARD -> DashboardTab(uiState.bookSummary, bookId, viewModel)
                    BookTab.SLIDES -> SlidesTab(uiState.allCourses, onOpenSlideshow, {}, {})
                    BookTab.QUIZZES -> QuizzesTab(uiState.quizzes, onOpenQuiz, {})
                    BookTab.NOTES -> NotesTab(uiState.allNotes, onOpenNote, {}, {})
                    BookTab.MISTAKES -> MistakesTab(uiState.mistakes, { viewModel.updateQuestionNote(it, "Marked as fixed") }, {}) // Placeholder logic
                    BookTab.FLASHCARDS -> FlashcardsTab(uiState.flashcardDecks, onOpenFlashcard, {})
                    BookTab.PROMPTS -> PromptsTab(uiState.allPromptDecks, onOpenPrompt, {}, {})
                    BookTab.SOURCES -> SourcesTab(uiState.allSources, {}, {})
                }
            }
        }
    }
}

@Composable
fun DashboardSummaryCard(summary: BookKnowledgeSummary?) {
    summary ?: return
    val tokens = LocalMksDesignTokens.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(tokens.cardRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Assessment, null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(8.dp))
                Text("Study Progress", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            
            val progress = if (summary.totalQuestions == 0) 0f else 
                (summary.totalQuestions - summary.unansweredQuestions).toFloat() / summary.totalQuestions
            
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Completion", style = MaterialTheme.typography.bodySmall)
                    Text("${(progress * 100).toInt()}%", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                }
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                )
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                SmallStat("Due", summary.reviewSchedulesDue.toString(), MaterialTheme.colorScheme.error)
                SmallStat("Weak", summary.weakQuestions.toString(), MaterialTheme.colorScheme.secondary)
                SmallStat("Marked", summary.markedQuestions.toString(), MaterialTheme.colorScheme.tertiary)
                SmallStat("Mistakes", summary.openMistakes.toString(), MaterialTheme.colorScheme.outline)
            }
        }
    }
}

@Composable
private fun SmallStat(label: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = color)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun MagicActionsSection(
    bookId: Long,
    summary: BookKnowledgeSummary?,
    viewModel: BookToolsViewModel
) {
    summary ?: return
    val uiState by viewModel.uiState.collectAsState()
    val questions = uiState.questions

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            "Magic Actions",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (summary.markedQuestions > 0) {
                MagicActionChip(
                    label = "Draft Note from Marked",
                    icon = Icons.Rounded.AutoFixHigh,
                    onClick = { viewModel.createBlueprintFromMarked(bookId, "Marked Review - ${System.currentTimeMillis() % 10000}") }
                )
                MagicActionChip(
                    label = "Cards from Marked",
                    icon = Icons.Rounded.ViewCarousel,
                    onClick = { viewModel.createFlashcardDeckFromMarkedQuestions(bookId, "Marked Cards - ${System.currentTimeMillis() % 10000}") }
                )
            }
            if (summary.openMistakes > 0) {
                MagicActionChip(
                    label = "Note from Mistakes",
                    icon = Icons.Rounded.HistoryEdu,
                    onClick = { viewModel.createBlueprintFromMissed(bookId, "Mistakes Summary - ${System.currentTimeMillis() % 10000}") }
                )
                MagicActionChip(
                    label = "Cards from Mistakes",
                    icon = Icons.Rounded.ViewCarousel,
                    onClick = { viewModel.createFlashcardDeckFromMissedQuestions(bookId, "Mistakes Cards - ${System.currentTimeMillis() % 10000}") }
                )
            }
            if (questions.isNotEmpty()) {
                MagicActionChip(
                    label = stringResource(R.string.generate_from_questions),
                    icon = Icons.Rounded.Slideshow,
                    onClick = { viewModel.createSlideshowCourseFromQuestions(bookId, "Study Slides - ${System.currentTimeMillis() % 10000}", questions.map { it.id }) }
                )
            }
        }
    }
}

@Composable
private fun MagicActionChip(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    FilterChip(
        selected = true,
        onClick = onClick,
        label = { Text(label) },
        leadingIcon = { Icon(icon, null, Modifier.size(18.dp)) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
            selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Composable
private fun ToolCard(
    title: String,
    count: Int,
    icon: ImageVector,
    onClick: () -> Unit
) {
    val tokens = LocalMksDesignTokens.current
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(120.dp),
        shape = RoundedCornerShape(tokens.cardRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(
            Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(8.dp))
            Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            Text("$count items", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
