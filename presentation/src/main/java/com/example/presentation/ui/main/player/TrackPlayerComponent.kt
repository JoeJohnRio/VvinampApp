package com.example.presentation.ui.main.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.example.presentation.R
import com.example.presentation.ui.main.playback.PlaybackUiEvent
import com.example.presentation.ui.main.playback.PlaybackUiState
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.ui.theme.TSLabelMediumWhite
import com.example.presentation.ui.widget.CustomProgressSlider
import com.example.presentation.ui.widget.playback.CustomDurationText
import com.example.presentation.ui.widget.playback.CustomPositionText
import com.example.presentation.util.orErrorNoText

@Composable
fun TrackPlayerComponent(
    uiState: PlaybackUiState,
    uiEvent: (PlaybackUiEvent) -> Unit,
    onAddToPlaylistClicked: () -> Unit,
    player: Player?,
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
                    painter = painterResource(R.drawable.ic_plus_circle),
                    contentDescription = "Detail",
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        PlayerProgressBar(player)

        Spacer(modifier = Modifier.height(8.dp))

        PlayerControls(
            isPlaying = uiState.isPlaying,
            onClickTogglePlayback = uiEvent
        )
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun PlayerProgressBar(player: Player?) {
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
fun PlayerControls(isPlaying: Boolean, onClickTogglePlayback: (PlaybackUiEvent) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier
                .size(32.dp),
            onClick = {}
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.outline,
                painter = painterResource(R.drawable.ic_shuffle),
                contentDescription = "Detail",
            )
        }

        IconButton(
            modifier = Modifier
                .size(32.dp),
            onClick = {
            }
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.inversePrimary,
                painter = painterResource(R.drawable.ic_play_back),
                contentDescription = "Detail",
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
                onClickTogglePlayback(PlaybackUiEvent.TogglePlayback)
            }
        ) {
            Icon(
                modifier = Modifier
                    .graphicsLayer {
                        blendMode = BlendMode.DstOut
                    },
                tint = Color.Black
                ,painter = painterResource(
                    if (isPlaying) R.drawable.ic_pause
                    else R.drawable.ic_play
                ),
                contentDescription = if (isPlaying) "Pause" else "Play",
            )
        }

        IconButton(
            modifier = Modifier
                .size(32.dp),
            onClick = {}
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.inversePrimary,
                painter = painterResource(R.drawable.ic_play_forward),
                contentDescription = "Detail",
            )
        }

        IconButton(
            modifier = Modifier
                .size(32.dp),
            onClick = {}
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.outline,
                painter = painterResource(R.drawable.ic_loop),
                contentDescription = "Detail",
            )
        }
    }
}
