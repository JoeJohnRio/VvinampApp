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

fun Int?.orZero(): Int = this ?: 0