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
import com.example.domain.usecase.PlaybackUseCase
import com.example.vvinamp.ui.navigationbar.trackoption.TrackOptionsUiEvent
import com.example.vvinamp.util.showToast
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
    """(audioElement, url, fallbackUrl) => {
        if (window.vvinampHls) {
            window.vvinampHls.destroy();
            window.vvinampHls = null;
        }
        window.vvinampFallbackMp3 = fallbackUrl || '';
        const playWithCatch = (target) => {
            const p = target.play();
            if (p && typeof p.catch === 'function') {
                p.catch(e => console.warn('Playback play() was blocked or prevented:', e));
            }
        };
        if (typeof Hls !== 'undefined' && Hls.isSupported() && url.includes('.m3u8')) {
            const hls = new Hls({
                enableWorker: true,
                lowLatencyMode: true
            });
            window.vvinampHls = hls;
            hls.loadSource(url);
            hls.attachMedia(audioElement);
            hls.on(Hls.Events.MANIFEST_PARSED, () => {
                playWithCatch(audioElement);
            });
            hls.on(Hls.Events.ERROR, (event, data) => {
                console.warn('HLS event error:', data);
                if (data.fatal) {
                    if (window.vvinampFallbackMp3 && window.vvinampFallbackMp3 !== url) {
                        console.log('HLS fatal, switching to fallback URL:', window.vvinampFallbackMp3);
                        hls.destroy();
                        window.vvinampHls = null;
                        audioElement.src = window.vvinampFallbackMp3;
                        audioElement.load();
                        playWithCatch(audioElement);
                    } else if (data.type === Hls.ErrorTypes.NETWORK_ERROR) {
                        hls.startLoad();
                    } else if (data.type === Hls.ErrorTypes.MEDIA_ERROR) {
                        hls.recoverMediaError();
                    } else {
                        hls.destroy();
                    }
                }
            });
        } else {
            audioElement.src = url;
            audioElement.load();
            playWithCatch(audioElement);
        }
    }"""
)
private external fun playAudioSource(audioElement: HTMLAudioElement, url: String, fallbackUrl: String = definedExternally)

@JsFun(
    """(audioElement) => {
        audioElement.load();
    }"""
)
private external fun primeAudioElement(audioElement: HTMLAudioElement)

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
        // Remember where a play started so the player header can say so.
        when (event) {
            is PlaybackUiEvent.PlayContext -> _uiState.update { it.copy(playingFrom = event.playingFrom) }
            is PlaybackUiEvent.OnTrackClicked, is PlaybackUiEvent.OnYoutubeTrackClicked ->
                _uiState.update { it.copy(playingFrom = null) }
            else -> Unit
        }
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
            is PlaybackUiEvent.OnEmbedPlayingChanged -> {
                _uiState.update { it.copy(isPlaying = event.isPlaying) }
            }
            is PlaybackUiEvent.OnEmbedEnded -> skipToNext()
            is PlaybackUiEvent.OnEmbedError -> {
                showToast("This video can't be played here")
                _uiState.update { it.copy(youtubeEmbedId = null, isPlaying = false) }
                skipToNext()
            }
            is PlaybackUiEvent.OnEmbedPosition -> {
                _uiState.update { it.copy(embedPositionSeconds = event.seconds) }
            }

            is PlaybackUiEvent.SetEmbedPaused -> {
                _uiState.update { it.copy(isEmbedPaused = event.isPaused) }
            }
        }
    }

    private fun startPlayback(info: PlaybackInfoDomain) {
        val youtubeId = info.youtubeId
        if (youtubeId != null) {
            pause()
            _uiState.update {
                it.copy(
                    youtubeEmbedId = youtubeId,
                    embedSession = it.embedSession + 1,
                    embedPositionSeconds = 0,
                    isEmbedPaused = false,
                    isPlaying = false
                )
            }
            return
        }

        _uiState.update { it.copy(youtubeEmbedId = null, isEmbedPaused = false) }
        val fallback = info.mp3Fallback?.let { resolveAudioUrl(it) } ?: ""
        start(selectPlaybackUrl(info), fallback)
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
        trackClicked(
            track = track,
            type = ContentTypeDomain.TRACK,
            id = track.id
        )
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
                startPlayback(state.playbackInfo)
                saveTrackInBackground(track)
            }.onFailure { error ->
                _uiState.update { it.copy(error = error.message) }
                // The player stays on the current song, so say why nothing changed.
                showToast("Couldn't play the next song")
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
                startPlayback(state.playbackInfo)
                saveTrackInBackground(track)
            }.onFailure { error ->
                _uiState.update { it.copy(error = error.message) }
                // The player stays on the current song, so say why nothing changed.
                showToast("Couldn't play the previous song")
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
                startPlayback(state.playbackInfo)
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
        primeAudioElement(audioElement)
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    isDownloading = false,
                    currentTrack = track,
                    error = null
                )
            }
            playbackRepository.playbackInfo(track.id, type, id).onSuccess { info ->
                _uiState.update {
                    it.copy(
                        playbackInfo = info,
                        currentTrack = track,
                        isLoading = false,
                        error = null
                    )
                }
                startPlayback(info)
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
        val hls = playbackInfo.hlsUrl.trim()
        val mp3 = playbackInfo.mp3Fallback?.trim()

        if ((hls.isEmpty() || !hls.contains(".m3u8")) && !mp3.isNullOrEmpty()) {
            return resolveAudioUrl(mp3)
        }
        if (!mp3.isNullOrEmpty() && !canPlayHls()) {
            return resolveAudioUrl(mp3)
        }
        if (hls.isNotEmpty()) {
            return resolveAudioUrl(hls)
        }
        return resolveAudioUrl(mp3.orEmpty())
    }

    private fun canPlayHls(): Boolean {
        val nativeHls = audioElement.canPlayType("application/vnd.apple.mpegurl").toString() != ""
        return nativeHls || isHlsJsSupported()
    }

    fun start(url: String, fallbackUrl: String = "") {
        println("Starting playback with URL: $url, fallback: $fallbackUrl")
        playAudioSource(audioElement, url, fallbackUrl)
    }

    fun play() {
        playAudioElement(audioElement)
    }

    fun pause() {
        audioElement.pause()
    }

    private fun togglePlayback() {
        if (_uiState.value.youtubeEmbedId != null) {
            _uiState.update { it.copy(isEmbedPaused = !it.isEmbedPaused) }
            return
        }

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
