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
import com.example.domain.entity.ListeningHistory
import com.example.presentation.ui.home.history.ListeningHistoryGrid
import com.example.presentation.navigation.router.MainRouter
import com.example.presentation.ui.main.MainUiEvent
import com.example.presentation.ui.widget.SnackbarStyledHost
import com.example.presentation.util.UiState
import com.example.presentation.util.preview.PreviewContainer

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomePage(
    mainRouter: MainRouter,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val lazyGridState = rememberLazyGridState()
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val getListRecentHistory by viewModel.getListeningHistory.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is HomeUiEvent.NavigateToArtistPage -> {
                    mainRouter.navigateToArtistPage(event.history)
                }
            }
        }
    }

    LaunchedEffect(getListRecentHistory) {
        when (getListRecentHistory) {
            is UiState.Error -> {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()

                viewModel.setLoading(false)
            }

            UiState.Idle -> {
                viewModel.setLoading(false)
            }

            UiState.Loading -> {
                viewModel.setLoading(true)
            }

            is UiState.Success -> {
                viewModel.setLoading(false)

                Toast.makeText(context, "Jalan nih", Toast.LENGTH_SHORT).show()
                val data = (getListRecentHistory as UiState.Success<List<ListeningHistory>>).data
            }
        }
    }

    HomeScreen()
}

@Composable
private fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val historyState by viewModel.getListeningHistory.collectAsState()
    val historyList =
        if (historyState is UiState.Success) (historyState as UiState.Success<List<ListeningHistory>>).data else emptyList()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getListeningHistory()
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
            ListeningHistoryGrid(
                historyList = historyList,
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