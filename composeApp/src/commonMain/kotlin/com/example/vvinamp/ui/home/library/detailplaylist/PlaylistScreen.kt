package com.example.vvinamp.ui.home.library.detailplaylist
import vvinampapp.composeapp.generated.resources.*

import com.example.vvinamp.util.KmpLogger
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.domain.entity.enum.ContentTypeDomain
import com.example.domain.entity.enum.PlaybackContextType
import com.example.domain.entity.playlist.dummyPlaylists
import com.example.domain.entity.song.TrackDomain
import com.example.domain.entity.song.dummyRecommendedTracks
import com.example.domain.entity.song.trackDummy

import com.example.vvinamp.navigation.router.BottomNavRouter
import com.example.vvinamp.ui.home.artist.AnimatedTopSlidingNameNavigation
import com.example.vvinamp.ui.main.ScreenWrapper
import com.example.vvinamp.ui.main.playback.PlaybackUiEvent.*
import com.example.vvinamp.ui.main.playback.PlaybackUiState
import com.example.vvinamp.ui.main.playback.PlaybackViewModel
import com.example.vvinamp.util.ExtractDominantColorEffect
import com.example.vvinamp.util.orErrorNoText
import com.example.vvinamp.util.orZero
import com.example.vvinamp.util.preview.PreviewTheme
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun PlaylistScreen(
    playlistId: String,
    bottomNavRouter: BottomNavRouter,
    viewModel: PlaylistViewModel = koinViewModel(),
    playbackViewModel: PlaybackViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val playbackUiState by playbackViewModel.uiState.collectAsStateWithLifecycle()
    val playlistTracks = viewModel.playlistTracks.collectAsLazyPagingItems()

    LaunchedEffect(playlistId) {
        viewModel.getPlaylist(playlistId)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is PlaylistEvent.PlayTrack -> {
                    playbackViewModel.onEvent(
                        PlayContext(
                            contextType = PlaybackContextType.PLAYLIST,
                            contextId = event.playlistId,
                            offsetTrackId = event.trackId
                        )
                    )
                }

                is PlaylistEvent.PlayTrackRecommended -> {
                    playbackViewModel.onEvent(
                        OnTrackClicked(
                            track = event.track,
                            contentTypeDomain = ContentTypeDomain.PLAYLIST,
                            contentId = playlistId
                        )
                    )
                }

                is PlaylistEvent.NavigateToEditScreen -> {
                    bottomNavRouter.navigateToPlaylistEdit(event.playlistId)
                }

                PlaylistEvent.OnClickShuffle -> {
                    playbackViewModel.onEvent(ToggleShuffleClicked)
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
                playbackUiState = playbackUiState,
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

            if (uiState.isSortSheetVisible) {
                PlaylistTrackSortBottomSheet(
                    currentSort = uiState.sortBy,
                    onSortSelected = { selectedSort ->
                        viewModel.updateSort(selectedSort)
                    },
                    onDismiss = { viewModel.hideSortSheet() }
                )
            }

            if (uiState.isEditDetailSheetVisible) {
                EditPlaylistDetailBottomSheet(
                    uiState = uiState,
                    onEvent = { viewModel.onEvent(it) },
                    onDismiss = { viewModel.onEvent(PlaylistUiEvent.onCancelEditDetail) }
                )
            }
        }
    )
}

