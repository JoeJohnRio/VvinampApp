package com.example.presentation.ui.home.library.detailplaylist.editplaylist

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.domain.entity.enum.PlaybackContextType
import com.example.domain.entity.song.TrackDomain
import com.example.presentation.navigation.router.BottomNavRouter
import com.example.presentation.ui.main.ScreenWrapper
import com.example.presentation.ui.main.playback.PlaybackUiEvent.PlayContext
import com.example.presentation.ui.main.playback.PlaybackViewModel
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun PlaylistEditScreen(
    playlistId: String,
    bottomNavRouter: BottomNavRouter,
    viewModel: PlaylistEditViewModel = hiltViewModel(),
    playbackViewModel: PlaybackViewModel = hiltViewModel()
) {
    LaunchedEffect(playlistId) {
        viewModel.setPlaylistId(playlistId)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val playlistTracks = viewModel.playlistTracks.collectAsLazyPagingItems()

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
                }
            )
        }
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditPlaylistLibraryContent(
    playlistId: String,
    onEvent: (PlaylistEditUiEvent) -> Unit = {},
    playlistTracks: LazyPagingItems<TrackDomain>
) {
    val tracks = remember { mutableStateListOf<TrackDomain>() }
    val movedTrackIds = remember { mutableStateSetOf<String>() }
    val deletedTrackIds = remember { mutableStateSetOf<String>() }

    LaunchedEffect(playlistTracks.itemCount) {
        if (tracks.isEmpty() && playlistTracks.itemCount > 0) {
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
                                tracks.removeIf { it.id == event.trackId }
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
