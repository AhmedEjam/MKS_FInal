package com.ahmedyejam.mks.ui.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.ahmedyejam.mks.data.preferences.DataStoreManager
import com.ahmedyejam.mks.ui.session.SessionManagementScreen
import com.ahmedyejam.mks.ui.session.SessionViewModel
import kotlinx.coroutines.launch

enum class QuizTab(val title: String, val icon: ImageVector) {
    SESSIONS("Sessions", Icons.Rounded.History),
    BROWSE("Browse", Icons.Rounded.List)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizDetailTabsScreen(
    quizId: Long,
    quizTitle: String?,
    sessionViewModel: SessionViewModel,
    questionsViewModel: QuizQuestionsViewModel,
    dataStoreManager: DataStoreManager,
    onSessionSelected: (Long, Boolean) -> Unit,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val tabs = QuizTab.entries.toTypedArray()
    val pagerState = rememberPagerState(initialPage = 0) { tabs.size }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(quizTitle ?: "Quiz Details") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                        }
                    }
                )
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
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
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize().padding(padding),
            beyondViewportPageCount = 1
        ) { page ->
            when (tabs[page]) {
                QuizTab.SESSIONS -> {
                    SessionManagementScreen(
                        quizId = quizId,
                        viewModel = sessionViewModel,
                        dataStoreManager = dataStoreManager,
                        isEmbedded = true,
                        onSessionSelected = onSessionSelected,
                        onNavigateBack = onBack
                    )
                }
                QuizTab.BROWSE -> {
                    QuizQuestionsScreen(
                        viewModel = questionsViewModel,
                        isEmbedded = true,
                        onBack = onBack
                    )
                }
            }
        }
    }
}
