package com.example.vvinamp.ui.home.library

import com.example.domain.entity.playlist.LibraryCategory
import com.example.domain.entity.playlist.PlaylistSortOrderDomain

sealed class LibraryUiEvent {
    data class onPlaylistClicked(val playlistId: String): LibraryUiEvent()
    data class onLibraryCategoryClicked(val category: LibraryCategory) : LibraryUiEvent()
    object onLibraryViewModesClicked: LibraryUiEvent()
    object onSortByClicked: LibraryUiEvent()
    object onCreateClicked: LibraryUiEvent()

    object onCreateNewPlaylistClicked: LibraryUiEvent()
    object onSearchClicked: LibraryUiEvent()
}