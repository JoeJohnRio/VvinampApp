package com.example.vvinamp.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeImageBitmap
import coil3.SingletonImageLoader
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.toBitmap

// A 64px copy of the cover has plenty of pixels to pick a colour from.
private const val COVER_SAMPLE_SIZE = 64

// shortcut: never evicts; a browser session only plays a few hundred covers. Swap for an LRU if that grows.
private val coverColorCache = mutableMapOf<String, Color>()

@Composable
actual fun ExtractDominantColorEffect(
    imageUrl: String?,
    onColorExtracted: (Color) -> Unit
) {
    val context = LocalPlatformContext.current
    val currentOnColorExtracted by rememberUpdatedState(onColorExtracted)

    LaunchedEffect(imageUrl) {
        if (imageUrl.isNullOrBlank()) return@LaunchedEffect

        val color = coverColorCache[imageUrl] ?: run {
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .size(COVER_SAMPLE_SIZE)
                .build()
            val result = SingletonImageLoader.get(context).execute(request) as? SuccessResult
                ?: return@LaunchedEffect

            val bitmap = result.image.toBitmap().asComposeImageBitmap()
            val pixels = IntArray(bitmap.width * bitmap.height)
            bitmap.readPixels(pixels)
            pickDarkMutedColor(pixels).also { coverColorCache[imageUrl] = it }
        }
        currentOnColorExtracted(color)
    }
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
