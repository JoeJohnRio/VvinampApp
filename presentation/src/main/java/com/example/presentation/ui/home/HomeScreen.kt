package com.example.presentation.ui.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.entity.ListeningHistoryDomain
import com.example.presentation.ui.home.history.ListeningHistoryGrid
import com.example.presentation.navigation.router.MainRouter
import com.example.presentation.ui.widget.LoadingScreen
import com.example.presentation.ui.widget.SnackbarStyledHost
import com.example.presentation.util.Render
import com.example.presentation.util.UiState
import com.example.presentation.util.preview.PreviewContainer

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomePage(
    mainRouter: MainRouter,
    viewModel: HomeViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is HomeUiEvent.NavigateToArtistPage -> {
                    mainRouter.navigateToArtistPage(event.history)
                }
            }
        }
    }

    HomeScreen()
}

@Composable
private fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val currentState = viewModel.uiState.value.listHistory

    LaunchedEffect(Unit) {
        if (currentState !is UiState.Success) {
            viewModel.getListeningHistory()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarStyledHost(snackbarHostState)
        },
        backgroundColor = MaterialTheme.colorScheme.onPrimary,
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .padding(contentPadding)
                .background(MaterialTheme.colorScheme.onPrimary)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            uiState.listHistory.Render(
                onSuccess = {
                    ListeningHistoryGrid(it)
                },
                onLoading = {
                    LoadingScreen()
                },
                onError = {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@Preview(device = Devices.PIXEL_3, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    PreviewContainer {
        HomeScreen()
    }
}