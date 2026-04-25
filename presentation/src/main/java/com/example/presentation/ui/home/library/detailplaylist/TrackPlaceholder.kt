package com.example.presentation.ui.home.library.detailplaylist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.presentation.util.shimmerEffect

@Composable
fun TrackPlaceholder(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Album Art Shimmer (assuming a 48dp square or circle)
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(4.dp)) // or CircleShape if rounded
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Title and Subtitle Shimmer
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // Track Title Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f) // Occupies 70% of available width
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Artist Name Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f) // Occupies 40% of available width
                    .height(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Optional: trailing icon placeholder (like the 3 dots menu)
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .shimmerEffect()
        )
    }
}