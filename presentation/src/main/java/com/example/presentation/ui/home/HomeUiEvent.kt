package com.example.presentation.ui.home

import com.example.domain.entity.ListeningHistoryDomain

sealed class HomeUiEvent {
    data class NavigateToArtistPage(val history: ListeningHistoryDomain) : HomeUiEvent()
}