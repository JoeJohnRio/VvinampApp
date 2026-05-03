package com.example.presentation.ui.home.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.entity.playlist.PlaylistMinimal
import com.example.domain.entity.playlist.playlistMinimalDummy
import com.example.presentation.R
import com.example.presentation.ui.home.library.widget.image.PlaylistCover
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.util.preview.PreviewContainer

@Composable
fun LibraryGridItem(
    playlistMinimal: PlaylistMinimal,
    onEvent: (LibraryUiEvent) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onEvent(LibraryUiEvent.onPlaylistClicked(playlistMinimal.id))
            }
    ) {
        PlaylistCover(
            covers = playlistMinimal.albumCovers,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )


        Spacer(Modifier.height(12.dp))

        Column() {
            Text(
                text = playlistMinimal.title,
                style = TSDisplayMediumWhite().copy(
                    fontSize = 15.sp
                ),
                maxLines = 1,
            )
            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (true) {
                    Icon(
                        painter = painterResource(R.drawable.ic_pin),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null,
                    )

                    Spacer(Modifier.width(4.dp))
                }

                Text(
                    text = "Playlist • ${playlistMinimal.username}",
                    style = TSDisplayMediumWhite().copy(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.surfaceBright
                    ),
                    maxLines = 1,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LibraryGridItemPreview() {
    PreviewContainer {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            LibraryGridItem(playlistMinimalDummy)
        }
    }
}