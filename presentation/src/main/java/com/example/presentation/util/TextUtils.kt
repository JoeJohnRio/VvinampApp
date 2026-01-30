package com.example.presentation.util

fun String?.orErrorNoText(): String {
    return if (this.isNullOrBlank()) "No text" else this
}