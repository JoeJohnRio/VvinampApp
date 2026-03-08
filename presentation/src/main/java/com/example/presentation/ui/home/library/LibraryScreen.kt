package com.example.presentation.ui.home.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.domain.entity.playlist.PlaylistMinimal
import com.example.presentation.R
import com.example.presentation.entities.ButtonData
import com.example.presentation.navigation.router.BottomNavRouter
import com.example.presentation.ui.main.ScreenWrapper
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
                pagingItems = pagingItems
            )
        }
    )
}

@Composable
fun LibraryContent(uiState: LibraryUiState, pagingItems: LazyPagingItems<PlaylistMinimal>) {
    LazyColumn(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.background
            )
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        stickyHeader {
            TopNavigationWithButtons(
                title = stringResource(R.string.title_your_library),
                listOfButtons = listOf(
                    ButtonData(
                        iconRes = R.drawable.ic_search,
                        onClick = {

                        }
                    ),
                    ButtonData(
                        iconRes = R.drawable.ic_create_plus,
                        onClick = {

                        }
                    )
                )
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