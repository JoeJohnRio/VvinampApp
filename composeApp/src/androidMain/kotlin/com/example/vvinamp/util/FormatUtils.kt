package com.example.vvinamp.util

import android.icu.text.CompactDecimalFormat
import java.util.Locale

actual object FormatUtils {
    actual fun formatCompact(count: Long): String {
        val formatter = CompactDecimalFormat.getInstance(Locale.US, CompactDecimalFormat.CompactStyle.SHORT)
        return formatter.format(count)
    }
}
