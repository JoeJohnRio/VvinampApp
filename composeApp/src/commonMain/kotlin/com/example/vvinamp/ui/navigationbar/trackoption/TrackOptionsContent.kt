package com.example.vvinamp.ui.navigationbar.trackoption
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.example.vvinamp.ui.main.playback.PlaybackUiEvent
import com.example.vvinamp.ui.widget.text.IconWithTextOnly
import com.example.vvinamp.util.preview.PreviewTheme

@Composable
fun TrackOptionsContent(
    onEvent: (TrackOptionsUiEvent) -> Unit,
) {
    Column {
        IconWithTextOnly(
            modifier = Modifier.padding(top = 8.dp),
            painter = painterResource(Res.drawable.ic_share),
            text = "Share"
        ) { onEvent(TrackOptionsUiEvent.OnShareClick) }

        IconWithTextOnly(
            painter = painterResource(Res.drawable.ic_plus_circle),
            text = "Add to playlist"
        ) { onEvent(TrackOptionsUiEvent.OnAddToPlaylistClick) }

        IconWithTextOnly(
            painter = painterResource(Res.drawable.ic_list_music),
            text = "Add to Queue"
        ) { onEvent(TrackOptionsUiEvent.OnAddToQueueClick) }

        IconWithTextOnly(
            painter = painterResource(Res.drawable.ic_close),
            text = "Hide in this playlist"
        ) { onEvent(TrackOptionsUiEvent.OnHideTrackClick) }

        IconWithTextOnly(
            painter = painterResource(Res.drawable.ic_target),
            text = "Go to album"
        ) { onEvent(TrackOptionsUiEvent.OnGoToAlbumClick) }

        IconWithTextOnly(
            painter = painterResource(Res.drawable.ic_peoples_solid),
            text = "Go to artist"
        ) { onEvent(TrackOptionsUiEvent.OnGoToArtistClick) }

        IconWithTextOnly(
            painter = painterResource(Res.drawable.ic_stopwatch),
            text = "Sleep timer"
        ) { onEvent(TrackOptionsUiEvent.OnSleepTimerClick) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
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