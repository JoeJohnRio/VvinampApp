@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)

package com.example.vvinamp.ui.main.playback

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.BuildKonfig
import com.example.domain.entity.enum.ContentTypeDomain
import com.example.domain.entity.enum.PlaybackContextType
import com.example.domain.entity.playbackinfo.PlaybackInfoDomain
import com.example.domain.entity.song.TrackDomain
import com.example.domain.entity.playbackinfo.toTrackDomain
import com.example.domain.entity.youtube.YoutubeDownloadState
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

@JsFun(
    """(audioElement, url) => {
        if (window.vvinampHls) {
            window.vvinampHls.destroy();
            window.vvinampHls = null;
        }
        if (typeof Hls !== 'undefined' && Hls.isSupported() && url.includes('.m3u8')) {
            const hls = new Hls();
            window.vvinampHls = hls;
            hls.loadSource(url);
            hls.attachMedia(audioElement);
            hls.on(Hls.Events.MANIFEST_PARSED, () => {
                audioElement.play().catch(e => console.warn('Autoplay blocked:', e));
            });
            hls.on(Hls.Events.ERROR, (event, data) => {
                console.error('HLS error:', data);
            });
        } else {
            audioElement.src = url;
            audioElement.load();
            audioElement.play().catch(e => console.warn('Autoplay blocked:', e));
        }
    }"""
)
private external fun playAudioSource(audioElement: HTMLAudioElement, url: String)

@JsFun(
    """(audioElement) => {
        audioElement.play().catch(e => console.warn('Autoplay blocked:', e));
    }"""
)
private external fun playAudioElement(audioElement: HTMLAudioElement)

@JsFun(
    """() => {
        if (window.vvinampHls) {
            window.vvinampHls.destroy();
            window.vvinampHls = null;
        }
    }"""
)
private external fun destroyHls()

