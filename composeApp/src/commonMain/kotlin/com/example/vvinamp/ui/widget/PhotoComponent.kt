package com.example.vvinamp.ui.widget
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.unit.dp



@Composable
fun NoImagePlaceholder() {
    Image(
        painter = painterResource(Res.drawable.bg_image),
        contentDescription = "",
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun ImageError() {
    Image(
        painter = painterResource(Res.drawable.ic_image_not_supported_24),
        contentDescription = "Error loading image"
    )
    Text("Image failed to load", modifier = Modifier.padding(top = 6.dp))
}