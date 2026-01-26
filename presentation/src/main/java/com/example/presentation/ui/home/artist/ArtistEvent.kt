package com.example.presentation.ui.home.artist

import com.example.domain.entity.ListeningHistory


sealed class ArtistEvent {
    data class OnHistoryClicked(val history: ListeningHistory): ArtistEvent()
}