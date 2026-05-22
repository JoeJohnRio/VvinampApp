package com.example.presentation.ui.home.library.detailplaylist.editplaylist

import com.example.presentation.ui.home.library.detailplaylist.PlaylistUiEvent

sealed class PlaylistEditUiEvent {
    data class OnClickPlayTrack(val trackId: String): PlaylistEditUiEvent()
    data class OnClickHold(val trackId: String): PlaylistEditUiEvent()
}