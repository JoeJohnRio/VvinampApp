package com.example.presentation.ui.home.library

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.entity.playlist.LibraryCategory
import com.example.domain.entity.playlist.PlaylistMinimal
import com.example.domain.entity.playlist.PlaylistSortOrderDomain
import com.example.domain.usecase.PlaylistUseCase
import com.example.presentation.ui.home.HomeEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val useCase: PlaylistUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState = _uiState.asStateFlow()
    private val _events = MutableSharedFlow<LibraryEvent>()
    val events = _events.asSharedFlow()

    fun onEvent(event: LibraryUiEvent) {
        when (event) {
            is LibraryUiEvent.onLibraryCategoryClicked -> {
                updateLibraryCategory(event.category)
            }

            is LibraryUiEvent.onPlaylistClicked -> {
                onPlaylistClicked(event.playlistId)
            }

            LibraryUiEvent.onCreateClicked -> {
                showCreatePlaylistOptionSheet()
            }
            LibraryUiEvent.onSearchClicked -> {
                navigateToSearchResultScreen()
            }
            LibraryUiEvent.onLibraryViewModesClicked -> {
                toggleLibraryViewMode()
            }
            LibraryUiEvent.onSortByClicked -> {
                showSortSheet()
            }

            LibraryUiEvent.onCreateNewPlaylistClicked -> {
                navigateToCreatePlaylist()
            }

        }
    }

    val myPlaylists: Flow<PagingData<PlaylistMinimal>> = uiState
        .flatMapLatest { uiState ->
            useCase.getMyPlaylistFeedUseCase.invoke(uiState.currentSort)
        }
        .cachedIn(viewModelScope)

    fun onPlaylistClicked(playlistId: String) {
        viewModelScope.launch {
            _events.emit(LibraryEvent.NavigateToPlaylistDetail(playlistId))
        }
    }

    fun updateLibraryCategory(category: LibraryCategory) {
        _uiState.update {
            it.copy(
                libraryCategory = category
            )
        }
    }

    fun navigateToCreatePlaylist() {
        viewModelScope.launch {
            hideCreatePlaylistSheet()
            _events.emit(LibraryEvent.NavigateToCreatePlaylist)
        }
    }

    fun navigateToSearchResultScreen() {
        viewModelScope.launch {
            hideCreatePlaylistSheet()
            _events.emit(LibraryEvent.NavigateToSearchResult)
        }
    }

    fun showSortSheet() {
        _uiState.update { it.copy(isSortSheetVisible = true) }
    }
    fun hideSortSheet() {
        _uiState.update { it.copy(isSortSheetVisible = false) }
    }

    fun showCreatePlaylistOptionSheet() {
        _uiState.update { it.copy(isCreatePlaylistOptionSheetVisible = true) }
    }
    fun hideCreatePlaylistSheet() {
        _uiState.update { it.copy(isCreatePlaylistOptionSheetVisible = false) }
    }

    fun toggleLibraryViewMode() {
        _uiState.update { it.copy(isLibraryViewModeGrid = !it.isLibraryViewModeGrid) }
    }


    fun updateSort(newSort: PlaylistSortOrderDomain) {
        _uiState.update {
            it.copy(
                currentSort = newSort,
                isSortSheetVisible = false // Auto-hide after selection
            )
        }
    }
}