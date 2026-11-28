package com.example.vvinamp.ui.home.library.detailplaylist
import com.example.vvinamp.ui.main.playback.PlayingFrom
import com.example.vvinamp.ui.theme.orThemeBackground
import vvinampapp.composeapp.generated.resources.*

import com.example.vvinamp.util.KmpLogger
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.koin.compose.koinInject
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
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
import com.example.vvinamp.util.PaddingSizeUtil
import com.example.vvinamp.util.PaddingSizeUtil.TopBarHeight
import com.example.vvinamp.util.PaddingSizeUtil.PlaylistNoPhotoSize
import com.example.vvinamp.util.preview.PreviewTheme
import com.example.vvinamp.util.showToast
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun PlaylistScreen(
    playlistId: String,
    bottomNavRouter: BottomNavRouter,
    viewModel: PlaylistViewModel = koinViewModel(),
    playbackViewModel: PlaybackViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val playbackUiState by playbackViewModel.uiState.collectAsStateWithLifecycle(PlaybackUiState())
    val playlistTracks = viewModel.playlistTracks.collectAsLazyPagingItems()

    // Runs on every visit: the view model is shared, so its song list can be left over from an earlier visit.
    LaunchedEffect(playlistId) {
        viewModel.getPlaylist(playlistId)
        playlistTracks.refresh()
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is PlaylistEvent.PlayTrack -> {
                    playbackViewModel.onEvent(
                        PlayContext(
                            contextType = PlaybackContextType.PLAYLIST,
                            contextId = event.playlistId,
                            offsetTrackId = event.trackId,
                            playingFrom = uiState.playlist?.let { PlayingFrom(PlayingFrom.PLAYLIST, it.title, event.playlistId) }
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

                PlaylistEvent.RefreshTracks -> {
                    playlistTracks.refresh()
                }

                PlaylistEvent.NavigateToSearch -> {
                    // Search adds straight to this playlist instead of asking which one.
                    bottomNavRouter.navigateToSearchResults(
                        targetPlaylistId = playlistId,
                        targetPlaylistTitle = uiState.playlist?.title
                    )
                }

                PlaylistEvent.PlaylistDeleted -> {
                    showToast("Playlist deleted")
                    bottomNavRouter.navigateBack()
                }
            }
        }
    }

    if (uiState.isDeleteConfirmVisible) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(PlaylistUiEvent.onDismissDeletePlaylist) },
            title = { Text("Delete this playlist?") },
            text = { Text("\"${uiState.playlist?.title.orEmpty()}\" will be removed from your library. You can't undo this.") },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.onEvent(PlaylistUiEvent.onConfirmDeletePlaylist) },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(PlaylistUiEvent.onDismissDeletePlaylist) }) {
                    Text("Cancel")
                }
            }
        )
    }

    ScreenWrapper(
        isLoading = uiState.isLoading,
        error = uiState.error,
        onRetry = {
            viewModel.getPlaylist(playlistId)
            playlistTracks.refresh()
        },
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

    // Empty playlists use a fixed grey, as Spotify does, so the bar matches the background.
    val headerColor = if (isSongExist) uiState.dominantColor.orThemeBackground() else Color(0xff474747)

    val headerBackground = Brush.verticalGradient(
        0f to headerColor,
        0.5f to MaterialTheme.colorScheme.background
    )

    // Real height of the header item, so the animation follows the actual layout instead of fixed dp guesses.
    var headerHeightPx by remember { mutableIntStateOf(0) }
    // Height of the title text under the hero; it can wrap to two lines.
    var filledTitleHeightPx by remember { mutableIntStateOf(0) }

    val headerScrolledPx by remember {
        derivedStateOf {
            if (scrollState.firstVisibleItemIndex == 0) {
                scrollState.firstVisibleItemScrollOffset
            } else {
                headerHeightPx
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
        val density = LocalDensity.current
        val barBottomPx = with(density) { (innerPadding.calculateTopPadding() + TopBarHeight).toPx() }
        val barFadePx = with(density) { 64.dp.toPx() }
        val titleSlidePx = with(density) { 32.dp.toPx() }

        // Empty playlist: the bar is solid once it reaches the middle of the cover. The cover sits centered in
        // the row under the top spacer, so its middle is that spacer plus half the cover.
        val emptyCoverMiddlePx = with(density) {
            (innerPadding.calculateTopPadding() + TopBarHeight + 8.dp + PlaylistNoPhotoSize / 2).toPx()
        }

        // Playlist with songs: the title sits under the hero (status bar + 16dp + hero + 16dp spacer).
        // The change runs from the title's top edge reaching the bar's bottom to its bottom edge reaching it.
        // Measured from the real layout, so Android's status bar and font size are taken into account.
        val filledTitleTopPx = with(density) {
            (innerPadding.calculateTopPadding() + 16.dp + PaddingSizeUtil.PlaylistHeroSize + 16.dp).toPx()
        }

        // Empty playlist: bar fades in over the last 64dp before the cover's middle reaches the bar; title over the last 32dp.
        val scrollProgress by remember(barBottomPx, emptyCoverMiddlePx, filledTitleTopPx, isSongExist) {
            derivedStateOf {
                if (headerHeightPx == 0) {
                    ScrollProgress(0f, 0f)
                } else if (isSongExist && filledTitleHeightPx > 0) {
                    val titleTopOnScreen = filledTitleTopPx - headerScrolledPx
                    val progress = ((barBottomPx - titleTopOnScreen) / filledTitleHeightPx).coerceIn(0f, 1f)
                    ScrollProgress(topBarProgress = progress, topBarNameProgress = progress)
                } else {
                    val solidPointPx = if (isSongExist) headerHeightPx.toFloat() else emptyCoverMiddlePx
                    val distanceToBar = solidPointPx - headerScrolledPx - barBottomPx
                    ScrollProgress(
                        topBarProgress = (1f - distanceToBar / barFadePx).coerceIn(0f, 1f),
                        topBarNameProgress = (1f - distanceToBar / titleSlidePx).coerceIn(0f, 1f),
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationY = -headerScrolledPx.toFloat()
                }
                .background(headerBackground)
        )

        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // One keyed header item in both states. A 0dp first item made the list report index 1 on open,
            // which showed the bar fully before any scroll.
            item(key = HEADER_KEY) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onSizeChanged { headerHeightPx = it.height }
                ) {
                    if (isSongExist) {
                        PlaylistHeroImage(
                            playlist = uiState.playlist,
                            modifier = Modifier,
                            scrollStateProvider = { headerScrolledPx.toFloat() },
                            innerPadding = innerPadding
                        )

                        Spacer(Modifier.height(16.dp))

                        PlaylistDetailTrackFilled(
                            playlist = uiState.playlist,
                            titleModifier = Modifier.onSizeChanged { filledTitleHeightPx = it.height }
                        )
                    } else {
                        // Starts below the top bar so the back button never sits on the cover.
                        Spacer(Modifier.height(innerPadding.calculateTopPadding() + TopBarHeight + 8.dp))

                        PlaylistDetailComponent(
                            playlist = uiState.playlist,
                            isCreator = uiState.isPlaylistCreator,
                            onClickEditDetails = { onEvent(PlaylistUiEvent.onClickChange) }
                        )
                    }
                }
            }

            if (isSongExist) {
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

                // The playlist header arrives before its songs, so fill the gap with placeholder rows.
                if (playlistTracks.itemCount == 0 && playlistTracks.loadState.refresh is LoadState.Loading) {
                    items(6) {
                        TrackPlaceholder()
                        Spacer(Modifier.height(16.dp))
                    }
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
                    PlaylistEmptyActions(
                        playlist = uiState.playlist,
                        isCreator = uiState.isPlaylistCreator,
                        onClickShare = { onEvent(PlaylistUiEvent.onClickShare) },
                        onClickAdd = { onEvent(PlaylistUiEvent.onRowClickAdd) }
                    )
                }
            }

            item {
                Spacer(Modifier.height(32.dp))
            }

            item {
                // Leave out songs already in the playlist; ones just added here stay so their tick shows.
                // shortcut: only songs on the pages loaded so far are checked.
                val inPlaylist = playlistTracks.itemSnapshotList.items.mapTo(HashSet()) { it.id }
                RecommendedTrackComponent(
                    uiState = uiState.copy(
                        recommendedTracks = uiState.recommendedTracks?.filter { it.isAdded || it.id !in inPlaylist }
                    ),
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
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            title = uiState.playlist?.title.orErrorNoText(),
            topBarAlpha = scrollProgress.topBarProgress,
            titleProgress = scrollProgress.topBarNameProgress,
            topBarColor = headerColor,
            onBackClick = {
                onBackClicked()
            },
            onMoreClick = { onEvent(PlaylistUiEvent.onClickDetail) }
        )
    }
}

private const val HEADER_KEY = "playlist_header"

private data class ScrollProgress(
    val topBarProgress: Float,
    val topBarNameProgress: Float,
)

@Preview
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

@Preview
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

@Preview
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
