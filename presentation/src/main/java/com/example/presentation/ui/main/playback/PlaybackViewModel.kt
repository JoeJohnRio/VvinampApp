package com.example.presentation.ui.main.playback

import android.app.Application
import android.content.ComponentName
import androidx.annotation.OptIn
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.data.BuildConfig
import com.example.domain.entity.song.TrackDomain
import com.example.domain.usecase.PlaybackUseCase
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlaybackViewModel @OptIn(UnstableApi::class)
@Inject constructor(
    private val playbackRepository: PlaybackUseCase,
    private val application: Application
) : AndroidViewModel(application)  {
    private val _uiState = MutableStateFlow(PlaybackUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<PlaybackUiEvent>()
    val events = _events.asSharedFlow()

    private val _mediaController = MutableStateFlow<MediaController?>(null)
    val mediaController = _mediaController.asStateFlow()

    init {
        val sessionToken =
            SessionToken(application, ComponentName(application, PlaybackService::class.java))
        val controllerFuture = MediaController.Builder(application, sessionToken).buildAsync()

        // Inside your ViewModel
        controllerFuture.addListener({
            val controller = controllerFuture.get()
            _mediaController.update { controller }

            // Attach the listener here
            controller.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    // Update your UI state here
                    _uiState.value = _uiState.value.copy(isPlaying = isPlaying)
                }

                // You can also override other events like buffering or errors
                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    if (playbackState == Player.STATE_BUFFERING) {
                        // Show loading spinner
                    }
                }
            })
        }, MoreExecutors.directExecutor())
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
        mediaController.value?.setMediaItem(mediaItem)
        mediaController.value?.prepare()
        mediaController.value?.play()
    }

    override fun onCleared() {
        super.onCleared()
        mediaController.value?.release()
    }

    fun play() {
        mediaController.value?.play()
    }

    fun pause() {
        mediaController.value?.pause()
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
                    currentTrack = track,
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
    fun isPlaying(): Boolean = mediaController.value?.isPlaying == true

    fun updateDominantColor(color: Color) {
        _uiState.update { it.copy(dominantColor = color) }
    }

}