package com.example.vvinamp.ui.navigationbar

import org.koin.compose.viewmodel.koinViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.domain.entity.search.SearchItemDomain
import com.example.vvinamp.ui.search.searchresult.AddToPlaylistBottomSheet
import com.example.vvinamp.ui.main.player.AddToPlaylistViewModel
import com.example.vvinamp.util.showToast
import com.example.vvinamp.ui.theme.orThemeBackground
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vvinamp.navigation.NavigationState
import com.example.vvinamp.navigation.graph.BottomNavDestination
import com.example.vvinamp.navigation.router.BottomNavRouter
import com.example.vvinamp.ui.home.library.CreatePlaylistBottomSheet
import com.example.vvinamp.ui.home.library.LibraryUiEvent
import com.example.vvinamp.ui.main.playback.PlaybackUiEvent
import com.example.vvinamp.ui.main.playback.PlaybackViewModel
import com.example.vvinamp.ui.main.playback.SleepTimerBottomSheet
import com.example.vvinamp.ui.main.player.BottomSheetState
import com.example.vvinamp.ui.main.player.PlayerSheet
import com.example.vvinamp.ui.navigationbar.trackoption.TrackOptionBottomSheet
import com.example.vvinamp.ui.navigationbar.trackoption.TrackOptionsUiEvent
import com.example.vvinamp.ui.navigationbar.util.ScrimAlphaBoxAnchored
import com.example.vvinamp.util.ExtractDominantColorEffect
import kotlinx.coroutines.launch
import com.example.vvinamp.ui.main.playback.PlaybackUiState
import com.example.vvinamp.ui.main.playback.PlayingFrom
import com.example.vvinamp.ui.widget.playback.DownloadProgressDialog

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContainer(
    playbackViewModel: PlaybackViewModel,
    bottomNavRouter: BottomNavRouter,
    navigationState: NavigationState,
    content: @Composable () -> Unit,
) {
    val playbackUiState by playbackViewModel.uiState.collectAsStateWithLifecycle(PlaybackUiState())
    val mediaController by playbackViewModel.mediaController.collectAsStateWithLifecycle(null)
    val uiState = NavigationBarUiState()
    val density = LocalDensity.current
    val navBars = WindowInsets.navigationBars
    val bottomPadding = navBars.asPaddingValues().calculateBottomPadding()
    val scope = rememberCoroutineScope()
    val trackOptionSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val addToPlaylistViewModel: AddToPlaylistViewModel = koinViewModel()
    val trackToAdd by addToPlaylistViewModel.track.collectAsStateWithLifecycle()
    val isAddingToPlaylist by addToPlaylistViewModel.isAdding.collectAsStateWithLifecycle()

    val playerState = remember {
        AnchoredDraggableState(initialValue = BottomSheetState.COLLAPSED)
    }

    val isButtonNav = with(density) {
        bottomPadding > 24.dp // Usually gesture bars are ~16dp-20dp, buttons are ~48dp+
    }

    val embedId = playbackUiState.youtubeEmbedId
    val isPlayerExpanded = playerState.targetValue == BottomSheetState.EXPANDED

    // Keyed on the session too, so starting the same YouTube song again also opens the player.
    LaunchedEffect(embedId, playbackUiState.embedSession) {
        if (embedId != null) playerState.animateTo(BottomSheetState.EXPANDED)
    }

    // The mini bar is too small for YouTube's minimum player size, so collapsing pauses the song.
    LaunchedEffect(embedId, isPlayerExpanded) {
        if (embedId != null) playbackViewModel.onEvent(PlaybackUiEvent.SetEmbedPaused(!isPlayerExpanded))
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
                        if (embedId != null) {
                            scope.launch { playerState.animateTo(BottomSheetState.EXPANDED) }
                        } else {
                            playbackViewModel.onEvent(PlaybackUiEvent.TogglePlayback)
                        }
                    },
                    backgroundColor = playbackUiState.dominantColor.orThemeBackground(),
                    isPlaying = playbackUiState.isPlaying,
                    onClickAdd = { addToPlaylistViewModel.open(it) },
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

    if (playbackUiState.isDownloading) {
        DownloadProgressDialog(
            progress = playbackUiState.downloadProgress,
            trackTitle = playbackUiState.currentTrack?.title.orEmpty()
        )
    }

    ScrimAlphaBoxAnchored(playerState)

    // Leaving the player for another screen: close the menu and the player, then go there.
    fun openFromPlayer(navigate: () -> Unit) {
        playbackViewModel.onEvent(PlaybackUiEvent.ToggleTrackOptionSheet(false))
        scope.launch { playerState.animateTo(BottomSheetState.COLLAPSED) }
        navigate()
    }

    PlayerSheet(
        playbackUiState = playbackUiState,
        player = mediaController,
        state = playerState,
        onEvent = { event ->
            playbackViewModel.onEvent(event)
        },
        onOpenArtist = { artistId -> openFromPlayer { bottomNavRouter.navigateToArtistPage(artistId) } },
        onOpenSource = { source ->
            val id = source.id ?: return@PlayerSheet
            when (source.label) {
                PlayingFrom.PLAYLIST -> openFromPlayer { bottomNavRouter.navigateToPlaylistDetail(id) }
                PlayingFrom.ALBUM -> openFromPlayer { bottomNavRouter.navigateToAlbum(id) }
                PlayingFrom.ARTIST -> openFromPlayer { bottomNavRouter.navigateToArtistPage(id) }
            }
        },
        onAddToPlaylist = { playbackUiState.currentTrack?.let { addToPlaylistViewModel.open(it) } }
    )

//    ScrimAlphaBoxAnchored(trackOptionState)

    TrackOptionBottomSheet(
        onEvent = {
            playbackViewModel.onEvent(it)
        },
        onTrackOptionsEvent = { event ->
            val track = playbackUiState.currentTrack
            when (event) {
                TrackOptionsUiEvent.OnGoToAlbumClick -> track?.albumId?.let { id ->
                    openFromPlayer { bottomNavRouter.navigateToAlbum(id) }
                }
                TrackOptionsUiEvent.OnGoToArtistClick -> track?.artistId?.let { id ->
                    openFromPlayer { bottomNavRouter.navigateToArtistPage(id) }
                }
                TrackOptionsUiEvent.OnAddToQueueClick -> track?.let {
                    playbackViewModel.onEvent(PlaybackUiEvent.AddToQueue(listOf(it.id)))
                    playbackViewModel.onEvent(PlaybackUiEvent.ToggleTrackOptionSheet(false))
                    showToast("Added to queue")
                }
                TrackOptionsUiEvent.OnAddToPlaylistClick -> track?.let {
                    playbackViewModel.onEvent(PlaybackUiEvent.ToggleTrackOptionSheet(false))
                    addToPlaylistViewModel.open(it)
                }
                // shortcut: sharing from here isn't built yet.
                TrackOptionsUiEvent.OnShareClick -> showToast("Coming soon")
                else -> playbackViewModel.onEventTrackOptions(event)
            }
        },
        sheetState = trackOptionSheetState,
        uiState = playbackUiState
    )

    trackToAdd?.let { track ->
        AddToPlaylistBottomSheet(
            track = SearchItemDomain.TrackSearch(
                id = track.id,
                title = track.title,
                duration = track.duration,
                albumCoverUrl = track.albumCoverUrl,
                artistName = track.artistName
            ),
            playlists = addToPlaylistViewModel.myPlaylists.collectAsLazyPagingItems(),
            isAdding = isAddingToPlaylist,
            onPlaylistSelected = { playlistId, playlistTitle -> addToPlaylistViewModel.add(playlistId, playlistTitle) },
            onDismiss = { addToPlaylistViewModel.dismiss() }
        )
    }
}
