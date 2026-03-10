package com.example.presentation.ui.home.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.domain.entity.playlist.LibraryCategory
import com.example.domain.entity.playlist.LibraryCategory.Companion.toLibraryCategory
import com.example.domain.entity.playlist.PlaylistMinimal
import com.example.domain.entity.playlist.PlaylistSortOrderDomain
import com.example.presentation.R
import com.example.presentation.entities.ButtonData
import com.example.presentation.navigation.router.BottomNavRouter
import com.example.presentation.ui.main.ScreenWrapper
import com.example.presentation.ui.theme.TSBodyMediumWhite
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.ui.theme.TSHeadlineMediumWhite
import com.example.presentation.ui.widget.ButtonListWithState
import com.example.presentation.ui.widget.ButtonVariableOutlined
import com.example.presentation.ui.widget.TopNavigationWithButtons


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LibraryScreen(
    bottomNavRouter: BottomNavRouter,
    viewModel: LibraryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val pagingItems = viewModel.myPlaylists.collectAsLazyPagingItems()

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
        }
    )
}

@Composable
fun LibraryContent(
    uiState: LibraryUiState,
    pagingItems: LazyPagingItems<PlaylistMinimal>,
    onEvent: (LibraryUiEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.background
            )
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        stickyHeader {
            LibraryTopBar(
                onEvent = onEvent
            )
        }

        item {
            LibrarySortBar(
                onEvent = onEvent,
            )
        }

        items(
            count = pagingItems.itemCount,
            key = pagingItems.itemKey { it.id }
        ) { index ->
            val playlist = pagingItems[index]

            if (playlist != null) {
                Text(
                    text = playlist.title,
                    color = Color.White
                )
            }
        }

        // Handle Paging's internal loading states here
        when (pagingItems.loadState.append) {
            is LoadState.Loading -> item { CircularProgressIndicator() }
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

@Composable
fun LibraryTopBar(onEvent: (LibraryUiEvent) -> Unit) {
    TopNavigationWithButtons(
        title = stringResource(R.string.title_your_library),
        listOfButtons = listOf(
            ButtonData(
                iconRes = R.drawable.ic_search,
                onClick = {
                    onEvent(LibraryUiEvent.onSearchClicked)
                }
            ),
            ButtonData(
                iconRes = R.drawable.ic_create_plus,
                onClick = {
                    onEvent(LibraryUiEvent.onCreatePlaylistClicked)
                }
            )
        )
    )

    Spacer(Modifier.height(12.dp))

    ButtonListWithState(
        modifier = Modifier
            .fillMaxWidth(),
        buttonHorizontalPadding = 12.dp,
        buttonHeight = 32.dp,
        textStyle = TSDisplayMediumWhite().copy(
            fontSize = 11.sp
        ),
        listButton = listOf(
            LibraryCategory.PLAYLIST.displayName,
            LibraryCategory.PODCAST.displayName,
            LibraryCategory.ALBUM.displayName,
            LibraryCategory.ARTIST.displayName,
            LibraryCategory.DOWNLOADED.displayName,
        ),
        outlineColor = Color.Transparent,
        textColor = MaterialTheme.colorScheme.inversePrimary,
        textColorClicked = MaterialTheme.colorScheme.onPrimary,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        containerColorClicked = MaterialTheme.colorScheme.primary,
        onClick = { gender ->
            onEvent(LibraryUiEvent.onLibraryCategoryClicked(gender.toLibraryCategory()))
        }
    )
}

@Composable
fun LibrarySortBar(
    onEvent: (LibraryUiEvent) -> Unit,
    sortBy: String = PlaylistSortOrderDomain.RECENTS.label
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.clickable {
                onEvent(LibraryUiEvent.onSortByClicked)
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.inversePrimary,
                painter = painterResource(R.drawable.ic_arrow_up_down),
                contentDescription = null,
            )

            Spacer(Modifier.width(4.dp))

            Text(
                text = sortBy,
                style = TSDisplayMediumWhite().copy(
                    fontSize = 12.sp
                )
            )
        }

        IconButton(
            onClick = { onEvent(LibraryUiEvent.onLibraryViewModesClicked) }
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.inversePrimary,
                painter = painterResource(R.drawable.ic_grid),
                contentDescription = null,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    currentSort: PlaylistSortOrderDomain,
    onSortSelected: (PlaylistSortOrderDomain) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true // Keeps it tight to the content
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() } // The "draggable" visual cue
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp) // Extra padding for system navigation bars
        ) {
            Text(
                text = "Sort by",
                style = TSHeadlineMediumWhite().copy(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )

            Divider(
                Modifier.padding(top = 16.dp),
                color = MaterialTheme.colorScheme.surfaceContainer
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                PlaylistSortOrderDomain.entries.forEach { option ->
                    SortOptionRow(
                        label = option.label,
                        isSelected = option == currentSort,
                        onClick = {
                            onSortSelected(option)
                            onDismiss()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SortOptionRow(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = TSDisplayMediumWhite().copy(
                fontSize = 14.sp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inversePrimary
            ),
        )
        if (isSelected) {
            Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
    }
}