package com.example.presentation.ui.home.library.detailplaylist

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.domain.entity.playlist.PlaylistComplete
import com.example.presentation.R
import com.example.presentation.util.PaddingSizeUtil
import com.example.presentation.util.PaddingSizeUtil.TopBarHeight
import kotlin.math.roundToInt
@Composable
fun PlaylistHeroImage(
    playlist: PlaylistComplete?,
    modifier: Modifier = Modifier,
    scrollStateProvider: () -> Float,
    innerPadding: PaddingValues
) {
    val startSize = PaddingSizeUtil.AlbumImageSize
    val endSize = startSize * 0.3f
    val density = LocalDensity.current

    val maxPx = with(density) { startSize.toPx() }
    val minPx = with(density) { endSize.toPx() }

    val maxShrinkPx = maxPx - minPx

    val fadeOutDistancePx = with(density) { 100.dp.toPx() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                val currentScroll = scrollStateProvider()
                val collapsePx = currentScroll.coerceIn(0f, maxShrinkPx)
                translationY = collapsePx
                val overscrollPx = currentScroll - maxShrinkPx
                val fadeProgress = (overscrollPx / fadeOutDistancePx).coerceIn(0f, 1f)
                alpha = 1f - fadeProgress
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(innerPadding.calculateTopPadding() + (TopBarHeight/2)))

        AsyncImage(
            model = playlist?.albumCovers?.firstOrNull(),
            contentDescription = null,
            placeholder = painterResource(R.drawable.bg_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
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
        )
    }
}