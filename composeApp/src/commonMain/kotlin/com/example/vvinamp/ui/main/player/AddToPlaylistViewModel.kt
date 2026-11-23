package com.example.vvinamp.ui.main.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.entity.playlist.PlaylistMinimal
import com.example.domain.entity.song.TrackDomain
import com.example.domain.usecase.PlaylistUseCase
import com.example.vvinamp.util.isDuplicateTrackError
import com.example.vvinamp.util.showToast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Adds the song in the player to one of the user's playlists. */
class AddToPlaylistViewModel(
    private val useCase: PlaylistUseCase
) : ViewModel() {

    private val _track = MutableStateFlow<TrackDomain?>(null)
    /** The song the playlist picker is open for, or null when it's closed. */
    val track = _track.asStateFlow()

    private val _isAdding = MutableStateFlow(false)
    val isAdding = _isAdding.asStateFlow()

    val myPlaylists: Flow<PagingData<PlaylistMinimal>> =
        useCase.getMyPlaylistUseCase.invoke().cachedIn(viewModelScope)

    fun open(track: TrackDomain) {
        _track.value = track
    }

    fun dismiss() {
        _track.value = null
    }

    fun add(playlistId: String, playlistTitle: String) {
        val track = _track.value ?: return
        if (_isAdding.value) return

        viewModelScope.launch {
            _isAdding.value = true
            // A YouTube search result that was never saved has its YouTube ID here instead of a song ID.
            val result = if (isValidYoutubeId(track.id)) {
                useCase.addYoutubeTrackToPlaylistUseCase.invoke(
                    playlistId = playlistId,
                    youtubeId = track.id,
                    title = track.title,
                    artistName = track.artistName,
                    thumbnailUrl = track.albumCoverUrl,
                    duration = track.duration
                )
            } else {
                useCase.addSongToPlaylistUseCase.invoke(playlistId = playlistId, trackId = track.id)
            }
            _isAdding.value = false

            val alreadyThere = result.exceptionOrNull()?.isDuplicateTrackError() == true
            if (result.isSuccess || alreadyThere) _track.value = null
            when {
                result.isSuccess -> showToast("Added to $playlistTitle")
                alreadyThere -> showToast("Already in $playlistTitle")
                else -> showToast("Couldn't add: ${result.exceptionOrNull()?.message ?: "try again"}")
            }
        }
    }
}
