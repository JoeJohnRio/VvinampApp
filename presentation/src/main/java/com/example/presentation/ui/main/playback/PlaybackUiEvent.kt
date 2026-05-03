package com.example.presentation.ui.main.playback

import com.example.domain.entity.enum.ContentType
import com.example.domain.entity.song.TrackDomain

sealed class PlaybackUiEvent {
    data object TogglePlayback : PlaybackUiEvent()
    data object ToggleLoopClicked: PlaybackUiEvent()
    data object ToggleShuffleClicked: PlaybackUiEvent()
    data class OnTrackClicked(
        val track: TrackDomain,
        val contentType: ContentType,
        val contentId: String,
    ) : PlaybackUiEvent()
}