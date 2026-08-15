package com.example.vvinamp.util

actual object FormatUtils {
    actual fun formatCompact(count: Long): String {
        return when {
            count >= 1_000_000_000 -> "${(count / 1_000_000_000.0).toFixed(1)}B"
            count >= 1_000_000 -> "${(count / 1_000_000.0).toFixed(1)}M"
            count >= 1_000 -> "${(count / 1_000.0).toFixed(1)}K"
            else -> count.toString()
        }
    }
}

private fun Double.toFixed(digits: Int): String {
    val multiplier = 10.0.pow(digits)
    return (kotlin.math.round(this * multiplier) / multiplier).toString()
}

private fun Double.pow(n: Int): Double {
    var result = 1.0
    repeat(n) { result *= this }
    return result
}
