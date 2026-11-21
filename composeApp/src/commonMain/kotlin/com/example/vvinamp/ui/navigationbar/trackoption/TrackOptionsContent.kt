package com.example.vvinamp.ui.navigationbar.trackoption
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.example.vvinamp.ui.main.playback.PlaybackUiEvent
import com.example.vvinamp.ui.widget.text.IconWithTextOnly
import com.example.vvinamp.ui.widget.text.ImageVectorWithTextOnly
import com.example.vvinamp.util.preview.PreviewTheme

@Composable
fun TrackOptionsContent(
    onEvent: (TrackOptionsUiEvent) -> Unit,
    canGoToAlbum: Boolean = true,
    canGoToArtist: Boolean = true,
) {
    Column {
        ImageVectorWithTextOnly(
            modifier = Modifier.padding(top = 8.dp),
            imageVector = Icons.Default.Share,
            text = "Share"
        ) { onEvent(TrackOptionsUiEvent.OnShareClick) }

        ImageVectorWithTextOnly(
            imageVector = Icons.Filled.AddCircle,
            text = "Add to playlist"
        ) { onEvent(TrackOptionsUiEvent.OnAddToPlaylistClick) }

        ImageVectorWithTextOnly(
            imageVector = Icons.AutoMirrored.Filled.QueueMusic,
            text = "Add to Queue"
        ) { onEvent(TrackOptionsUiEvent.OnAddToQueueClick) }

        // Only songs that came through the play queue know their album and artist.
        if (canGoToAlbum) {
            ImageVectorWithTextOnly(
                imageVector = Icons.Default.Album,
                text = "Go to album"
            ) { onEvent(TrackOptionsUiEvent.OnGoToAlbumClick) }
        }

        if (canGoToArtist) {
            ImageVectorWithTextOnly(
                imageVector = Icons.Default.Person,
                text = "Go to artist"
            ) { onEvent(TrackOptionsUiEvent.OnGoToArtistClick) }
        }

        ImageVectorWithTextOnly(
            imageVector = Icons.Default.Timer,
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