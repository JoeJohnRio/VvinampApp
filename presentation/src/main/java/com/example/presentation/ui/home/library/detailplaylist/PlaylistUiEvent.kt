package com.example.presentation.ui.home.library.detailplaylist

import com.example.domain.entity.song.TrackDomain

sealed class PlaylistUiEvent {
    data class onClickAdd(
        val playlistId: String,
        val trackId: String
    ) : PlaylistUiEvent()

    data class onClickTrack(
        val track: TrackDomain
    ) : PlaylistUiEvent()

    object onClickChange : PlaylistUiEvent()
    object onClickShare : PlaylistUiEvent()
    object onClickDetail : PlaylistUiEvent()
}