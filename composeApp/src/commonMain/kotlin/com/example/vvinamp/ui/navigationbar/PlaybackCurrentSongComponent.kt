package com.example.vvinamp.ui.navigationbar
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.example.domain.entity.song.TrackDomain
import com.example.domain.entity.song.trackDummy

import com.example.vvinamp.ui.theme.AppColours
import com.example.vvinamp.ui.theme.TSDisplayMediumWhite
import com.example.vvinamp.ui.theme.TSLabelMediumWhite
import com.example.vvinamp.util.ExtractDominantColorEffect
import com.example.vvinamp.util.SpotifyMarquee
//import com.example.vvinamp.util.SimpleMarqueeText
import com.example.vvinamp.util.preview.PreviewTheme

@Composable
fun PlaybackCurrentSongComponent(
    currentlyPlaying: TrackDomain,
    backgroundColor: Color,
    onClickPlay: () -> Unit = {},
    onClickTrack: (TrackDomain) -> Unit = {},
    isPlaying: Boolean = false
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable {
                onClickTrack(currentlyPlaying)
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(4.dp)),
            painter = rememberAsyncImagePainter(
                model = currentlyPlaying.albumCoverUrl,
                placeholder = painterResource(Res.drawable.bg_image),
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
                text = currentlyPlaying.artistName,
                style = TSLabelMediumWhite().copy(
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.inversePrimary
                )
            )
        }

        Spacer(Modifier.width(8.dp))

        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(Res.drawable.ic_plus_circle),
            tint = MaterialTheme.colorScheme.inversePrimary,
            contentDescription = null,
        )

        Spacer(Modifier.width(18.dp))

        IconButton(
            onClick = { onClickPlay() },
            modifier = Modifier.size(32.dp) // Standard IconButton size
        ) {
            Icon(
                painter = painterResource(
                    if (isPlaying) Res.drawable.ic_pause
                    else Res.drawable.ic_play
                ),
                contentDescription = if (isPlaying) "Pause" else "Play",
                tint = MaterialTheme.colorScheme.inversePrimary,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(Modifier.width(18.dp))
    }
}

@Preview
@Composable
fun PlaybackCurrentSongComponentPreview() {
    PreviewTheme {
        PlaybackCurrentSongComponent(currentlyPlaying = trackDummy.first(), AppColours.PrimaryGreen)
    }
}
