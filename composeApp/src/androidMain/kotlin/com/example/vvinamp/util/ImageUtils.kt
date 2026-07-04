package com.example.vvinamp.util

import android.content.Context
import android.net.Uri
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AndroidImageUtils : KoinComponent {
    val context: Context by inject()
    
    suspend fun readBytes(uri: String): ByteArray {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(Uri.parse(uri))
        val bytes = inputStream?.readBytes() ?: throw Exception("Failed to read image")
        inputStream.close()
        return bytes
    }
}

actual suspend fun readBytesFromUri(uri: String): ByteArray {
    return AndroidImageUtils().readBytes(uri)
}
