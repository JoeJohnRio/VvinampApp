package com.example.presentation.ui.main.playback

import com.example.domain.entity.playbackinfo.PlaybackInfoDomain
import com.example.domain.entity.song.TrackDomain

data class PlaybackUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPlaying: Boolean = false,
    val playbackInfo: PlaybackInfoDomain? = null,
    val currentlyPlaying: TrackDomain? = null
)