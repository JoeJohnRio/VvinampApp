package com.example.presentation.ui.home.artist

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.entity.artist.ArtistProfileDomain
import com.example.domain.entity.artist.artistProfileDummy
import com.example.domain.entity.song.TopSongsDomain
import com.example.domain.entity.song.TrackDomain
import com.example.domain.entity.song.topSongsDummy
import com.example.presentation.R
import com.example.presentation.ui.main.playback.PlaybackUiState
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.ui.theme.TSHeadlineMediumWhite
import com.example.presentation.ui.widget.ButtonVariableOutlined
import com.example.presentation.util.preview.PreviewTheme
import com.example.presentation.util.verticalGradientEdge

@Composable
fun TopSongsComponent(
    artistProfile: ArtistProfileDomain,
    topSongs: TopSongsDomain,
    onTrackClicked: (TrackDomain) -> Unit,
    playbackUiState: PlaybackUiState = PlaybackUiState(),
) {
    var isExpanded by remember { mutableStateOf(false) }

    val displayTasks = if (isExpanded) {
        topSongs.tracks
    } else {
        topSongs.tracks.take(6)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(R.string.title_popular),
            style = TSHeadlineMediumWhite().copy(
                fontSize = 20.sp
            )
        )

        Spacer(Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .heightIn(max = if (isExpanded) 1000.dp else 400.dp)
                .verticalGradientEdge(
                    isExpanded = isExpanded,
                    color = MaterialTheme.colorScheme.background
                )
        ) {
            displayTasks.forEachIndexed { index, song ->
                SongRow(
                    playbackUiState = playbackUiState,
                    song = song,
                    index = index,
                    onTrackClicked = onTrackClicked
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ButtonVariableOutlined(
                text = if (!isExpanded) "See more" else "See less",
                textStyle = TSDisplayMediumWhite().copy(
                    color = MaterialTheme.colorScheme.inversePrimary,
                    fontWeight = FontWeight.Bold
                ),
                buttonHeight = 25.dp,
                onClick = {
                    isExpanded = !isExpanded
                }
            )
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Preview
@Composable
fun TopSongsComponentPreview() {
    PreviewTheme {
        TopSongsComponent(
            artistProfile = artistProfileDummy,
            topSongs = topSongsDummy,
            onTrackClicked = {},
        )
    }
}