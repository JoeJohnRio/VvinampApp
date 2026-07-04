package com.example.vvinamp.ui.home.artist

import com.example.domain.entity.ListeningHistoryDomain


sealed class ArtistEvent {
    data class OnHistoryClicked(val history: ListeningHistoryDomain): ArtistEvent()
}