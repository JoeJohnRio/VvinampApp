package com.example.presentation.ui.navigationbar

import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.navigation.NavigationState
import com.example.presentation.navigation.graph.BottomNavDestination
import com.example.presentation.navigation.router.BottomNavRouter
import com.example.presentation.ui.home.library.CreatePlaylistBottomSheet
import com.example.presentation.ui.home.library.LibraryUiEvent
import com.example.presentation.ui.main.playback.PlaybackUiEvent
import com.example.presentation.ui.main.playback.PlaybackViewModel
import com.example.presentation.ui.main.playback.SleepTimerBottomSheet
import com.example.presentation.ui.main.player.BottomSheetState
import com.example.presentation.ui.main.player.PlayerSheet
import com.example.presentation.ui.navigationbar.trackoption.TrackOptionBottomSheet
import com.example.presentation.ui.navigationbar.trackoption.TrackOptionsUiEvent
import com.example.presentation.ui.navigationbar.util.ScrimAlphaBoxAnchored
import com.example.presentation.util.ExtractDominantColorEffect
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
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
    val trackOptionSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val playerState = remember {
        AnchoredDraggableState(initialValue = BottomSheetState.COLLAPSED)
    }

    val isButtonNav = with(density) {
        bottomPadding > 24.dp // Usually gesture bars are ~16dp-20dp, buttons are ~48dp+
    }

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
                            playerState.animateTo(BottomSheetState.EXPANDED)
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
                        BottomNavDestination.Create -> playbackViewModel.onEvent(PlaybackUiEvent.ToggleCreatePlaylistOptionSheet(true))
                        BottomNavDestination.Library -> bottomNavRouter.navigateToLibrary()
                        BottomNavDestination.Search -> bottomNavRouter.navigateToSearch()
                        else -> {

                        }
                    }
                }
            )
        }
    }

    if (playbackUiState.isCreatePlaylistOptionSheet) {
        CreatePlaylistBottomSheet(
            onEvent = { event ->
                when (event) {
                    LibraryUiEvent.onCreateNewPlaylistClicked -> {
                        playbackViewModel.onEvent(PlaybackUiEvent.ToggleCreatePlaylistOptionSheet(false))
                        bottomNavRouter.navigateToCreatePlaylist()
                    }

                    else -> {

                    }
                }
            },
            onDismiss = { playbackViewModel.onEvent(PlaybackUiEvent.ToggleCreatePlaylistOptionSheet(false)) }
        )
    }

    if (playbackUiState.isSleepTimerSheetVisible) {
        SleepTimerBottomSheet(
            currentOption = playbackUiState.activeSleepTimer,
            onOptionSelected = { option ->
                playbackViewModel.onEventTrackOptions(TrackOptionsUiEvent.OnSleepTimerSelected(option))
            },
            onDismiss = { playbackViewModel.hideSleepTimerSheet() }
        )
    }

    ScrimAlphaBoxAnchored(playerState)

    PlayerSheet(
        playbackUiState = playbackUiState,
        player = mediaController,
        state = playerState,
        onEvent = { event ->
            playbackViewModel.onEvent(event)
        }
    )

//    ScrimAlphaBoxAnchored(trackOptionState)

    TrackOptionBottomSheet(
        onEvent = {
            playbackViewModel.onEvent(it)
        },
        onTrackOptionsEvent = {
            playbackViewModel.onEventTrackOptions(it)
        },
        sheetState = trackOptionSheetState,
        uiState = playbackUiState
    )
}
