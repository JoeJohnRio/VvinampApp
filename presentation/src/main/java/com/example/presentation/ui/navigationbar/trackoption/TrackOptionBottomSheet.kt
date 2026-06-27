package com.example.presentation.ui.navigationbar.trackoption

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.domain.entity.song.trackDummy
import com.example.presentation.R
import com.example.presentation.ui.main.playback.PlaybackUiEvent
import com.example.presentation.ui.main.playback.PlaybackUiState
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.ui.theme.TSLabelMediumWhite
import com.example.presentation.util.PaddingSizeUtil
import com.example.presentation.util.orErrorNoText
import com.example.presentation.util.preview.PreviewTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackOptionBottomSheet(
    onEvent: (PlaybackUiEvent) -> Unit,
    onTrackOptionsEvent: (TrackOptionsUiEvent) -> Unit,
    sheetState: SheetState,
    uiState: PlaybackUiState
) {
    if (uiState.isTrackOptionSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                onEvent(PlaybackUiEvent.ToggleTrackOptionSheet(false))
            },
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() },
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ) {
            TrackOptionBottomSheetComponent(
                uiState = uiState,
                onEvent = onTrackOptionsEvent
            )
        }
    }
}

@Composable
fun TrackOptionBottomSheetComponent(
    onEvent: (TrackOptionsUiEvent) -> Unit,
    uiState: PlaybackUiState
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = uiState.currentTrack?.albumCoverUrl,
                contentDescription = null,
                placeholder = painterResource(R.drawable.bg_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .size(PaddingSizeUtil.ListeningHistoryPhotoSize)
            )

            Spacer(Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = uiState.currentTrack?.title.orErrorNoText(),
                    style = TSDisplayMediumWhite().copy(
                        color = MaterialTheme.colorScheme.inversePrimary
                    )
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "${uiState.currentTrack?.artistName.orErrorNoText()} • " +
                            "${uiState.currentTrack?.title.orErrorNoText()}",
                    style = TSLabelMediumWhite().copy(
                        color = MaterialTheme.colorScheme.surfaceBright,
                        fontSize = 11.sp
                    )
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )

        TrackOptionsContent(
            onEvent = onEvent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "Track Option Bottom Sheet",
    showBackground = true
)
@Composable
fun TrackOptionBottomSheetPreview() {
    PreviewTheme {
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            TrackOptionBottomSheetComponent(
                onEvent = {

                },
                uiState = PlaybackUiState(
                    currentTrack = trackDummy.first()
                )
            )
        }
    }
}
