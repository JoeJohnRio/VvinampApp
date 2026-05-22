package com.example.presentation.ui.home.library.detailplaylist.editplaylist

import com.example.presentation.ui.home.library.detailplaylist.PlaylistEvent

sealed class PlaylistEditEvent {
    data class PlayTrack(val playlistId: String, val trackId: String): PlaylistEditEvent()
}
