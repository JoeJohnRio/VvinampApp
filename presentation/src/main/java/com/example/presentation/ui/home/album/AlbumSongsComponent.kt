package com.example.presentation.ui.home.album

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.entity.album.SimplifiedTrackDomain
import com.example.domain.entity.album.dummyAlbumDomain
import com.example.presentation.R
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.util.preview.PreviewTheme

@Composable
fun AlbumSongsComponent(uiState: AlbumUiState, onSongClick: (SimplifiedTrackDomain) -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(32.dp))

        ListSongsComponent(uiState.tracks, onSongClick)

        Spacer(Modifier.height(16.dp))

        Text(
            text = "${uiState.album?.trackCount} songs ∙ ${uiState.album?.totalDuration} min",
            style = TSDisplayMediumWhite().copy(
                fontSize = 16.sp
            )
        )
    }
}

@Composable
fun ListSongsComponent(
    tracks: List<SimplifiedTrackDomain>,
    onSongClick: (SimplifiedTrackDomain) -> Unit
) {
    tracks.forEach { track ->
        Row(
            modifier = Modifier.fillMaxWidth()
                .clickable {
                    onSongClick(track)
                },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column() {
                Text(
                    text = track.title,
                    style = TSDisplayMediumWhite().copy(
                        fontSize = 16.sp
                    )
                )

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
                modifier = Modifier.size(24.dp) // Standard IconButton size
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_bullet_vertical),
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
        AlbumSongsComponent(AlbumUiState(album = dummyAlbumDomain),{})
    }
}