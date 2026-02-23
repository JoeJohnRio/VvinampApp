package com.example.presentation.ui.home.artist

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.example.domain.entity.song.TrackDomain
import com.example.domain.entity.song.topSongsDummy
import com.example.presentation.R
import com.example.presentation.ui.main.playback.PlaybackUiState
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.ui.widget.ButtonVariableOutlined
import com.example.presentation.ui.widget.SpotifyPlayingIndicator
import com.example.presentation.util.preview.PreviewTheme
import com.example.presentation.util.verticalGradientEdge

@Composable
fun SongRow(
    song: TrackDomain,
    index: Int,
    onTrackClicked: (TrackDomain) -> Unit,
    playbackUiState: PlaybackUiState = PlaybackUiState()
) {
    val isCurrentTrackPlayed = playbackUiState.currentTrack?.id == song.id

    Row(
        modifier = Modifier
            .clickable{ onTrackClicked(song) }
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${index + 1}",
            color = MaterialTheme.colorScheme.inversePrimary,
            modifier = Modifier.width(30.dp),
            style = TSDisplayMediumWhite().copy(
                fontSize = 16.sp
            )
        )

        Image(
            painter = rememberAsyncImagePainter(
                model = song.albumCoverUrl,
                placeholder = painterResource(R.drawable.bg_image),
            ),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row() {
                if (isCurrentTrackPlayed) {
                    SpotifyPlayingIndicator(
                        isPlaying = playbackUiState.isPlaying,
                        color = MaterialTheme.colorScheme.primary,
                    )

                    Spacer(Modifier.width(4.dp))
                }

                Text(
                    text = song.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TSDisplayMediumWhite().copy(
                        fontSize = 16.sp,
                        color = if (isCurrentTrackPlayed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inversePrimary,
                    )
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = song.streamCount.toString(),
                color = Color.Gray,
                style = TSDisplayMediumWhite().copy(
                    fontSize = 12.sp,
                    color = Color.Gray,
                )
            )
        }

        if (index == 1) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF1DB954),
                modifier = Modifier
                    .size(28.dp)
                    .padding(horizontal = 4.dp)
            )
        }

        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}

@Preview
@Composable
fun SongRowPreview() {
    val topSongs = topSongsDummy

    var isExpanded by remember { mutableStateOf(false) }

    val displayTasks = if (isExpanded) {
        topSongs.tracks
    } else {
        topSongs.tracks.take(6)
    }

    PreviewTheme {
        LazyColumn() {
            item {
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
                            song = song,
                            index = index,
                            onTrackClicked = {},
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            if (!isExpanded) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ButtonVariableOutlined(
                            text = "See More",
                            textStyle = TSDisplayMediumWhite().copy(
                                color = MaterialTheme.colorScheme.inversePrimary,
                                fontWeight = FontWeight.Bold
                            ),
                        )
                    }
                }
            }
        }
    }
}
