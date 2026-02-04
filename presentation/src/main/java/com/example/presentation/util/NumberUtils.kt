package com.example.presentation.util

fun Int.addCommas(): String {
    return "%,d".format(this)
}