package com.example.youtubemusicclone.ui.home

data class HomeUiState(
    val showLoading: Boolean = true,
    val errorMessage: String? = null,
)

sealed class HomeNavigationState {
    data class MusicDetails(val movieId: Int) : HomeNavigationState()
}
