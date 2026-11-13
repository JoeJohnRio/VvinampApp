package com.example.vvinamp.util

import androidx.compose.ui.graphics.Color
import kotlin.math.abs

private const val HUE_BUCKETS = 12

// Targets of Palette's dark muted swatch, which the Android player uses.
private const val DARK_MUTED_LIGHTNESS = 0.26f
private const val DARK_MUTED_MAX_SATURATION = 0.4f

/**
 * Picks the player background colour from album cover pixels on platforms without Android's Palette.
 * Uses the same filter as the Android version (no near-black, near-white or grey pixels), takes the
 * most common hue, then turns it into a dark muted colour. Falls back to dark grey like Android does.
 */
fun pickDarkMutedColor(argbPixels: IntArray): Color {
    val red = FloatArray(HUE_BUCKETS)
    val green = FloatArray(HUE_BUCKETS)
    val blue = FloatArray(HUE_BUCKETS)
    val counts = IntArray(HUE_BUCKETS)

    for (argb in argbPixels) {
        if ((argb ushr 24) < 128) continue
        val r = (argb shr 16 and 0xFF) / 255f
        val g = (argb shr 8 and 0xFF) / 255f
        val b = (argb and 0xFF) / 255f
        val (hue, saturation, lightness) = rgbToHsl(r, g, b)
        if (lightness <= 0.15f || lightness >= 0.85f || saturation <= 0.10f) continue

        val bucket = (hue / 360f * HUE_BUCKETS).toInt().coerceIn(0, HUE_BUCKETS - 1)
        red[bucket] += r
        green[bucket] += g
        blue[bucket] += b
        counts[bucket]++
    }

    val best = counts.indices.maxBy { counts[it] }
    val count = counts[best]
    if (count == 0) return Color.DarkGray

    val (hue, saturation, _) = rgbToHsl(red[best] / count, green[best] / count, blue[best] / count)
    return Color.hsl(
        hue = hue,
        saturation = saturation.coerceAtMost(DARK_MUTED_MAX_SATURATION),
        lightness = DARK_MUTED_LIGHTNESS,
    )
}

private fun rgbToHsl(r: Float, g: Float, b: Float): FloatArray {
    val max = maxOf(r, g, b)
    val min = minOf(r, g, b)
    val lightness = (max + min) / 2f
    val delta = max - min
    if (delta == 0f) return floatArrayOf(0f, 0f, lightness)

    val saturation = (delta / (1f - abs(2f * lightness - 1f))).coerceIn(0f, 1f)
    val hue = when (max) {
        r -> ((g - b) / delta).mod(6f)
        g -> (b - r) / delta + 2f
        else -> (r - g) / delta + 4f
    } * 60f
    return floatArrayOf(hue.coerceIn(0f, 359.99f), saturation, lightness)
}
