package com.example.vvinamp.ui.home
import vvinampapp.composeapp.generated.resources.*

import com.example.domain.entity.ListeningHistoryDomain

sealed class HomeEvent {
    data class NavigateToArtistScreen(val history: ListeningHistoryDomain) : HomeEvent()
    data class PlayTrack(val history: ListeningHistoryDomain): HomeEvent()
    data class NavigateToPlaylistScreen(val history: ListeningHistoryDomain): HomeEvent()
    data class NavigateToAlbumScreen(val history: ListeningHistoryDomain): HomeEvent()
    data object NavigateToSettingsScreen : HomeEvent()
    data object NavigateToSearchResultScreen : HomeEvent()
}
