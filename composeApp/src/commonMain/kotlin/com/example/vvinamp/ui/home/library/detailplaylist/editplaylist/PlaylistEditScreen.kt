package com.example.vvinamp.ui.home.library.detailplaylist.editplaylist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.paging.LoadState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.koin.compose.koinInject
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.domain.entity.enum.PlaybackContextType
import com.example.domain.entity.song.TrackDomain
import com.example.vvinamp.navigation.router.BottomNavRouter
import com.example.vvinamp.ui.main.ScreenWrapper
import com.example.vvinamp.ui.main.playback.PlaybackUiEvent.PlayContext
import com.example.vvinamp.ui.main.playback.PlaybackViewModel
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun PlaylistEditScreen(
    playlistId: String,
    bottomNavRouter: BottomNavRouter,
    viewModel: PlaylistEditViewModel = koinViewModel(),
    playbackViewModel: PlaybackViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val playlistTracks = viewModel.playlistTracks.collectAsLazyPagingItems()

    // The view model is shared across visits, so reload in case the songs changed since the last edit.
    LaunchedEffect(playlistId) {
        viewModel.setPlaylistId(playlistId)
        playlistTracks.refresh()
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is PlaylistEditEvent.PlayTrack -> {
                    playbackViewModel.onEvent(
                        PlayContext(
                            contextType = PlaybackContextType.PLAYLIST,
                            contextId = event.playlistId,
                            offsetTrackId = event.trackId
                        )
                    )
                }
                is PlaylistEditEvent.SaveSuccess -> {
                    bottomNavRouter.navigateBack()
                }
            }
        }
    }

    if (uiState.error != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text("Update Failed") },
            text = { Text(uiState.error ?: "An unknown error occurred while saving the playlist.") },
            confirmButton = {
                TextButton(onClick = { 
                    viewModel.clearError()
                }) {
                    Text("Retry")
                }
            },
            dismissButton = {
                TextButton(onClick = { bottomNavRouter.navigateBack() }) {
                    Text("Go Back")
                }
            }
        )
    }

    ScreenWrapper(
        isLoading = uiState.isLoading,
        error = uiState.error,
        content = {
            EditPlaylistLibraryContent(
                playlistTracks = playlistTracks,
                playlistId = playlistId,
                onEvent = {
                    viewModel.onEvent(it)
                },
                onClose = { bottomNavRouter.navigateBack() }
            )
        }
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditPlaylistLibraryContent(
    playlistId: String,
    onEvent: (PlaylistEditUiEvent) -> Unit = {},
    onClose: () -> Unit = {},
    playlistTracks: LazyPagingItems<TrackDomain>
) {
    val tracks = remember { mutableStateListOf<TrackDomain>() }
    val movedTrackIds = remember { mutableStateSetOf<String>() }
    val deletedTrackIds = remember { mutableStateSetOf<String>() }

    // Copy the loaded songs into the editable list each time fresh data arrives, until the first edit.
    val refreshState = playlistTracks.loadState.refresh
    LaunchedEffect(refreshState, playlistTracks.itemCount) {
        if (refreshState is LoadState.NotLoading && movedTrackIds.isEmpty() && deletedTrackIds.isEmpty()) {
            tracks.clear()
            for (i in 0 until playlistTracks.itemCount) {
                playlistTracks[i]?.let { tracks.add(it) }
            }
        }
    }

    val lazyListState = rememberLazyListState()
    val reorderableState = rememberReorderableLazyListState(lazyListState) { from, to ->
        val movedItem = tracks.removeAt(from.index)
        tracks.add(to.index, movedItem)
        movedTrackIds.add(movedItem.id)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Playlist") },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close without saving"
                        )
                    }
                },
                actions = {
                    TextButton(onClick = {
                        onEvent(
                            PlaylistEditUiEvent.OnSave(
                                tracks = tracks.toList(),
                                movedTrackIds = movedTrackIds.toSet(),
                                deletedTrackIds = deletedTrackIds.toSet()
                            )
                        )
                    }) { Text("Save") }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            state = lazyListState,
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tracks.size, key = { tracks[it].id }) { index ->
                val track = tracks[index]
                ReorderableItem(reorderableState, key = track.id) { isDragging ->
                    ItemPlaylistTrackEdit(
                        track = track,
                        draggableHandle = Modifier.draggableHandle(),
                        onEvent = { event ->
                            if (event is PlaylistEditUiEvent.OnDelete) {
                                tracks.removeAll { it.id == event.trackId }
                                deletedTrackIds.add(event.trackId)
                                movedTrackIds.remove(event.trackId)
                            } else {
                                onEvent(event)
                            }
                        },
                    )
                }
            }
        }
    }
}
