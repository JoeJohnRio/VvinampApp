package com.example.vvinamp.util

import kotlin.math.absoluteValue
actual object ListenerUtils {

    actual fun formatCompact(count: Long): String {
        val value = kotlin.math.abs(count.toDouble())
        val sign = if (count < 0) "-" else ""

        return when {
            value < 1_000 -> count.toString()
            value < 1_000_000 -> sign + compact(value / 1_000) + "K"
            value < 1_000_000_000 -> sign + compact(value / 1_000_000) + "M"
            value < 1_000_000_000_000 -> sign + compact(value / 1_000_000_000) + "B"
            else -> sign + compact(value / 1_000_000_000_000) + "T"
        }
    }

    private fun compact(value: Double): String {
        val rounded = (value * 10).toLong() / 10.0

        return if (rounded % 1.0 == 0.0) {
            rounded.toLong().toString()
        } else {
            rounded.toString()
        }
    }
}