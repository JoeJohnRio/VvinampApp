package com.example.vvinamp.ui.widget.playback

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Text

@Composable
actual fun CustomPositionText(
    player: Any?,
    color: Color
) {
    Text("0:00", color = color)
}

@Composable
actual fun CustomDurationText(
    player: Any?,
    color: Color
) {
    Text("0:00", color = color)
}

@Composable
actual fun CustomProgressSlider(
    player: Any?,
    modifier: Modifier
) {
    // TODO: iOS implementation
}
