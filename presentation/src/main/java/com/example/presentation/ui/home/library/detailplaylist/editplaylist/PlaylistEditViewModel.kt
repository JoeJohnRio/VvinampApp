package com.example.presentation.ui.home.library.detailplaylist.editplaylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.entity.song.TrackDomain
import com.example.domain.usecase.PlaylistUseCase
import com.example.presentation.ui.home.library.detailplaylist.PlaylistEvent
import com.example.presentation.ui.home.library.detailplaylist.PlaylistEvent.PlayTrack
import com.example.presentation.ui.home.library.detailplaylist.PlaylistUiEvent
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

    fun setPlaylistId(playlistId: String) {
        _uiState.update {
            it.copy(
                playlistId = playlistId
            )
        }
    }
}