package com.ahmedyejam.mks.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmedyejam.mks.ui.theme.LocalMksDesignTokens

/**
 * Shared study-player chrome.
 *
 * Every player in MKS (flashcards, slideshow, notes, prompt decks) previously invented its own
 * progress indicator, its own control layout, and its own button styling. The components in this
 * file are the single vocabulary all of them now share:
 *
 * - [StudyPlayerScaffold] — the vertical structure: progress header, content, control bar.
 * - [StudyProgressHeader] — position readout + progress bar + optional trailing slot (e.g. timer).
 * - [StudyControlBar]     — previous / primary / next, with consistent weight and touch targets.
 * - [StudyStepDots]       — compact per-item indicator for short decks.
 * - [FlipSurface]         — the 3D flip used by flashcards.
 *
 * All of them read `MaterialTheme.colorScheme` and `LocalMksDesignTokens` only — no literal colors —
 * so they adapt to all 7 themes and flatten correctly when `tokens.isPlain` is set.
 */

/** Minimum touch target for any player control. Below this, controls fail accessibility guidance. */
private val MinTouchTarget = 48.dp

/** Fixed height for the control bar so the content above never resizes between states. */
private val ControlBarHeight = 72.dp

@Composable
fun StudyPlayerScaffold(
    position: Int,
    total: Int,
    modifier: Modifier = Modifier,
    progressLabel: String? = null,
    trailing: @Composable (() -> Unit)? = null,
    controls: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        StudyProgressHeader(
            position = position,
            total = total,
            progressLabel = progressLabel,
            trailing = trailing
        )
        content()
        controls()
    }
}

@Composable
fun StudyProgressHeader(
    position: Int,
    total: Int,
    modifier: Modifier = Modifier,
    progressLabel: String? = null,
    trailing: @Composable (() -> Unit)? = null
) {
    val tokens = LocalMksDesignTokens.current
    val safeTotal = total.coerceAtLeast(1)
    val target = ((position + 1).toFloat() / safeTotal).coerceIn(0f, 1f)
    // Animate so the bar reads as motion between items rather than a jump.
    val progress by animateFloatAsState(
        targetValue = target,
        animationSpec = tokens.animationSpec(300),
        label = "studyProgress"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = tokens.pagePadding, vertical = tokens.compactGap),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = progressLabel ?: "${position + 1} / $safeTotal",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.weight(1f))
            trailing?.invoke()
        }
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

/**
 * The control bar every player shares.
 *
 * [primary] occupies the centre and carries the screen's main verb (Flip, Mark studied, Run).
 * Previous/next flank it and keep a fixed footprint, so swapping the primary action never shifts
 * the navigation controls under the user's thumb.
 */
@Composable
fun StudyControlBar(
    onPrevious: (() -> Unit)?,
    onNext: (() -> Unit)?,
    previousLabel: String,
    nextLabel: String,
    modifier: Modifier = Modifier,
    previousEnabled: Boolean = true,
    nextEnabled: Boolean = true,
    primary: (@Composable () -> Unit)? = null
) {
    val tokens = LocalMksDesignTokens.current
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = if (tokens.isPlain) 0.dp else 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .heightIn(min = ControlBarHeight)
                .padding(horizontal = tokens.pagePadding, vertical = tokens.compactGap),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(tokens.compactGap)
        ) {
            StudyNavButton(
                onClick = onPrevious,
                enabled = previousEnabled && onPrevious != null,
                label = previousLabel,
                icon = true
            )
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                primary?.invoke()
            }
            StudyNavButton(
                onClick = onNext,
                enabled = nextEnabled && onNext != null,
                label = nextLabel,
                icon = false
            )
        }
    }
}

/**
 * Icon-only previous/next control.
 *
 * Icon-only keeps the footprint fixed regardless of translated label length — the Arabic labels are
 * materially longer than the English ones, and a text button here wrapped at large font scales.
 * The label is still exposed to accessibility services via [contentDescription].
 */
@Composable
private fun StudyNavButton(
    onClick: (() -> Unit)?,
    enabled: Boolean,
    label: String,
    icon: Boolean
) {
    OutlinedButton(
        onClick = { onClick?.invoke() },
        enabled = enabled,
        modifier = Modifier
            .size(MinTouchTarget)
            .semantics { contentDescription = label },
        shape = CircleShape,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
    ) {
        Icon(
            imageVector = if (icon) Icons.AutoMirrored.Filled.ArrowBack else Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null
        )
    }
}

/**
 * Compact per-item indicator. Only rendered for short sequences — past [maxDots] the dots stop
 * being readable and the progress bar in the header carries the information instead.
 */
