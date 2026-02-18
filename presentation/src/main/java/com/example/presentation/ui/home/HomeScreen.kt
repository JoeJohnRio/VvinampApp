package com.example.presentation.ui.home

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.entity.listeningHistoryDummy
import com.example.presentation.navigation.router.BottomNavRouter
import com.example.presentation.ui.home.artist.ArtistEvent
import com.example.presentation.ui.home.history.ListeningHistoryGrid
import com.example.presentation.ui.main.ScreenWrapper
import com.example.presentation.ui.widget.TopBar
import com.example.presentation.util.preview.PreviewTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    bottomNavRouter: BottomNavRouter,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
//        viewModel.getListeningHistory()
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is HomeEvent.NavigateToArtistScreen -> {
                    bottomNavRouter.navigateToArtistPage(event.history)
                }

                is HomeEvent.NavigateToSettingsScreen -> {
                    bottomNavRouter.navigateToSettings()
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