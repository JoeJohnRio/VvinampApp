package com.example.vvinamp.ui.search.searchresult

import com.example.vvinamp.ui.main.playback.PlayingFrom
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import com.example.vvinamp.util.preview.PreviewContainer
import kotlinx.coroutines.flow.flowOf
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.compose.koinInject
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.domain.entity.search.SearchItemDomain
import com.example.domain.entity.search.toTrackDomain
import com.example.vvinamp.navigation.router.BottomNavRouter
import com.example.vvinamp.ui.main.ScreenWrapper
import com.example.domain.entity.enum.PlaybackContextType
import com.example.vvinamp.ui.main.playback.PlaybackUiEvent
import com.example.vvinamp.ui.main.playback.PlaybackUiState
import com.example.vvinamp.ui.main.playback.PlaybackViewModel
import com.example.vvinamp.ui.search.SearchType
import com.example.vvinamp.ui.search.SearchUiState
import com.example.vvinamp.ui.search.SearchViewModel
import com.example.vvinamp.ui.theme.TSBodyMediumWhite
import com.example.vvinamp.ui.theme.TSDisplayMediumWhite
import com.example.vvinamp.ui.widget.ButtonListWithState
import com.example.vvinamp.ui.widget.ButtonVariableOutlined
import com.example.vvinamp.ui.widget.TextFieldSearch

@Composable
fun SearchResultsScreen(
    bottomNavRouter: BottomNavRouter,
    playbackViewModel: PlaybackViewModel = koinInject(),
) {
    val viewModel: SearchViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val searchItems = viewModel.searchResults.collectAsLazyPagingItems()
    val playlists = viewModel.myPlaylists.collectAsLazyPagingItems()
    val playbackUiState by playbackViewModel.uiState.collectAsStateWithLifecycle(PlaybackUiState())

    if (uiState.trackForPlaylist != null) {
        AddToPlaylistBottomSheet(
            track = uiState.trackForPlaylist!!,
            playlists = playlists,
            isAdding = uiState.isAddingToPlaylist,
            onPlaylistSelected = { playlistId, playlistTitle ->
                viewModel.addTrackToPlaylist(playlistId, playlistTitle)
            },
            onDismiss = {
                viewModel.onDismissAddToPlaylist()
            }
        )
    }

    val refreshError = (searchItems.loadState.refresh as? LoadState.Error)?.error?.message
    val isYoutubeFailure = refreshError != null && uiState.searchType == SearchType.YOUTUBE
    val displayError = uiState.error ?: refreshError.takeUnless { isYoutubeFailure }

    LaunchedEffect(isYoutubeFailure) {
        if (isYoutubeFailure) {
            viewModel.onSearchTypeChanged(SearchType.CATALOG)
        }
    }

    ScreenWrapper(
        isLoading = uiState.isLoading,
        error = displayError,
        onRetry = {
            searchItems.retry()
        },
        content = {
            SearchResultsContent(
                uiState = uiState,
                playbackUiState = playbackUiState,
                onTypeSearch = {
                    viewModel.onSearchInput(it)
                },
                onSearchSubmit = {
                    viewModel.onSearchSubmit()
                },
                onSearchTypeChanged = {
                    viewModel.onSearchTypeChanged(it)
                },
                searchItems = searchItems,
                onBackClicked = {
                    bottomNavRouter.navigateBack()
                },
                onAddToPlaylistClicked = {
                    viewModel.onOpenAddToPlaylist(it)
                },
                onItemClicked = { searchItem ->
                    when (searchItem) {
                        is SearchItemDomain.TrackSearch -> {
                            // Queue the catalog songs loaded so far, starting at the tapped one.
                            playbackViewModel.onEvent(
                                PlaybackUiEvent.PlayContext(
                                    contextType = PlaybackContextType.SEARCH_RESULTS,
                                    trackIds = searchItems.itemSnapshotList.items
                                        .filterIsInstance<SearchItemDomain.TrackSearch>()
                                        .map { it.id },
                                    offsetTrackId = searchItem.id,
                                    playingFrom = PlayingFrom("SEARCH", "\"${uiState.searchText}\"")
                                )
                            )
                        }
                        is SearchItemDomain.YoutubeTrackSearch -> {
                            playbackViewModel.onEvent(
                                PlaybackUiEvent.OnYoutubeTrackClicked(
                                    track = searchItem.toTrackDomain()
                                )
                            )
                        }
                        is SearchItemDomain.AlbumSearch -> {
                            bottomNavRouter.navigateToAlbum(searchItem.id)
                        }
                        is SearchItemDomain.ArtistSearch -> {
                            bottomNavRouter.navigateToArtistPage(searchItem.id)
                        }
                        else -> {
                            //do nothing
                        }
                    }
                }
            )
        }
    )
}

