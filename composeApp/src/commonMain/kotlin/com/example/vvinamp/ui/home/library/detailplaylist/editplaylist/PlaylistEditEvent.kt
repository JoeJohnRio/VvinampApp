package com.example.vvinamp.ui.home.library.detailplaylist.editplaylist

sealed class PlaylistEditEvent {
    data class PlayTrack(val playlistId: String, val trackId: String): PlaylistEditEvent()
    data object SaveSuccess : PlaylistEditEvent()
}
