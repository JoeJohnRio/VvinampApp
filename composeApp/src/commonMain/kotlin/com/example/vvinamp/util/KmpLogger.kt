package com.example.vvinamp.util
import com.example.vvinamp.util.showToast

expect object KmpLogger {
    fun d(tag: String, message: String)
}

expect fun showToast(message: String)
