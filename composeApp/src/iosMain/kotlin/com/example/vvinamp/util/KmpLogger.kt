package com.example.vvinamp.util

actual object KmpLogger {
    actual fun d(tag: String, message: String) {
        println("[$tag] $message")
    }
}

actual fun showToast(message: String) {
    // TODO: Implement iOS Toast
    println("TOAST: $message")
}
