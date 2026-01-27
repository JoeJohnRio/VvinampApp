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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _getArtistScreenData = MutableStateFlow<UiState<ArtistScreenDomain>>(UiState.Idle)
    val getArtistScreenData = _getArtistScreenData.asStateFlow()

    fun onEvent(event: ArtistEvent) {
        when (event) {
            is ArtistEvent.OnHistoryClicked -> {}
        }
    }

    fun getArtistScreenData(artistId: String) {
        viewModelScope.launch {
            _getArtistScreenData.value = UiState.Loading
            _getArtistScreenData.value = artistUseCase.getArtistScreenData.invoke(artistId).toUiState()
        }
    }
}
