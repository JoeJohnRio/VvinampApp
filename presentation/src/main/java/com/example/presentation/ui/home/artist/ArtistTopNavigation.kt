package com.example.presentation.ui.home.artist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp


@Composable
fun ArtistTopNavigation(
    innerPadding: PaddingValues,
    title: String,
    topBarAlpha: Float, // Passed from the scroll logic (0f to 1f)
    onBackClick: () -> Unit,
    topBarNameAlpha: Float,
    collapseFraction: Float
) {
    val surfaceHeight = 56.dp + innerPadding.calculateTopPadding()
    Surface(
        color = MaterialTheme.colorScheme.background.copy(
            alpha = topBarAlpha
        ),
        contentColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .height(surfaceHeight)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter // Aligns the Row to the bottom
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Column(
                        modifier = Modifier.background(
                            color = MaterialTheme.colorScheme.background.copy(alpha = 0.3f - collapseFraction),
                            shape = CircleShape
                        ).padding(8.dp),
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .graphicsLayer {
                            this.alpha = topBarNameAlpha
                        }
                )
            }
        }
    }
}