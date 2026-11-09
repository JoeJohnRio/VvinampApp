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
    """(container, videoId, onState, onError) => {
        container.style.width = '100%';
        container.style.height = '100%';
        const target = document.createElement('div');
        container.appendChild(target);
        const create = () => {
            container.vvinampPlayer = new YT.Player(target, {
                width: '100%',
                height: '100%',
                videoId,
                playerVars: { autoplay: 1, playsinline: 1, rel: 0, origin: window.location.origin },
                events: {
                    onStateChange: (event) => onState(event.data),
                    onError: (event) => onError(event.data),
                },
            });
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
    onState: (Int) -> Unit,
    onError: (Int) -> Unit,
)

@JsFun(
    """(container, isPaused) => {
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
) {
    val currentOnPlayingChange by rememberUpdatedState(onPlayingChange)
    val currentOnEnded by rememberUpdatedState(onEnded)
    val currentOnError by rememberUpdatedState(onError)

    key(videoId) {
        val container = remember { document.createElement("div") as HTMLElement }

        HtmlElementView(
            factory = {
                createYoutubePlayer(
                    container = container,
                    videoId = videoId,
                    onState = { state ->
                        dispatchYoutubeState(state, currentOnPlayingChange, currentOnEnded)
                    },
                    onError = { code -> currentOnError(code) },
                )
                container
            },
            modifier = modifier,
            onRelease = { destroyYoutubePlayer(it) },
        )

        LaunchedEffect(isPaused) {
            setYoutubePaused(container, isPaused)
        }
    }
}
