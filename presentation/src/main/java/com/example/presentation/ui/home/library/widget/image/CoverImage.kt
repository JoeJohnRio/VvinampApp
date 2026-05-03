package com.example.presentation.ui.home.library.widget.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import com.example.presentation.R


@Composable
fun CoverImage(
    model: Any?,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = model,
        contentDescription = null,
        placeholder = painterResource(id = R.drawable.bg_image),
        error = painterResource(id = R.drawable.bg_image),
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}