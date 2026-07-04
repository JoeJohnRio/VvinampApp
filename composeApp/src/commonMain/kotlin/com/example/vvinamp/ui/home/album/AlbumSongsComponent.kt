package com.example.vvinamp.ui.home.album
import vvinampapp.composeapp.generated.resources.*

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
import androidx.compose.material.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.entity.album.SimplifiedTrackDomain
import com.example.domain.entity.album.dummyAlbumDomain

import com.example.vvinamp.ui.main.playback.PlaybackUiState
import com.example.vvinamp.ui.theme.TSDisplayMediumWhite
import com.example.vvinamp.ui.widget.SpotifyPlayingIndicator
import com.example.vvinamp.util.preview.PreviewTheme
import com.example.vvinamp.util.toMinutes

@Composable
fun AlbumSongsComponent(
    uiState: AlbumUiState,
    onSongClick: (SimplifiedTrackDomain) -> Unit,
    playbackUiState: PlaybackUiState = PlaybackUiState()
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(32.dp))

        ListSongsComponent(uiState.tracks, onSongClick, playbackUiState)

        Spacer(Modifier.height(16.dp))

        Text(
            text = "${uiState.album?.trackCount} songs ∙ ${uiState.album?.totalDuration?.toMinutes()} min",
            style = TSDisplayMediumWhite().copy(
                fontSize = 16.sp
            )
        )
    }
}

@Composable
fun ListSongsComponent(
    tracks: List<SimplifiedTrackDomain>,
    onSongClick: (SimplifiedTrackDomain) -> Unit,
    playbackUiState: PlaybackUiState
) {
    tracks.forEach { track ->
        val isCurrentTrackPlayed = playbackUiState.currentTrack?.id == track.id
        val color = if (isCurrentTrackPlayed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inversePrimary

        Row(
            modifier = Modifier.fillMaxWidth()
                .clickable {
                    onSongClick(track)
                },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column() {
                Row() {
                    if (isCurrentTrackPlayed) {
                        SpotifyPlayingIndicator(
                            isPlaying = playbackUiState.isPlaying,
                            color = MaterialTheme.colorScheme.primary,
                        )

                        Spacer(Modifier.width(4.dp))
                    }

                    Text(
                        text = track.title,
                        style = TSDisplayMediumWhite().copy(
                            fontSize = 16.sp,
                            color = color
                        )
                    )
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = track.artistName,
                    style = TSDisplayMediumWhite().copy(
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }


            IconButton(
                onClick = {
                    //add to liked song
                },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_bullet_vertical),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.inversePrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(Modifier.height(20.dp))
    }
}

@Preview
@Composable
fun AlbumSongsComponentPreview() {
    PreviewTheme {
        AlbumSongsComponent(AlbumUiState(album = dummyAlbumDomain), {},)
    }
}