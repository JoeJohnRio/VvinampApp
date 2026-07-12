package com.example.vvinamp.ui.main.playback

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

expect class PlaybackViewModel : ViewModel {
    val uiState: StateFlow<PlaybackUiState>
    fun onEvent(event: PlaybackUiEvent)
    fun isPlaying(): Boolean
    fun updateDominantColor(color: Color)
}
