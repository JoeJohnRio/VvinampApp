package com.example.vvinamp.util

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.verticalGradientEdge(isExpanded: Boolean, color: Color) =
    this
        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent()
            if (!isExpanded) {
                drawRect(
                    brush = Brush.verticalGradient(
                        0.7f to color, // Transparent
                        1.0f to Color.Transparent // Opaque
                    ),
                    blendMode = BlendMode.DstIn
                )
            }
        }

fun Modifier.verticalGradientEdge(color: Color) =
    this
        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent()
            drawRect(
                brush = Brush.verticalGradient(
                    0.1f to color, // Transparent
                    1.0f to Color.Transparent // Opaque
                ),
                blendMode = BlendMode.DstIn
            )

        }


fun Modifier.verticalGradientBottomNav(
    startColor: Color = Color.Transparent,
    midColor: Color,// Top color
    endColor: Color                          // Bottom color
) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(
            brush = Brush.verticalGradient(
                0.0f to startColor,
                0.7f to midColor,// Start at top (transparent)
                1f to endColor     // End at bottom (solid color)
            ),
            blendMode = BlendMode.Color
        )
    }

fun Modifier.fadingEdge(
    fadeWidth: Dp = 16.dp
): Modifier = this.then(
    Modifier.drawWithContent {
        val fadePx = fadeWidth.toPx()
        val width = size.width

        // Draw text first
        drawContent()

        // Apply alpha mask on top, but it only affects drawn content
        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(Color.Transparent, Color.Black),
                startX = 0f,
                endX = fadePx
            ),
            size = size,
            blendMode = BlendMode.DstIn
        )
        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(Color.Black, Color.Transparent),
                startX = width - fadePx,
                endX = width
            ),
            size = size,
            blendMode = BlendMode.DstIn
        )
    }
)

fun Modifier.shimmerEffect(): Modifier = composed {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.5f),
        Color.LightGray.copy(alpha = 0.2f)
    )

    val transition = rememberInfiniteTransition(label = "shimmer_transition")

    // Animate from 0 to 1000f over 1.2 seconds, repeating infinitely
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    // The brush uses the animated value to move the gradient diagonally
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim, y = translateAnim)
    )

    this.background(brush)
}
