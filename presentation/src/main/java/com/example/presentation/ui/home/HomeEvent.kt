package com.example.presentation.ui.home

import com.example.domain.entity.ListeningHistoryDomain

sealed class HomeEvent {
    data class NavigateToArtistScreen(val history: ListeningHistoryDomain) : HomeEvent()
    data object NavigateToSettingsScreen : HomeEvent()
}