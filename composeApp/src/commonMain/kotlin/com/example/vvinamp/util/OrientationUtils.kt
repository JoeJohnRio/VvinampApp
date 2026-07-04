package com.example.vvinamp.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun Modifier.lockOrientation(isLocked: Boolean): Modifier
