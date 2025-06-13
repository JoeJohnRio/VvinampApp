package com.example.youtubemusicclone.util.preview

import androidx.compose.runtime.Composable
import com.example.youtubemusicclone.ui.theme.AppTheme

@Composable
fun PreviewContainer(
    content: @Composable () -> Unit
) {
    AppTheme {
        content()
    }
}