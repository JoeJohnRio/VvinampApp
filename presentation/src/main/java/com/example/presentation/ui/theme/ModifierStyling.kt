package com.example.presentation.ui.theme

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush

fun Modifier.normalGradientBackground(): Modifier = this.background(
    brush = Brush.verticalGradient(
        colors = listOf(AppColours.GradientTop, AppColours.GradientBottom)
    )
)

fun Modifier.whiteBlackGradientBackground(): Modifier = this.background(
    brush = Brush.verticalGradient(
        colors = listOf(
            AppColours.Black300,
            AppColours.Black200,
            AppColours.Black150,
            AppColours.Black100
        )
    )
)


