package com.example.vvinamp.ui.main.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun YoutubeEmbed(
    videoId: String,
    isPaused: Boolean,
    onPlayingChange: (Boolean) -> Unit,
    onEnded: () -> Unit,
    onError: (Int) -> Unit,
    modifier: Modifier,
) {
}