@Composable
fun PlaylistContent(
    uiState: PlaylistUiState,
    playbackUiState: PlaybackUiState,
    onEvent: (PlaylistUiEvent) -> Unit,
    playlistTracks: LazyPagingItems<TrackDomain>,
    updateDominantColor: (Color) -> Unit,
    onBackClicked: () -> Unit,
) {
    val scrollState = rememberLazyListState()

    //this is the temporary way
    val isSongExist = uiState.playlist?.totalDuration.orZero() > 0

    val headerHeightPx = with(LocalDensity.current) { 400.dp.toPx() }
    val topBarStartPx = with(LocalDensity.current) { (215).dp.toPx() }
    val topBarEndPx = with(LocalDensity.current) { (255).dp.toPx() }
    val topBarNameStartPx = with(LocalDensity.current) { (230).dp.toPx() }
    val topBarNameEndPx = with(LocalDensity.current) { (260).dp.toPx() }

    val topBarRange = remember { topBarEndPx - topBarStartPx }
    val topBarNameRange = remember { topBarNameEndPx - topBarNameStartPx }

    val noSongsBackgroundColor =
        if (isSongExist) {
            Brush.verticalGradient(
                0f to uiState.dominantColor,
                0.5f to MaterialTheme.colorScheme.background
            )
        } else {
            Brush.verticalGradient(
                0f to Color(0xff474747),
                0.5f to MaterialTheme.colorScheme.background
            )
        }

    data class ScrollProgress(
        val topBarProgress: Float,
        val topBarNameProgress: Float,
    )

    val scrollProgress by remember {
        derivedStateOf {
            val isScrolledPastFirst = scrollState.firstVisibleItemIndex > 0

            if (isScrolledPastFirst) {
                ScrollProgress(1f, 1f)
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
                )
            }
        }
    }

    ExtractDominantColorEffect(
        imageUrl = uiState.playlist?.coverImage?.ifEmpty { uiState.playlist.albumCovers.firstOrNull() },
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
                .graphicsLayer {
                    val offset = if (scrollState.firstVisibleItemIndex == 0) {
                        -scrollState.firstVisibleItemScrollOffset.toFloat()
                    } else {
                        -headerHeightPx
                    }
                    translationY = offset
                }
                .background(
                    noSongsBackgroundColor
                )
        )

        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            if (isSongExist) {
                item {
                    PlaylistHeroImage(
                        playlist = uiState.playlist,
                        modifier = Modifier,
                        scrollStateProvider = {
                            scrollState.firstVisibleItemScrollOffset.toFloat()
                        },
                        innerPadding = innerPadding
                    )
                }

                item {
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    PlaylistDetailTrackFilled(
                        uiState.playlist
                    )
                }

                item {
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    PlaylistActionRow(
                        uiState = uiState,
                        playbackUiState = playbackUiState,
                        onEvent = onEvent
                    )
                }

                item {
                    Spacer(Modifier.height(16.dp))
                }

                items(
                    count = playlistTracks.itemCount,
                    key = playlistTracks.itemKey { it.id },

                    ) { index ->
                    val track = playlistTracks[index]

                    if (track != null) {
                        ItemPlaylistTrackComponent(
                            uiState = uiState,
                            track = track,
                            onEvent = onEvent
                        )
                    } else {
                        TrackPlaceholder()
                    }

                    Spacer(Modifier.height(16.dp))
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
                        .height(100.dp)
                        .fillMaxWidth()
                )
            }
        }


        AnimatedTopSlidingNameNavigation(
            innerPadding = innerPadding,
            drawableId = Res.drawable.ic_arrow_back,
            title = uiState.playlist?.title.orErrorNoText(),
            topBarAlpha = scrollProgress.topBarProgress,
            titleProgress = scrollProgress.topBarNameProgress,
            topBarColor = uiState.dominantColor,
            onBackClick = {
                onBackClicked()
            }
        )
    }
}

@Preview(name = "With Recommended Tracks Detailed", showBackground = true)
@Composable
fun PlaylistContentWithRecommendationsDetailedPreview() {
    val pagingDataFlow = MutableStateFlow(PagingData.from(trackDummy))
    val lazyPaging = pagingDataFlow.collectAsLazyPagingItems()

    PreviewTheme {
        PlaylistContent(
            uiState = PlaylistUiState(
                recommendedTracks = dummyRecommendedTracks,
                isPlaylistCreator = true,
                playlist = dummyPlaylists.first()
            ),
            playbackUiState = PlaybackUiState(),
            playlistTracks = lazyPaging,
            updateDominantColor = { _ -> },
            onBackClicked = {},
            onEvent = {}
        )
    }
}

@Preview(name = "With Recommended Tracks", showBackground = true)
@Composable
fun PlaylistContentWithRecommendationsPreview() {
    val pagingDataFlow = MutableStateFlow(PagingData.from(trackDummy))
    val lazyPaging = pagingDataFlow.collectAsLazyPagingItems()

    PreviewTheme {
        PlaylistContent(
            uiState = PlaylistUiState(
                recommendedTracks = dummyRecommendedTracks,
            ),
            playbackUiState = PlaybackUiState(),
            playlistTracks = lazyPaging,
            updateDominantColor = { _ -> },
            onBackClicked = {},
            onEvent = {}
        )
    }
}

@Preview(name = "Without Recommended Tracks", showBackground = true)
@Composable
fun PlaylistContentWithoutRecommendationsPreview() {
    val pagingDataFlow = MutableStateFlow(PagingData.from(trackDummy))
    val lazyPaging = pagingDataFlow.collectAsLazyPagingItems()

    PreviewTheme {
        PlaylistContent(
            uiState = PlaylistUiState(
                recommendedTracks = emptyList() // List kosong
            ),
            playbackUiState = PlaybackUiState(),
            playlistTracks = lazyPaging,
            updateDominantColor = { _ -> },
            onBackClicked = {},
            onEvent = {}
        )
    }
}


fun Int.calculateCollaboratorPadding(): Dp {
    val xDistance = (this + 1) * 16
    return xDistance.dp
}
