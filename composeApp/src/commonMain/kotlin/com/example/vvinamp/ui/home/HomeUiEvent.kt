package com.example.vvinamp.ui.home

import com.example.domain.entity.ListeningHistoryDomain
import com.example.domain.entity.song.RecommendedTrackDomain

sealed class HomeUiEvent {
    data class OnHistoryClicked(val history: ListeningHistoryDomain): HomeUiEvent()
    data class OnRecommendedTrackClicked(val track: RecommendedTrackDomain): HomeUiEvent()
    data class OnPlaylistClicked(val playlistId: String): HomeUiEvent()
    data object OnSettingsClicked: HomeUiEvent()
    data object OnSearchClicked: HomeUiEvent()
}
