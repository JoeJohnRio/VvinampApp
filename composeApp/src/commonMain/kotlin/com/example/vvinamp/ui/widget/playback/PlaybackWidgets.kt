package com.example.vvinamp.ui.widget.playback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
expect fun CustomPositionText(
    player: Any?,
    color: Color = Color.Unspecified
)

@Composable
expect fun CustomDurationText(
    player: Any?,
    color: Color = Color.Unspecified
)

@Composable
expect fun CustomProgressSlider(
    player: Any?,
    modifier: Modifier = Modifier
)

private val SeekThumbSize = 12.dp

/** Spotify-style seek bar shared by the Android and web players: a thin track and a small white thumb. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerSeekBar(
    value: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    // Material pads sliders to 48dp tall by default, which pushes the time labels away from the bar.
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
        Slider(
            value = value,
            onValueChange = onValueChange,
            onValueChangeFinished = onValueChangeFinished,
            modifier = modifier.extendedByThumb(),
            enabled = enabled,
            thumb = {
                Box(
                    modifier = Modifier
                        .size(SeekThumbSize)
                        .background(Color.White, CircleShape)
                )
            },
            track = { sliderState ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(Color.DarkGray, CircleShape)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(sliderState.value.coerceIn(0f, 1f))
                            .fillMaxHeight()
                            .background(Color.White, CircleShape)
                    )
                }
            },
        )
    }
}

// Material insets the track by half a thumb on each side so the thumb can reach both ends.
// Widening the slider by one thumb lines the track up with the title and time labels, like Spotify.
private fun Modifier.extendedByThumb(): Modifier = layout { measurable, constraints ->
    if (!constraints.hasBoundedWidth) {
        val placeable = measurable.measure(constraints)
        return@layout layout(placeable.width, placeable.height) { placeable.place(0, 0) }
    }
    val extra = SeekThumbSize.roundToPx()
    val placeable = measurable.measure(
        constraints.copy(
            minWidth = constraints.minWidth + extra,
            maxWidth = constraints.maxWidth + extra,
        )
    )
    layout(placeable.width - extra, placeable.height) { placeable.place(-extra / 2, 0) }
}
