package com.example.vvinamp.ui.home.library
import vvinampapp.composeapp.generated.resources.*

sealed class LibraryEvent {
    object NavigateToCreatePlaylist : LibraryEvent()
    object NavigateToSearchResult : LibraryEvent()

    data class NavigateToPlaylistDetail(val playlistId: String) : LibraryEvent()
}