package com.example.vvinamp.ui.home.library
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import androidx.paging.compose.collectAsLazyPagingItems

import com.example.vvinamp.navigation.router.BottomNavRouter
import com.example.vvinamp.ui.main.ScreenWrapper
import com.example.vvinamp.util.preview.PreviewTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LibraryScreen(
    bottomNavRouter: BottomNavRouter,
    viewModel: LibraryViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

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
        }
    )
}

@Composable
fun LibraryContent(
    uiState: LibraryUiState,
    onEvent: (LibraryUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            LibraryTopBar(onEvent = onEvent)
        }
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(contentPadding)
                .padding(horizontal = 16.dp),
        ) {
            LibrarySortBar(
                onEvent = onEvent,
                sortBy = uiState.currentSort.label,
                isLibraryViewModeGrid = uiState.isLibraryViewModeGrid
            )
        }
    }
}

@Preview
@Composable
fun LibraryScreenPreview() {
    PreviewTheme {
        // LibraryContent logic
    }
}
