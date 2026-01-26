package com.example.presentation.ui.home.artist

import com.example.domain.entity.ListeningHistory

sealed class ArtistUiEvent {
    data class NavigateToArtistPage(val history: ListeningHistory) : ArtistUiEvent()
}