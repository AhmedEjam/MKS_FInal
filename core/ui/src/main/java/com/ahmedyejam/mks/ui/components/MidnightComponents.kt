package com.ahmedyejam.mks.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ahmedyejam.mks.ui.theme.LocalMksDesignTokens
import com.ahmedyejam.mks.ui.theme.premiumGlassBackground

/**
 * Midnight Premium reusable components.
 *
 * These are theme-scoped by construction: every color comes from [MaterialTheme.colorScheme] or
 * [LocalMksDesignTokens], and every gradient/glow is gated on `tokens.useGradients` so the Plain
 * themes fall back to flat surfaces. See `Important docs/DESIGN.md` for the full spec.
 */

private fun Modifier.hairline(color: Color, radius: Dp): Modifier =
    this.border(BorderStroke(1.dp, color), RoundedCornerShape(radius))

/**
 * Glassy elevated surface — the workhorse card. Adds a hairline border and, when [selected],
 * a soft lavender (primary) glow ring. Falls back to a flat surface on Plain themes.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit
) {
    val tokens = LocalMksDesignTokens.current
    val scheme = MaterialTheme.colorScheme
    val radius = tokens.cardRadius
    val ringColor = if (selected) scheme.primary else scheme.outline
    val ringWidth = if (selected) 1.5.dp else 1.dp

    var base = Modifier
        .then(modifier)
        .clip(RoundedCornerShape(radius))

    base = if (tokens.useGradients) {
        base.premiumGlassBackground(colorScheme = scheme, tokens = tokens, cornerRadius = radius)
    } else {
        base.background(scheme.surface, RoundedCornerShape(radius))
    }

    base = base.border(BorderStroke(ringWidth, ringColor), RoundedCornerShape(radius))
    if (onClick != null) base = base.clickable(onClick = onClick)

    Column(modifier = base.padding(contentPadding), content = content)
}

/**
 * Primary action — lavender fill, dark text, optional leading icon. [glow] adds a faint colored
 * shadow-halo behind it (skipped on Plain themes).
 */
@Composable
fun PrimaryPillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true,
    glow: Boolean = true
) {
    val tokens = LocalMksDesignTokens.current
    val scheme = MaterialTheme.colorScheme
    val container = if (enabled) scheme.primary else scheme.primary.copy(alpha = 0.4f)

    Box(
        modifier = modifier
            .then(
                if (glow && tokens.useGradients && enabled) {
                    Modifier.background(
                        brush = Brush.radialGradient(
                            listOf(scheme.primary.copy(alpha = 0.25f), Color.Transparent)
                        ),
                        shape = CircleShape
                    )
                } else Modifier
            )
            .clip(CircleShape)
            .background(container, CircleShape)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                Icon(leadingIcon, contentDescription = null, tint = scheme.onPrimary, modifier = Modifier.size(18.dp))
            }
            Text(
                text = text,
                color = scheme.onPrimary,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/** Secondary action — transparent with a hairline border. */
@Composable
fun GhostPillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true
) {
    val scheme = MaterialTheme.colorScheme
    Box(
        modifier = modifier
            .clip(CircleShape)
            .border(BorderStroke(1.dp, scheme.outline), CircleShape)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                Icon(leadingIcon, contentDescription = null, tint = scheme.onSurface, modifier = Modifier.size(18.dp))
            }
            Text(text, color = scheme.onSurface, style = MaterialTheme.typography.labelLarge)
        }
    }
}

/**
 * Thin rounded progress bar with the lavender→orange gradient fill. [progress] in 0f..1f.
 * Plain themes render a flat primary fill.
 */
@Composable
fun GradientProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 6.dp,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    val tokens = LocalMksDesignTokens.current
    val scheme = MaterialTheme.colorScheme
    val clamped = progress.coerceIn(0f, 1f)
    val fillBrush = if (tokens.useGradients) {
        Brush.horizontalGradient(listOf(scheme.primary, scheme.secondary))
    } else {
        Brush.horizontalGradient(listOf(scheme.primary, scheme.primary))
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(CircleShape)
            .background(trackColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(clamped)
                .height(height)
                .clip(CircleShape)
                .background(fillBrush)
        )
    }
}

/**
 * Circular gradient progress ring with arbitrary center [content] (score %, "18/42 due today").
 * [progress] in 0f..1f.
 */
