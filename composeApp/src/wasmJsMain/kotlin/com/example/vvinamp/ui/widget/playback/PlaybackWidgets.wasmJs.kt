package com.example.vvinamp.ui.widget.playback

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.vvinamp.ui.theme.TSLabelMediumWhite
import kotlinx.browser.window
import org.w3c.dom.HTMLAudioElement

@Composable
fun rememberAudioPosition(audio: HTMLAudioElement): Double {
    var position by remember { mutableDoubleStateOf(audio.currentTime) }
    DisposableEffect(audio) {
        val listener = { _: org.w3c.dom.events.Event ->
            position = audio.currentTime
        }
        audio.addEventListener("timeupdate", listener)
        onDispose { audio.removeEventListener("timeupdate", listener) }
    }
    return position
}

@Composable
fun rememberAudioDuration(audio: HTMLAudioElement): Double {
    var duration by remember { mutableDoubleStateOf(audio.duration) }
    DisposableEffect(audio) {
        val listener = { _: org.w3c.dom.events.Event ->
            duration = audio.duration
        }
        audio.addEventListener("loadedmetadata", listener)
        audio.addEventListener("durationchange", listener)
        onDispose {
            audio.removeEventListener("loadedmetadata", listener)
            audio.removeEventListener("durationchange", listener)
        }
    }
    return duration
}

fun Double.formatAsTime(): String {
    if (this.isNaN() || this.isInfinite()) return "0:00"
    val totalSeconds = this.toInt()
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "$minutes:${seconds.toString().padStart(2, '0')}"
}

@Composable
actual fun CustomPositionText(
    player: Any?,
    color: Color
) {
    val audio = player as? HTMLAudioElement ?: return
    val position = rememberAudioPosition(audio)
    val finalColor = if (color == Color.Unspecified) MaterialTheme.colorScheme.inversePrimary else color

    Text(
        text = position.formatAsTime(),
        color = finalColor,
        style = TSLabelMediumWhite().copy(fontSize = 12.sp)
    )
}

@Composable
actual fun CustomDurationText(
    player: Any?,
    color: Color
) {
    val audio = player as? HTMLAudioElement ?: return
    val duration = rememberAudioDuration(audio)
    val finalColor = if (color == Color.Unspecified) MaterialTheme.colorScheme.inversePrimary else color

    Text(
        text = duration.formatAsTime(),
        color = finalColor,
        style = TSLabelMediumWhite().copy(fontSize = 12.sp)
    )
}

@Composable
actual fun CustomProgressSlider(
    player: Any?,
    modifier: Modifier
) {
    val audio = player as? HTMLAudioElement ?: return
    val position = rememberAudioPosition(audio)
    val duration = rememberAudioDuration(audio)

    var isDragging by remember { mutableStateOf(false) }
    var dragValue by remember { mutableFloatStateOf(0f) }

    val currentProgress = if (duration > 0) (position / duration).toFloat() else 0f

    PlayerSeekBar(
        value = if (isDragging) dragValue else currentProgress,
        onValueChange = {
            isDragging = true
            dragValue = it
        },
        onValueChangeFinished = {
            audio.currentTime = (dragValue * duration)
            isDragging = false
        },
        modifier = modifier
    )
}
