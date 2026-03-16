package com.example.presentation.ui.home.library.createplaylist


sealed class CreatePlaylistEvent {
    data class NavigateToPlaylistDetail(val playlistId: String) : CreatePlaylistEvent()
}