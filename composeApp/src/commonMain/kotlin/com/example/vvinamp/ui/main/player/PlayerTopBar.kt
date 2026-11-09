package com.example.vvinamp.ui.main.player
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.vvinamp.ui.main.playback.PlaybackUiEvent
import com.example.vvinamp.ui.main.playback.PlaybackUiState
import com.example.vvinamp.ui.theme.TSDisplayMediumWhite
import com.example.vvinamp.ui.theme.TSLabelMediumWhite
import com.example.vvinamp.util.orErrorNoText

@Composable
fun PlayerTopBar(
    onEvent: (PlaybackUiEvent) -> Unit,
    uiState: PlaybackUiState,
    onCollapse: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier
                .size(24.dp),
            onClick = onCollapse
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.inversePrimary,
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Collapse",
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "PLAYING FROM ARTIST",
                style = TSLabelMediumWhite().copy(
                    fontSize = 12.sp
                )
            )

            Text(
                text = uiState.currentTrack?.artistName.orErrorNoText(),
                style = TSDisplayMediumWhite().copy(
                    fontSize = 12.sp
                )
            )
        }

        // The options sheet would cover the embedded player, which YouTube's terms don't allow.
        if (uiState.youtubeEmbedId == null) {
            IconButton(
                modifier = Modifier
                    .size(24.dp),
                onClick = {
                    onEvent(PlaybackUiEvent.ToggleTrackOptionSheet(true))
                }
            ) {
                Icon(
                    tint = MaterialTheme.colorScheme.inversePrimary,
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Detail",
                )
            }
        } else {
            Spacer(Modifier.size(24.dp))
        }
    }
}