package com.example.presentation.ui.main.playback

import androidx.compose.ui.graphics.Color
import com.example.domain.entity.playbackinfo.PlaybackInfoDomain
import com.example.domain.entity.playbackinfo.PlayerQueueDomain
import com.example.domain.entity.song.TrackDomain
import com.example.presentation.ui.theme.AppColours

data class PlaybackUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPlaying: Boolean = false,
    val isLooping: Boolean = false,
    val isShuffle: Boolean = false,
    val playbackInfo: PlaybackInfoDomain? = null,
    val currentTrack: TrackDomain? = null,
    val queue: PlayerQueueDomain? = null,
    val dominantColor: Color = AppColours.BackgroundBlack
)