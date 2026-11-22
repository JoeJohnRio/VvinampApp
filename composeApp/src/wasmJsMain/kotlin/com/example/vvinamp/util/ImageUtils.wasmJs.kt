package com.example.vvinamp.util

import kotlinx.browser.window
import kotlinx.coroutines.await
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.khronos.webgl.get
import org.w3c.fetch.Response

actual suspend fun readBytesFromUri(uri: String): ByteArray {
    val response = window.fetch(uri).await<Response>()
    val buffer = response.arrayBuffer().await<ArrayBuffer>()
    val view = Int8Array(buffer)
    return ByteArray(view.length) { view[it] }
}
