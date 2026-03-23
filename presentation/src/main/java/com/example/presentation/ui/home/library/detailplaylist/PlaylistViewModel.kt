package com.example.presentation.ui.home.library.detailplaylist

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.entity.song.TrackDomain
import com.example.domain.usecase.PlaylistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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

    fun onEvent(event: PlaylistUiEvent) {
        when (event) {
            PlaylistUiEvent.onClickChange -> {

            }

            PlaylistUiEvent.onClickDetail -> {

            }

            PlaylistUiEvent.onClickShare -> {

            }
        }
    }

    fun updateDominantColor(color: Color) {
        _uiState.update { it.copy(dominantColor = color) }
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
}