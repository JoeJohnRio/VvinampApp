package com.example.presentation.ui.home.library.createplaylist

data class CreatePlaylistUiState(
    val error: String? = null,
    val isLoading: Boolean = false,
    val playlistId: String? = null,
    val playlistName: String = ""
)