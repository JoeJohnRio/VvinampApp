package com.example.vvinamp.ui.home
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
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.entity.listeningHistoryDummy
import com.example.vvinamp.navigation.router.BottomNavRouter
import com.example.vvinamp.ui.home.history.ListeningHistoryGrid
import com.example.vvinamp.ui.main.ScreenWrapper
import com.example.vvinamp.ui.widget.TopBar
import com.example.vvinamp.util.preview.PreviewTheme
import org.koin.compose.viewmodel.koinViewModel
import com.example.domain.entity.enum.ContentTypeDomain
import com.example.domain.entity.song.toTrackDomain
import com.example.vvinamp.ui.main.playback.PlaybackUiEvent
import com.example.vvinamp.ui.main.playback.PlaybackViewModel


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    bottomNavRouter: BottomNavRouter,
    viewModel: HomeViewModel = koinViewModel(),
    playbackViewModel: PlaybackViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getListeningHistory()
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is HomeEvent.NavigateToArtistScreen -> {
                    bottomNavRouter.navigateToArtistPage(event.history.contentId)
                }

                is HomeEvent.PlayTrack -> {
                    playbackViewModel.onEvent(
                        PlaybackUiEvent.OnTrackClicked(
                            track = event.history.toTrackDomain(),
                            contentTypeDomain = ContentTypeDomain.TRACK,
                            contentId = event.history.contentId
                        )
                    )
                }

                is HomeEvent.NavigateToSettingsScreen -> {
                    bottomNavRouter.navigateToSettings()
                }

                is HomeEvent.NavigateToSearchResultScreen -> {
                    bottomNavRouter.navigateToSearchResults()
                }

                is HomeEvent.NavigateToAlbumScreen -> {
                    bottomNavRouter.navigateToAlbum(event.history.contentId)
                }
                is HomeEvent.NavigateToPlaylistScreen -> {
                    bottomNavRouter.navigateToPlaylistDetail(event.history.contentId)
                }
            }
        }
    }

    ScreenWrapper(
        isLoading = uiState.isLoading,
        error = uiState.error,
        onRetry = {
            viewModel.getListeningHistory()
        },
        content = {
            HomeContent(
                uiState = uiState,
                onEvent = {
                    viewModel.onEvent(it)
                },
            )
        }
    )
}

@Composable
private fun HomeContent(
    uiState: HomeUiState = HomeUiState(),
    onEvent: (HomeUiEvent) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopBar(
                "",
                onSearchClick = {
                    onEvent(HomeUiEvent.OnSearchClicked)
                },
                onSettingsClick = {
                    onEvent(HomeUiEvent.OnSettingsClicked)
                }
            )
        }
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(contentPadding)
                .padding(16.dp),
        ) {
            uiState.listHistory.let {
                ListeningHistoryGrid(
                    historyList = it,
                    onEvent = onEvent
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    PreviewTheme {
        HomeContent(
            uiState = HomeUiState(
                listHistory = listeningHistoryDummy
            )
        )
    }
}