package com.example.presentation.ui.home.library.detailplaylist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.domain.entity.playlist.PlaylistComplete
import com.example.presentation.R
import com.example.presentation.ui.theme.TSHeadlineMediumWhite
import com.example.presentation.util.PaddingUtil
import com.example.presentation.util.orErrorNoText

@Composable
fun PlaylistHeroImage(
    innerPadding: PaddingValues,
    playlist: PlaylistComplete?, // Replace with your image source
    modifier: Modifier = Modifier,
    collapseFraction: Float,
    artistNameProgress: Float
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = 16.dp,
            )
            .padding(top = innerPadding.calculateTopPadding() + PaddingUtil.NormalPadding)
            .graphicsLayer {
                alpha = 1f - collapseFraction
            }
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        1f to MaterialTheme.colorScheme.background.copy(alpha = 0.7f)
                    )
                )
        )
        Column(
            modifier = Modifier.alpha(
                alpha = 1f - artistNameProgress
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = playlist?.coverImage,
                contentDescription = null,
                placeholder = painterResource(R.drawable.bg_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .size(PaddingUtil.AlbumImageSize)
            )

            Spacer(Modifier.height(32.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = playlist?.title.orErrorNoText(),
                style = TSHeadlineMediumWhite().copy(
                    fontSize = 22.sp
                )
            )
        }
    }
}