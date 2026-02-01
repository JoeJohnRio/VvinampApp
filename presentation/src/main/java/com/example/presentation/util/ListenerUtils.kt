package com.example.presentation.util

import android.icu.text.CompactDecimalFormat
import java.util.Locale

object ListenerUtils {
    /**
     * Converts a number to a compact string (e.g., 1.5M, 200K)
     */
    fun formatCompact(count: Long, locale: Locale = Locale.US): String {
        val formatter = CompactDecimalFormat.getInstance(locale, CompactDecimalFormat.CompactStyle.SHORT)
        return formatter.format(count)
    }
}