@Composable
fun StudyStepDots(
    total: Int,
    currentIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    completed: (Int) -> Boolean = { false },
    maxDots: Int = 20
) {
    if (total <= 1 || total > maxDots) return
    val tokens = LocalMksDesignTokens.current
    val indices = remember(total) { (0 until total).toList() }
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = tokens.compactGap),
        horizontalArrangement = Arrangement.Center
    ) {
        items(indices) { index ->
            val isActive = index == currentIndex
            Box(
                Modifier
                    .padding(horizontal = 3.dp)
                    .size(if (isActive) 10.dp else 7.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isActive -> MaterialTheme.colorScheme.primary
                            completed(index) -> tokens.success
                            else -> MaterialTheme.colorScheme.outlineVariant
                        }
                    )
                    .clickable { onSelect(index) }
            )
        }
    }
}

/**
 * Two-faced surface with a 3D flip.
 *
 * Rotation is applied to the container while the visible face swaps at the 90° midpoint, so the
 * back face is never rendered mirrored. On Plain themes `tokens.animationSpec` collapses to `snap()`
 * and this degrades to an instant swap, which is the intended behaviour there.
 */
@Composable
fun FlipSurface(
    isFlipped: Boolean,
    onFlip: () -> Unit,
    modifier: Modifier = Modifier,
    flipDescription: String,
    front: @Composable () -> Unit,
    back: @Composable () -> Unit
) {
    val tokens = LocalMksDesignTokens.current
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tokens.animationSpec(450),
        label = "cardFlip"
    )
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = modifier
            .graphicsLayer {
                rotationY = rotation
                // Keeps the card from looking like it is punching through the screen.
                cameraDistance = 12f * density
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onFlip
            )
            .semantics { contentDescription = flipDescription },
        shape = RoundedCornerShape(tokens.cardRadius),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        tonalElevation = if (tokens.isPlain) 0.dp else 3.dp
    ) {
        Box(
            Modifier
                .fillMaxSize()
                // Counter-rotate the back face so its content reads normally.
                .graphicsLayer { rotationY = if (rotation > 90f) 180f else 0f }
        ) {
            if (rotation <= 90f) front() else back()
        }
    }
}

/**
 * Label identifying which face of a card is showing. Short text alone is ambiguous — "Paris" could
 * plausibly be a prompt or an answer — so the face is always named.
 */
@Composable
fun StudyFaceLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}

/**
 * Self-assessment rating row.
 *
 * The three ratings are visually ranked — the colour carries the meaning, so the buttons are
 * distinguishable without reading them. Previously all three were styled alike and the row said
 * nothing about what each choice meant.
 */
@Composable
fun StudyRatingBar(
    againLabel: String,
    goodLabel: String,
    easyLabel: String,
    onAgain: () -> Unit,
    onGood: () -> Unit,
    onEasy: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tokens = LocalMksDesignTokens.current
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(tokens.compactGap)
    ) {
        RatingButton(againLabel, tokens.wrong, onAgain, Modifier.weight(1f))
        RatingButton(goodLabel, tokens.warning, onGood, Modifier.weight(1f))
        RatingButton(easyLabel, tokens.success, onEasy, Modifier.weight(1f))
    }
}

@Composable
private fun RatingButton(
    label: String,
    accent: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.heightIn(min = MinTouchTarget),
        colors = ButtonDefaults.buttonColors(
            containerColor = accent.copy(alpha = 0.16f),
            contentColor = accent
        ),
        shape = RoundedCornerShape(LocalMksDesignTokens.current.chipRadius)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * Fixed-height slot for the primary action.
 *
 * Players swap what sits here as state changes (flashcards go from "Flip" to a rating row). Pinning
 * the height means the card above never resizes mid-interaction — the layout jump on every flip was
 * one of the most visible defects in the old flashcard player.
 */
@Composable
fun <T> StudyPrimarySlot(
    stateKey: T,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit
) {
    val tokens = LocalMksDesignTokens.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = MinTouchTarget)
            .padding(horizontal = tokens.pagePadding),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = stateKey,
            transitionSpec = {
                fadeIn(tokens.animationSpec(180)) togetherWith fadeOut(tokens.animationSpec(120))
            },
            label = "primaryAction"
        ) { key -> content(key) }
    }
}

/** Centred empty state for a player with nothing to show. */
@Composable
fun StudyEmptyState(
    title: String,
    body: String? = null,
    modifier: Modifier = Modifier
) {
    val tokens = LocalMksDesignTokens.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(tokens.pagePadding),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(tokens.compactGap)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            if (body != null) {
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
