package com.example.presentation.ui.home.artist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.entity.ListeningHistoryDomain
import com.example.domain.entity.artist.ArtistProfileDomain
import com.example.domain.entity.artist.artistProfileDummy
import com.example.domain.entity.song.TopReleasesDomain
import com.example.domain.entity.song.TopSongsDomain
import com.example.domain.entity.song.TrackDomain
import com.example.domain.entity.song.topReleaseDummy
import com.example.domain.entity.song.topSongsDummy
import com.example.presentation.ui.main.ScreenWrapper
import com.example.presentation.ui.main.playback.PlaybackUiEvent
import com.example.presentation.ui.main.playback.PlaybackViewModel
import com.example.presentation.util.orErrorNoText
import com.example.presentation.util.preview.PreviewTheme

@Composable
fun ArtistScreen(
    artistViewModel: ArtistViewModel = hiltViewModel(),
    playbackViewModel: PlaybackViewModel = hiltViewModel(),
    history: ListeningHistoryDomain,
    onBackClick: () -> Unit
) {
    val artistUiState by artistViewModel.uiState.collectAsState()

    LaunchedEffect(history) {
        artistViewModel.getArtistScreenData(history.contentId)
    }

    ScreenWrapper(
        isLoading = artistUiState.isLoading,
        error = artistUiState.error,
        content = {
            artistUiState.artistProfile?.let {
                ArtistProfileContent(
                    artistProfile = it,
                    topSongs = artistUiState.topSongs,
                    topReleases = artistUiState.topReleases,
                    onBackClick = onBackClick,
                    onTrackClicked = { track ->
                        playbackViewModel.onEvent(PlaybackUiEvent.TrackClicked(track))
                    },
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
    onBackClick: () -> Unit,
    onTrackClicked: (TrackDomain) -> Unit
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
                    topBarProgress = ((currentScroll - topBarStartPx) / topBarRange).coerceIn(0f, 1f),
                    topBarNameProgress = ((currentScroll - topBarNameStartPx) / topBarNameRange).coerceIn(0f, 1f),
                    collapseFraction = (currentScroll / headerHeightPx).coerceIn(0f, 1f)
                )
            }
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ArtistHeroImage(
                artistProfile = artistProfile,
                artistNameProgress = scrollProgress.topBarProgress,
                collapseFraction = scrollProgress.collapseFraction,
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
                        modifier = Modifier.background(
                            color = MaterialTheme.colorScheme.background
                        )
                    ) {
                        ArtistActionsRow(
                            artistProfile = artistProfile,
                            isFollowing = true,
                            onFollowClick = { /* Handle follow click */ },
                            onPlayClick = { /* Handle play click */ },
                            onShuffleClick = { /* Handle shuffle click */ }
                        )

                        Spacer(Modifier.height(16.dp))
                    }
                }

                item {
                    topSongs?.let {
                        TopSongsComponent(
                            topSongs = it,
                            artistProfile = artistProfile,
                            onTrackClicked = onTrackClicked
                        )
                    }
                }

                item {
                    topReleases?.let {
                        PopularReleasesContent(
                            it
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

            ArtistTopNavigation(
                innerPadding = innerPadding,
                title = artistProfile.name.orErrorNoText(),
                topBarAlpha = scrollProgress.topBarProgress,
                topBarNameAlpha = scrollProgress.topBarNameProgress,
                collapseFraction = scrollProgress.collapseFraction,
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
            artistProfile = artistProfileDummy,
            topSongs = topSongsDummy,
            topReleases = topReleaseDummy,
            onBackClick = {

            },
            onTrackClicked = {

            }
        )
    }
}