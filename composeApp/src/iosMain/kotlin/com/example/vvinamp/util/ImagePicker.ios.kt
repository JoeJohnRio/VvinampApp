package com.example.vvinamp.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.uikit.LocalUIViewController
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIImage
import platform.darwin.NSObject

@Composable
actual fun rememberImagePickerLauncher(onImagePicked: (String) -> Unit): ImagePickerLauncher {
    val viewController = LocalUIViewController.current
    return remember(viewController) {
        val delegate = object : NSObject(), PHPickerViewControllerDelegateProtocol {
            override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
                picker.dismissViewControllerAnimated(true, null)
                val result = didFinishPicking.firstOrNull() as? PHPickerResult
                // Simple notification that picking is done. 
                // In production, you'd extract the data here.
                if (result != null) {
                    onImagePicked("ios-image-picked")
                }
            }
        }

        object : ImagePickerLauncher {
            override fun launch() {
                val configuration = PHPickerConfiguration()
                configuration.filter = platform.PhotosUI.PHPickerFilter.imagesFilter
                configuration.selectionLimit = 1

                val picker = PHPickerViewController(configuration)
                picker.delegate = delegate
                viewController.presentViewController(picker, true, null)
            }
        }
    }
}
