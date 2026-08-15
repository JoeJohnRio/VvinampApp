package com.example.vvinamp.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
actual fun ExtractDominantColorEffect(
    imageUrl: String?,
    onColorExtracted: (Color) -> Unit
) {
    // Placeholder for Wasm
}

actual fun getMiddleColor(
    firstColor: Color,
    secondColor: Color
): Color {
    return Color(
        red = (firstColor.red + secondColor.red) / 2f,
        green = (firstColor.green + secondColor.green) / 2f,
        blue = (firstColor.blue + secondColor.blue) / 2f,
        alpha = (firstColor.alpha + secondColor.alpha) / 2f
    )
}
