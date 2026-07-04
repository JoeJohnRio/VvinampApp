package com.example.presentation.ui.home.artist

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.ArtistUseCase
import com.example.domain.util.NetworkMonitor
import com.example.presentation.base.BaseViewModel
class ArtistViewModel(
    val artistUseCase: ArtistUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArtistUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ArtistUiEvent>()
    val events = _events.asSharedFlow()

    fun onEvent(event: ArtistEvent) {
        when (event) {
            is ArtistEvent.OnHistoryClicked -> {}
        }
    }

    fun clearUiState() {
        _uiState.update {
            it.copy(
                isLoading = false,
                artistProfile = null,
                topSongs = null,
                topReleases = null,
                error = null
            )
        }
    }

    fun updateDominantColor(color: Color) {
        _uiState.update { it.copy(dominantColor = color) }
    }

    fun getArtistScreenData(artistId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = artistUseCase.getArtistScreenData(artistId)

            result.onSuccess { data ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        artistProfile = data.artistProfile,
                        topSongs = data.topSongs,
                        topReleases = data.topReleases,
                        error = null
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to load artist data"
                    )
                }
            }
        }
    }
}
