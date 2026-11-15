package com.example.vvinamp.ui.home.album

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import com.example.domain.entity.album.AlbumDomain
import com.example.domain.entity.album.SimplifiedTrackDomain
import com.example.vvinamp.util.preview.PreviewTheme

data class AlbumUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val album: AlbumDomain? = null,
    val tracks: List<SimplifiedTrackDomain> = listOf(),
    val dominantColor: Color = Color.Unspecified
)