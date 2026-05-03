package com.example.presentation.ui.home

import android.R.attr.bottom
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.entity.listeningHistoryDummy
import com.example.presentation.navigation.router.BottomNavRouter
import com.example.presentation.ui.home.artist.ArtistEvent
import com.example.presentation.ui.home.history.ListeningHistoryGrid
import com.example.presentation.ui.main.ScreenWrapper
import com.example.presentation.ui.widget.ButtonVariableOutlined
import com.example.presentation.ui.widget.TopBar
import com.example.presentation.util.preview.PreviewTheme
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.domain.entity.enum.ContentType
import com.example.domain.entity.song.toTrackDomain
import com.example.presentation.ui.main.playback.PlaybackUiEvent
import com.example.presentation.ui.main.playback.PlaybackViewModel


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    bottomNavRouter: BottomNavRouter,
    viewModel: HomeViewModel = hiltViewModel(),
    playbackViewModel: PlaybackViewModel = hiltViewModel()
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
                            contentType = ContentType.TRACK,
                            contentId = event.history.contentId
                        )
                    )
                }

                is HomeEvent.NavigateToSettingsScreen -> {
                    bottomNavRouter.navigateToSettings()
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
                onSettingsClicked = {
                    viewModel.onEvent(HomeUiEvent.OnSettingsClicked)
                }
            )
        }
    )
}

@Composable
private fun HomeContent(
    uiState: HomeUiState = HomeUiState(),
    onSettingsClicked: () -> Unit = {},
    onEvent: (HomeUiEvent) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopBar(
                "",
                onSearchClick = {},
                onSettingsClick = {
                    onSettingsClicked()
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