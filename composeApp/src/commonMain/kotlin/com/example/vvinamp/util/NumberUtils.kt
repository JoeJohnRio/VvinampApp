package com.example.vvinamp.util

fun Int.addCommas(): String {
    val s = this.toString()
    val builder = StringBuilder()
    var count = 0
    for (i in s.length - 1 downTo 0) {
        builder.append(s[i])
        count++
        if (count == 3 && i > 0 && s[i - 1] != '-') {
            builder.append(',')
            count = 0
        }
    }
    return builder.reverse().toString()
}

fun Int.toMinutes(): Int {
    return this / 60
}

/** Turns a length in seconds into "1 hr 5 min", "5 min 23 sec" or "45 sec". */
fun Int.toDurationLabel(): String {
    val total = coerceAtLeast(0)
    val hours = total / 3600
    val minutes = total % 3600 / 60
    val seconds = total % 60
    return when {
        hours > 0 -> "$hours hr $minutes min"
        minutes > 0 -> "$minutes min $seconds sec"
        else -> "$seconds sec"
    }
}

fun Int?.orZero(): Int = this ?: 0