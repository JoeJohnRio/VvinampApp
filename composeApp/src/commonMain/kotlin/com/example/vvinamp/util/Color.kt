package com.example.vvinamp.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
expect fun ExtractDominantColorEffect(
    imageUrl: String?,
    onColorExtracted: (Color) -> Unit
)

expect fun getMiddleColor(
    firstColor: Color,
    secondColor: Color
): Color
