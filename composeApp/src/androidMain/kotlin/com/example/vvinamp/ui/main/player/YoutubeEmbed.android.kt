package com.example.vvinamp.ui.main.player

import android.annotation.SuppressLint
import android.graphics.Color
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

private fun embedPage(videoId: String, origin: String): String = """
    <!doctype html>
    <html>
    <head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style>html, body, #player { margin: 0; width: 100%; height: 100%; background: #000; }</style>
    </head>
    <body>
    <div id="player"></div>
    <script src="https://www.youtube.com/iframe_api"></script>
    <script>
    var player;
    function onYouTubeIframeAPIReady() {
        player = new YT.Player('player', {
            width: '100%',
            height: '100%',
            videoId: '$videoId',
            playerVars: { autoplay: 1, playsinline: 1, rel: 0, origin: '$origin' },
            events: {
                onStateChange: function (event) { VvinampBridge.onState(event.data); },
                onError: function (event) { VvinampBridge.onError(event.data); }
            }
        });
    }
    function setPaused(isPaused) {
        if (!player || !player.pauseVideo) return;
        if (isPaused) { player.pauseVideo(); } else { player.playVideo(); }
    }
    </script>
    </body>
    </html>
""".trimIndent()

@SuppressLint("SetJavaScriptEnabled")
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

    LaunchedEffect(videoId) {
        if (!isValidYoutubeId(videoId)) currentOnError(YOUTUBE_ERROR_INVALID_ID)
    }
    if (!isValidYoutubeId(videoId)) return

    key(videoId) {
        AndroidView(
            modifier = modifier,
            factory = { context ->
                WebView(context).apply {
                    setBackgroundColor(Color.BLACK)
                    settings.javaScriptEnabled = true
                    settings.mediaPlaybackRequiresUserGesture = false

                    addJavascriptInterface(
                        object {
                            @JavascriptInterface
                            fun onState(state: Int) {
                                post { dispatchYoutubeState(state, currentOnPlayingChange, currentOnEnded) }
                            }

                            @JavascriptInterface
                            fun onError(code: Int) {
                                post { currentOnError(code) }
                            }
                        },
                        "VvinampBridge",
                    )

                    // YouTube identifies WebView clients by Referer, which needs an HTTPS base URL (error 153 without it).
                    val origin = "https://${context.packageName}"
                    loadDataWithBaseURL(origin, embedPage(videoId, origin), "text/html", "utf-8", null)
                }
            },
            update = { webView ->
                webView.evaluateJavascript("if (window.setPaused) setPaused($isPaused);", null)
            },
            onRelease = { it.destroy() },
        )
    }
}
