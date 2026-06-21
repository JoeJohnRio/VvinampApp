package com.example.presentation.ui.home.library

sealed class LibraryEvent {
    object NavigateToCreatePlaylist : LibraryEvent()
    object NavigateToSearchResult : LibraryEvent()

    data class NavigateToPlaylistDetail(val playlistId: String) : LibraryEvent()
}