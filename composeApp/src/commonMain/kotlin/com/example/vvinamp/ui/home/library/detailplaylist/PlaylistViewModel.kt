package com.example.vvinamp.ui.home.library.detailplaylist
import androidx.compose.ui.graphics.Color
import vvinampapp.composeapp.generated.resources.*

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.data.api.playlist.toChecked
import com.example.domain.entity.playlist.PlaylistTrackSortByDomain
import com.example.domain.entity.song.TrackDomain
import com.example.domain.usecase.AuthUseCases
import com.example.domain.usecase.PlaylistUseCase
import com.example.vvinamp.ui.home.library.detailplaylist.PlaylistEvent.*
import com.example.vvinamp.util.checkCoverImage
import com.example.vvinamp.util.isDuplicateTrackError
import com.example.vvinamp.util.readBytesFromUri
import com.example.vvinamp.util.showToast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val authUseCases: AuthUseCases,
    private val useCase: PlaylistUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlaylistUiState())
    val uiState = _uiState.asStateFlow()

    val playlistTracks: Flow<PagingData<TrackDomain>> = uiState
        .map { it.playlist?.id to it.sortBy }
        .filter { (id, _) -> !id.isNullOrBlank() }
        .distinctUntilChanged()
        .flatMapLatest { (id, sortBy) ->
            useCase.getPlaylistTracksUseCase.invoke(
                id = id!!,
                sortBy = sortBy
            )
        }
        .cachedIn(viewModelScope)

    private val _events = MutableSharedFlow<PlaylistEvent>()
    val events = _events.asSharedFlow()

    fun onEvent(event: PlaylistUiEvent) {
        when (event) {
            is PlaylistUiEvent.onClickAdd -> {
                addSongsToPlaylist(
                    playlistId = event.playlistId,
                    trackId = event.trackId
                )
            }

            is PlaylistUiEvent.onClickTrack -> {
                viewModelScope.launch {
                    _events.emit(
                        PlayTrack(
                            playlistId = event.playlistId,
                            trackId = event.trackId
                        )
                    )
                }
            }

            is PlaylistUiEvent.onClickTrackRecommended -> {
                viewModelScope.launch {
                    _events.emit(
                        PlayTrackRecommended(
                            track = event.track
                        )
                    )
                }
            }

            is PlaylistUiEvent.onRowClickEdit -> {
                viewModelScope.launch {
                    _events.emit(
                        NavigateToEditScreen(event.playlistId)
                    )
                }
            }

            is PlaylistUiEvent.onClickPlayAll -> {
                viewModelScope.launch {
                    _events.emit(
                        PlayTrack(
                            playlistId = event.playlistId,
                            trackId = ""
                        )
                    )
                }
            }

            PlaylistUiEvent.onClickChange -> {
                showEditDetailSheet()
            }

            PlaylistUiEvent.onClickDetail -> {
                showToast("Coming soon")
            }

            PlaylistUiEvent.onClickShare -> {
                showToast("Coming soon")
            }

            PlaylistUiEvent.ToggleShuffleClicked -> {
                toggleShuffleClicked()
            }

            PlaylistUiEvent.onRowClickAdd -> {
                viewModelScope.launch { _events.emit(PlaylistEvent.NavigateToSearch) }
            }

            PlaylistUiEvent.onRowClickEditDetail -> {
                showEditDetailSheet()
            }

            PlaylistUiEvent.onRowClickSort -> {
                showSortSheet()
            }

            is PlaylistUiEvent.onEditNameChanged -> {
                _uiState.update { it.copy(editName = event.name) }
            }

            is PlaylistUiEvent.onEditDescriptionChanged -> {
                _uiState.update { it.copy(editDescription = event.description) }
            }

            is PlaylistUiEvent.onCoverImageChanged -> {
                event.uri?.let { pickCover(it) }
            }

            PlaylistUiEvent.onSaveEditDetail -> {
                savePlaylistDetail()
            }

            PlaylistUiEvent.onCancelEditDetail -> {
                hideEditDetailSheet()
            }

            PlaylistUiEvent.onMakePrivate -> {
                togglePrivacy()
            }

            PlaylistUiEvent.onDeletePlaylist -> {
                _uiState.update { it.copy(isDeleteConfirmVisible = true) }
            }

            PlaylistUiEvent.onConfirmDeletePlaylist -> {
                deletePlaylist()
            }

            PlaylistUiEvent.onDismissDeletePlaylist -> {
                _uiState.update { it.copy(isDeleteConfirmVisible = false) }
            }
        }
    }

    private fun togglePrivacy() {
        val playlist = uiState.value.playlist ?: return
        viewModelScope.launch {
            useCase.updatePlaylistUseCase(id = playlist.id, isPublic = !playlist.isPublic)
                .onSuccess { getPlaylist(playlist.id, showLoading = false) }
                .onFailure { error -> showToast("Couldn't change privacy: ${error.message ?: "try again"}") }
        }
    }

    private fun deletePlaylist() {
        val playlistId = uiState.value.playlist?.id ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isDeleteConfirmVisible = false, isSaving = true) }
            useCase.deletePlaylistUseCase(playlistId)
                .onSuccess {
                    _uiState.update { it.copy(isSaving = false, isEditDetailSheetVisible = false) }
                    _events.emit(PlaylistEvent.PlaylistDeleted)
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isSaving = false) }
                    showToast("Couldn't delete: ${error.message ?: "try again"}")
                }
        }
    }

    fun showSortSheet() {
        _uiState.update { it.copy(isSortSheetVisible = true) }
    }

    fun hideSortSheet() {
        _uiState.update { it.copy(isSortSheetVisible = false) }
    }

    fun updateSort(sortBy: PlaylistTrackSortByDomain) {
        _uiState.update {
            it.copy(
                sortBy = sortBy,
                isSortSheetVisible = false
            )
        }
    }

    private fun showEditDetailSheet() {
        _uiState.update {
            it.copy(
                isEditDetailSheetVisible = true,
                editName = it.playlist?.title.orEmpty(),
                editDescription = it.playlist?.description.orEmpty(),
                editCover = null
            )
        }
    }

    private fun hideEditDetailSheet() {
        _uiState.update { it.copy(isEditDetailSheetVisible = false) }
    }

    private fun pickCover(uri: String) {
        viewModelScope.launch {
            val bytes = runCatching { readBytesFromUri(uri) }.getOrDefault(ByteArray(0))
            checkCoverImage(bytes)
                .onSuccess { image -> _uiState.update { it.copy(editCover = image) } }
                .onFailure { showToast(it.message ?: "Couldn't read that image") }
        }
    }

    private fun savePlaylistDetail() {
        val state = uiState.value
        val playlistId = state.playlist?.id ?: return
        if (state.isSaving) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, isUploadingCover = state.editCover != null) }

            // 1. Upload the cover first, so a failed upload leaves the name and description unsaved too.
            state.editCover?.let { cover ->
                val fileName = "cover_${kotlin.time.Clock.System.now().toEpochMilliseconds()}.${cover.extension}"
                val uploaded = useCase.uploadPlaylistCoverUseCase(
                    playlistId = playlistId,
                    fileName = fileName,
                    contentType = cover.contentType,
                    content = cover.bytes
                )
                _uiState.update { it.copy(isUploadingCover = false) }
                uploaded.onFailure { error ->
                    _uiState.update { it.copy(isSaving = false) }
                    showToast("Cover upload failed: ${error.message ?: "try again"}")
                    return@launch
                }
            }

            // 2. Update name and description.
            useCase.updatePlaylistUseCase(
                id = playlistId,
                title = state.editName,
                description = state.editDescription
            ).onSuccess {
                _uiState.update { it.copy(isSaving = false, isEditDetailSheetVisible = false, editCover = null) }
                if (state.editCover != null) showToast("Cover updated")
                getPlaylist(playlistId, showLoading = false)
            }.onFailure { error ->
                _uiState.update { it.copy(isSaving = false) }
                showToast("Couldn't save: ${error.message ?: "try again"}")
            }
        }
    }

    fun setIsPlaylistCreator() {
        viewModelScope.launch {
            authUseCases.getCurrentUserDetail.invoke().onSuccess { data ->
                _uiState.update {
                    it.copy(
                        isPlaylistCreator = it.playlist?.creator?.id == data.id
                    )
                }
            }
            // A failed creator check just hides the owner controls; no need to block the page.
        }
    }

    fun updateDominantColor(color: Color) {
        _uiState.update { it.copy(dominantColor = color) }
    }

    fun toggleShuffleClicked() {
        viewModelScope.launch {
            _events.emit(OnClickShuffle)
        }
    }

    fun addSongsToPlaylist(
        playlistId: String,
        trackId: String
    ) {
        val state = uiState.value
        val alreadyAdded = state.recommendedTracks?.any { it.id == trackId && it.isAdded } == true
        if (alreadyAdded || trackId in state.addingTrackIds) return

        _uiState.update { it.copy(addingTrackIds = it.addingTrackIds + trackId) }
        viewModelScope.launch {
            val result = useCase.addSongToPlaylistUseCase.invoke(
                playlistId = playlistId,
                trackId = trackId
            )
            _uiState.update { it.copy(addingTrackIds = it.addingTrackIds - trackId) }

            result.onSuccess {
                markRecommendedAdded(trackId)
                // Reload the header (its length decides whether Play, Sort and Edit show) and the song list.
                getPlaylist(playlistId, showLoading = false)
                _events.emit(PlaylistEvent.RefreshTracks)
            }.onFailure { error ->
                if (error.isDuplicateTrackError()) {
                    markRecommendedAdded(trackId)
                    showToast("Already in ${uiState.value.playlist?.title ?: "this playlist"}")
                } else {
                    showToast("Couldn't add song: ${error.message ?: "try again"}")
                }
            }
        }
    }

    private fun markRecommendedAdded(trackId: String) {
        _uiState.update { state ->
            state.copy(
                recommendedTracks = state.recommendedTracks?.map { track ->
                    if (track.id == trackId) track.toChecked() else track
                }
            )
        }
    }

    fun getPlaylist(id: String, showLoading: Boolean = true) {
        viewModelScope.launch {
            // One view model serves every playlist page, so drop a different playlist instead of showing it while this one loads.
            _uiState.update {
                it.copy(
                    isLoading = showLoading,
                    error = null,
                    playlist = it.playlist?.takeIf { current -> current.id == id }
                )
            }

            useCase.getPlaylistUseCase.invoke(id).onSuccess { data ->
                _uiState.update {
                    it.copy(
                        playlist = data,
                        isLoading = false
                    )
                }
                // A quiet reload keeps the current recommendations so their ticks stay put.
                if (showLoading) {
                    setIsPlaylistCreator()
                    getRecommendedTracks()
                }
            }.onFailure { error ->
                if (showLoading) {
                    // Only a failed first load blocks the page; there's nothing to show without the playlist.
                    _uiState.update { it.copy(error = error.message, isLoading = false) }
                } else {
                    showToast("Couldn't refresh playlist: ${error.message ?: "try again"}")
                }
            }
        }
    }

    fun getRecommendedTracks() {
        viewModelScope.launch {
            useCase.getRecommendedTracksUseCase.invoke().onSuccess { data ->
                _uiState.update {
                    it.copy(
                        recommendedTracks = data,
                    )
                }
            }
            // Recommendations are optional; if they fail the section just doesn't show.
        }
    }
}
