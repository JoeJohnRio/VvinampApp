package com.example.vvinamp.ui.home.library.widget.image
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import coil3.compose.AsyncImage



@Composable
fun CoverImage(
    model: Any?,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = model,
        contentDescription = null,
        placeholder = painterResource(id = Res.drawable.bg_image),
        error = painterResource(id = Res.drawable.bg_image),
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}