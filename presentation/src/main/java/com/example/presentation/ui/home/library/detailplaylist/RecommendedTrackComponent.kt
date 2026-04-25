package com.example.presentation.ui.home.library.detailplaylist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.entity.song.dummyRecommendedTracks
import com.example.presentation.R
import com.example.presentation.ui.theme.TSHeadlineMediumWhite
import com.example.presentation.util.preview.PreviewTheme

@Composable
fun RecommendedTrackComponent(
    uiState: PlaylistUiState,
    onEvent: (PlaylistUiEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.title_recommended_songs),
            style = TSHeadlineMediumWhite().copy(
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.inversePrimary
            )
        )

        Spacer(Modifier.height(32.dp))

        uiState.recommendedTracks?.forEach { track ->
            RecommendedTrackItem(
                playlistId = uiState.playlist?.id.orEmpty(),
                track = track,
                onEvent = onEvent
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecommendedTrackComponentPreview() {
    PreviewTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
        ) {
            RecommendedTrackComponent(
                PlaylistUiState(
                    recommendedTracks = dummyRecommendedTracks
                ),
                onEvent = {}
            )
        }
    }
}