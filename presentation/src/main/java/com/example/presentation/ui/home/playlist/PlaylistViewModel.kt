package com.example.presentation.ui.home.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.PlaylistUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlaylistViewModel @Inject constructor(
    private val useCase: PlaylistUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlaylistUiState())
    val uiState = _uiState.asStateFlow()

}