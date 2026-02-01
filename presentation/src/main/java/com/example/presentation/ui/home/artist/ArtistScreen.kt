package com.example.presentation.ui.home.artist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.draw.alpha
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
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.ui.theme.TSHeadlineMediumWhite
import com.example.presentation.util.ListenerUtils
import com.example.presentation.util.orErrorNoText

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
//                SpotifyArtistScreen(
//                    artistProfile = it.profileImage.orErrorNoText()
//                )
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
    val headerHeightPx = with(LocalDensity.current) { 400.dp.toPx() }
    val density = LocalDensity.current
    val topBarJourney: Pair<Float, Float> = Pair(
        with(density) { 215.dp.toPx() },
        with(density) { 225.dp.toPx() }
    )
    val topBarRange = topBarJourney.second - topBarJourney.first

    val topBarProgress by remember {
        derivedStateOf {
            if (scrollState.firstVisibleItemIndex > 0) {
                1f
            } else {
                val currentScroll = scrollState.firstVisibleItemScrollOffset.toFloat()
                val progress = (currentScroll - topBarJourney.first) / topBarRange
                progress.coerceIn(0f, 1f)
            }
        }
    }

    val topBarNameJourney: Pair<Float, Float> = Pair(
        with(density) { 225.dp.toPx() },
        with(density) { 250.dp.toPx() }
    )
    val topBarNameRange = topBarNameJourney.second - topBarNameJourney.first

    val topBarNameProgress by remember {
        derivedStateOf {
            if (scrollState.firstVisibleItemIndex > 0) {
                1f
            } else {
                val currentScroll = scrollState.firstVisibleItemScrollOffset.toFloat()
                val progress = (currentScroll - topBarNameJourney.first) / topBarNameRange
                progress.coerceIn(0f, 1f)
            }
        }
    }

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
                artistProfile = artistProfile,
                artistNameProgress = topBarProgress,
                collapseFraction = collapseFraction,
                modifier = Modifier
                    .height(300.dp)
                    .graphicsLayer {
                        translationY = -scrollState.firstVisibleItemScrollOffset * 1f
                    }
            )

            LazyColumn(
                state = scrollState,
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Spacer(Modifier.height(300.dp))
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.onPrimary)
                            .padding(
                                start = 16.dp,
                                top = 16.dp,
                            )
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 8.dp),
                            text = "${ListenerUtils.formatCompact(artistProfile.monthlyListeners.toLong())} monthly listeners",
                            style = TSDisplayMediumWhite().copy(
                                fontSize = 12.sp
                            )
                        )

                        ArtistActionsRow(
                            artistProfile = artistProfile,
                            isFollowing = true,
                            onFollowClick = { /* Handle follow click */ },
                            onPlayClick = { /* Handle play click */ },
                            onShuffleClick = { /* Handle shuffle click */ }
                        )
                    }
                }
                items(100) {
                    Text(
                        modifier = Modifier
                            .background(Color.Black)
                            .fillMaxWidth(),
                        text = "song",
                        fontSize = 10.sp
                    )
//                SongRow(it)
                }
            }

            TopNavigation(
                title = artistProfile.name.orErrorNoText(),
                topBarAlpha = topBarProgress,
                topBarNameAlpha = topBarNameProgress,
                onBackClick = {
                    onBackClick()
                }
            )
        }
    }
}

@Composable
fun ArtistImage(
    artistProfile: ArtistProfileDomain, // Replace with your image source
    modifier: Modifier = Modifier,
    collapseFraction: Float,
    artistNameProgress: Float
) {

    Image(
        painter = rememberAsyncImagePainter(artistProfile.profileImage),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )

    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        1f to Color.Black.copy(alpha = 0.5f)
                    )
                )
                .graphicsLayer {
                    alpha = 1f - collapseFraction
                }
        )
        Column(
            modifier = Modifier
                .align(
                    Alignment.BottomStart
                )
                .alpha(
                    alpha = 1f - artistNameProgress
                )
                .padding(
                    start = 12.dp,
                    bottom = 18.dp
                )
        ) {
            Text(
                text = artistProfile.name,
                style = TSHeadlineMediumWhite().copy(
                    fontSize = 50.sp
                )
            )
        }
    }
}

@Composable
fun TopNavigation(
    title: String,
    topBarAlpha: Float, // Passed from the scroll logic (0f to 1f)
    onBackClick: () -> Unit,
    topBarNameAlpha: Float
) {
    Surface(
        color = Color.Black.copy(
            alpha = topBarAlpha
        ),
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