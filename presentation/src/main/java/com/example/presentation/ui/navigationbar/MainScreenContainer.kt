package com.example.presentation.ui.navigationbar

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.navigation.NavigationState
import com.example.presentation.navigation.graph.BottomNavDestination
import com.example.presentation.navigation.router.BottomNavRouter
import com.example.presentation.ui.main.playback.PlaybackUiEvent
import com.example.presentation.ui.main.playback.PlaybackViewModel
import com.example.presentation.ui.main.player.PlayerState
import com.example.presentation.ui.main.player.PlayerSheet
import com.example.presentation.util.ExtractDominantColorEffect
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainScreenContainer(
    playbackViewModel: PlaybackViewModel,
    bottomNavRouter: BottomNavRouter,
    navigationState: NavigationState,
    content: @Composable () -> Unit,
) {
    val playbackUiState by playbackViewModel.uiState.collectAsStateWithLifecycle()
    val mediaController by playbackViewModel.mediaController.collectAsStateWithLifecycle()
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

// 1. Calculate the current offset, handling the initial NaN state before anchors are ready
    val offset = if (playerState.offset.isNaN()) 0f else playerState.offset
    val collapsedAnchor = playerState.anchors.positionOf(PlayerState.COLLAPSED).let {
        if (it.isNaN() || it == 0f) 1f else it
    }

// 2. Calculate a progress fraction from 0f (fully collapsed) to 1f (fully expanded)
    val expansionProgress = (1f - (offset / collapsedAnchor)).coerceIn(0f, 1f)

// 3. Multiply progress by your maximum desired alpha (0.5f)
    val scrimAlpha = expansionProgress * 0.5f

    ExtractDominantColorEffect(
        imageUrl = playbackUiState.currentTrack?.albumCoverUrl,
        onColorExtracted = { color ->
            playbackViewModel.updateDominantColor(color)
        }
    )

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
                    onClickPlay = {
                        playbackViewModel.onEvent(PlaybackUiEvent.TogglePlayback)
                    },
                    backgroundColor = playbackUiState.dominantColor,
                    isPlaying = playbackUiState.isPlaying,
                    onClickTrack = {
                        scope.launch {
                            playerState.animateTo(PlayerState.EXPANDED)
                        }
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
                        BottomNavDestination.Library -> bottomNavRouter.navigateToLibrary()
                        BottomNavDestination.Premium -> bottomNavRouter.navigateToPremium()
                        BottomNavDestination.Search -> bottomNavRouter.navigateToSearch()
                        else -> {

                        }
                    }
                }
            )
        }
    }

    if (scrimAlpha > 0f) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = scrimAlpha))
        )
    }

    PlayerSheet(
        playbackUiState = playbackUiState,
        player = mediaController,
        state = playerState,
        uiEvent = { event ->
            playbackViewModel.onEvent(event)
        }
    )
}