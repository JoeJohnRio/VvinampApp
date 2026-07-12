package com.example.vvinamp.util

import org.jetbrains.compose.resources.DrawableResource

data class ButtonData(
    val iconRes: DrawableResource,
    val onClick: () -> Unit
)