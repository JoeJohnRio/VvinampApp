package com.example.vvinamp.ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun Loader() {
    CircularProgressIndicator(color = Color.LightGray)
}

@Composable
fun LoaderFullScreen(
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center
) {
    Box(
        modifier.fillMaxSize(),
        contentAlignment = alignment
    ) {
        Loader()
    }
}

@Preview
@Composable
private fun LoaderPreview() {
    Surface {
        Loader()
    }
}