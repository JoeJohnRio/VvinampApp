package com.example.vvinamp.ui.home.library.detailplaylist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.entity.playlist.PlaylistComplete
import com.example.domain.entity.playlist.dummyPlaylists
import com.example.vvinamp.ui.home.library.widget.image.CollapsingPlaylistCover
import com.example.vvinamp.util.PaddingSizeUtil
import com.example.vvinamp.util.PaddingSizeUtil.TopBarHeight
import com.example.vvinamp.util.preview.PreviewTheme

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

        CollapsingPlaylistCover(
            coverUrl = playlist?.coverImage.orEmpty(),
            coverDefault = playlist?.albumCovers.orEmpty(),
            scrollStateProvider = { scrollStateProvider() },
            maxPx = maxPx,
            maxShrinkPx = maxShrinkPx,
            modifier = Modifier
        )
    }
}

@Preview(apiLevel = 36)
@Composable
private fun PagePreview() {
    PreviewTheme {
        PlaylistHeroImage(
            playlist = dummyPlaylists.first(),
            scrollStateProvider = {
                0f
            },
            innerPadding = PaddingValues()
        )
    }
}
