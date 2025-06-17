package com.example.presentation.util.preview

import androidx.compose.runtime.Composable
import com.example.presentation.ui.theme.AppTheme

@Composable
fun PreviewContainer(
    content: @Composable () -> Unit
) {
    AppTheme {
        content()
    }
}