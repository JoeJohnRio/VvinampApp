package com.example.vvinamp.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.url.URL

@Composable
actual fun rememberImagePickerLauncher(onImagePicked: (String) -> Unit): ImagePickerLauncher {
    return remember {
        object : ImagePickerLauncher {
            override fun launch() {
                val input = document.createElement("input") as HTMLInputElement
                input.type = "file"
                input.accept = "image/jpeg,image/png,image/webp"
                input.onchange = {
                    val file = input.files?.item(0)
                    if (file != null) {
                        // A blob: URL points at the chosen file; readBytesFromUri fetches it.
                        // shortcut: the URL is never revoked, so each pick keeps its file in memory until the tab closes.
                        onImagePicked(URL.createObjectURL(file))
                    }
                    null
                }
                input.click()
            }
        }
    }
}
