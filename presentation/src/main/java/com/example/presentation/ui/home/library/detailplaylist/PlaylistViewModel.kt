package com.example.presentation.ui.home.library.detailplaylist

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.data.api.playlist.toChecked
import com.example.domain.entity.playlist.PlaylistTrackSortByDomain
import com.example.domain.entity.song.TrackDomain
import com.example.domain.usecase.AuthUseCases
import com.example.domain.usecase.PlaylistUseCase
import com.example.presentation.ui.home.library.detailplaylist.PlaylistEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val useCase: PlaylistUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlaylistUiState())
    val uiState = _uiState.asStateFlow()

    val playlistTracks: Flow<PagingData<TrackDomain>> = uiState
        .flatMapLatest { state ->
            useCase.getPlaylistTracksUseCase.invoke(
                id = state.playlist?.id.orEmpty(),
                sortBy = state.sortBy
            )
        }
        .cachedIn(viewModelScope)

    private val _events = MutableSharedFlow<PlaylistEvent>()
    val events = _events.asSharedFlow()

    fun onEvent(event: PlaylistUiEvent) {
        when (event) {
            is PlaylistUiEvent.onClickAdd -> {
                addSongsToPlaylist(
                    playlistId = event.playlistId,
                    trackId = event.trackId
                )
            }

            is PlaylistUiEvent.onClickTrack -> {
                viewModelScope.launch {
                    _events.emit(
                        PlayTrack(
                            playlistId = event.playlistId,
                            trackId = event.trackId
                        )
                    )
                }
            }

            is PlaylistUiEvent.onClickTrackRecommended -> {
                viewModelScope.launch {
                    _events.emit(
                        PlayTrackRecommended(
                            track = event.track
                        )
                    )
                }
            }

            is PlaylistUiEvent.onRowClickEdit -> {
                viewModelScope.launch {
                    _events.emit(
                        NavigateToEditScreen(event.playlistId)
                    )
                }
            }

            is PlaylistUiEvent.onClickPlayAll -> {
                viewModelScope.launch {
                    _events.emit(
                        PlayTrack(
                            playlistId = event.playlistId,
                            trackId = ""
                        )
                    )
                }
            }

            PlaylistUiEvent.onClickChange -> {

            }

            PlaylistUiEvent.onClickDetail -> {

            }

            PlaylistUiEvent.onClickShare -> {

            }

            PlaylistUiEvent.ToggleShuffleClicked -> {
                toggleShuffleClicked()
            }

            PlaylistUiEvent.onRowClickAdd -> {

            }

            PlaylistUiEvent.onRowClickEditDetail -> {
                showEditDetailSheet()
            }

            PlaylistUiEvent.onRowClickSort -> {
                showSortSheet()
            }

            is PlaylistUiEvent.onEditNameChanged -> {
                _uiState.update { it.copy(editName = event.name) }
            }

            is PlaylistUiEvent.onEditDescriptionChanged -> {
                _uiState.update { it.copy(editDescription = event.description) }
            }

            PlaylistUiEvent.onSaveEditDetail -> {
                savePlaylistDetail()
            }

            PlaylistUiEvent.onCancelEditDetail -> {
                hideEditDetailSheet()
            }

            PlaylistUiEvent.onMakePrivate -> {
                // Implement make private logic
            }

            PlaylistUiEvent.onDeletePlaylist -> {
                // Implement delete playlist logic
            }
        }
    }

    fun showSortSheet() {
        _uiState.update { it.copy(isSortSheetVisible = true) }
    }

    fun hideSortSheet() {
        _uiState.update { it.copy(isSortSheetVisible = false) }
    }

    fun updateSort(sortBy: PlaylistTrackSortByDomain) {
        _uiState.update {
            it.copy(
                sortBy = sortBy,
                isSortSheetVisible = false
            )
        }
    }

    private fun showEditDetailSheet() {
        _uiState.update {
            it.copy(
                isEditDetailSheetVisible = true,
                editName = it.playlist?.title.orEmpty(),
                editDescription = it.playlist?.description.orEmpty()
            )
        }
    }

    private fun hideEditDetailSheet() {
        _uiState.update { it.copy(isEditDetailSheetVisible = false) }
    }

    private fun savePlaylistDetail() {
        // Implement save logic via useCase
        hideEditDetailSheet()
    }

    fun setIsPlaylistCreator() {
        viewModelScope.launch {
            authUseCases.getCurrentUserDetail.invoke().onSuccess { data ->
                _uiState.update {
                    it.copy(
                        isPlaylistCreator = it.playlist?.creator?.id == data.id
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        error = error.message
                    )
                }
            }
        }
    }

    fun updateDominantColor(color: Color) {
        _uiState.update { it.copy(dominantColor = color) }
    }

    fun toggleShuffleClicked() {
        viewModelScope.launch {
            _events.emit(OnClickShuffle)
        }
    }

    fun addSongsToPlaylist(
        playlistId: String,
        trackId: String
    ) {
        viewModelScope.launch {
            useCase.addSongToPlaylistUseCase.invoke(
                playlistId = playlistId,
                trackId = trackId
            ).onSuccess { data ->
                val updatedTracks = uiState.value.recommendedTracks?.map { track ->
                    if (track.id == trackId) track.toChecked() else track
                }

                _uiState.update {
                    it.copy(
                        recommendedTracks = updatedTracks
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        error = error.message
                    )
                }
            }
        }
    }

    fun getPlaylist(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            useCase.getPlaylistUseCase.invoke(id).onSuccess { data ->
                _uiState.update {
                    it.copy(
                        playlist = data,
                        isLoading = false
                    )
                }
                setIsPlaylistCreator()
                getRecommendedTracks()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        error = error.message,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun getRecommendedTracks() {
        viewModelScope.launch {
            useCase.getRecommendedTracksUseCase.invoke().onSuccess { data ->
                _uiState.update {
                    it.copy(
                        recommendedTracks = data,
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        error = error.message,
                    )
                }
            }
        }
    }
}
