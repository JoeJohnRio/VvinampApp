package com.example.vvinamp.ui.home.library.detailplaylist.editplaylist
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.entity.song.TrackDomain
import com.example.domain.entity.song.trackDummy

import com.example.vvinamp.ui.theme.TSDisplayMediumWhite
import com.example.vvinamp.ui.theme.TSHeadlineMediumWhite
import com.example.vvinamp.ui.widget.image.ImageWithPlayButton
import com.example.vvinamp.util.preview.PreviewTheme

@Composable
fun LazyItemScope.ItemPlaylistTrackEdit(
    draggableHandle: Modifier,
    track: TrackDomain,
    onEvent: (PlaylistEditUiEvent) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onEvent(
                    PlaylistEditUiEvent.OnClickHold(
                        trackId = track.id
                    )
                )
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.clickable {
                    onEvent(PlaylistEditUiEvent.OnDelete(trackId = track.id))
                },
                painter = painterResource(Res.drawable.ic_stop_remove),
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.surfaceBright
            )

            Spacer(Modifier.width(8.dp))

            ImageWithPlayButton(
                imageUrl = track.albumCoverUrl,
                onClick = {
                    onEvent(PlaylistEditUiEvent.OnClickPlayTrack(track.id))
                }
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
            modifier = draggableHandle.clickable(
                onClick = {
                    onEvent(PlaylistEditUiEvent.OnClickPlayTrack(trackId = track.id))
                }
            ),
            painter = painterResource(Res.drawable.ic_burger),
            contentDescription = "Reorder",
            tint = MaterialTheme.colorScheme.surfaceBright
        )
    }
}


@Preview
@Composable
fun ItemPlaylistTrackEditPreview() {
    PreviewTheme {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(trackDummy) { item ->
                ItemPlaylistTrackEdit(
                    track = item,
                    onEvent = {

                    },
                    draggableHandle = Modifier
                )
            }
        }
    }
}
