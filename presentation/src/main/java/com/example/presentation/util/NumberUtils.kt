package com.example.presentation.util

fun Int.addCommas(): String {
    return "%,d".format(this)
}

fun Int.toMinutes(): Int {
    return Math.floorDiv(this, 60)
}