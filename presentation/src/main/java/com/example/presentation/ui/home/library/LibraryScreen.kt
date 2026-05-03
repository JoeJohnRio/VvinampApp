package com.example.presentation.ui.home.library

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.domain.entity.playlist.PlaylistMinimal
import com.example.presentation.navigation.router.BottomNavRouter
import com.example.presentation.ui.main.ScreenWrapper
import com.example.presentation.ui.widget.ButtonVariableOutlined
import com.example.presentation.ui.widget.LoadingScreen


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LibraryScreen(
    bottomNavRouter: BottomNavRouter,
    viewModel: LibraryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val pagingItems = viewModel.myPlaylists.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                LibraryEvent.NavigateToCreatePlaylist -> {
                    Log.d("joel123", "bottomNav Navigate")
                    bottomNavRouter.navigateToCreatePlaylist()
                }

                is LibraryEvent.NavigateToPlaylistDetail -> {
                    bottomNavRouter.navigateToPlaylistDetail(event.playlistId)
                }
            }
        }
    }

    ScreenWrapper(
        isLoading = uiState.isLoading,
        error = uiState.error,
        onRetry = {

        },
        content = {
            LibraryContent(
                uiState = uiState,
                pagingItems = pagingItems,
                onEvent = { event ->
                    viewModel.onEvent(event)
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
                Log.d("joel123", "test")
                CreatePlaylistBottomSheet(
                    onEvent = {
                        viewModel.onEvent(it)
                    },
                    onDismiss = { viewModel.hideCreatePlaylistSheet() }
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryContent(
    uiState: LibraryUiState,
    pagingItems: LazyPagingItems<PlaylistMinimal>,
    onEvent: (LibraryUiEvent) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(if (uiState.isLibraryViewModeGrid) 3 else 1),
        state = rememberLazyGridState(),
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.background
            )
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        stickyHeader {
            LibraryTopBar(
                onEvent = onEvent
            )
        }

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

        item {
            Spacer(Modifier.height(50.dp))
        }

        when (pagingItems.loadState.refresh) {
            is LoadState.Loading -> item {
                Log.d("joel123", "loading nih")
                LoadingScreen()
            }

            is LoadState.Error -> item {
                ButtonVariableOutlined(
                    text = "Retry",
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .padding(bottom = 12.dp),
                    onClick = {
                        pagingItems.retry()
                    }
                )
            }

            is LoadState.NotLoading -> Unit
        }
    }
}

