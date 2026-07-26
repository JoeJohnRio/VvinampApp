package com.example.vvinamp.ui.home.library.widget.image
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import coil3.compose.AsyncImage
import com.example.vvinamp.util.preview.previewSafePainter


@Composable
fun CoverImage(
    model: Any?,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = model,
        contentDescription = null,
        placeholder = previewSafePainter(Res.drawable.bg_image),
        error = previewSafePainter(Res.drawable.bg_image),
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}