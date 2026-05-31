package com.ahmedyejam.mks.ui.review

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ahmedyejam.mks.data.review.ReviewQueueItem
import com.ahmedyejam.mks.ui.components.EmptyStateCard
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import com.ahmedyejam.mks.data.review.ReviewQueueType
import com.ahmedyejam.mks.ui.components.LoadingErrorState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewDashboardScreen(
    viewModel: ReviewDashboardViewModel,
    focusedMistakeId: Long? = null,
    onBack: () -> Unit,
    onOpenRoute: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(focusedMistakeId, state.queue) {
        if (focusedMistakeId != null && state.queue.isNotEmpty()) {
            val index = state.queue.indexOfFirst { it.id == focusedMistakeId.toString() && it.type == ReviewQueueType.MISTAKE }
            if (index != -1) {
                listState.animateScrollToItem(index + 1) // +1 for summary header
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Review Dashboard") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") } }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item { LoadingErrorState(state.isLoading, state.error, viewModel::refresh) }
            item { com.ahmedyejam.mks.ui.components.SummaryCard("Flashcards due", state.summary.dueFlashcards.toString(), "Cards scheduled for review") }
            item { com.ahmedyejam.mks.ui.components.SummaryCard("Blueprints due", state.summary.dueBlueprints.toString(), "Manual review queue") }
            item { com.ahmedyejam.mks.ui.components.SummaryCard("Mistakes due", state.summary.dueMistakes.toString(), "Open mistake reviews") }
            item { com.ahmedyejam.mks.ui.components.SummaryCard("Mistakes scheduled", state.summary.pendingMistakes.toString(), "Open mistakes not due yet") }
            item { com.ahmedyejam.mks.ui.components.SummaryCard("Marked / weak", "${state.summary.markedQuestions} / ${state.summary.weakQuestions}", "Marked and weak questions") }
            if (!state.isLoading && state.queue.isEmpty()) {
                item { EmptyStateCard("No due reviews", "There are no due review items right now.") }
            } else {
                items(state.queue, key = { it.type.name + it.id }) { item ->
                    ReviewQueueCard(item, viewModel::markReviewed, viewModel::snoozeOneWeek, onOpenRoute)
                }
            }
        }
    }
}

@Composable
private fun ReviewQueueCard(
    item: ReviewQueueItem,
    onMarkReviewed: (ReviewQueueItem) -> Unit,
    onSnooze: (ReviewQueueItem) -> Unit,
    onOpenRoute: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = item.route != null) { item.route?.let(onOpenRoute) }
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(item.type.name.replace('_', ' '), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            Text(item.title, style = MaterialTheme.typography.titleSmall)
            if (!item.subtitle.isNullOrBlank()) Text(item.subtitle, style = MaterialTheme.typography.bodySmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = { onMarkReviewed(item) }) { Text("Reviewed") }
                TextButton(onClick = { onSnooze(item) }) { Text("Snooze 1 week") }
            }
        }
    }
}
