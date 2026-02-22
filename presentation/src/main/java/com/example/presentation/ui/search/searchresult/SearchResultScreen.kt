package com.example.presentation.ui.search.searchresult


import android.R.attr.track
import android.util.Log
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.domain.entity.search.SearchItemDomain
import com.example.domain.entity.search.toTrackDomain
import com.example.presentation.navigation.router.BottomNavRouter
import com.example.presentation.ui.main.ScreenWrapper
import com.example.presentation.ui.main.playback.PlaybackUiEvent
import com.example.presentation.ui.main.playback.PlaybackViewModel
import com.example.presentation.ui.search.SearchUiState
import com.example.presentation.ui.search.SearchViewModel
import com.example.presentation.ui.theme.TSBodyMediumWhite
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.ui.theme.TSHeadlineMediumWhite
import com.example.presentation.ui.theme.TSLabelMediumWhite
import com.example.presentation.ui.widget.TextFieldSearch

@Composable
fun SearchResultsScreen(
    bottomNavRouter: BottomNavRouter,
    playbackViewModel: PlaybackViewModel = hiltViewModel(),
) {
    val viewModel: SearchViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val searchItems = viewModel.searchResults.collectAsLazyPagingItems()

    ScreenWrapper(
        isLoading = uiState.isLoading,
        error = uiState.error,
        content = {
            SearchResultsContent(
                uiState = uiState,
                router = bottomNavRouter,
                onTypeSearch = {
                    viewModel.onSearchInput(it)
                },
                searchItems = searchItems,
                onBackClicked = {
                    bottomNavRouter.navigateBack()
                },
                onItemClicked = { searchItem ->
                    when (searchItem) {
                        is SearchItemDomain.TrackSearch -> {
                            playbackViewModel.onEvent(
                                PlaybackUiEvent.TrackClicked(
                                    searchItem.toTrackDomain()
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
//                        is SearchItemDomain.PlaylistSearch ->
//                        is SearchItemDomain.PodcastEpisodeSearch ->
//                        is SearchItemDomain.PodcastSeriesSearch ->
                    }
                }
            )
        }
    )
}

@Composable
fun SearchResultsContent(
    uiState: SearchUiState,
    router: BottomNavRouter,
    onTypeSearch: (String) -> Unit,
    searchItems: LazyPagingItems<SearchItemDomain>,
    onBackClicked: () -> Unit = {},
    onItemClicked: (SearchItemDomain) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TextFieldSearch(
                text = uiState.searchText,
                onValueChanged = {
                    onTypeSearch(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                onBackClicked = onBackClicked
            )
        }
    ) { contentPadding ->

        val isEmpty = searchItems.itemCount == 0 && uiState.searchText.isNotEmpty()
        val isLoading = searchItems.loadState.refresh is LoadState.Loading

        LazyColumn(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(Modifier.height(16.dp))
            }

            items(
                count = searchItems.itemCount,
                key = { index ->
                    // Unique key for stable lists (optional but recommended)
                    val item = searchItems[index]
                    when (item) {
                        is SearchItemDomain.TrackSearch -> "track_${item.id}"
                        is SearchItemDomain.AlbumSearch -> "album_${item.id}"
                        is SearchItemDomain.ArtistSearch -> "artist_${item.id}"
                        else -> index
                    }
                }
            ) { index ->
                SearchItemComponent(searchItems[index], onItemClicked)
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (searchItems.loadState.append is LoadState.Loading) {
                item {
                    CircularProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
        }

        when {
            isLoading -> {
                // Initial full-screen loading
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            isEmpty -> {
                // Truly empty result
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "No results found for \"${uiState.searchText}\"",
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