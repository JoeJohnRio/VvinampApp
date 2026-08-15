@file:kotlin.OptIn(ExperimentalMaterial3Api::class)

package com.example.vvinamp.ui.widget.playback

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Slider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.indicators.ProgressIndicator

@OptIn(
    UnstableApi::class,
    ExperimentalMaterial3Api::class,
)
@Composable
actual fun CustomProgressSlider(
    player: Any?,
    modifier: Modifier
) {
    val realPlayer = player as? Player ?: return
    var sliderWidthPx by remember { mutableIntStateOf(0) }

    // 1. Wrap with Media3's ProgressIndicator to handle Player state & polling automatically
    ProgressIndicator(
        player = realPlayer,
        totalTickCount = sliderWidthPx
    ) {
        // These properties (currentPositionProgress, changingProgressEnabled, etc.)
        // are provided directly by the ProgressIndicator's receiver scope.
        var isDragging by remember { mutableStateOf(false) }
        var seekPosition by remember { mutableFloatStateOf(0f) }

        CompositionLocalProvider(
            LocalMinimumInteractiveComponentSize provides Dp.Unspecified
        ) {
            Slider(
                // Use Media3's normalized 0f..1f progress value
                value = if (isDragging) seekPosition else currentPositionProgress,
                onValueChange = { newValue ->
                    isDragging = true
                    seekPosition = newValue
                },
                onValueChangeFinished = {
                    // Tell Media3 to seek the actual player
                    updateCurrentPositionProgress(seekPosition)
                    isDragging = false
                },
                modifier = modifier.onSizeChanged { size -> sliderWidthPx = size.width },
                enabled = changingProgressEnabled,

                // 2. FULL CUSTOMIZATION: Replace the default Thumb (Drag Handle)
                thumb = {
                    Box(
                        modifier = Modifier
                            .size(12.dp) // Spotify uses a small/hidden thumb
                            .background(Color.White, CircleShape)
                    )
                },

                // 3. FULL CUSTOMIZATION: Replace the default Track (The Line)
                track = { sliderState ->
                    val fraction = sliderState.value.coerceIn(0f, 1f)

                    // Background Track (Unplayed portion)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .background(Color.DarkGray, CircleShape)
                    ) {
                        // Foreground Track (Played portion)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction)
                                .fillMaxHeight()
                                .background(Color.White, CircleShape)
                        )
                    }
                }
            )
        }
    }
}
