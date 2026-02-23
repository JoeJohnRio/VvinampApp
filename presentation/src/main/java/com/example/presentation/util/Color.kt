package com.example.presentation.util

import android.util.Log
import android.util.LruCache
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette
import coil3.BitmapImage
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val dominantColorCache = LruCache<String, Int>(100)

fun getMiddleColor(
    firstColor: Color,
    secondColor: Color
): Color {
    return Color(ColorUtils.blendARGB(firstColor.toArgb(), secondColor.toArgb(), 0.5f))
}

@Composable
fun ExtractDominantColorEffect(
    imageUrl: String?,
    onColorExtracted: (Color) -> Unit // Callback to update uiState
) {
    val context = LocalContext.current

    LaunchedEffect(imageUrl) {
        if (imageUrl.isNullOrBlank()) return@LaunchedEffect

        // 1. Check cache first
        val cachedColor = dominantColorCache.get(imageUrl)
        if (cachedColor != null) {
            onColorExtracted(Color(cachedColor))
            return@LaunchedEffect
        }

        // 2. Fetch and calculate
        withContext(Dispatchers.IO) {
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .size(128)
                .allowHardware(false)
                .build()

            val result = context.imageLoader.execute(request)
            if (result is SuccessResult) {
                val bitmap = (result.image as? BitmapImage)?.bitmap
                Log.d("joel123", "bitmap $bitmap")
                bitmap?.let {
                    val palette = Palette
                        .from(it)
                        .maximumColorCount(24) // Increase color count for better extraction
                        .addFilter { _, hsl ->
                            val isBlack = hsl[2] <= 0.15f // Reject dark colors (Lightness < 15%)
                            val isWhite = hsl[2] >= 0.85f // Reject light colors (Lightness > 85%)
                            val isGrayscale =
                                hsl[1] <= 0.10f // Reject dull/gray colors (Saturation < 10%)

                            // Only allow colors that are NOT black, white, or grayscale
                            !isBlack && !isWhite && !isGrayscale
                        }.generate()
                    val extractedColorInt = palette.getDarkMutedColor(Color.DarkGray.toArgb()) // Always use a dark fallback

                    dominantColorCache.put(imageUrl, extractedColorInt)

                    // Push the color back to the main thread / ViewModel
                    withContext(Dispatchers.Main) {
                        Log.d("joel123", "extracted $extractedColorInt")
                        onColorExtracted(Color(extractedColorInt))
                    }
                }
            }
        }
    }
}
