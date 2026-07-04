package com.example.vvinamp.ui.home.album
import vvinampapp.composeapp.generated.resources.*

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
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.domain.entity.album.AlbumDomain

import com.example.vvinamp.ui.theme.TSHeadlineMediumWhite
import com.example.vvinamp.util.PaddingSizeUtil
import com.example.vvinamp.util.orErrorNoText


@Composable
fun AlbumHeroImage(
    innerPadding: PaddingValues,
    albumDomain: AlbumDomain?, // Replace with your image source
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
            .padding(top = innerPadding.calculateTopPadding() + PaddingSizeUtil.NormalPadding)
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
                model = albumDomain?.coverArt,
                contentDescription = null,
                placeholder = painterResource(Res.drawable.bg_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .size(PaddingSizeUtil.AlbumImageSize)
            )

            Spacer(Modifier.height(32.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = albumDomain?.title.orErrorNoText(),
                style = TSHeadlineMediumWhite().copy(
                    fontSize = 22.sp
                )
            )
        }
    }
}