package com.example.presentation.ui.home.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.entity.playlist.PlaylistMinimal
import com.example.domain.entity.playlist.PlaylistSortOrderDomain
import com.example.domain.usecase.PlaylistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val useCase: PlaylistUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: LibraryUiEvent) {
        when (event) {
            is LibraryUiEvent.onLibraryCategoryClicked -> {
                _uiState.update {
                    it.copy(
                        libraryCategory = event.category
                    )
                }
            }

            is LibraryUiEvent.onPlaylistSortOrderClicked -> {}
            LibraryUiEvent.onCreatePlaylistClicked -> {}
            LibraryUiEvent.onSearchClicked -> {}
            LibraryUiEvent.onLibraryViewModesClicked -> {}
            LibraryUiEvent.onSortByClicked -> {
                showSortSheet()
            }
        }
    }

    val myPlaylists: Flow<PagingData<PlaylistMinimal>> = uiState
        .flatMapLatest { uiState ->
            useCase.getMyPlaylistFeedUseCase.invoke(uiState.currentSort)
        }
        .cachedIn(viewModelScope)

    fun showSortSheet() {
        _uiState.update { it.copy(isSortSheetVisible = true) }
    }

    fun hideSortSheet() {
        _uiState.update { it.copy(isSortSheetVisible = false) }
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