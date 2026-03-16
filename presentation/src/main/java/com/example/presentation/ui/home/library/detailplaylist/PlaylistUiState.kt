package com.example.presentation.ui.home.library.detailplaylist

import androidx.compose.ui.graphics.Color
import com.example.domain.entity.playlist.PlaylistComplete
import com.example.presentation.ui.theme.AppColours

data class PlaylistUiState(
    val error: String? = null,
    val isLoading: Boolean = false,
    val playlist: PlaylistComplete? = null,
    val dominantColor: Color = AppColours.BackgroundBlack
)