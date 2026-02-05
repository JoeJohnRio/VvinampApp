package com.example.presentation.ui.home.artist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.domain.entity.artist.ArtistProfileDomain
import com.example.presentation.ui.theme.TSHeadlineMediumWhite


@Composable
fun ArtistHeroImage(
    artistProfile: ArtistProfileDomain, // Replace with your image source
    modifier: Modifier = Modifier,
    collapseFraction: Float,
    artistNameProgress: Float
) {
    Image(
        painter = rememberAsyncImagePainter(artistProfile.profileImage),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )

    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        1f to MaterialTheme.colorScheme.background.copy(alpha = 0.7f)
                    )
                )
                .graphicsLayer {
                    alpha = 1f - collapseFraction
                }
        )
        Column(
            modifier = Modifier
                .align(
                    Alignment.BottomStart
                )
                .alpha(
                    alpha = 1f - artistNameProgress
                )
                .padding(
                    start = 12.dp,
                    bottom = 18.dp
                )
        ) {
            Text(
                text = artistProfile.name,
                style = TSHeadlineMediumWhite().copy(
                    fontSize = 50.sp
                )
            )
        }
    }
}