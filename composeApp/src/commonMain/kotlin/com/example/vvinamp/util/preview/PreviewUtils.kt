package com.example.vvinamp.util.preview

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalInspectionMode
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

// Utility to safely handle resources in Previews where Compose Multiplatform Resources might fail to load
@Composable
fun previewSafePainter(resource: DrawableResource): Painter {
    return if (LocalInspectionMode.current) {
        rememberVectorPainter(Icons.Default.Image)
    } else {
        painterResource(resource)
    }
}

@Composable
fun previewSafeString(resource: StringResource, fallback: String): String {
    return if (LocalInspectionMode.current) {
        fallback
    } else {
        stringResource(resource)
    }
}
