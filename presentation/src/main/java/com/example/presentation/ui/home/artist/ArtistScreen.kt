package com.example.presentation.ui.home.artist

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.example.domain.entity.ListeningHistory
import com.example.presentation.ui.widget.ImageSize
import com.example.presentation.ui.widget.NoImagePlaceholder
import com.example.presentation.ui.widget.toPX
@Composable
fun SpotifyArtistScreen(history: ListeningHistory) {
    // 1. Scaffold handles the "Outer" edges (Top and Bottom bars)
    val scrollState = rememberLazyListState()
    val headerHeightPx = with(LocalDensity.current) { 300.dp.toPx() }

    // Logic to calculate how much of the header is collapsed (0.0 to 1.0)
    val collapseFraction by remember {
        derivedStateOf {
            if (scrollState.firstVisibleItemIndex > 0) 1f
            else (scrollState.firstVisibleItemScrollOffset / headerHeightPx).coerceIn(0f, 1f)
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        // This ensures the Scaffold knows how to handle the system bars
        contentWindowInsets = WindowInsets.systemBars
    ) { innerPadding ->
        // 2. Use a Box to layer your Background Image and List
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            ArtistImage(
                imageUrl = history.contentImage,
                modifier = Modifier
                    .height(300.dp)
                    .graphicsLayer {
                        // Subtle Parallax: move at 40% scroll speed
                        translationY = -scrollState.firstVisibleItemScrollOffset * 0.4f
                        // Fade out
                        alpha = 1f - collapseFraction
                    }
            )

            // LAYER 2: The List
            LazyColumn(state = scrollState, modifier = Modifier.fillMaxSize()) {
                item {
                    // This spacer makes room for the image to be seen
                    Spacer(Modifier.height(300.dp))
                }
                item {
                    Text(
                        text = "ArtistProfile",
                        fontSize = 50.sp
                    )
//                ArtistProfileInfo() // Big Name, Follow button, etc.
                }
                items(100) {
                    Text(
                        text = "song",
                        fontSize = 10.sp
                    )
//                SongRow(it)
                }
            }

            // LAYER 3: The Sticky Nav
            TopNavigation(
                title = history.contentName,
                alpha = collapseFraction,
                onBackClick = { /* Handle back */ }
            )
        }
    }
}

@Composable
fun ArtistImage(
    imageUrl: String, // Replace with your image source
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.DarkGray) // Placeholder
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Optional: Add a bottom-to-top gradient so the text
        // in the list remains readable as it scrolls up
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        1f to Color.Black.copy(alpha = 0.5f)
                    )
                )
        )
    }
}

@Composable
fun TopNavigation(
    title: String,
    alpha: Float, // Passed from the scroll logic (0f to 1f)
    onBackClick: () -> Unit
) {
    Surface(
        color = Color.Black.copy(alpha = alpha),
        contentColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }

            // The title only becomes visible as the alpha increases
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .graphicsLayer {
                        // Only show title when the background is mostly opaque
                        this.alpha = if (alpha > 0.8f) 1f else 0f
                    }
            )
        }
    }
}