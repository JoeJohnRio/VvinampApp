package com.example.vvinamp.ui.home.library.createplaylist

sealed class CreatePlaylistUiEvent {
    data class onChangePlaylistName(val playlistName: String) : CreatePlaylistUiEvent()
    object onCreatePlaylistClick : CreatePlaylistUiEvent()
}