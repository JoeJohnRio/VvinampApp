package com.example.presentation.ui.home.library.detailplaylist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.domain.entity.song.TrackDomain
import com.example.presentation.R
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.ui.theme.TSHeadlineMediumWhite

@Composable
fun ItemPlaylistTrackComponent(
    uiState: PlaylistUiState,
    track: TrackDomain,
    onEvent: (PlaylistUiEvent) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onEvent(PlaylistUiEvent.onClickTrack(
                    playlistId = uiState.playlist?.id.orEmpty(),
                    trackId = track.id
                ))
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = track.albumCoverUrl,
                contentDescription = null,
                placeholder = painterResource(R.drawable.bg_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(Modifier.width(8.dp))

            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = track.title,
                    style = TSDisplayMediumWhite().copy(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.inversePrimary
                    )
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = track.artistName,
                    style = TSHeadlineMediumWhite().copy(
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.surfaceBright
                    )
                )
            }
        }

        Icon(
            modifier = Modifier.clickable(
                onClick = {
                }
            ),
            painter = painterResource(R.drawable.ic_bullet_vertical),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.surfaceBright
        )
    }
}