package com.example.vvinamp.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement

@Composable
actual fun rememberImagePickerLauncher(onImagePicked: (String) -> Unit): ImagePickerLauncher {
    return remember {
        object : ImagePickerLauncher {
            override fun launch() {
                val input = document.createElement("input") as HTMLInputElement
                input.type = "file"
                input.accept = "image/*"
                input.onchange = {
                    val file = input.files?.item(0)
                    if (file != null) {
                        onImagePicked("wasm-image-picked")
                    }
                    null
                }
                input.click()
            }
        }
    }
}
