package com.example.presentation.util

fun String.checkPassword() : Boolean {
    var isPasswordValid = false

    if (this.length >= 10) {
        isPasswordValid = true
    }

    return isPasswordValid
}