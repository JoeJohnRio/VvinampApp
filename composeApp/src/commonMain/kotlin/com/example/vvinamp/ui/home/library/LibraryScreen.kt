package com.example.vvinamp.ui.home.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.domain.entity.playlist.PlaylistMinimal
import com.example.domain.entity.playlist.playlistMinimalListDummy
import com.example.vvinamp.navigation.router.BottomNavRouter
import com.example.vvinamp.ui.main.ScreenWrapper
import com.example.vvinamp.ui.theme.TSBodyMediumWhite
import com.example.vvinamp.ui.theme.TSDisplayMediumWhite
import com.example.vvinamp.ui.widget.ButtonVariableOutlined
import com.example.vvinamp.util.preview.PreviewTheme
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LibraryScreen(
    bottomNavRouter: BottomNavRouter,
    viewModel: LibraryViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val pagingItems = viewModel.myPlaylists.collectAsLazyPagingItems()

    // The view model outlives this screen, so reload when coming back from creating or deleting a playlist.
    LaunchedEffect(Unit) {
        pagingItems.refresh()
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is LibraryEvent.NavigateToPlaylistDetail -> {
                    bottomNavRouter.navigateToPlaylistDetail(event.playlistId)
                }
                LibraryEvent.NavigateToCreatePlaylist -> {
                    bottomNavRouter.navigateToCreatePlaylist()
                }
                LibraryEvent.NavigateToSearchResult -> {
                    bottomNavRouter.navigateToSearch()
                }
            }
        }
    }

    ScreenWrapper(
        isLoading = uiState.isLoading,
        error = uiState.error,
        content = {
            LibraryContent(
                uiState = uiState,
                pagingItems = pagingItems,
                onEvent = {
                    viewModel.onEvent(it)
                }
            )

            if (uiState.isSortSheetVisible) {
                SortBottomSheet(
                    currentSort = uiState.currentSort,
                    onSortSelected = { selectedSort ->
                        viewModel.updateSort(selectedSort)
                    },
                    onDismiss = { viewModel.hideSortSheet() }
                )
            }

            if (uiState.isCreatePlaylistOptionSheetVisible) {
                CreatePlaylistBottomSheet(
                    onEvent = { viewModel.onEvent(it) },
                    onDismiss = { viewModel.hideCreatePlaylistSheet() }
                )
            }
        }
    )
}

@Composable
fun LibraryContent(
    uiState: LibraryUiState,
    pagingItems: LazyPagingItems<PlaylistMinimal>,
    onEvent: (LibraryUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            LibraryTopBar(onEvent = onEvent)
        }
    ) { contentPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(if (uiState.isLibraryViewModeGrid) 3 else 1),
            state = rememberLazyGridState(),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(contentPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                LibrarySortBar(
                    onEvent = onEvent,
                    sortBy = uiState.currentSort.label,
                    isLibraryViewModeGrid = uiState.isLibraryViewModeGrid
                )
            }

            items(
                count = pagingItems.itemCount,
                key = pagingItems.itemKey { it.id }
            ) { index ->
                val playlist = pagingItems[index]
                if (playlist != null) {
                    if (uiState.isLibraryViewModeGrid) {
                        LibraryGridItem(playlist, onEvent)
                    } else {
                        LibraryItem(playlist, onEvent)
                    }
                }
            }

            val refresh = pagingItems.loadState.refresh
            item(span = { GridItemSpan(maxLineSpan) }) {
                when {
                    refresh is LoadState.Loading && pagingItems.itemCount == 0 -> LibraryLoading()

                    refresh is LoadState.Error -> LibraryMessage(
                        title = "Couldn't load your playlists",
                        body = "Check your connection and try again.",
                        buttonText = "Retry",
                        onClick = { pagingItems.retry() }
                    )

                    refresh is LoadState.NotLoading && pagingItems.itemCount == 0 -> LibraryMessage(
                        title = "No playlists yet",
                        body = "Create one, then add songs from Search or the recommendations.",
                        buttonText = "Create playlist",
                        onClick = { onEvent(LibraryUiEvent.onCreateNewPlaylistClicked) }
                    )

                    // Keeps the last playlist clear of the mini player and bottom bar.
                    else -> Spacer(Modifier.height(50.dp))
                }
            }
        }
    }
}

@Composable
private fun LibraryLoading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun LibraryMessage(
    title: String,
    body: String,
    buttonText: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            style = TSDisplayMediumWhite().copy(fontSize = 18.sp),
            textAlign = TextAlign.Center
        )
        Text(
            text = body,
            style = TSBodyMediumWhite().copy(color = MaterialTheme.colorScheme.outline),
            textAlign = TextAlign.Center
        )
        ButtonVariableOutlined(
            text = buttonText,
            modifier = Modifier.padding(top = 4.dp),
            onClick = onClick
        )
    }
}

@Preview
@Composable
fun LibraryScreenPreview() {
    val fakeFlow = remember { MutableStateFlow(PagingData.from(playlistMinimalListDummy)) }
    val pagingItems = fakeFlow.collectAsLazyPagingItems()

    PreviewTheme {
        LibraryContent(
            uiState = LibraryUiState(),
            pagingItems = pagingItems,
            onEvent = {}
        )
    }
}
