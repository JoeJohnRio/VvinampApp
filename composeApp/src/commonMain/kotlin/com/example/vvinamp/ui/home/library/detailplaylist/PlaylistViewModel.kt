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
                _uiState.update { it.copy(editCoverUri = event.uri) }
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
                .onFailure { error -> _uiState.update { it.copy(error = error.message) } }
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
                    _uiState.update { it.copy(isSaving = false, error = error.message) }
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
                editCoverUri = null,
                error = null
            )
        }
    }

    private fun hideEditDetailSheet() {
        _uiState.update { it.copy(isEditDetailSheetVisible = false) }
    }

    private fun savePlaylistDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            val currentState = uiState.value
            val playlistId = currentState.playlist?.id ?: return@launch

            try {
                // 1. Upload cover if changed
                currentState.editCoverUri?.let { uriString ->
                    val bytes = com.example.vvinamp.util.readBytesFromUri(uriString)

                    val fileName = "cover_${kotlin.time.Clock.System.now().toEpochMilliseconds()}.jpg"
                    useCase.uploadPlaylistCoverUseCase(
                        playlistId = playlistId,
                        fileName = fileName,
                        contentType = "image/jpeg",
                        content = bytes
                    ).onFailure { throw Exception(it.message ?: "Upload failed") }
                }

                // 2. Update playlist details
                useCase.updatePlaylistUseCase(
                    id = playlistId,
                    title = currentState.editName,
                    description = currentState.editDescription
                ).onSuccess {
                    _uiState.update { it.copy(isSaving = false) }
                    getPlaylist(playlistId) // Refresh playlist data
                    hideEditDetailSheet()
                }.onFailure { throw Exception(it.message ?: "Update failed") }

            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, error = e.message) }
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
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        error = error.message
                    )
                }
            }
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
        viewModelScope.launch {
            useCase.addSongToPlaylistUseCase.invoke(
                playlistId = playlistId,
                trackId = trackId
            ).onSuccess { data ->
                val updatedTracks = uiState.value.recommendedTracks?.map { track ->
                    if (track.id == trackId) track.toChecked() else track
                }

                _uiState.update {
                    it.copy(
                        recommendedTracks = updatedTracks
                    )
                }
                // Reload the header (its length decides whether Play, Sort and Edit show) and the song list.
                getPlaylist(playlistId, showLoading = false)
                _events.emit(PlaylistEvent.RefreshTracks)
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        error = error.message
                    )
                }
            }
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
                _uiState.update {
                    it.copy(
                        error = error.message,
                        isLoading = false
                    )
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
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        error = error.message,
                    )
                }
            }
        }
    }
}
