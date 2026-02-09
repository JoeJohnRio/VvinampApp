package com.example.presentation.util

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
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
            blendMode = BlendMode.Color  // Changed from BlendMode.Color
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