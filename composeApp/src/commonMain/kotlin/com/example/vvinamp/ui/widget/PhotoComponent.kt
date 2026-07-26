package com.example.vvinamp.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.vvinamp.util.preview.previewSafePainter
import org.jetbrains.compose.resources.painterResource
import vvinampapp.composeapp.generated.resources.Res
import vvinampapp.composeapp.generated.resources.bg_image


@Composable
fun NoImagePlaceholder() {
    Image(
        painter = previewSafePainter(Res.drawable.bg_image),
        contentDescription = "",
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun ImageError() {
    Image(
        painter = previewSafePainter(Res.drawable.bg_image),
        contentDescription = "Error loading image"
    )
    Text("Image failed to load", modifier = Modifier.padding(top = 6.dp))
}