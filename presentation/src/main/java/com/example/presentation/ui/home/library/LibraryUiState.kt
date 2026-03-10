package com.example.presentation.ui.home.library

import com.example.domain.entity.playlist.LibraryCategory
import com.example.domain.entity.playlist.PlaylistSortOrderDomain

data class LibraryUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val libraryCategory: LibraryCategory = LibraryCategory.ALL,
    val currentSort: PlaylistSortOrderDomain = PlaylistSortOrderDomain.RECENTS,
    val isSortSheetVisible: Boolean = false, // The toggle
)