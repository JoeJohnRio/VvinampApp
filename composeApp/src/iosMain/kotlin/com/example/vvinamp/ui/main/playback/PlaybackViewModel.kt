package com.example.vvinamp.ui.main.playback

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.vvinamp.ui.navigationbar.trackoption.TrackOptionsUiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

actual class PlaybackViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PlaybackUiState())
    actual val uiState: StateFlow<PlaybackUiState> = _uiState.asStateFlow()

    actual val mediaController: StateFlow<Any?> = MutableStateFlow<Any?>(null).asStateFlow()

    actual fun onEvent(event: PlaybackUiEvent) {
        // TODO: Implement iOS playback (AVPlayer)
    }

    actual fun onEventTrackOptions(event: TrackOptionsUiEvent) {
        // TODO: Implement track options
    }

    actual fun hideSleepTimerSheet() {
        // TODO: Implement
    }

    actual fun isPlaying(): Boolean = false

    actual fun updateDominantColor(color: Color) {
        _uiState.value = _uiState.value.copy(dominantColor = color)
    }
}
