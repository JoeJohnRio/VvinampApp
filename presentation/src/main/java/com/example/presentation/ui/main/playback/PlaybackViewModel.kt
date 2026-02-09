package com.example.presentation.ui.main.playback

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.data.BuildConfig
import com.example.domain.entity.song.TrackDomain
import com.example.domain.usecase.PlaybackUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlaybackViewModel @Inject constructor(
    private val playbackRepository: PlaybackUseCase,
    private val exoPlayer: ExoPlayer
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlaybackUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<PlaybackUiEvent>()
    val events = _events.asSharedFlow()

    init {
        exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _uiState.value = _uiState.value.copy(isPlaying = isPlaying)
            }
        })
    }
    fun onEvent(event: PlaybackUiEvent) {
        when (event) {
            is PlaybackUiEvent.TogglePlayback -> {
                togglePlayback()
            }

            is PlaybackUiEvent.TrackClicked -> {
                trackClicked(event.track)
            }
        }
    }

    fun start(url: String) {
        val mediaItem = MediaItem.fromUri(url)
        exoPlayer.apply {
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    fun play() {
        exoPlayer.play()
    }

    fun pause() {
        exoPlayer.pause()
    }

    fun togglePlayPause() {
        exoPlayer.let {
            if (it.isPlaying) {
                it.pause()
            } else {
                it.play()
            }
        }
    }

    fun release() {
        exoPlayer.release()
    }

    private fun togglePlayback() {
        if (isPlaying()) {
            pause()
        } else {
            play()
        }
    }

    private fun trackClicked(track: TrackDomain) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            playbackRepository.playbackInfo.invoke(track.id).onSuccess {
                _uiState.value = _uiState.value.copy(
                    playbackInfo = it,
                    currentlyPlaying = track,
                    isLoading = false,
                    error = null
                )
                start(BuildConfig.BASE_URL + it.hlsUrl)
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = it.message ?: "Failed to toggle playback"
                )
            }
        }
    }
    fun isPlaying(): Boolean = exoPlayer.isPlaying

}