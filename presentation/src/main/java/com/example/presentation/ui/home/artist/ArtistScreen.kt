package com.example.presentation.ui.home.artist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.domain.entity.ListeningHistoryDomain
import com.example.domain.entity.artist.ArtistProfileDomain
import com.example.domain.entity.song.TopReleasesDomain
import com.example.domain.entity.song.TopSongsDomain
import com.example.presentation.ui.main.ScreenWrapper
import com.example.presentation.util.orErrorNoText
import kotlinx.coroutines.delay

@Composable
fun ArtistScreen(
    viewModel: ArtistViewModel = hiltViewModel(),
    history: ListeningHistoryDomain,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(history) {
        viewModel.clearUiState()
        viewModel.getArtistScreenData(history.contentId)
    }

    ScreenWrapper(
        isLoading = uiState.isLoading,
        error = uiState.error,
        content = {
            uiState.artistProfile?.let {
                ArtistProfileContent(
                    artistProfile = it,
                    topSongs = uiState.topSongs,
                    topReleases = uiState.topReleases,
                    onBackClick = onBackClick
                )
            }
        }
    )
}

@Composable
fun ArtistProfileContent(
    artistProfile: ArtistProfileDomain,
    topSongs: TopSongsDomain?,
    topReleases: TopReleasesDomain?,
    onBackClick: () -> Unit
) {
    val scrollState = rememberLazyListState()
    val headerHeightPx = with(LocalDensity.current) { 300.dp.toPx() }

    val collapseFraction by remember {
        derivedStateOf {
            if (scrollState.firstVisibleItemIndex > 0) 1f
            else (scrollState.firstVisibleItemScrollOffset / headerHeightPx).coerceIn(0f, 1f)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            ArtistImage(
                imageUrl = artistProfile.profileImage.orErrorNoText(),
                modifier = Modifier
                    .height(300.dp)
                    .graphicsLayer {
                        translationY = -scrollState.firstVisibleItemScrollOffset * 0.4f
                        alpha = 1f - collapseFraction
                    }
            )

            LazyColumn(state = scrollState, modifier = Modifier.fillMaxSize()) {
                item {
                    Spacer(Modifier.height(300.dp))
                }
                item {
                    Text(
                        text = "ArtistProfile",
                        fontSize = 50.sp
                    )
                }
                items(100) {
                    Text(
                        text = "song",
                        fontSize = 10.sp
                    )
//                SongRow(it)
                }
            }

            TopNavigation(
                title = artistProfile.name.orErrorNoText(),
                alpha = collapseFraction,
                onBackClick = {
                    onBackClick()
                }
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