@JsFun("""() => typeof Hls !== 'undefined' && Hls.isSupported()""")
private external fun isHlsJsSupported(): Boolean

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
        onended = {
            if (!_uiState.value.isLooping) {
                skipToNext()
            }
        }
        addEventListener("error", {
            val code = error?.code?.toInt() ?: 0
            val errorMsg = when (code) {
                1 -> "Playback aborted"
                2 -> "Network error"
                3 -> "Decoding error"
                4 -> "Source not supported (Possible HLS issue)"
                else -> "Unknown playback error"
            }
            println("Audio Error: $errorMsg (code: $code)")
            _uiState.update { it.copy(isLoading = false, error = errorMsg) }
        })
    }

    private var sleepTimerJob: Job? = null

    init {
        _mediaController.value = audioElement
        viewModelScope.launch {
            try {
                playbackRepository.getCurrentTrackFlow().collect { savedTrack ->
                    if (savedTrack != null && _uiState.value.currentTrack == null) {
                        _uiState.update { it.copy(currentTrack = savedTrack) }
                    }
                }
            } catch (e: Exception) {
                println("Failed to load initial track from database: ${e.message}")
            }
        }
    }

    actual fun onEventTrackOptions(event: TrackOptionsUiEvent) {
        when (event) {
            is TrackOptionsUiEvent.OnSleepTimerClick -> showSleepTimerSheet()
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
                when (event.contentTypeDomain) {
                    ContentTypeDomain.TRACK -> {
                        trackClicked(
                            track = event.track,
                            id = event.track.id,
                            type = ContentTypeDomain.TRACK
                        )
                    }
                    ContentTypeDomain.PLAYLIST, ContentTypeDomain.ALBUM, ContentTypeDomain.ARTIST -> {
                        trackClicked(
                            track = event.track,
                            id = event.contentId,
                            type = event.contentTypeDomain
                        )
                    }
                    else -> {
                        trackClicked(
                            track = event.track,
                            id = event.track.id,
                            type = ContentTypeDomain.TRACK
                        )
                    }
                }
            }
            is PlaybackUiEvent.PlayContext -> {
                playContext(event.contextType, event.contextId, event.trackIds, event.offsetTrackId)
            }
            is PlaybackUiEvent.SkipToNext -> skipToNext()
            is PlaybackUiEvent.SkipToPrevious -> skipToPrevious()
            is PlaybackUiEvent.ClearQueue -> clearQueue()
            is PlaybackUiEvent.AddToQueue -> addToQueue(event.trackIds)
            is PlaybackUiEvent.OnYoutubeTrackClicked -> playYoutube(event.track)
            is PlaybackUiEvent.ToggleCreatePlaylistOptionSheet -> {
                if (event.isShow) showCreatePlaylistOptionSheet() else hideCreatePlaylistOptionSheet()
            }
            is PlaybackUiEvent.ToggleTrackOptionSheet -> {
                if (event.isShow) showTrackOptionSheet() else hideTrackOptionSheet()
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
        option?.durationMillis?.let {
            sleepTimerJob = viewModelScope.launch {
                delay(it)
                pause()
                _uiState.update { it.copy(activeSleepTimer = null) }
            }
        }
    }

    fun showSleepTimerSheet() {
        _uiState.update { it.copy(isSleepTimerSheetVisible = true) }
    }

    actual fun hideSleepTimerSheet() {
        _uiState.update { it.copy(isSleepTimerSheetVisible = false) }
    }

    fun showCreatePlaylistOptionSheet() {
        _uiState.update { it.copy(isCreatePlaylistOptionSheet = true) }
    }

    fun hideCreatePlaylistOptionSheet() {
        _uiState.update { it.copy(isCreatePlaylistOptionSheet = false) }
    }

    fun showTrackOptionSheet() {
        _uiState.update { it.copy(isTrackOptionSheet = true) }
    }

    fun hideTrackOptionSheet() {
        _uiState.update { it.copy(isTrackOptionSheet = false) }
    }

    private fun playYoutube(track: TrackDomain) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    isDownloading = true,
                    downloadProgress = 0,
                    currentTrack = track,
                    error = null
                )
            }
            playbackRepository.playYoutube.downloadAndPlay(track.id).collect { state ->
                when (state) {
                    is YoutubeDownloadState.Progress -> {
                        _uiState.update { it.copy(downloadProgress = state.progress) }
                    }
                    is YoutubeDownloadState.Completed -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isDownloading = false,
                                playbackInfo = state.playbackInfo,
                                currentTrack = track,
                                error = null
                            )
                        }
                        start(selectPlaybackUrl(state.playbackInfo))
                        saveTrackInBackground(track)
                    }
                    is YoutubeDownloadState.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isDownloading = false,
                                error = state.message
                            )
                        }
                    }
                }
            }
        }
    }

    private fun skipToNext() {
        viewModelScope.launch {
            playbackRepository.skipToNext().onSuccess { state ->
                val track = state.queue.currentTrack?.toTrackDomain()
                _uiState.update {
                    it.copy(
                        queue = state.queue,
                        playbackInfo = state.playbackInfo,
                        currentTrack = track,
                        error = null
                    )
                }
                start(selectPlaybackUrl(state.playbackInfo))
                saveTrackInBackground(track)
            }.onFailure { error ->
                _uiState.update { it.copy(error = error.message) }
            }
        }
    }

    private fun skipToPrevious() {
        viewModelScope.launch {
            playbackRepository.skipToPrevious().onSuccess { state ->
                val track = state.queue.currentTrack?.toTrackDomain()
                _uiState.update {
                    it.copy(
                        queue = state.queue,
                        playbackInfo = state.playbackInfo,
                        currentTrack = track,
                        error = null
                    )
                }
                start(selectPlaybackUrl(state.playbackInfo))
                saveTrackInBackground(track)
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

    private fun playContext(type: PlaybackContextType, id: String?, trackIds: List<String>?, offset: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            playbackRepository.playContext(type, id, trackIds, offset).onSuccess { state ->
                val track = state.queue.currentTrack?.toTrackDomain()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        playbackInfo = state.playbackInfo,
                        queue = state.queue,
                        currentTrack = track,
                        error = null
                    )
                }
                start(selectPlaybackUrl(state.playbackInfo))
                saveTrackInBackground(track)
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

    private fun trackClicked(
        track: TrackDomain,
        type: ContentTypeDomain = ContentTypeDomain.TRACK,
        id: String = track.id
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            playbackRepository.playbackInfo(track.id, type, id).onSuccess { info ->
                _uiState.update {
                    it.copy(
                        playbackInfo = info,
                        currentTrack = track,
                        isLoading = false,
                        error = null
                    )
                }
                start(selectPlaybackUrl(info))
                saveTrackInBackground(track)
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to play track"
                    )
                }
            }
        }
    }

    private fun saveTrackInBackground(track: TrackDomain?) {
        if (track == null) return
        viewModelScope.launch {
            try {
                playbackRepository.saveCurrentTrack(track)
            } catch (e: Exception) {
                println("Failed to save current track: ${e.message}")
            }
        }
    }

    private fun resolveAudioUrl(path: String): String {
        var clean = path.trim()
        if (clean.startsWith(BuildKonfig.BASE_URL + "http://") || clean.startsWith(BuildKonfig.BASE_URL + "https://")) {
            clean = clean.removePrefix(BuildKonfig.BASE_URL)
        }
        return if (clean.startsWith("http://") || clean.startsWith("https://")) {
            clean
        } else if (clean.startsWith("/")) {
            BuildKonfig.BASE_URL + clean
        } else {
            BuildKonfig.BASE_URL + "/" + clean
        }
    }

    private fun selectPlaybackUrl(playbackInfo: PlaybackInfoDomain): String {
        val mp3 = playbackInfo.mp3Fallback
        return if (!mp3.isNullOrBlank() && !canPlayHls()) {
            resolveAudioUrl(mp3)
        } else {
            resolveAudioUrl(playbackInfo.hlsUrl)
        }
    }

    private fun canPlayHls(): Boolean {
        val nativeHls = audioElement.canPlayType("application/vnd.apple.mpegurl").toString() != ""
        return nativeHls || isHlsJsSupported()
    }

    fun start(url: String) {
        println("Starting playback with URL: $url")
        playAudioSource(audioElement, url)
    }

    fun play() {
        playAudioElement(audioElement)
    }

    fun pause() {
        audioElement.pause()
    }

    private fun togglePlayback() {
        if (isPlaying()) {
            pause()
        } else {
            val current = _uiState.value.currentTrack
            if (_uiState.value.playbackInfo == null && current != null) {
                trackClicked(current)
            } else {
                play()
            }
        }
    }

    private fun toggleLoopClicked() {
        _uiState.update { it.copy(isLooping = !it.isLooping) }
        audioElement.loop = _uiState.value.isLooping
    }

    private fun toggleShuffleClicked() {
        _uiState.update { it.copy(isShuffle = !it.isShuffle) }
    }

    actual fun isPlaying(): Boolean = _uiState.value.isPlaying
    actual fun updateDominantColor(color: Color) { _uiState.update { it.copy(dominantColor = color) } }

    override fun onCleared() {
        super.onCleared()
        destroyHls()
        audioElement.pause()
        audioElement.src = ""
    }
}
