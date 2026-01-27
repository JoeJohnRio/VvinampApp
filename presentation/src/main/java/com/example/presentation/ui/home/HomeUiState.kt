package com.example.presentation.ui.home

import com.example.domain.entity.ListeningHistoryDomain
import com.example.presentation.util.UiState

data class HomeUiState(
    val listHistory: UiState<List<ListeningHistoryDomain>> = UiState.Idle,
    val isLoading: Boolean = false,
    val error: String? = null
)