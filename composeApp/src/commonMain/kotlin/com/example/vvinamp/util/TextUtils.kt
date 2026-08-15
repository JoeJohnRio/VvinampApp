package com.example.vvinamp.util

import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

fun String?.orErrorNoText(): String {
    return if (this.isNullOrBlank()) "No text" else this
}

@Composable
fun SpotifyMarquee(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            // Needed so we can mask alpha without messing siblings
            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
            .fadingEdge() // custom modifier below
    ) {
        Text(
            text = text,
            modifier = Modifier
                .basicMarquee(
                    iterations = Int.MAX_VALUE,
                    animationMode = MarqueeAnimationMode.Immediately,
                    repeatDelayMillis = 1000,
                    velocity = 40.dp // tune speed
                ),
            style = style,
            maxLines = 1,
            overflow = TextOverflow.Clip
        )
    }
}
