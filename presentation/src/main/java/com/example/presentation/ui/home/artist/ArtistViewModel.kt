package com.example.presentation.ui.home.artist

import androidx.lifecycle.viewModelScope
import com.example.domain.entity.artist.ArtistScreenDomain
import com.example.domain.usecase.ArtistUseCase
import com.example.domain.usecase.HistoryUseCase
import com.example.domain.util.NetworkMonitor
import com.example.presentation.base.BaseViewModel
import com.example.presentation.ui.home.HomeUiEvent
import com.example.presentation.ui.home.HomeUiState
import com.example.presentation.ui.home.usecase.GetMoviesWithSeparators
import com.example.presentation.util.UiState
import com.example.presentation.util.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    val networkMonitor: NetworkMonitor,
    val artistUseCase: ArtistUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ArtistUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ArtistUiEvent>()
    val events = _events.asSharedFlow()

    fun onEvent(event: ArtistEvent) {
        when (event) {
            is ArtistEvent.OnHistoryClicked -> {}
        }
    }

    fun clearUiState() {
        _uiState.update {
            it.copy(
                isLoading = false,
                artistProfile = null,
                topSongs = null,
                topReleases = null,
                error = null
            )
        }
    }

    fun getArtistScreenData(artistId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = artistUseCase.getArtistScreenData(artistId)

            result.onSuccess { data ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        artistProfile = data.artistProfile,
                        topSongs = data.topSongs,
                        topReleases = data.topReleases,
                        error = null
                    )
                }
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
