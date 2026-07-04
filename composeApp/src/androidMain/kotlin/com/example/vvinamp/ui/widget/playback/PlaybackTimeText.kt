package com.example.vvinamp.ui.widget.playback

import androidx.annotation.OptIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util.getStringForTime
import androidx.media3.ui.compose.material3.indicator.TimeFormat
import androidx.media3.ui.compose.state.ProgressStateWithTickInterval
import android.text.format.DateUtils
import androidx.media3.common.C
import androidx.compose.runtime.*
import androidx.media3.common.Player
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.unit.sp
import com.example.vvinamp.ui.theme.TSLabelMediumWhite

@Composable
fun rememberPlayerPosition(player: Player): Long {
    var position by remember { mutableLongStateOf(player.currentPosition) }
    var isPlaying by remember { mutableStateOf(player.isPlaying) }

    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                // Instantly update position on events like seeking
                position = player.currentPosition
                isPlaying = player.isPlaying
            }
        }
        player.addListener(listener)
        onDispose { player.removeListener(listener) }
    }

    // Continuously poll the position while the media is playing
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (isActive) {
                position = player.currentPosition
                delay(1000) // Update UI every second
            }
        }
    }

    return position
}

@Composable
fun rememberPlayerDuration(player: Player): Long {
    var duration by remember { mutableLongStateOf(player.duration) }

    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                // Update duration when track changes or media finishes loading
                if (events.containsAny(
                        Player.EVENT_PLAYBACK_STATE_CHANGED,
                        Player.EVENT_TIMELINE_CHANGED,
                        Player.EVENT_MEDIA_ITEM_TRANSITION
                    )
                ) {
                    duration = player.duration
                }
            }
        }
        player.addListener(listener)
        onDispose { player.removeListener(listener) }
    }

    return duration
}

fun Long.formatAsTime(): String {
    // Media3 returns C.TIME_UNSET (-9223372036854775807) when duration is unknown
    val seconds = if (this == C.TIME_UNSET || this < 0) 0 else this / 1000
    return DateUtils.formatElapsedTime(seconds)
}

@Composable
fun CustomPositionText(
    player: Player,
    color: Color = MaterialTheme.colorScheme.inversePrimary
) {
    val position = rememberPlayerPosition(player)

    Text(
        text = position.formatAsTime(),
        color = color,
        style = TSLabelMediumWhite().copy(
            fontSize = 12.sp
        )
    )
}

@Composable
fun CustomDurationText(
    player: Player,
    color: Color = MaterialTheme.colorScheme.inversePrimary
) {
    val duration = rememberPlayerDuration(player)

    Text(
        text = duration.formatAsTime(),
        color = color,
        style = TSLabelMediumWhite().copy(
            fontSize = 12.sp
        )
    )
}
