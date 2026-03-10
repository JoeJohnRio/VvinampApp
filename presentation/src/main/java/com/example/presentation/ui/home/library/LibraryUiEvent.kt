package com.example.presentation.ui.home.library

import com.example.domain.entity.playlist.LibraryCategory
import com.example.domain.entity.playlist.PlaylistSortOrderDomain

sealed class LibraryUiEvent {
    data class onLibraryCategoryClicked(val category: LibraryCategory) : LibraryUiEvent()
    data class onPlaylistSortOrderClicked(val sortOrder: PlaylistSortOrderDomain) : LibraryUiEvent()
    object onLibraryViewModesClicked: LibraryUiEvent()
    object onSortByClicked: LibraryUiEvent()
    object onCreatePlaylistClicked: LibraryUiEvent()
    object onSearchClicked: LibraryUiEvent()
}