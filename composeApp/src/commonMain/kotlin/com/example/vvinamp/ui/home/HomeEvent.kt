package com.example.vvinamp.ui.home

import com.example.domain.entity.ListeningHistoryDomain
import com.example.domain.entity.song.RecommendedTrackDomain

sealed class HomeEvent {
    data class NavigateToArtistScreen(val history: ListeningHistoryDomain) : HomeEvent()
    data class PlayTrack(val history: ListeningHistoryDomain): HomeEvent()
    data class PlayRecommendedTrack(val track: RecommendedTrackDomain): HomeEvent()
    data class NavigateToPlaylistScreen(val history: ListeningHistoryDomain): HomeEvent()
    data class NavigateToPlaylistDetail(val playlistId: String): HomeEvent()
    data class NavigateToAlbumScreen(val history: ListeningHistoryDomain): HomeEvent()
    data object NavigateToSettingsScreen : HomeEvent()
    data object NavigateToSearchResultScreen : HomeEvent()
}
