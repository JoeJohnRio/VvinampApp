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
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = useCase.createPlaylistUseCase(title)

            result.onSuccess { data ->
                _events.emit(CreatePlaylistEvent.NavigateToPlaylistDetail(data))
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to load artist data"
                    )
                }
            }
        }
    }
}