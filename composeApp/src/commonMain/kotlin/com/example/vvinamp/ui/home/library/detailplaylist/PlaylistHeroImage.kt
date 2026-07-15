package com.example.vvinamp.ui.home.library.detailplaylist
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.domain.entity.playlist.PlaylistComplete
import com.example.domain.entity.playlist.dummyPlaylists

import com.example.vvinamp.ui.home.library.widget.image.PlaylistCover
import com.example.vvinamp.util.PaddingSizeUtil
import com.example.vvinamp.util.preview.PreviewTheme

@Composable
fun PlaylistHeroImage(
    playlist: PlaylistComplete?,
    modifier: Modifier = Modifier,
    scrollStateProvider: () -> Float,
    innerPadding: PaddingValues
) {
    val scrollOffset = scrollStateProvider()
    val totalTopPadding = innerPadding.calculateTopPadding()
    val heroSize = PaddingSizeUtil.PlaylistHeroSize
    val heroSizePx = heroSize.value * 3 // Approximation

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = totalTopPadding + 16.dp)
            .height(heroSize) // Fixed height to prevent jumps
            .zIndex(1f),
        contentAlignment = Alignment.Center
    ) {
        PlaylistCover(
            coverImage = playlist?.coverImage ?: "",
            covers = playlist?.albumCovers ?: emptyList(),
            modifier = Modifier
                .size(PaddingSizeUtil.PlaylistNoPhotoSize)
                .graphicsLayer {
                    // Parallax & Fade
                    val progress = (scrollOffset / heroSizePx).coerceIn(0f, 1f)
                    alpha = 1f - progress
                    scaleX = 1f - (progress * 0.2f)
                    scaleY = 1f - (progress * 0.2f)
                }
                .clip(RoundedCornerShape(8.dp))
        )
    }
}

@Preview
@Composable
fun PlaylistHeroImagePreview() {
    PreviewTheme {
        PlaylistHeroImage(
            playlist = dummyPlaylists.first(),
            scrollStateProvider = { 0f },
            innerPadding = PaddingValues(0.dp)
        )
    }
}
