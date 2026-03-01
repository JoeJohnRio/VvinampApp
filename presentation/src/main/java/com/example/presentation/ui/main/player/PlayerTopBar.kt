package com.example.presentation.ui.main.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.R
import com.example.presentation.ui.main.playback.PlaybackUiState
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.ui.theme.TSLabelMediumWhite
import com.example.presentation.util.orErrorNoText

@Composable
fun PlayerTopBar(
    uiState: PlaybackUiState,
    onCollapse: () -> Unit,
    onMoreOptionClicked: () -> Unit
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
                painter = painterResource(R.drawable.ic_chevron_down),
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

        IconButton(
            modifier = Modifier
                .size(24.dp),
            onClick = onMoreOptionClicked
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.inversePrimary,
                painter = painterResource(R.drawable.ic_bullet_vertical),
                contentDescription = "Detail",
            )
        }
    }
}