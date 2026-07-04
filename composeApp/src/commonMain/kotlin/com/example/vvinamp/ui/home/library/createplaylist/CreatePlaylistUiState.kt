package com.example.vvinamp.ui.home.library.createplaylist

data class CreatePlaylistUiState(
    val error: String? = null,
    val isLoading: Boolean = false,
    val playlistId: String? = null,
    val playlistName: String = ""
)