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
    defaultColor: Color = Color.LightGray,
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
                    val palette = Palette.from(it).generate()
                    val extractedColorInt = palette.getDarkVibrantColor(defaultColor.toArgb())

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
