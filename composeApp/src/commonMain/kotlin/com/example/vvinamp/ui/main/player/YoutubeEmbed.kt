package com.example.vvinamp.ui.main.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

internal const val YOUTUBE_STATE_ENDED = 0
internal const val YOUTUBE_STATE_PLAYING = 1
internal const val YOUTUBE_STATE_PAUSED = 2
internal const val YOUTUBE_ERROR_INVALID_ID = 2

private val youtubeIdPattern = Regex("[A-Za-z0-9_-]{11}")

internal fun isValidYoutubeId(videoId: String): Boolean = youtubeIdPattern.matches(videoId)

internal fun dispatchYoutubeState(
    state: Int,
    onPlayingChange: (Boolean) -> Unit,
    onEnded: () -> Unit,
) {
    when (state) {
        YOUTUBE_STATE_PLAYING -> onPlayingChange(true)
        YOUTUBE_STATE_PAUSED -> onPlayingChange(false)
        YOUTUBE_STATE_ENDED -> {
            onPlayingChange(false)
            onEnded()
        }
    }
}

/** Official YouTube player. YouTube's terms require it to stay visible, at least 200x200, with nothing covering it. */
@Composable
expect fun YoutubeEmbed(
    videoId: String,
    isPaused: Boolean,
    onPlayingChange: (Boolean) -> Unit,
    onEnded: () -> Unit,
    onError: (Int) -> Unit,
    modifier: Modifier = Modifier,
    /** Where to start, in seconds, so reopening the player continues instead of restarting. */
    startSeconds: Int = 0,
    /** Reports the playback position about once a second. */
    onPosition: (Int) -> Unit = {},
)
