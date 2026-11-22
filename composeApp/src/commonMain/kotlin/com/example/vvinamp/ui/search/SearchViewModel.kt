package com.example.vvinamp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.entity.search.SearchItemDomain
import com.example.domain.repository.SearchRepository
import com.example.domain.entity.playlist.PlaylistMinimal
import com.example.domain.usecase.PlaylistUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.vvinamp.util.isDuplicateTrackError
import com.example.vvinamp.util.showToast

class SearchViewModel(
    private val repository: SearchRepository,
    private val playlistUseCase: PlaylistUseCase? = null,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    val myPlaylists: Flow<PagingData<PlaylistMinimal>> = (playlistUseCase?.getMyPlaylistUseCase?.invoke() ?: emptyFlow())
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchResults: Flow<PagingData<SearchItemDomain>> = _uiState
        .map { it.activeQuery to it.searchType }
        .distinctUntilChanged()
        .debounce { (_, searchType) ->
            if (searchType == SearchType.CATALOG) CATALOG_DEBOUNCE_MS else 0L
        }
        .flatMapLatest { (searchText, searchType) ->
            if (searchText.isBlank()) return@flatMapLatest emptyFlow()

            when (searchType) {
                SearchType.CATALOG -> repository.search(
                    query = searchText,
                    pageSize = 10
                )
                SearchType.YOUTUBE -> repository.searchYoutubeFromApi(
                    query = searchText,
                    pageSize = 10
                )
            }
        }
        .cachedIn(viewModelScope)

    fun onSearchInput(query: String) {
        _uiState.update {
            it.copy(
                searchText = query,
                submittedText = if (query.isBlank()) "" else it.submittedText
            )
        }
    }

    fun onSearchSubmit() {
        _uiState.update { it.copy(submittedText = it.searchText.trim()) }
    }

    fun onSearchTypeChanged(type: SearchType) {
        _uiState.update {
            it.copy(
                searchType = type,
                submittedText = it.searchText.trim()
            )
        }
    }

    fun setTargetPlaylist(playlistId: String?, playlistTitle: String?) {
        _uiState.update { it.copy(targetPlaylistId = playlistId, targetPlaylistTitle = playlistTitle) }
    }

    // Catalog and YouTube songs take the same path: straight into the target playlist, or the picker without one.
    fun onAddClicked(item: SearchItemDomain) {
        val state = _uiState.value
        val targetId = state.targetPlaylistId
        if (item.addKey() in state.addingIds) return

        if (targetId == null) {
            _uiState.update { it.copy(trackForPlaylist = item) }
        } else if (!state.isAddedToTarget(item.addKey())) {
            addToPlaylist(item, targetId, state.targetPlaylistTitle ?: "playlist")
        }
    }

    fun onDismissAddToPlaylist() {
        _uiState.update { it.copy(trackForPlaylist = null) }
    }

    fun addTrackToPlaylist(playlistId: String, playlistTitle: String) {
        val track = _uiState.value.trackForPlaylist ?: return
        addToPlaylist(track, playlistId, playlistTitle)
    }

    private fun addToPlaylist(item: SearchItemDomain, playlistId: String, playlistTitle: String) {
        val useCase = playlistUseCase ?: return
        val id = item.addKey()

        viewModelScope.launch {
            _uiState.update { it.copy(isAddingToPlaylist = true, addingIds = it.addingIds + id) }
            val result = when (item) {
                is SearchItemDomain.TrackSearch -> useCase.addSongToPlaylistUseCase.invoke(
                    playlistId = playlistId,
                    trackId = item.id
                )
                is SearchItemDomain.YoutubeTrackSearch -> useCase.addYoutubeTrackToPlaylistUseCase.invoke(
                    playlistId = playlistId,
                    youtubeId = item.id,
                    title = item.title,
                    artistName = item.channel,
                    thumbnailUrl = item.thumbnail,
                    duration = item.duration
                )
                else -> return@launch
            }

            val added = result.isSuccess || result.exceptionOrNull()?.isDuplicateTrackError() == true
            _uiState.update {
                it.copy(
                    isAddingToPlaylist = false,
                    addingIds = it.addingIds - id,
                    trackForPlaylist = if (added) null else it.trackForPlaylist,
                    addedKeys = if (added) it.addedKeys + "$playlistId/$id" else it.addedKeys
                )
            }
            when {
                result.isSuccess -> showToast("Added to $playlistTitle")
                added -> showToast("Already in $playlistTitle")
                else -> showToast("Couldn't add: ${result.exceptionOrNull()?.message ?: "try again"}")
            }
        }
    }

    private companion object {
        const val CATALOG_DEBOUNCE_MS = 500L
    }
}
