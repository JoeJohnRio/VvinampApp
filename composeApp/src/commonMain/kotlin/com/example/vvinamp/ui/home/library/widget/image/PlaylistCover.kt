package com.example.vvinamp.ui.home.library.widget.image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun PlaylistCover(
    coverImage: String,
    covers: List<String>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
    ) {
        if (coverImage.isNotEmpty()) {
            CoverImage(model = coverImage, modifier = Modifier.fillMaxSize())
        } else if (covers.size >= 4) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(modifier = Modifier.weight(1f)) {
                    CoverImage(model = covers[0], modifier = Modifier.weight(1f).fillMaxHeight())
                    CoverImage(model = covers[1], modifier = Modifier.weight(1f).fillMaxHeight())
                }
                Row(modifier = Modifier.weight(1f)) {
                    CoverImage(model = covers[2], modifier = Modifier.weight(1f).fillMaxHeight())
                    CoverImage(model = covers[3], modifier = Modifier.weight(1f).fillMaxHeight())
                }
            }
        } else {
            CoverImage(model = covers.firstOrNull(), modifier = Modifier.fillMaxSize())
        }
    }
}