@Composable
fun SearchResultsContent(
    uiState: SearchUiState,
    onTypeSearch: (String) -> Unit,
    onSearchTypeChanged: (SearchType) -> Unit,
    searchItems: LazyPagingItems<SearchItemDomain>,
    onBackClicked: () -> Unit = {},
    onItemClicked: (SearchItemDomain) -> Unit = {},
    onAddToPlaylistClicked: (SearchItemDomain.YoutubeTrackSearch) -> Unit = {},
    playbackUiState: PlaybackUiState = PlaybackUiState(),
    onSearchSubmit: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                TextFieldSearch(
                    text = uiState.searchText,
                    onValueChanged = {
                        onTypeSearch(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    onBackClicked = onBackClicked,
                    onSearch = onSearchSubmit
                )

                ButtonListWithState(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    buttonHorizontalPadding = 12.dp,
                    buttonHeight = 32.dp,
                    textStyle = TSDisplayMediumWhite().copy(fontSize = 11.sp),
                    listButton = SearchType.entries.map { it.label },
                    selectedButton = uiState.searchType.label,
                    outlineColor = Color.Transparent,
                    textColor = MaterialTheme.colorScheme.inversePrimary,
                    textColorClicked = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    containerColorClicked = MaterialTheme.colorScheme.primary,
                    onClick = { label ->
                        SearchType.entries.find { it.label == label }?.let {
                            onSearchTypeChanged(it)
                        }
                    }
                )
            }
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            val isLoading = (searchItems.loadState.refresh is LoadState.Loading)
                    && uiState.activeQuery.isNotEmpty()

            val isEmpty = !isLoading
                    && searchItems.loadState.append.endOfPaginationReached
                    && searchItems.itemCount == 0
                    && uiState.activeQuery.isNotEmpty()

            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                item {
                    Spacer(Modifier.height(16.dp))
                }

                items(
                    count = searchItems.itemCount,
                    key = { index ->
                        when (val item = searchItems.peek(index)) {
                            is SearchItemDomain.TrackSearch -> "track_${item.id}_$index"
                            is SearchItemDomain.YoutubeTrackSearch -> "youtube_${item.id}_$index"
                            is SearchItemDomain.AlbumSearch -> "album_${item.id}_$index"
                            is SearchItemDomain.ArtistSearch -> "artist_${item.id}_$index"
                            else -> "item_$index"
                        }
                    }
                ) { index ->
                    SearchItemComponent(
                        domain = searchItems[index],
                        onClicked = onItemClicked,
                        playbackUiState = playbackUiState,
                        onAddToPlaylistClicked = onAddToPlaylistClicked
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (searchItems.loadState.append is LoadState.Loading) {
                    item {
                        CircularProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }
                }

                if (searchItems.loadState.append is LoadState.Error) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = (searchItems.loadState.append as LoadState.Error).error.message ?: "Failed to load more results",
                                color = MaterialTheme.colorScheme.error,
                                style = TSDisplayMediumWhite().copy(fontSize = 12.sp)
                            )
                            Spacer(Modifier.height(8.dp))
                            ButtonVariableOutlined(
                                text = "Retry",
                                onClick = { searchItems.retry() }
                            )
                        }
                    }
                }
            }

            when {
                isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                isEmpty -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = "No results found for \"${uiState.activeQuery}\"",
                                style = TSBodyMediumWhite().copy(
                                    fontSize = 22.sp,
                                    textAlign = TextAlign.Center
                                )
                            )
                            Spacer(Modifier.height(16.dp))

                            Text(
                                text = "Please make sure your words are spelled correctly or use less or different keywords.",
                                style = TSDisplayMediumWhite().copy(
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SearchResultsScreenPreview() {
    val sampleTracks = listOf(
        SearchItemDomain.TrackSearch(
            id = "1",
            title = "Sample Track 1",
            duration = 180,
            albumCoverUrl = "",
            artistName = "Sample Artist 1"
        ),
        SearchItemDomain.TrackSearch(
            id = "2",
            title = "Sample Track 2",
            duration = 240,
            albumCoverUrl = "",
            artistName = "Sample Artist 2"
        ),
        SearchItemDomain.AlbumSearch(
            id = "3",
            title = "Sample Album",
            coverArt = "",
            artistName = "Sample Artist"
        ),
        SearchItemDomain.ArtistSearch(
            id = "4",
            name = "Sample Artist",
            verified = true,
            profileImage = ""
        )
    )

    val pagingData = PagingData.from(sampleTracks)
    val searchItems = flowOf(pagingData).collectAsLazyPagingItems()

    PreviewContainer {
        SearchResultsContent(
            uiState = SearchUiState(
                searchText = "Sample",
                searchType = SearchType.CATALOG
            ),
            onTypeSearch = {},
            onSearchTypeChanged = {},
            searchItems = searchItems,
            onBackClicked = {},
            onItemClicked = {},
            playbackUiState = PlaybackUiState()
        )
    }
}

@Preview
@Composable
fun SearchResultsScreenEmptyPreview() {
    val pagingData = PagingData.from(emptyList<SearchItemDomain>())
    val searchItems = flowOf(pagingData).collectAsLazyPagingItems()

    PreviewContainer {
        SearchResultsContent(
            uiState = SearchUiState(
                searchText = "Unknown",
                searchType = SearchType.CATALOG
            ),
            onTypeSearch = {},
            onSearchTypeChanged = {},
            searchItems = searchItems,
            onBackClicked = {},
            onItemClicked = {},
            playbackUiState = PlaybackUiState()
        )
    }
}
