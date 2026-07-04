package com.example.vvinamp.ui.widget

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SpotifyPlayingIndicator(
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    // Create an infinite transition for the bouncing effect
    val transition = rememberInfiniteTransition(label = "audio_wave")

    // Helper function to animate a single bar
    @Composable
    fun animateBarHeight(
        min: Float,
        max: Float,
        duration: Int,
        delay: Int
    ): Float {
        // If it's not playing, just return the minimum height (dots)
        if (!isPlaying) return min

        val height by transition.animateFloat(
            initialValue = min,
            targetValue = max,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = duration, delayMillis = delay, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "bar_height"
        )
        return height
    }

    // Three bars with different max heights, durations, and delays for a random look
    val bar1Height = animateBarHeight(min = 3f, max = 12f, duration = 400, delay = 0)
    val bar2Height = animateBarHeight(min = 3f, max = 16f, duration = 500, delay = 200)
    val bar3Height = animateBarHeight(min = 3f, max = 10f, duration = 350, delay = 100)

    Row(
        modifier = modifier.height(16.dp).padding(bottom = 2.dp),
        verticalAlignment = Alignment.Bottom, // Align to the bottom so they grow upwards
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        // Bar 1
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(bar1Height.dp)
                .clip(RoundedCornerShape(50))
                .background(color)
        )
        // Bar 2
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(bar2Height.dp)
                .clip(RoundedCornerShape(50))
                .background(color)
        )
        // Bar 3
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(bar3Height.dp)
                .clip(RoundedCornerShape(50))
                .background(color)
        )
    }
}
