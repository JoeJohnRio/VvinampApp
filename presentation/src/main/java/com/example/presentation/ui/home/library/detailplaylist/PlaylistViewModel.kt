package com.example.presentation.ui.home.library.detailplaylist

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.data.api.playlist.toChecked
import com.example.domain.entity.song.TrackDomain
import com.example.domain.usecase.PlaylistUseCase
import com.example.presentation.ui.home.library.detailplaylist.PlaylistEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
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
    private val useCase: PlaylistUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlaylistUiState())
    val uiState = _uiState.asStateFlow()

    val playlistTracks: Flow<PagingData<TrackDomain>> = uiState
        .flatMapLatest { uiState ->
            useCase.getPlaylistTracksUseCase.invoke(uiState.playlist?.id.orEmpty())
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

            }

            PlaylistUiEvent.onClickChange -> {

            }

            PlaylistUiEvent.onClickDetail -> {

            }

            PlaylistUiEvent.onClickShare -> {

            }

            PlaylistUiEvent.onClickPlayAll -> {

            }

            PlaylistUiEvent.ToggleShuffleClicked -> {
                toggleShuffleClicked()
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

            val result = useCase.addSongToPlaylistUseCase.invoke(
                playlistId = playlistId,
                trackId = trackId
            )
            result.onSuccess { data ->
                val updatedTracks = uiState.value.recommendedTracks?.map { track ->
                    if (track.id == trackId) track.toChecked() else track
                }

                _uiState.update {
                    it.copy(
                        recommendedTracks = updatedTracks
                    )
                }
            }.onFailure {
                _uiState.update {
                    it.copy(
                        error = it.error
                    )
                }
            }
        }
    }

    fun getPlaylist(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = useCase.getPlaylistUseCase.invoke(id)
            result.onSuccess { data ->
                Log.d("joel123", "${data}")
                _uiState.update {
                    it.copy(
                        playlist = data,
                        isLoading = false
                    )
                }
                getRecommendedTracks()
            }.onFailure {
                _uiState.update {
                    it.copy(
                        error = it.error,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun getRecommendedTracks() {
        viewModelScope.launch {

            val result = useCase.getRecommendedTracksUseCase.invoke()

            result.onSuccess { data ->
                _uiState.update {
                    it.copy(
                        recommendedTracks = data,
                    )
                }
            }.onFailure {
                _uiState.update {
                    it.copy(
                        error = it.error,
                    )
                }
            }
        }
    }
}