package com.example.vvinamp.ui.main.playback

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.BuildKonfig
import com.example.domain.entity.enum.ContentTypeDomain
import com.example.domain.entity.enum.PlaybackContextType
import com.example.domain.entity.song.TrackDomain
import com.example.domain.entity.playbackinfo.toTrackDomain
import com.example.domain.usecase.PlaybackUseCase
import com.example.vvinamp.ui.navigationbar.trackoption.TrackOptionsUiEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.browser.document
import org.w3c.dom.HTMLAudioElement

actual class PlaybackViewModel(
    private val playbackRepository: PlaybackUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlaybackUiState())
    actual val uiState: StateFlow<PlaybackUiState> = _uiState.asStateFlow()

    private val _mediaController = MutableStateFlow<Any?>(null)
    actual val mediaController: StateFlow<Any?> = _mediaController.asStateFlow()

    private val audioElement = (document.createElement("audio") as HTMLAudioElement).apply {
        onplay = { _uiState.update { it.copy(isPlaying = true) } }
        onpause = { _uiState.update { it.copy(isPlaying = false) } }
        onwaiting = { _uiState.update { it.copy(isLoading = true) } }
        onplaying = { _uiState.update { it.copy(isLoading = false) } }
    }

    private var sleepTimerJob: Job? = null

    init {
        _mediaController.value = audioElement
        viewModelScope.launch {
            playbackRepository.getCurrentTrackFlow().collect { savedTrack ->
                if (savedTrack != null && _uiState.value.currentTrack == null) {
                    _uiState.update { it.copy(currentTrack = savedTrack) }
                }
            }
        }
    }

    actual fun onEventTrackOptions(event: TrackOptionsUiEvent) {
        when (event) {
            is TrackOptionsUiEvent.OnSleepTimerClick -> { _uiState.update { it.copy(isSleepTimerSheetVisible = true) } }
            is TrackOptionsUiEvent.OnSleepTimerSelected -> {
                setSleepTimer(event.option)
                hideSleepTimerSheet()
            }
            else -> {}
        }
    }

    actual fun onEvent(event: PlaybackUiEvent) {
        when (event) {
            is PlaybackUiEvent.TogglePlayback -> togglePlayback()
            is PlaybackUiEvent.ToggleLoopClicked -> toggleLoopClicked()
            is PlaybackUiEvent.ToggleShuffleClicked -> toggleShuffleClicked()
            is PlaybackUiEvent.OnTrackClicked -> {
                trackClicked(event.track, event.contentTypeDomain ?: ContentTypeDomain.TRACK, event.contentId ?: event.track.id)
            }
            is PlaybackUiEvent.PlayContext -> {
                playContext(event.contextType, event.contextId, event.trackIds, event.offsetTrackId)
            }
            is PlaybackUiEvent.SkipToNext -> skipToNext()
            is PlaybackUiEvent.SkipToPrevious -> skipToPrevious()
            is PlaybackUiEvent.OnYoutubeTrackClicked -> playYoutube(event.track)
            is PlaybackUiEvent.ToggleCreatePlaylistOptionSheet -> _uiState.update { it.copy(isCreatePlaylistOptionSheet = event.isShow) }
            is PlaybackUiEvent.ToggleTrackOptionSheet -> _uiState.update { it.copy(isTrackOptionSheet = event.isShow) }
            else -> {}
        }
    }

    private fun setSleepTimer(option: SleepTimerOption?) {
        sleepTimerJob?.cancel()
        _uiState.update { it.copy(activeSleepTimer = option) }
        option?.durationMillis?.let {
            sleepTimerJob = viewModelScope.launch {
                delay(it)
                pause()
                _uiState.update { it.copy(activeSleepTimer = null) }
            }
        }
    }

    actual fun hideSleepTimerSheet() { _uiState.update { it.copy(isSleepTimerSheetVisible = false) } }

    private fun playYoutube(track: TrackDomain) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            playbackRepository.playYoutube(track.id).onSuccess { playbackInfo ->
                _uiState.update { it.copy(isLoading = false, playbackInfo = playbackInfo, currentTrack = track, error = null) }
                playbackRepository.saveCurrentTrack(track)
                start(BuildKonfig.BASE_URL + playbackInfo.hlsUrl)
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, error = error.message) }
            }
        }
    }

    private fun skipToNext() {
        viewModelScope.launch {
            playbackRepository.skipToNext().onSuccess { state ->
                val track = state.queue.currentTrack?.toTrackDomain()
                _uiState.update { it.copy(queue = state.queue, playbackInfo = state.playbackInfo, currentTrack = track, error = null) }
                track?.let { playbackRepository.saveCurrentTrack(it) }
                start(BuildKonfig.BASE_URL + state.playbackInfo.hlsUrl)
            }
        }
    }

    private fun skipToPrevious() {
        viewModelScope.launch {
            playbackRepository.skipToPrevious().onSuccess { state ->
                val track = state.queue.currentTrack?.toTrackDomain()
                _uiState.update { it.copy(queue = state.queue, playbackInfo = state.playbackInfo, currentTrack = track, error = null) }
                track?.let { playbackRepository.saveCurrentTrack(it) }
                start(BuildKonfig.BASE_URL + state.playbackInfo.hlsUrl)
            }
        }
    }

    private fun playContext(type: PlaybackContextType, id: String?, trackIds: List<String>?, offset: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            playbackRepository.playContext(type, id, trackIds, offset).onSuccess { state ->
                val track = state.queue.currentTrack?.toTrackDomain()
                _uiState.update { it.copy(isLoading = false, playbackInfo = state.playbackInfo, queue = state.queue, currentTrack = track, error = null) }
                track?.let { playbackRepository.saveCurrentTrack(it) }
                start(BuildKonfig.BASE_URL + state.playbackInfo.hlsUrl)
            }
        }
    }

    private fun trackClicked(track: TrackDomain, type: ContentTypeDomain, id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            playbackRepository.playbackInfo(track.id, type, id).onSuccess { info ->
                _uiState.update { it.copy(playbackInfo = info, currentTrack = track, isLoading = false, error = null) }
                playbackRepository.saveCurrentTrack(track)
                start(BuildKonfig.BASE_URL + info.hlsUrl)
            }
        }
    }

    fun start(url: String) {
        audioElement.src = url
        audioElement.load()
        audioElement.play()
    }

    fun play() { audioElement.play() }
    fun pause() { audioElement.pause() }

    private fun togglePlayback() { if (isPlaying()) pause() else play() }
    private fun toggleLoopClicked() {
        _uiState.update { it.copy(isLooping = !it.isLooping) }
        audioElement.loop = _uiState.value.isLooping
    }
    private fun toggleShuffleClicked() { _uiState.update { it.copy(isShuffle = !it.isShuffle) } }

    actual fun isPlaying(): Boolean = _uiState.value.isPlaying
    actual fun updateDominantColor(color: Color) { _uiState.update { it.copy(dominantColor = color) } }

    override fun onCleared() {
        super.onCleared()
        audioElement.pause()
        audioElement.src = ""
    }
}
