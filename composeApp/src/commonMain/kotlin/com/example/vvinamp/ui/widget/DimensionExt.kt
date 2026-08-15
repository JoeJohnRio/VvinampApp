package com.example.vvinamp.ui.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.vvinamp.util.getScreenWidthDp

@Composable
fun Dp.toPX(): Int {
    val density = LocalDensity.current
    return with(density) { this@toPX.toPx().toInt() }
}

class ImageSize(
    val width: Dp,
    val height: Dp
) {
    companion object {
        @Composable
        fun getImageFixedSize(): ImageSize {
            val screenWidth = getScreenWidthDp()
            val aspectRatio = 19f / 6f
            val imageWidth = screenWidth / 3
            val imageHeight = imageWidth * aspectRatio
            return ImageSize(imageWidth, imageHeight)
        }

        @Composable
        fun getImageListeningHistorySize(): ImageSize {
            val imageWidth = 56.dp
            val imageHeight = 56.dp
            return ImageSize(imageWidth, imageHeight)
        }
    }
}