package com.example.vvinamp.util

import android.icu.text.CompactDecimalFormat
import java.util.Locale

actual object ListenerUtils {

    private val formatter by lazy {
        CompactDecimalFormat.getInstance(
            Locale.getDefault(),
            CompactDecimalFormat.CompactStyle.SHORT
        )
    }

    actual fun formatCompact(count: Long): String =
        formatter.format(count)
}