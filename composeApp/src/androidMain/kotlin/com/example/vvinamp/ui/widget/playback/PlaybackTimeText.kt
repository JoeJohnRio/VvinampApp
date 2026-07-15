package com.example.vvinamp.ui.widget.playback

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
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
actual fun CustomPositionText(
    player: Any?,
    color: Color
) {
    val realPlayer = player as? Player ?: return
    val position = rememberPlayerPosition(realPlayer)

    val finalColor = if (color == Color.Unspecified) MaterialTheme.colorScheme.inversePrimary else color

    Text(
        text = position.formatAsTime(),
        color = finalColor,
        style = TSLabelMediumWhite().copy(
            fontSize = 12.sp
        )
    )
}

@Composable
actual fun CustomDurationText(
    player: Any?,
    color: Color
) {
    val realPlayer = player as? Player ?: return
    val duration = rememberPlayerDuration(realPlayer)

    val finalColor = if (color == Color.Unspecified) MaterialTheme.colorScheme.inversePrimary else color

    Text(
        text = duration.formatAsTime(),
        color = finalColor,
        style = TSLabelMediumWhite().copy(
            fontSize = 12.sp
        )
    )
}
