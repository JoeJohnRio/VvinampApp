package com.example.presentation.ui.home.library.detailplaylist.editplaylist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.domain.entity.enum.PlaybackContextType
import com.example.domain.entity.song.TrackDomain
import com.example.domain.entity.song.trackDummy
import com.example.presentation.navigation.router.BottomNavRouter
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
    val playbackUiState by playbackViewModel.uiState.collectAsStateWithLifecycle()
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
            }
        }
    }

    EditPlaylistLibraryContent(
        playlistTracks = playlistTracks,
        playlistId = playlistId,
        onEvent = {
            viewModel.onEvent(it)
        }
    )
//    ScreenWrapper(
//        isLoading = uiState.isLoading,
//        error = uiState.error,
//        content = {
//            PlaylistContent(
//                uiState = uiState,
//                playbackUiState = playbackUiState,
//                playlistTracks = playlistTracks,
//                updateDominantColor = { color ->
//                    viewModel.updateDominantColor(color)
//                },
//                onBackClicked = {
//                    bottomNavRouter.navigateBack()
//                },
//                onEvent = {
//                    viewModel.onEvent(it)
//                }
//            )
//        }
//    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditPlaylistLibraryContent(
    playlistId: String,
    onEvent: (PlaylistEditUiEvent) -> Unit = {},
    onSave: (List<TrackDomain>) -> Unit = {},
    playlistTracks: LazyPagingItems<TrackDomain>
) {

    val tracks = trackDummy.toMutableStateList()

    val lazyListState = rememberLazyListState()

    val reorderableState = rememberReorderableLazyListState(lazyListState) { from, to ->
        val movedItem = tracks.removeAt(from.index)
        tracks.add(to.index, movedItem)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Playlist") },
                actions = {
                    TextButton(onClick = {
//                        onSave(tracks.toList())
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
//            items(tracks, key = { it.id }) { track ->
//                ReorderableItem(reorderableState, key = track.id) { isDragging ->
//                    ItemTrackEdit(
//                        track = track,
//                        draggableHandle = Modifier.draggableHandle(),
//                        onEvent = onEvent
//                    )
//                }
//            }


            items(
                count = playlistTracks.itemCount,
                key = playlistTracks.itemKey { it.id },

                ) { index ->

                val track = playlistTracks[index]

                ReorderableItem(reorderableState, key = track?.id.orEmpty()) { isDragging ->
                    track?.let {
                        ItemTrackEdit(
                            track = it,
                            draggableHandle = Modifier.draggableHandle(),
                            onEvent = onEvent,
                        )
                    }
                }
            }
        }
    }
}