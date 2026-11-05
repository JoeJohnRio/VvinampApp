package com.example.vvinamp.ui.home.artist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.entity.artist.ArtistProfileDomain
import com.example.domain.entity.artist.artistProfileDummy
import com.example.domain.entity.enum.ContentTypeDomain
import com.example.domain.entity.song.TopReleasesDomain
import com.example.domain.entity.song.TopSongsDomain
import com.example.domain.entity.song.TrackDomain
import com.example.domain.entity.song.topReleaseDummy
import com.example.domain.entity.song.topSongsDummy
import com.example.vvinamp.navigation.router.BottomNavRouter
import com.example.vvinamp.ui.main.ScreenWrapper
import com.example.vvinamp.ui.main.playback.PlaybackUiEvent
import com.example.vvinamp.ui.main.playback.PlaybackUiState
import com.example.vvinamp.ui.main.playback.PlaybackViewModel
import com.example.vvinamp.util.ExtractDominantColorEffect
import com.example.vvinamp.util.orErrorNoText
import com.example.vvinamp.util.preview.PreviewTheme

@Composable
fun ArtistScreen(
    artistViewModel: ArtistViewModel = koinViewModel(),
    playbackViewModel: PlaybackViewModel = koinViewModel(),
    artistId: String,
    onBackClick: () -> Unit,
    bottomNavRouter: BottomNavRouter
) {
    val artistUiState by artistViewModel.uiState.collectAsStateWithLifecycle()
    val playbackUiState by playbackViewModel.uiState.collectAsStateWithLifecycle(PlaybackUiState())


    LaunchedEffect(artistId) {
        artistViewModel.getArtistScreenData(artistId)
    }

    ScreenWrapper(
        isLoading = artistUiState.isLoading,
        error = artistUiState.error,
        content = {
            artistUiState.artistProfile?.let {
                ArtistProfileContent(
                    uiState = artistUiState,
                    playbackUiState = playbackUiState,
                    artistProfile = it,
                    topSongs = artistUiState.topSongs,
                    topReleases = artistUiState.topReleases,
                    onBackClick = onBackClick,
                    onClickAlbum = {
                        bottomNavRouter.navigateToAlbum(it)
                    },
                    onTrackClicked = { track ->
                        playbackViewModel.onEvent(
                            PlaybackUiEvent.OnTrackClicked(
                                track = track.copy(
                                    artistProfileImageUrl = it.profileImage,
                                    artistBio = it.bio,
                                    artistMonthlyListeners = it.monthlyListeners.toLong()
                                ),
                                contentTypeDomain = ContentTypeDomain.ARTIST,
                                contentId = artistId
                            )
                        )
                    },
                    updateDominantColor = { color ->
                        artistViewModel.updateDominantColor(color)
                    },
                )
            }
        }
    )
}

@Composable
fun ArtistProfileContent(
    uiState: ArtistUiState,
    artistProfile: ArtistProfileDomain,
    topSongs: TopSongsDomain?,
    topReleases: TopReleasesDomain?,
    onBackClick: () -> Unit,
    onClickAlbum: (String) -> Unit = {},
    updateDominantColor: (Color) -> Unit,
    onTrackClicked: (TrackDomain) -> Unit,
    playbackUiState: PlaybackUiState = PlaybackUiState()
) {
    val scrollState = rememberLazyListState()
    val headerHeightPx = with(LocalDensity.current) { 400.dp.toPx() }
    val topBarStartPx = with(LocalDensity.current) { 215.dp.toPx() }
    val topBarEndPx = with(LocalDensity.current) { 225.dp.toPx() }
    val topBarNameStartPx = with(LocalDensity.current) { 225.dp.toPx() }
    val topBarNameEndPx = with(LocalDensity.current) { 250.dp.toPx() }

    val topBarRange = remember { topBarEndPx - topBarStartPx }
    val topBarNameRange = remember { topBarNameEndPx - topBarNameStartPx }

    data class ScrollProgress(
        val topBarProgress: Float,
        val topBarNameProgress: Float,
        val collapseFraction: Float
    )

    val scrollProgress by remember {
        derivedStateOf {
            val isScrolledPastFirst = scrollState.firstVisibleItemIndex > 0

            if (isScrolledPastFirst) {
                ScrollProgress(1f, 1f, 1f)
            } else {
                val currentScroll = scrollState.firstVisibleItemScrollOffset.toFloat()

                ScrollProgress(
                    topBarProgress = ((currentScroll - topBarStartPx) / topBarRange).coerceIn(
                        0f,
                        1f
                    ),
                    topBarNameProgress = ((currentScroll - topBarNameStartPx) / topBarNameRange).coerceIn(
                        0f,
                        1f
                    ),
                    collapseFraction = (currentScroll / headerHeightPx).coerceIn(0f, 1f)
                )
            }
        }
    }

    ExtractDominantColorEffect(
        imageUrl = artistProfile.profileImage,
        onColorExtracted = { color ->
            updateDominantColor(color)
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        val offset = if (scrollState.firstVisibleItemIndex == 0) {
                            -scrollState.firstVisibleItemScrollOffset.toFloat()
                        } else {
                            -headerHeightPx
                        }
                        translationY = offset
                    }
                    .background(
                        Brush.verticalGradient(
                            0.3f to uiState.dominantColor,
                            0.45f to MaterialTheme.colorScheme.background
                        )
                    )
            ) {
                ArtistHeroImage(
                    artistProfile = artistProfile,
                    artistNameProgress = scrollProgress.topBarProgress,
                    collapseFraction = scrollProgress.collapseFraction,
                    modifier = Modifier
                        .height(300.dp)
                )
            }

            LazyColumn(
                state = scrollState,
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Spacer(Modifier.height(300.dp))
                }

                item {
                    ArtistActionsRow(
                        artistProfile = artistProfile,
                        isFollowing = true,
                        onFollowClick = { /* Handle follow click */ },
                        onPlayClick = { /* Handle play click */ },
                        onShuffleClick = { /* Handle shuffle click */ }
                    )

                    Spacer(Modifier.height(16.dp))
                }

                item {
                    topSongs?.let {
                        TopSongsComponent(
                            topSongs = it,
                            artistProfile = artistProfile,
                            onTrackClicked = onTrackClicked,
                            playbackUiState = playbackUiState
                        )
                    }
                }

                item {
                    topReleases?.let {
                        PopularReleasesContent(
                            it,
                            onClickAlbum = onClickAlbum
                        )
                    }
                }

                item {
                    ArtistAbout(artistProfile)
                }

                item {
                    Spacer(modifier = Modifier.height(100.dp + innerPadding.calculateBottomPadding()))
                }
            }

            AnimatedTopNavigation(
                innerPadding = innerPadding,
                title = artistProfile.name.orErrorNoText(),
                topBarAlpha = scrollProgress.topBarProgress,
                topBarNameAlpha = scrollProgress.topBarNameProgress,
                topBarColor = uiState.dominantColor,
                onBackClick = {
                    onBackClick()
                }
            )
        }
    }
}

@Preview
@Composable
fun ArtistProfileContentPreview() {
    PreviewTheme {
        ArtistProfileContent(
            uiState = ArtistUiState(),
            artistProfile = artistProfileDummy,
            topSongs = topSongsDummy,
            topReleases = topReleaseDummy,
            onBackClick = {

            },
            updateDominantColor = {},
            onTrackClicked = {},
        )
    }
}