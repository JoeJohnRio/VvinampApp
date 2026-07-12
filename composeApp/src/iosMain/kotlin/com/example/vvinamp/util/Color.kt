package com.example.vvinamp.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

actual fun getMiddleColor(
    firstColor: Color,
    secondColor: Color
): Color {
    // TODO: Implement iOS blend
    return firstColor
}

@Composable
actual fun ExtractDominantColorEffect(
    imageUrl: String?,
    onColorExtracted: (Color) -> Unit
) {
    // TODO: Implement iOS dominant color extraction
}
