package com.example.presentation.ui.navigationbar

import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.presentation.navigation.NavigationState
import com.example.presentation.navigation.graph.BottomNavDestination
import com.example.presentation.navigation.router.BottomNavRouter
import com.example.presentation.ui.main.playback.PlaybackUiState
import com.example.presentation.ui.main.player.PlayerState
import com.example.presentation.ui.main.player.SpotifyPlayerSheet
import com.example.presentation.util.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainScreenContainer(
    bottomNavRouter: BottomNavRouter,
    navigationState: NavigationState,
    playbackUiState: PlaybackUiState,
    onClickPlay: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val uiState = NavigationBarUiState()
    val density = LocalDensity.current
    val navBars = WindowInsets.navigationBars
    val bottomPadding = navBars.asPaddingValues().calculateBottomPadding()
    val scope = rememberCoroutineScope()

    val playerState = remember {
        AnchoredDraggableState(initialValue = PlayerState.COLLAPSED)
    }

    // Convert the padding to DP to check the height
    val isButtonNav = with(density) {
        bottomPadding > 24.dp // Usually gesture bars are ~16dp-20dp, buttons are ~48dp+
    }

    Box(
        Modifier
            .fillMaxSize(1f)
    ) {
        content()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = if (isButtonNav) bottomPadding else 0.dp)
        ) {
            playbackUiState.currentTrack?.let {
                PlaybackCurrentSongComponent(
                    currentlyPlaying = it,
                    onClickPlay = onClickPlay,
                    backgroundColor = playbackUiState.dominantColor,
                    isPlaying = playbackUiState.isPlaying,
                    onClickTrack = {

                    }
                )
            }

            BottomNavigationBar(
                items = uiState.bottomItems,
                navigationState = navigationState,
                onItemClick = { bottomItem ->
                    when (bottomItem.page) {
                        BottomNavDestination.Home -> bottomNavRouter.navigateToHome()
                        BottomNavDestination.Create -> bottomNavRouter.navigateToCreate()
//                        BottomNavDestination.Library -> bottomNavRouter.navigateToLibrary()
                        BottomNavDestination.Library -> {
                            scope.launch {
                                playerState.animateTo(PlayerState.EXPANDED)
                            }
                        }
                        BottomNavDestination.Premium -> bottomNavRouter.navigateToPremium()
                        BottomNavDestination.Search -> bottomNavRouter.navigateToSearch()
                        else -> {

                        }
                    }
                }
            )
        }
    }
    SpotifyPlayerSheet(
        bottomNavHeight = 0, // pass your bottom nav height in px if needed
        state = playerState
    )
}