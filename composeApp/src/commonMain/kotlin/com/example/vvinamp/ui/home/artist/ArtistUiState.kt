package com.example.vvinamp.ui.home.artist

import androidx.compose.ui.graphics.Color
import com.example.domain.entity.artist.ArtistProfileDomain
import com.example.domain.entity.song.TopReleasesDomain
import com.example.domain.entity.song.TopSongsDomain
import com.example.vvinamp.ui.theme.AppColours

data class ArtistUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val artistProfile: ArtistProfileDomain? = null,
    val topSongs: TopSongsDomain? = null,
    val topReleases: TopReleasesDomain? = null,
    val dominantColor: Color = AppColours.BackgroundBlack
)