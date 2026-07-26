package com.example.vvinamp.util.preview

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalInspectionMode
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

//not really needed anymore, fixed
@Composable
fun previewSafePainter(resource: DrawableResource): Painter {
    return if (LocalInspectionMode.current) {
        rememberVectorPainter(Icons.Default.Image)
    } else {
        previewSafePainter(resource)
    }
}
