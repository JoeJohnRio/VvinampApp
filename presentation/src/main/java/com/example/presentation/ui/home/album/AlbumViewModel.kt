package com.example.presentation.ui.home.album

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.AlbumUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
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