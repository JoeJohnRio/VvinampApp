package com.example.vvinamp.ui.widget.playback

import androidx.annotation.OptIn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.indicators.ProgressIndicator

@OptIn(UnstableApi::class)
@Composable
actual fun CustomProgressSlider(
    player: Any?,
    modifier: Modifier
) {
    val realPlayer = player as? Player ?: return
    var sliderWidthPx by remember { mutableIntStateOf(0) }

    // Media3's ProgressIndicator polls the player and exposes its position as a 0f..1f progress value.
    ProgressIndicator(
        player = realPlayer,
        totalTickCount = sliderWidthPx
    ) {
        var isDragging by remember { mutableStateOf(false) }
        var seekPosition by remember { mutableFloatStateOf(0f) }

        PlayerSeekBar(
            value = if (isDragging) seekPosition else currentPositionProgress,
            onValueChange = { newValue ->
                isDragging = true
                seekPosition = newValue
            },
            onValueChangeFinished = {
                updateCurrentPositionProgress(seekPosition)
                isDragging = false
            },
            modifier = modifier.onSizeChanged { size -> sliderWidthPx = size.width },
            enabled = changingProgressEnabled,
        )
    }
}
