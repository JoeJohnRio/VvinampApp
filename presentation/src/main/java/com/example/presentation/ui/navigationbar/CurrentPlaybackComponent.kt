package com.example.presentation.ui.navigationbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.rememberAsyncImagePainter
import com.example.domain.entity.song.TrackDomain
import com.example.domain.entity.song.trackDummy
import com.example.presentation.R
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.ui.theme.TSLabelMediumWhite
import com.example.presentation.util.SpotifyMarquee
//import com.example.presentation.util.SimpleMarqueeText
import com.example.presentation.util.preview.PreviewTheme

@Composable
fun CurrentPlaybackComponent(
    currentlyPlaying: TrackDomain,
    onClickPlay: () -> Unit = {},
    isPlaying: Boolean = false
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .background(
                color = Color(0xff61531d),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(4.dp)),
            painter = rememberAsyncImagePainter(
                model = "https://i.ytimg.com/vi/Ep2Ao9--eQc/hq720.jpg?sqp=-oaymwEhCK4FEIIDSFryq4qpAxMIARUAAAAAGAElAADIQj0AgKJD&rs=AOn4CLDR3nkdjjtmd7PrKAqcsxhXfdAzUA",
                placeholder = painterResource(R.drawable.bg_image),
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            SpotifyMarquee(
                text = currentlyPlaying.title,
                style = TSDisplayMediumWhite().copy(
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.inversePrimary
                )
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Sheila on 7",
                style = TSLabelMediumWhite().copy(
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.inversePrimary
                )
            )
        }

        Spacer(Modifier.width(8.dp))

        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(R.drawable.ic_plus_circle),
            tint = MaterialTheme.colorScheme.inversePrimary,
            contentDescription = null,
        )

        Spacer(Modifier.width(18.dp))

        IconButton(
            onClick = { onClickPlay() },
            modifier = Modifier.size(48.dp) // Standard IconButton size
        ) {
            Icon(
                painter = painterResource(
                    if (isPlaying) R.drawable.ic_pause
                    else R.drawable.ic_play
                ),
                contentDescription = if (isPlaying) "Pause" else "Play",
                tint = MaterialTheme.colorScheme.inversePrimary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.width(18.dp))
    }
}

@Composable
fun VideoPlayer(m3u8Url: String) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(m3u8Url))
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                }
            }
        )
    ) {
        onDispose {
            exoPlayer.release()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CurrentPlaybackComponentPreview() {
    PreviewTheme {
        CurrentPlaybackComponent(currentlyPlaying = trackDummy.first(),)
    }
}