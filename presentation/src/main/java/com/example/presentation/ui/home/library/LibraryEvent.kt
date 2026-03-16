package com.example.presentation.ui.home.library

sealed class LibraryEvent {
    object NavigateToCreatePlaylist : LibraryEvent()

    data class NavigateToPlaylistDetail(val playlistId: String) : LibraryEvent()
}