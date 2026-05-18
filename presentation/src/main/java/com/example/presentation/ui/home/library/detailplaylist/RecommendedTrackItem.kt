package com.example.presentation.ui.home.library.detailplaylist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.data.api.playlist.toTrackDomain
import com.example.domain.entity.song.RecommendedTrackDomain
import com.example.domain.entity.song.dummyRecommendedTracks
import com.example.presentation.R
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.ui.theme.TSHeadlineMediumWhite
import com.example.presentation.util.preview.PreviewTheme


@Composable
fun RecommendedTrackItem(
    playlistId: String,
    track: RecommendedTrackDomain,
    onEvent: (PlaylistUiEvent) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onEvent(
                    PlaylistUiEvent.onClickTrackRecommended(
                        track.toTrackDomain()
                    )
                )
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

        if (track.isAdded) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        } else {
            Icon(
                modifier = Modifier.clickable(
                    onClick = {
                        onEvent(
                            PlaylistUiEvent.onClickAdd(
                                playlistId = playlistId,
                                trackId = track.id
                            )
                        )
                    }
                ),
                painter = painterResource(R.drawable.ic_plus_circle),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceBright
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RecommendedTrackItemPreview() {
    PreviewTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
        ) {
            RecommendedTrackItem(
                playlistId = "",
                track = dummyRecommendedTracks.first(),
                onEvent = {}
            )
        }
    }
}