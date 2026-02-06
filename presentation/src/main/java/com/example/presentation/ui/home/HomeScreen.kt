package com.example.presentation.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.entity.listeningHistoryDummy
import com.example.presentation.navigation.router.MainRouter
import com.example.presentation.ui.home.history.ListeningHistoryGrid
import com.example.presentation.ui.main.ScreenWrapper
import com.example.presentation.util.preview.PreviewTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    mainRouter: MainRouter,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getListeningHistory()
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is HomeUiEvent.NavigateToArtistPage -> {
                    mainRouter.navigateToArtistPage(event.history)
                }
            }
        }
    }

    HomeContent(
        uiState = uiState,
        onRetry = {
            viewModel.getListeningHistory()
        }
    )
}

@Composable
private fun HomeContent(
    uiState: HomeUiState = HomeUiState(),
    onRetry: () -> Unit = {}
) {
    ScreenWrapper(
        isLoading = uiState.isLoading,
        error = uiState.error,
        onRetry = {
            onRetry()
        },
        content = {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                uiState.listHistory.let {
                    ListeningHistoryGrid(it)
                }
            }
        }
    )
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