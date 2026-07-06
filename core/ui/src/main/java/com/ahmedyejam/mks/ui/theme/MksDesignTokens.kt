package com.ahmedyejam.mks.ui.theme

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class MksDesignTokens(
    val success: Color,
    val warning: Color,
    val selected: Color,
    val correct: Color,
    val wrong: Color,
    val cardRadius: Dp = 18.dp,
    val chipRadius: Dp = 14.dp,
    val pagePadding: Dp = 16.dp,
    val compactGap: Dp = 8.dp,
    val relaxedGap: Dp = 16.dp,
    val isPlain: Boolean = false,
    val themeName: String = "FOREST",
    val cardElevation: Dp = 2.dp,
    val useGradients: Boolean = true,
    val glassAlpha: Float = 0.70f,
    val glassAlphaHeavy: Float = 0.85f,
    val glassAlphaLight: Float = 0.40f
) {
    fun <T> animationSpec(durationMillis: Int = 300): FiniteAnimationSpec<T> {
        return if (isPlain) snap() else tween(durationMillis)
    }
}

val LocalMksDesignTokens = staticCompositionLocalOf {
    MksDesignTokens(
        success = Color(0xFF3C7A54),
        warning = Color(0xFFC47A1C),
        selected = Color(0xFFD65A2C),
        correct = Color(0xFF2E7D32),
        wrong = Color(0xFFBA1A1A)
    )
}

@androidx.compose.runtime.Composable
fun androidx.compose.ui.Modifier.premiumGlassBackground(
    colorScheme: androidx.compose.material3.ColorScheme = androidx.compose.material3.MaterialTheme.colorScheme,
    tokens: MksDesignTokens = LocalMksDesignTokens.current,
    baseAlpha: Float = tokens.glassAlpha,
    cornerRadius: Dp = tokens.cardRadius
): androidx.compose.ui.Modifier {
    return if (tokens.useGradients) {
        this.background(
            brush = androidx.compose.ui.graphics.Brush.linearGradient(
                listOf(
                    colorScheme.surface.copy(alpha = baseAlpha + 0.15f),
                    colorScheme.surface.copy(alpha = baseAlpha),
                    colorScheme.primaryContainer.copy(alpha = baseAlpha - 0.20f)
                )
            ),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(cornerRadius)
        )
    } else {
        this.background(
            color = colorScheme.surface.copy(alpha = baseAlpha),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(cornerRadius)
        )
    }
}
