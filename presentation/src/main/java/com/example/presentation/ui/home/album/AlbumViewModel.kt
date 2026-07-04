package com.example.presentation.ui.home.album

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.AlbumUseCase
class AlbumViewModel(
    val albumUseCase: AlbumUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AlbumUiState())
    val uiState = _uiState.asStateFlow()

    fun getAlbum(albumId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = albumUseCase.getAlbum(albumId)

            result.onSuccess { data ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        album = data,
                        tracks = data.tracks,
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to load album data"
                    )
                }
            }
        }
    }

    fun updateDominantColor(color: Color) {
        _uiState.update { it.copy(dominantColor = color) }
    }
}