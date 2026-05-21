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
    data class onClickPlayAll(
        val playlistId: String,
    ) : PlaylistUiEvent()
    object onClickChange : PlaylistUiEvent()
    object onClickShare : PlaylistUiEvent()
    object ToggleShuffleClicked : PlaylistUiEvent()
    object onClickDetail : PlaylistUiEvent()

    object onRowClickAdd : PlaylistUiEvent()

    object onRowClickEdit : PlaylistUiEvent()

    object onRowClickSort : PlaylistUiEvent()

    object onRowClickEditDetail : PlaylistUiEvent()
}