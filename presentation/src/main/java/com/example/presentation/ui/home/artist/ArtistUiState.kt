package com.example.presentation.ui.home.artist

import com.example.domain.entity.artist.ArtistProfileDomain
import com.example.domain.entity.song.TopReleasesDomain
import com.example.domain.entity.song.TopSongsDomain

data class ArtistUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val artistProfile: ArtistProfileDomain? = null,
    val topSongs: TopSongsDomain? = null,
    val topReleases: TopReleasesDomain? = null,
)