package com.example.presentation.ui.home

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.presentation.entities.MusicListItem
import com.example.presentation.navigation.Page
import com.example.presentation.ui.main.MainRouter
import com.example.presentation.ui.home.HomeNavigationState.MusicDetails
import com.example.presentation.ui.navigationbar.NavigationBarSharedViewModel
import com.example.presentation.ui.widget.LoaderFullScreen
import com.example.presentation.ui.widget.MusicList
import com.example.presentation.ui.widget.PullToRefresh
import com.example.presentation.util.collectAsEffectJoel
import com.example.presentation.util.preview.PreviewContainer
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomePage(
    mainRouter: MainRouter,
    viewModel: HomeViewModel,
    sharedViewModel: NavigationBarSharedViewModel,
) {
    val musicPaging = viewModel.movies.collectAsLazyPagingItems()
    val uiState by viewModel.uiState.collectAsState()
    val pullToRefreshState = rememberPullRefreshState(uiState.showLoading, { viewModel.onRefresh() })
    val lazyGridState = rememberLazyGridState()

    viewModel.navigationState.collectAsEffectJoel { navigationState ->
        when (navigationState) {
            is MusicDetails -> mainRouter.navigateToMovieDetails(navigationState.movieId)
        }
    }
    viewModel.refreshListState.collectAsEffectJoel {
        musicPaging.refresh()
    }

    sharedViewModel.bottomItem.collectAsEffectJoel {
        if (it.page == Page.Music) {
            lazyGridState.animateScrollToItem(0)
        }
    }

    LaunchedEffect(key1 = musicPaging.loadState) {
        viewModel.onLoadStateUpdate(musicPaging.loadState)
    }

    PullToRefresh(state = pullToRefreshState, refresh = uiState.showLoading) {
        HomeScreen(
            movies = musicPaging,
            uiState = uiState,
            lazyGridState = lazyGridState,
            onMovieClick = viewModel::onMovieClicked
        )
    }
}

@Composable
private fun HomeScreen(
    movies: LazyPagingItems<MusicListItem>,
    uiState: HomeUiState,
    lazyGridState: LazyGridState,
    onMovieClick: (movieId: Int) -> Unit
) {
    Surface {
        if (uiState.showLoading) {
            LoaderFullScreen()
        } else {
            MusicList(movies, onMovieClick, lazyGridState)
        }
    }
}

@Preview(device = Devices.PIXEL_3, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    val movies = flowOf(
        PagingData.from(
            listOf<MusicListItem>(
                MusicListItem.Music(9, "", ""),
                MusicListItem.Music(9, "", ""),
                MusicListItem.Music(9, "", ""),
                MusicListItem.Music(9, "", ""),
                MusicListItem.Music(9, "", ""),
                MusicListItem.Music(9, "", ""),
                MusicListItem.Music(9, "", ""),
                MusicListItem.Music(9, "", ""),
                MusicListItem.Music(9, "", ""),
            )
        )
    ).collectAsLazyPagingItems()
    PreviewContainer {
        HomeScreen(
            movies = movies,
            uiState = HomeUiState(
                showLoading = false,
                errorMessage = null,
            ),
            lazyGridState = rememberLazyGridState(),
            onMovieClick = {}
        )
    }
}