package com.example.vvinamp.ui.main.playback

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import com.example.vvinamp.ui.navigationbar.trackoption.TrackOptionsUiEvent

expect class PlaybackViewModel : ViewModel {
    val uiState: StateFlow<PlaybackUiState>
    val mediaController: StateFlow<Any?>
    
    fun onEvent(event: PlaybackUiEvent)
    fun onEventTrackOptions(event: TrackOptionsUiEvent)
    fun hideSleepTimerSheet()
    fun isPlaying(): Boolean
    fun updateDominantColor(color: Color)
}
