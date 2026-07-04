package com.example.vvinamp.ui.main.playback

import android.app.Application
import android.content.ComponentName
import android.util.Log
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
import com.example.domain.entity.enum.ContentTypeDomain
import com.example.domain.entity.enum.PlaybackContextType
import com.example.domain.entity.song.TrackDomain
import com.example.domain.entity.playbackinfo.toTrackDomain
import com.example.domain.usecase.PlaybackUseCase
import com.example.vvinamp.ui.navigationbar.trackoption.TrackOptionsUiEvent
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class PlaybackViewModel @OptIn(UnstableApi::class)
constructor(
    private val playbackRepository: PlaybackUseCase,
    private val application: Application
) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(PlaybackUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<PlaybackEvent>()
    val events = _events.asSharedFlow()

    private val _mediaController = MutableStateFlow<MediaController?>(null)
    val mediaController = _mediaController.asStateFlow()

    private var sleepTimerJob: Job? = null

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

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    if (uiState.value.activeSleepTimer == SleepTimerOption.END_OF_TRACK && reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO) {
                        pause()
                        setSleepTimer(null)
                    }
                }
            })
        }, MoreExecutors.directExecutor())

        // Load persisted track on startup
        viewModelScope.launch {
            Log.d("joel123", "ini jalan ")
            playbackRepository.getCurrentTrackFlow().collect { savedTrack ->
                Log.d("joel123", "ini jalan ${savedTrack?.albumCoverUrl}")
                if (savedTrack != null && _uiState.value.currentTrack == null) {
                    _uiState.update { it.copy(currentTrack = savedTrack) }
                }
            }
        }
    }

    fun onEventTrackOptions(event: TrackOptionsUiEvent) {
        when (event) {
            is TrackOptionsUiEvent.OnSleepTimerClick -> {
                showSleepTimerSheet()
            }
            is TrackOptionsUiEvent.OnSleepTimerSelected -> {
                setSleepTimer(event.option)
                hideSleepTimerSheet()
            }
            else -> {

            }
        }
    }

    fun onEvent(event: PlaybackUiEvent) {
        when (event) {
            is PlaybackUiEvent.TogglePlayback -> {
                togglePlayback()
            }

            is PlaybackUiEvent.ToggleLoopClicked -> {
                toggleLoopClicked()
            }

            is PlaybackUiEvent.ToggleShuffleClicked -> {
                toggleShuffleClicked()
            }

            is PlaybackUiEvent.OnTrackClicked -> {
                when (event.contentTypeDomain) {
                    ContentTypeDomain.TRACK -> {
                        trackClicked(
                            track = event.track,
                            contentId = event.track.id,
                            contentTypeDomain = ContentTypeDomain.TRACK
                        )
                    }

                    ContentTypeDomain.PLAYLIST, ContentTypeDomain.ALBUM, ContentTypeDomain.ARTIST -> {
                        trackClicked(
                            track = event.track,
                            contentId = event.contentId,
                            contentTypeDomain = event.contentTypeDomain
                        )
                    }

                    else -> {
                        trackClicked(
                            track = event.track,
                            contentId = event.track.id,
                            contentTypeDomain = ContentTypeDomain.TRACK
                        )
                    }
                }

            }

            is PlaybackUiEvent.PlayContext -> {
                playContext(
                    contextType = event.contextType,
                    contextId = event.contextId,
                    trackIds = event.trackIds,
                    offsetTrackId = event.offsetTrackId
                )
            }

            is PlaybackUiEvent.SkipToNext -> {
                skipToNext()
            }

            is PlaybackUiEvent.SkipToPrevious -> {
                skipToPrevious()
            }

            is PlaybackUiEvent.ClearQueue -> {
                clearQueue()
            }

            is PlaybackUiEvent.AddToQueue -> {
                addToQueue(event.trackIds)
            }

            is PlaybackUiEvent.OnYoutubeTrackClicked -> {
                playYoutube(event.track)
            }

            is PlaybackUiEvent.ToggleCreatePlaylistOptionSheet -> {
                if (event.isShow) {
                    showCreatePlaylistOptionSheet()
                } else {
                    hideCreatePlaylistOptionSheet()
                }
            }

            is PlaybackUiEvent.ToggleTrackOptionSheet -> {
                if (event.isShow) {
                    showTrackOptionSheet()
                } else {
                    hideTrackOptionSheet()
                }
            }

            is PlaybackUiEvent.OnSleepTimerSelected -> {
                setSleepTimer(event.option)
                hideSleepTimerSheet()
            }
        }
    }

    private fun setSleepTimer(option: SleepTimerOption?) {
        sleepTimerJob?.cancel()
        _uiState.update { it.copy(activeSleepTimer = option) }

        if (option != null && option.durationMillis != null) {
            sleepTimerJob = viewModelScope.launch {
                delay(option.durationMillis)
                pause()
                _uiState.update { it.copy(activeSleepTimer = null) }
            }
        }
    }

    fun showSleepTimerSheet() {
        _uiState.update { it.copy(isSleepTimerSheetVisible = true) }
    }

    fun hideSleepTimerSheet() {
        _uiState.update { it.copy(isSleepTimerSheetVisible = false) }
    }

    private fun playYoutube(track: TrackDomain) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            playbackRepository.playYoutube(track.id).onSuccess { playbackInfo ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        playbackInfo = playbackInfo,
                        currentTrack = track,
                        error = null
                    )
                }
                Log.d("joel123", "playyoutube")
                playbackRepository.saveCurrentTrack(track)
                start(BuildConfig.BASE_URL + playbackInfo.hlsUrl)
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to play YouTube track"
                    )
                }
            }
        }
    }

    private fun skipToNext() {
        viewModelScope.launch {
            playbackRepository.skipToNext().onSuccess { state ->
                val trackDomain = state.queue.currentTrack?.toTrackDomain()
                _uiState.update {
                    it.copy(
                        queue = state.queue,
                        playbackInfo = state.playbackInfo,
                        currentTrack = trackDomain,
                        error = null
                    )
                }
                Log.d("joel123", "skiptonext")
                trackDomain?.let { playbackRepository.saveCurrentTrack(it) }
                state.playbackInfo.let {
                    start(BuildConfig.BASE_URL + it.hlsUrl)
                }
            }.onFailure { error ->
                _uiState.update { it.copy(error = error.message) }
            }
        }
    }

    private fun skipToPrevious() {
        viewModelScope.launch {
            playbackRepository.skipToPrevious().onSuccess { state ->
                val trackDomain = state.queue.currentTrack?.toTrackDomain()
                _uiState.update {
                    it.copy(
                        queue = state.queue,
                        playbackInfo = state.playbackInfo,
                        currentTrack = trackDomain,
                        error = null
                    )
                }
                Log.d("joel123", "skiptoprevious")
                trackDomain?.let { playbackRepository.saveCurrentTrack(it) }
                state.playbackInfo.let {
                    start(BuildConfig.BASE_URL + it.hlsUrl)
                }
            }.onFailure { error ->
                _uiState.update { it.copy(error = error.message) }
            }
        }
    }

    private fun clearQueue() {
        viewModelScope.launch {
            playbackRepository.clearQueue().onSuccess { queue ->
                _uiState.update {
                    it.copy(
                        queue = queue,
                        error = null
                    )
                }
            }.onFailure { error ->
                _uiState.update { it.copy(error = error.message) }
            }
        }
    }

    private fun addToQueue(trackIds: List<String>) {
        viewModelScope.launch {
            playbackRepository.addToQueue(trackIds).onSuccess { queue ->
                _uiState.update {
                    it.copy(
                        queue = queue,
                        error = null
                    )
                }
            }.onFailure { error ->
                _uiState.update { it.copy(error = error.message) }
            }
        }
    }

    private fun playContext(
        contextType: PlaybackContextType,
        contextId: String?,
        trackIds: List<String>?,
        offsetTrackId: String
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            playbackRepository.playContext(
                contextType = contextType,
                contextId = contextId,
                trackIds = trackIds,
                offsetTrackId = offsetTrackId
            ).onSuccess { state ->
                Log.d("joel123", "playContext")
                val trackDomain = state.queue.currentTrack?.toTrackDomain()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        playbackInfo = state.playbackInfo,
                        queue = state.queue,
                        currentTrack = trackDomain,
                        error = null
                    )
                }
                trackDomain?.let { playbackRepository.saveCurrentTrack(it) }
                state.playbackInfo.let {
                    start(BuildConfig.BASE_URL + it.hlsUrl)
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to play context"
                    )
                }
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

    private fun toggleLoopClicked() {
        _uiState.update {
            it.copy(
                isLooping = !uiState.value.isLooping
            )
        }

        setRepeatMode(
            _uiState.value.isLooping.toRepeatInt()
        )
    }

    fun Boolean.toRepeatInt(): Int {
        if (true) {
            return Player.REPEAT_MODE_ALL
        } else {
            return Player.REPEAT_MODE_OFF
        }
    }

    fun setRepeatMode(mode: Int) {
        mediaController.value?.repeatMode = mode
    }

    private fun toggleShuffleClicked() {
        _uiState.update {
            it.copy(
                isShuffle = !uiState.value.isShuffle
            )
        }
    }

    private fun trackClicked(
        track: TrackDomain,
        contentTypeDomain: ContentTypeDomain,
        contentId: String
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            playbackRepository.playbackInfo.invoke(
                trackId = track.id,
                contentTypeDomain = contentTypeDomain,
                contentId = contentId
            ).onSuccess {
                _uiState.value = _uiState.value.copy(
                    playbackInfo = it,
                    currentTrack = track,
                    isLoading = false,
                    error = null
                )
                Log.d("joel123", "trackClicked")
                playbackRepository.saveCurrentTrack(track)
                start(BuildConfig.BASE_URL + it.hlsUrl)
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = it.message ?: "Failed to toggle playback"
                )
            }
        }
    }

    fun showCreatePlaylistOptionSheet() {
        _uiState.update {
            it.copy(
                isCreatePlaylistOptionSheet = true
            )
        }
    }

    fun hideCreatePlaylistOptionSheet() {
        _uiState.update {
            it.copy(
                isCreatePlaylistOptionSheet = false
            )
        }
    }

    fun showTrackOptionSheet() {
        _uiState.update {
            it.copy(
                isTrackOptionSheet = true
            )
        }
    }

    fun hideTrackOptionSheet() {
        _uiState.update {
            it.copy(
                isTrackOptionSheet = false
            )
        }
    }

    fun isPlaying(): Boolean = mediaController.value?.isPlaying == true

    fun updateDominantColor(color: Color) {
        _uiState.update { it.copy(dominantColor = color) }
    }

}
