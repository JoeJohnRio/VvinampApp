package com.example.presentation.ui.home

import com.example.domain.entity.ListeningHistory

sealed class HomeUiEvent {
    data class NavigateToArtistPage(val history: ListeningHistory) : HomeUiEvent()
}