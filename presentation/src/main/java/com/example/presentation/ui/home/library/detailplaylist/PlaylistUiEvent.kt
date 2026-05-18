package com.example.presentation.ui.home.library.detailplaylist

import com.example.domain.entity.song.TrackDomain

sealed class PlaylistUiEvent {
    data class onClickAdd(
        val playlistId: String,
        val trackId: String
    ) : PlaylistUiEvent()

    data class onClickTrack(
        val playlistId: String,
        val trackId: String
    ) : PlaylistUiEvent()

    data class onClickTrackRecommended(
        val track: TrackDomain
    ) : PlaylistUiEvent()

    object onClickChange : PlaylistUiEvent()
    object onClickShare : PlaylistUiEvent()
    object ToggleShuffleClicked : PlaylistUiEvent()
    object onClickPlayAll : PlaylistUiEvent()
    object onClickDetail : PlaylistUiEvent()
}