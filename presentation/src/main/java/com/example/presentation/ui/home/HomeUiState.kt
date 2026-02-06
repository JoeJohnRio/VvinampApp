package com.example.presentation.ui.home

import com.example.domain.entity.ListeningHistoryDomain
import com.example.presentation.util.UiState

data class HomeUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val listHistory: List<ListeningHistoryDomain> = listOf(),
)