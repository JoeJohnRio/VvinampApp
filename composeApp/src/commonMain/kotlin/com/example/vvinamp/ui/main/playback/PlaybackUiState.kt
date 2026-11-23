package com.example.vvinamp.ui.main.playback

import androidx.compose.ui.graphics.Color
import com.example.domain.entity.playbackinfo.PlaybackInfoDomain
import com.example.domain.entity.playbackinfo.PlayerQueueDomain
import com.example.domain.entity.song.TrackDomain

data class PlaybackUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPlaying: Boolean = false,
    val isLooping: Boolean = false,
    val isShuffle: Boolean = false,
    val playbackInfo: PlaybackInfoDomain? = null,
    val currentTrack: TrackDomain? = null,
    val queue: PlayerQueueDomain? = null,
    val dominantColor: Color = Color.Unspecified,
    val isCreatePlaylistOptionSheet: Boolean = false,
    val isTrackOptionSheet: Boolean = false,
    val isSleepTimerSheetVisible: Boolean = false,
    val activeSleepTimer: SleepTimerOption? = null,
    val isDownloading: Boolean = false,
    val downloadProgress: Int = 0,
    val youtubeEmbedId: String? = null,
    val isEmbedPaused: Boolean = false,
    val playingFrom: PlayingFrom? = null,
    // Bumped each time a YouTube song is started, so starting the same song again makes a fresh player.
    val embedSession: Int = 0,
    val embedPositionSeconds: Int = 0
)
