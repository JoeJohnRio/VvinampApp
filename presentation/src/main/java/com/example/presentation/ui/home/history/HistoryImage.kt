package com.example.presentation.ui.home.history

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.size.Scale
import com.example.presentation.ui.widget.ImageSize
import com.example.presentation.ui.widget.NoImagePlaceholder
import com.example.presentation.ui.widget.toPX

@Composable
fun HistoryImage(
    imageUrl: String,
    imageSize: ImageSize
) {
    var scale by remember { mutableFloatStateOf(0.70f) }
    val animatedScale by animateFloatAsState(
        targetValue = scale,
        label = "FloatAnimation"
    )

    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .scale(Scale.FILL)
            .size(imageSize.width.toPX(), imageSize.height.toPX())
            .build(),
        loading = { NoImagePlaceholder() },
        error = { NoImagePlaceholder() },
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(imageSize.width, imageSize.height)
            .scale(animatedScale)
            .clip(RoundedCornerShape(4.dp)) // Optional: Rounded corners
    )
}