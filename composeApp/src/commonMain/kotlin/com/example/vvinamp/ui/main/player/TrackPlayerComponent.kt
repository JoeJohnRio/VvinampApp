package com.example.vvinamp.ui.main.player
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.vvinamp.ui.main.playback.PlaybackUiEvent
import com.example.vvinamp.ui.main.playback.PlaybackUiState
import com.example.vvinamp.ui.theme.TSDisplayMediumWhite
import com.example.vvinamp.ui.theme.TSLabelMediumWhite
import com.example.vvinamp.ui.widget.playback.CustomProgressSlider
import com.example.vvinamp.ui.widget.playback.CustomDurationText
import com.example.vvinamp.ui.widget.playback.CustomPositionText
import com.example.vvinamp.util.orErrorNoText

@Composable
fun TrackPlayerComponent(
    uiState: PlaybackUiState,
    onEvent: (PlaybackUiEvent) -> Unit,
    onAddToPlaylistClicked: () -> Unit,
    player: Any?,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column() {
                Text(
                    text = uiState.currentTrack?.title.orErrorNoText(),
                    style = TSDisplayMediumWhite().copy(
                        fontSize = 22.sp
                    )
                )

                Text(
                    text = uiState.currentTrack?.artistName.orErrorNoText(),
                    style = TSLabelMediumWhite().copy(
                        fontSize = 16.sp
                    )
                )
            }

            IconButton(
                modifier = Modifier
                    .size(32.dp),
                onClick = onAddToPlaylistClicked
            ) {
                Icon(
                    tint = MaterialTheme.colorScheme.inversePrimary,
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "Detail",
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // shortcut: embedded tracks use YouTube's own seek bar. Poll getCurrentTime to drive this slider instead.
        if (uiState.youtubeEmbedId == null) {
            PlayerProgressBar(player)

            Spacer(modifier = Modifier.height(8.dp))
        }

        PlayerControls(
            uiState = uiState,
            onEvent = onEvent
        )
    }
}

@Composable
fun PlayerProgressBar(player: Any?) {
    if (player != null) {
        CustomProgressSlider(
            player = player,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CustomPositionText(
                player = player,
            )

            CustomDurationText(
                player = player,
            )
        }
    }
}

@Composable
fun PlayerControls(uiState: PlaybackUiState, onEvent: (PlaybackUiEvent) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier
                .size(32.dp),
            onClick = {
                onEvent(PlaybackUiEvent.ToggleShuffleClicked)
            }
        ) {
            Icon(
                tint = if (uiState.isShuffle) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline
                },
                imageVector = Icons.Default.Shuffle,
                contentDescription = "Shuffle",
            )
        }

        IconButton(
            modifier = Modifier
                .size(32.dp),
            onClick = {
                onEvent(PlaybackUiEvent.SkipToPrevious)
            }
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.inversePrimary,
                imageVector = Icons.Default.SkipPrevious,
                contentDescription = "Previous",
            )
        }

        IconButton(
            modifier = Modifier
                .graphicsLayer {
                    compositingStrategy = CompositingStrategy.Offscreen
                }
                .drawBehind {
                    drawCircle(Color.White)
                }
                .size(64.dp),
            onClick = {
                onEvent(PlaybackUiEvent.TogglePlayback)
            }
        ) {
            Icon(
                modifier = Modifier
                    .graphicsLayer {
                        blendMode = BlendMode.DstOut
                    },
                tint = Color.Black,
                imageVector = if (uiState.isPlaying) Icons.Default.Pause
                    else Icons.Default.PlayArrow,
                contentDescription = if (uiState.isPlaying) "Pause" else "Play",
            )
        }

        IconButton(
            modifier = Modifier
                .size(32.dp),
            onClick = {
                onEvent(PlaybackUiEvent.SkipToNext)
            }
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.inversePrimary,
                imageVector = Icons.Default.SkipNext,
                contentDescription = "Next",
            )
        }

        IconButton(
            modifier = Modifier
                .size(32.dp),
            onClick = {
                onEvent(PlaybackUiEvent.ToggleLoopClicked)
            }
        ) {
            Icon(
                tint = if (uiState.isLooping) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline
                },
                imageVector = Icons.Default.Repeat,
                contentDescription = "Repeat",
            )
        }
    }
}
