package com.example.vvinamp.util

actual object KmpLogger {
    actual fun d(tag: String, message: String) {
        println("$tag: $message")
    }
}

actual fun showToast(message: String) {
    // Standard JS alert for now, or just log
    println("Toast: $message")
}
