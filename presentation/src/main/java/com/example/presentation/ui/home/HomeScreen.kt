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
import androidx.hilt.navigation.compose.hiltViewModel
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


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    bottomNavRouter: BottomNavRouter,
    viewModel: HomeViewModel = hiltViewModel(),
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

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                ConstraintLayout(
                    modifier = Modifier
                        .weight(0.7f)
                        .background(Color.White)
                ) {
                    val (textRef, btnRef) = createRefs()

                    // 1. Chains them together tightly, anchored to the left edge (0f)
                    createHorizontalChain(textRef, btnRef, chainStyle = ChainStyle.Packed(0f))

                    Text(
                        text = "Lorem",
                        modifier = Modifier.constrainAs(textRef) {
                            // 2. Allows text to shrink to a new line instead of breaking boundaries
                            width = Dimension.preferredWrapContent
                            centerVerticallyTo(parent)
                        },maxLines = 2,                                  // 1. Force single line
                        overflow = TextOverflow.Ellipsis,
                    )

                    ButtonVariableOutlined(
                        "Outline",
                        modifier = Modifier.constrainAs(btnRef) {
                            centerVerticallyTo(parent)
                        }
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(0.3f)
                        .background(Color.Red)
                ) {
                    Text("test")
                }
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