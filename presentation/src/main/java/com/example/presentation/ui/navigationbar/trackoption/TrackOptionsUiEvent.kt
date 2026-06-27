package com.example.presentation.ui.navigationbar.trackoption

import com.example.presentation.ui.main.playback.PlaybackUiEvent
import com.example.presentation.ui.main.playback.SleepTimerOption

sealed class TrackOptionsUiEvent {
    object OnShareClick : TrackOptionsUiEvent()
    object OnAddToPlaylistClick : TrackOptionsUiEvent()
    object OnAddToQueueClick : TrackOptionsUiEvent()
    object OnHideTrackClick : TrackOptionsUiEvent()
    object OnGoToAlbumClick : TrackOptionsUiEvent()
    object OnGoToArtistClick : TrackOptionsUiEvent()
    object OnSleepTimerClick : TrackOptionsUiEvent()
    data class OnSleepTimerSelected(val option: SleepTimerOption) : TrackOptionsUiEvent()
}