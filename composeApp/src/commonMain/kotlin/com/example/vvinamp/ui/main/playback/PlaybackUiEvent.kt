package com.example.vvinamp.ui.main.playback

import com.example.domain.entity.enum.ContentTypeDomain
import com.example.domain.entity.enum.PlaybackContextType
import com.example.domain.entity.song.TrackDomain

sealed class PlaybackUiEvent {
    data object TogglePlayback : PlaybackUiEvent()
    data object ToggleLoopClicked : PlaybackUiEvent()
    data object ToggleShuffleClicked : PlaybackUiEvent()
    data class OnTrackClicked(
        val track: TrackDomain,
        val contentTypeDomain: ContentTypeDomain,
        val contentId: String,
    ) : PlaybackUiEvent()

    data class PlayContext(
        val contextType: PlaybackContextType,
        val contextId: String? = null,
        val trackIds: List<String>? = null,
        val offsetTrackId: String,
        val playingFrom: PlayingFrom? = null
    ) : PlaybackUiEvent()

    data object SkipToNext : PlaybackUiEvent()
    data object SkipToPrevious : PlaybackUiEvent()
    data object ClearQueue : PlaybackUiEvent()
    data class AddToQueue(val trackIds: List<String>) : PlaybackUiEvent()
    data class OnYoutubeTrackClicked(val track: TrackDomain) : PlaybackUiEvent()
    data class ToggleCreatePlaylistOptionSheet(val isShow: Boolean) : PlaybackUiEvent()
    data class ToggleTrackOptionSheet(val isShow: Boolean) : PlaybackUiEvent()
    data class OnSleepTimerSelected(val option: SleepTimerOption) : PlaybackUiEvent()
    data class OnEmbedPlayingChanged(val isPlaying: Boolean) : PlaybackUiEvent()
    data object OnEmbedEnded : PlaybackUiEvent()
    data class OnEmbedError(val code: Int) : PlaybackUiEvent()
    data class SetEmbedPaused(val isPaused: Boolean) : PlaybackUiEvent()
    data class OnEmbedPosition(val seconds: Int) : PlaybackUiEvent()
}
