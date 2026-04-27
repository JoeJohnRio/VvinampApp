package com.example.presentation.ui.home.library.detailplaylist

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil3.compose.AsyncImage
import com.example.domain.entity.playlist.PlaylistComplete
import com.example.presentation.R
import com.example.presentation.util.PaddingSizeUtil
import com.example.presentation.util.PaddingSizeUtil.TopBarHeight

@Composable
fun PlaylistHeroImage(
    playlist: PlaylistComplete?, // Replace with your image source
    modifier: Modifier = Modifier,
    collapseFraction: Float,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = playlist?.albumCovers?.firstOrNull(),
            contentDescription = null,
            placeholder = painterResource(R.drawable.bg_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .size(PaddingSizeUtil.AlbumImageSize)
                .graphicsLayer {
//                    alpha = (1f - collapseFraction).coerceIn(0f, 1f)
                    Log.d("joel123", "${collapseFraction}")
                    val scale = lerp(start = 1f, stop = 0.5f, fraction = collapseFraction)
                    scaleX = scale
                    scaleY = scale
                }
        )
    }
}