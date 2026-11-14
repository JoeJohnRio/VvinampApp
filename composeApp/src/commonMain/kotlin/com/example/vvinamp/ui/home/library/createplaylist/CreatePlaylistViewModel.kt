package com.example.vvinamp.ui.home.library.createplaylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.PlaylistUseCase
import com.example.vvinamp.ui.home.library.LibraryEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    val useCase: PlaylistUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreatePlaylistUiState())
    val uiState = _uiState.asStateFlow()
    private val _events = MutableSharedFlow<CreatePlaylistEvent>()
    val events = _events.asSharedFlow()

    fun onEvent(event: CreatePlaylistUiEvent) {
        when (event) {
            is CreatePlaylistUiEvent.onChangePlaylistName -> {
                updatePlaylistName(event.playlistName)
            }

            CreatePlaylistUiEvent.onCreatePlaylistClick -> {
                createPlaylist(uiState.value.playlistName)
            }
        }
    }

    fun updatePlaylistName(name: String) {
        _uiState.update {
            it.copy(
                playlistName = name
            )
        }
    }

    fun createPlaylist(title: String) {
        val trimmedTitle = title.trim()
        if (trimmedTitle.isEmpty()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = useCase.createPlaylistUseCase(trimmedTitle)

            result.onSuccess { data ->
                // This view model is shared across visits, so clear it for the next playlist.
                _uiState.value = CreatePlaylistUiState()
                _events.emit(CreatePlaylistEvent.NavigateToPlaylistDetail(data))
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = exception.message ?: "Couldn't create the playlist"
                    )
                }
            }
        }
    }
}