package com.example.vvinamp.ui.main.playback

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

actual class PlaybackViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PlaybackUiState())
    actual val uiState: StateFlow<PlaybackUiState> = _uiState.asStateFlow()

    actual fun onEvent(event: PlaybackUiEvent) {
        // TODO: Implement iOS playback
    }

    actual fun isPlaying(): Boolean = false

    actual fun updateDominantColor(color: Color) {
        _uiState.value = _uiState.value.copy(dominantColor = color)
    }
}
