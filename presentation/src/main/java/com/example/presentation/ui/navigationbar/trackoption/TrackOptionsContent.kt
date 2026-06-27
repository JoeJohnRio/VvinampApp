package com.example.presentation.ui.navigationbar.trackoption

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.R
import com.example.presentation.ui.main.playback.PlaybackUiEvent
import com.example.presentation.ui.widget.text.IconWithTextOnly
import com.example.presentation.util.preview.PreviewTheme

@Composable
fun TrackOptionsContent(
    onEvent: (TrackOptionsUiEvent) -> Unit,
) {
    Column {
        IconWithTextOnly(
            modifier = Modifier.padding(top = 8.dp),
            painter = painterResource(R.drawable.ic_share),
            text = "Share"
        ) { onEvent(TrackOptionsUiEvent.OnShareClick) }

        IconWithTextOnly(
            painter = painterResource(R.drawable.ic_plus_circle),
            text = "Add to playlist"
        ) { onEvent(TrackOptionsUiEvent.OnAddToPlaylistClick) }

        IconWithTextOnly(
            painter = painterResource(R.drawable.ic_list_music),
            text = "Add to Queue"
        ) { onEvent(TrackOptionsUiEvent.OnAddToQueueClick) }

        IconWithTextOnly(
            painter = painterResource(R.drawable.ic_close),
            text = "Hide in this playlist"
        ) { onEvent(TrackOptionsUiEvent.OnHideTrackClick) }

        IconWithTextOnly(
            painter = painterResource(R.drawable.ic_target),
            text = "Go to album"
        ) { onEvent(TrackOptionsUiEvent.OnGoToAlbumClick) }

        IconWithTextOnly(
            painter = painterResource(R.drawable.ic_peoples_solid),
            text = "Go to artist"
        ) { onEvent(TrackOptionsUiEvent.OnGoToArtistClick) }

        IconWithTextOnly(
            painter = painterResource(R.drawable.ic_stopwatch),
            text = "Sleep timer"
        ) { onEvent(TrackOptionsUiEvent.OnSleepTimerClick) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "Track Option Bottom Sheet",
    showBackground = true
)
@Composable
fun TrackOptionsContentPreview() {
    PreviewTheme {
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            TrackOptionsContent(
                onEvent = {

                }
            )
        }
    }
}