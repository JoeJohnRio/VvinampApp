package com.example.vvinamp.ui.widget.playback

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

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