@Composable
fun ScoreRing(
    progress: Float,
    modifier: Modifier = Modifier,
    diameter: Dp = 160.dp,
    stroke: Dp = 12.dp,
    content: @Composable () -> Unit
) {
    val tokens = LocalMksDesignTokens.current
    val scheme = MaterialTheme.colorScheme
    val clamped = progress.coerceIn(0f, 1f)
    val trackColor = scheme.surfaceVariant
    val ringBrush = if (tokens.useGradients) {
        Brush.sweepGradient(listOf(scheme.primary, scheme.secondary, scheme.primary))
    } else {
        Brush.sweepGradient(listOf(scheme.primary, scheme.primary))
    }
    Box(modifier = modifier.size(diameter), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(diameter)) {
            val strokePx = stroke.toPx()
            drawArc(
                color = trackColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = androidx.compose.ui.geometry.Offset(strokePx / 2, strokePx / 2),
                size = androidx.compose.ui.geometry.Size(size.width - strokePx, size.height - strokePx),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokePx, cap = StrokeCap.Round)
            )
            drawArc(
                brush = ringBrush,
                startAngle = -90f,
                sweepAngle = 360f * clamped,
                useCenter = false,
                topLeft = androidx.compose.ui.geometry.Offset(strokePx / 2, strokePx / 2),
                size = androidx.compose.ui.geometry.Size(size.width - strokePx, size.height - strokePx),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokePx, cap = StrokeCap.Round)
            )
        }
        content()
    }
}

/** Translucent filter/category pill with a colored dot. [selected] gives a lavender fill. */
@Composable
fun CategoryChip(
    label: String,
    modifier: Modifier = Modifier,
    dotColor: Color? = null,
    selected: Boolean = false,
    trailingCount: Int? = null,
    onClick: (() -> Unit)? = null
) {
    val tokens = LocalMksDesignTokens.current
    val scheme = MaterialTheme.colorScheme
    val container = if (selected) scheme.primary else scheme.surfaceVariant.copy(alpha = 0.6f)
    val contentColor = if (selected) scheme.onPrimary else scheme.onSurface
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(tokens.chipRadius))
            .background(container, RoundedCornerShape(tokens.chipRadius))
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = 12.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (dotColor != null && !selected) {
            Box(Modifier.size(8.dp).clip(CircleShape).background(dotColor))
        }
        Text(label, color = contentColor, style = MaterialTheme.typography.labelMedium)
        if (trailingCount != null) {
            Text(
                trailingCount.toString(),
                color = contentColor.copy(alpha = 0.7f),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

/** Rounded leading icon tile tinted by a per-type [accent] (see DESIGN.md §2 accent map). */
@Composable
fun AccentIconTile(
    icon: ImageVector,
    accent: Color,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(12.dp))
            .background(accent.copy(alpha = 0.16f), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(size * 0.5f))
    }
}

/** Small count pill — due counts, "NEW", etc. */
@Composable
fun CountBadge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(color.copy(alpha = 0.18f), CircleShape)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(text, color = color, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
    }
}

/** Lavender→orange "AI" sparkle badge for AI features only. */
@Composable
fun AiBadge(
    modifier: Modifier = Modifier,
    label: String = "AI"
) {
    val tokens = LocalMksDesignTokens.current
    val scheme = MaterialTheme.colorScheme
    val brush = if (tokens.useGradients) {
        Brush.horizontalGradient(listOf(scheme.primary, scheme.secondary))
    } else {
        Brush.horizontalGradient(listOf(scheme.primary, scheme.primary))
    }
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(brush, CircleShape)
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text(label, color = scheme.onPrimary, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
    }
}

/** Uppercase muted group header with an optional trailing action (e.g. "Sort"). */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    trailing: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold
        )
        if (trailing != null) trailing()
    }
}

/** One spaced-repetition rating option. */
data class SrsRating(val label: String, val interval: String, val color: Color)

/**
 * The 4-button spaced-repetition rating row (Again / Hard / Good / Easy). Each pill shows its
 * label and next-due interval; [emphasizedIndex] gets a stronger fill (usually "Good").
 */
@Composable
fun SrsRatingRow(
    ratings: List<SrsRating>,
    onRate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    emphasizedIndex: Int = -1
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ratings.forEachIndexed { index, rating ->
            val emphasized = index == emphasizedIndex
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        rating.color.copy(alpha = if (emphasized) 0.22f else 0.12f),
                        RoundedCornerShape(14.dp)
                    )
                    .then(
                        if (emphasized) Modifier.hairline(rating.color, 14.dp) else Modifier
                    )
                    .clickable { onRate(index) }
                    .padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    rating.label,
                    color = rating.color,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    rating.interval,
                    color = rating.color.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}
