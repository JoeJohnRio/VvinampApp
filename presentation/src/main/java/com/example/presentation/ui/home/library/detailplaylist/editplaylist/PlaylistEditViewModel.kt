package com.example.presentation.ui.home.library.detailplaylist.editplaylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.entity.song.TrackDomain
import com.example.domain.usecase.PlaylistUseCase
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
class PlaylistEditViewModel @Inject constructor(
    private val useCase: PlaylistUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlaylistEditUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<PlaylistEditEvent>()
    val events = _events.asSharedFlow()

    fun onEvent(event: PlaylistEditUiEvent) {
        when (event) {
            is PlaylistEditUiEvent.OnClickHold -> {

            }
            is PlaylistEditUiEvent.OnClickPlayTrack -> playtrackFromPlaylist(event.trackId)
            is PlaylistEditUiEvent.OnSave -> savePlaylist(
                tracks = event.tracks,
                movedTrackIds = event.movedTrackIds,
                deletedTrackIds = event.deletedTrackIds
            )
            is PlaylistEditUiEvent.OnDelete -> {

            }
        }
    }

    val playlistTracks: Flow<PagingData<TrackDomain>> = uiState
        .flatMapLatest { uiState ->
            useCase.getPlaylistTracksUseCase.invoke(uiState.playlistId)
        }
        .cachedIn(viewModelScope)

    private fun playtrackFromPlaylist(
        trackId: String
    ) {
        viewModelScope.launch {
            _events.emit(
                PlaylistEditEvent.PlayTrack(
                    playlistId = uiState.value.playlistId,
                    trackId = trackId
                )
            )
        }
    }

    private fun savePlaylist(
        tracks: List<TrackDomain>,
        movedTrackIds: Set<String>,
        deletedTrackIds: Set<String>
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val repositionedSongs = tracks
                .mapIndexed { index, track -> Pair(track.id, index) }
                .filter { (trackId, _) -> movedTrackIds.contains(trackId) }

            useCase.editPlaylistUseCase.invoke(
                playlistId = uiState.value.playlistId,
                deletedTrackIds = deletedTrackIds.toList().ifEmpty { null },
                repositionedSongs = repositionedSongs.ifEmpty { null }
            ).onSuccess {
                _uiState.update { it.copy(isLoading = false) }
                _events.emit(PlaylistEditEvent.SaveSuccess)
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, error = error.message ?: "Update failed") }
            }
        }
    }

    fun setPlaylistId(playlistId: String) {
        _uiState.update {
            it.copy(
                playlistId = playlistId
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
