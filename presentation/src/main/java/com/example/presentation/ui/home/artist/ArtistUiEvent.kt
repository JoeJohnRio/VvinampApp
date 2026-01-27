package com.example.presentation.ui.home.artist

import com.example.domain.entity.ListeningHistoryDomain

sealed class ArtistUiEvent {
    data class NavigateToArtistPage(val history: ListeningHistoryDomain) : ArtistUiEvent()
}