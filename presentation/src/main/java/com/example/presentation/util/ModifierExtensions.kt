package com.example.presentation.util

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer

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