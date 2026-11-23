@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class, ExperimentalComposeUiApi::class)

package com.example.vvinamp.ui.main.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.HtmlElementView
import kotlinx.browser.document
import org.w3c.dom.HTMLElement

@JsFun(
    """(container, videoId, startSeconds, onState, onError, onTime) => {
        container.style.width = '100%';
        container.style.height = '100%';
        const target = document.createElement('div');
        container.appendChild(target);
        const create = () => {
            container.vvinampPlayer = new YT.Player(target, {
                width: '100%',
                height: '100%',
                videoId,
                playerVars: { autoplay: 1, playsinline: 1, rel: 0, start: startSeconds, origin: window.location.origin },
                events: {
                    // A pause asked for before the player was ready would otherwise be lost.
                    onReady: (event) => { if (container.vvinampWantPaused) event.target.pauseVideo(); },
                    onStateChange: (event) => onState(event.data),
                    onError: (event) => onError(event.data),
                },
            });
            container.vvinampTimer = setInterval(() => {
                const player = container.vvinampPlayer;
                if (player && player.getCurrentTime) onTime(Math.floor(player.getCurrentTime()));
            }, 1000);
        };
        if (window.YT && window.YT.Player) {
            create();
            return;
        }
        const previous = window.onYouTubeIframeAPIReady;
        window.onYouTubeIframeAPIReady = () => {
            if (previous) previous();
            create();
        };
    }"""
)
private external fun createYoutubePlayer(
    container: HTMLElement,
    videoId: String,
    startSeconds: Int,
    onState: (Int) -> Unit,
    onError: (Int) -> Unit,
    onTime: (Int) -> Unit,
)

@JsFun(
    """(container, isPaused) => {
        container.vvinampWantPaused = isPaused;
        const player = container.vvinampPlayer;
        if (!player || !player.pauseVideo) return;
        if (isPaused) {
            player.pauseVideo();
        } else {
            player.playVideo();
        }
    }"""
)
private external fun setYoutubePaused(container: HTMLElement, isPaused: Boolean)

@JsFun(
    """(container) => {
        clearInterval(container.vvinampTimer);
        if (container.vvinampPlayer && container.vvinampPlayer.destroy) {
            container.vvinampPlayer.destroy();
        }
        container.vvinampPlayer = null;
    }"""
)
private external fun destroyYoutubePlayer(container: HTMLElement)

@Composable
actual fun YoutubeEmbed(
    videoId: String,
    isPaused: Boolean,
    onPlayingChange: (Boolean) -> Unit,
    onEnded: () -> Unit,
    onError: (Int) -> Unit,
    modifier: Modifier,
    startSeconds: Int,
    onPosition: (Int) -> Unit,
) {
    val currentOnPlayingChange by rememberUpdatedState(onPlayingChange)
    val currentOnEnded by rememberUpdatedState(onEnded)
    val currentOnError by rememberUpdatedState(onError)
    val currentOnPosition by rememberUpdatedState(onPosition)

    key(videoId) {
        val container = remember { document.createElement("div") as HTMLElement }

        HtmlElementView(
            factory = {
                createYoutubePlayer(
                    container = container,
                    videoId = videoId,
                    startSeconds = startSeconds,
                    onState = { state ->
                        dispatchYoutubeState(state, currentOnPlayingChange, currentOnEnded)
                    },
                    onError = { code -> currentOnError(code) },
                    onTime = { seconds -> currentOnPosition(seconds) },
                )
                container
            },
            modifier = modifier,
            onRelease = {
                destroyYoutubePlayer(it)
                // The player is gone, so nothing is playing any more.
                currentOnPlayingChange(false)
            },
        )

        LaunchedEffect(isPaused) {
            setYoutubePaused(container, isPaused)
        }
    }
}
