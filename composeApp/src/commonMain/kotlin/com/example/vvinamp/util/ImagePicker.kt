package com.example.vvinamp.util

import androidx.compose.runtime.Composable

@Composable
expect fun rememberImagePickerLauncher(onImagePicked: (String) -> Unit): ImagePickerLauncher

interface ImagePickerLauncher {
    fun launch()
}
