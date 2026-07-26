package com.example.vvinamp.util

import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.DrawableResource

data class ButtonData(
    val iconRes: DrawableResource? = null,
    val imageVector: ImageVector? = null,
    val onClick: () -> Unit
)