package com.example.presentation.ui.home

import com.example.domain.entity.ListeningHistoryDomain

sealed class HomeUiEvent {
    data class OnHistoryClicked(val history: ListeningHistoryDomain): HomeUiEvent()
    data object OnSettingsClicked: HomeUiEvent()
}