package com.example.presentation.util.auth

fun isValidPassword(password: String): Boolean {
    val passwordRegex = Regex("^(?=.*[\\d\\W]).{10,}$")
    return passwordRegex.matches(password)
}