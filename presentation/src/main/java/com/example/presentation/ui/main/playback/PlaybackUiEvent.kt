package com.example.presentation.ui.main.playback

import com.example.domain.entity.song.TrackDomain

sealed class PlaybackUiEvent {
    data object TogglePlayback : PlaybackUiEvent()
    data class TrackClicked(val track: TrackDomain) : PlaybackUiEvent()
}