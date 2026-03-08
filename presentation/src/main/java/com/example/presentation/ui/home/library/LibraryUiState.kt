package com.example.presentation.ui.home.library

import com.example.domain.entity.playlist.PlaylistSortOrderDomain

data class LibraryUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val sortType: PlaylistSortOrderDomain = PlaylistSortOrderDomain.RECENTS,
)