package com.example.presentation.ui.home.library.detailplaylist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.domain.entity.song.TrackDomain
import com.example.presentation.R
import com.example.presentation.navigation.router.BottomNavRouter
import com.example.presentation.ui.home.HomeEvent
import com.example.presentation.ui.home.artist.AnimatedTopNavigation
import com.example.presentation.ui.main.ScreenWrapper
import com.example.presentation.ui.main.playback.PlaybackUiEvent
import com.example.presentation.ui.main.playback.PlaybackViewModel
import com.example.presentation.util.ExtractDominantColorEffect
import com.example.presentation.util.orErrorNoText
import com.example.presentation.util.orZero

@Composable
fun PlaylistScreen(
    playlistId: String,
    bottomNavRouter: BottomNavRouter,
    viewModel: PlaylistViewModel = hiltViewModel(),
    playbackViewModel: PlaybackViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val playlistTracks = viewModel.playlistTracks.collectAsLazyPagingItems()

    LaunchedEffect(playlistId) {
        viewModel.getPlaylist(playlistId)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is PlaylistEvent.PlayTrack -> {
                    playbackViewModel.onEvent(PlaybackUiEvent.OnTrackClicked(event.track))
                }
            }
        }
    }

    ScreenWrapper(
        isLoading = uiState.isLoading,
        error = uiState.error,
        content = {
            PlaylistContent(
                uiState = uiState,
                playlistTracks = playlistTracks,
                updateDominantColor = { color ->
                    viewModel.updateDominantColor(color)
                },
                onBackClicked = {
                    bottomNavRouter.navigateBack()
                },
                onEvent = {
                    viewModel.onEvent(it)
                }
            )
        }
    )
}

@Composable
fun PlaylistContent(
    uiState: PlaylistUiState,
    onEvent: (PlaylistUiEvent) -> Unit,
    playlistTracks: LazyPagingItems<TrackDomain>,
    updateDominantColor: (Color) -> Unit,
    onBackClicked: () -> Unit,
    onClickShare: () -> Unit = {},
    onClickMore: () -> Unit = {}
) {
    val scrollState = rememberLazyListState()

    //this is the temporary way
    val isSongExist = uiState.playlist?.totalDuration.orZero() > 0

    val headerHeightPx = with(LocalDensity.current) { 400.dp.toPx() }
    val topBarStartPx = with(LocalDensity.current) { 255.dp.toPx() }
    val topBarEndPx = with(LocalDensity.current) { 265.dp.toPx() }
    val topBarNameStartPx = with(LocalDensity.current) { 265.dp.toPx() }
    val topBarNameEndPx = with(LocalDensity.current) { 270.dp.toPx() }

    val topBarRange = remember { topBarEndPx - topBarStartPx }
    val topBarNameRange = remember { topBarNameEndPx - topBarNameStartPx }

    val noSongsBackgroundColor =
        Brush.verticalGradient(
            0f to Color(0xff474747),
            0.35f to MaterialTheme.colorScheme.background
        )

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
        imageUrl = uiState.playlist?.coverImage,
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
                .background(
                    noSongsBackgroundColor
                )
        ) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                if (isSongExist) {
                    item {
                        Spacer(Modifier.height(innerPadding.calculateTopPadding()))
                    }

                    items(
                        count = playlistTracks.itemCount,
                        key = playlistTracks.itemKey { it.id } // Provide stable keys if your Domain model has an ID
                    ) { index ->
                        val track = playlistTracks[index]

                        if (track != null) {
                            // Render the loaded track
                            PlaylistTrackComponent(
                                track = track,
                                onEvent = onEvent
                            )
                        } else {
                            // Render a shimmer or placeholder if Paging 3 is currently fetching this index
                            TrackPlaceholder()
                        }
                    }

                } else {
                    item {
                        Spacer(Modifier.height(innerPadding.calculateTopPadding()))
                    }

                    item {
                        PlaylistDetailComponent(
                            playlist = uiState.playlist,
                            paddingTop = innerPadding
                        )
                    }
                }

                item {
                    Spacer(Modifier.height(32.dp))
                }

                item {
                    RecommendedTrackComponent(
                        uiState = uiState,
                        onEvent = onEvent
                    )
                }

                item {
                    Spacer(
                        Modifier
                            .height(1000.dp)
                            .fillMaxWidth()
                    )
                }
            }


            AnimatedTopNavigation(
                innerPadding = innerPadding,
                drawableId = R.drawable.ic_arrow_back,
                title = uiState.playlist?.title.orErrorNoText(),
                topBarAlpha = scrollProgress.topBarProgress,
                topBarNameAlpha = scrollProgress.topBarNameProgress,
                topBarColor = uiState.dominantColor,
                onBackClick = {
                    onBackClicked()
                }
            )
        }
    }
}


fun Int.calculateCollaboratorPadding(): Dp {
    val xDistance = (this + 1) * 16
    return xDistance.dp
}