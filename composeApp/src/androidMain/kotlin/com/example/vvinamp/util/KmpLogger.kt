package com.example.vvinamp.util

import android.util.Log
import android.widget.Toast
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import android.content.Context

actual object KmpLogger {
    actual fun d(tag: String, message: String) {
        Log.d(tag, message)
    }
}

class AndroidToastHelper : KoinComponent {
    val context: Context by inject()
    fun show(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

actual fun showToast(message: String) {
    AndroidToastHelper().show(message)
}
