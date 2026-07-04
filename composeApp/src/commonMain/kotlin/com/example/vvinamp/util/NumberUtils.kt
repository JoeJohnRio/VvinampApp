package com.example.vvinamp.util

fun Int.addCommas(): String {
    return "%,d".format(this)
}

fun Int.toMinutes(): Int {
    return Math.floorDiv(this, 60)
}

fun Int?.orZero(): Int = this ?: 0