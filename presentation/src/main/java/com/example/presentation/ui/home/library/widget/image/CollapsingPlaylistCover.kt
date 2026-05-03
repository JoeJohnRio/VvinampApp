package com.example.presentation.ui.home.library.widget.image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt


@Composable
fun CollapsingPlaylistCover(
    covers: List<String>,
    scrollStateProvider: () -> Float,
    maxPx: Float,
    maxShrinkPx: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .layout { measurable, constraints ->
                val currentScroll = scrollStateProvider()
                val collapsePx = currentScroll.coerceIn(0f, maxShrinkPx)

                val sizePx = (maxPx - collapsePx).roundToInt()
                val maxPxInt = maxPx.roundToInt()

                val placeable = measurable.measure(
                    Constraints.fixed(sizePx, sizePx)
                )

                layout(maxPxInt, maxPxInt) {
                    val xOffset = (maxPxInt - sizePx) / 2
                    placeable.placeRelative(xOffset, 0)
                }
            }
            .clip(RoundedCornerShape(4.dp))
    ) {
        if (covers.size >= 4) {
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


@Composable
fun PlaylistCover(
    covers: List<String>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
    ) {
        if (covers.size >= 4) {
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