package com.example.presentation.ui.home.library.createplaylist

sealed class CreatePlaylistUiEvent {
    data class onChangePlaylistName(val playlistName: String) : CreatePlaylistUiEvent()
    object onCreatePlaylistClick : CreatePlaylistUiEvent()
}