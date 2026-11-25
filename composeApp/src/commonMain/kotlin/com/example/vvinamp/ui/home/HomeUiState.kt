package com.example.vvinamp.ui.home

import com.example.domain.entity.ListeningHistoryDomain
import com.example.domain.entity.playlist.PlaylistMinimal
import com.example.domain.entity.song.RecommendedTrackDomain

data class HomeUiState(
    // True until each section's first load finishes; later refreshes keep showing the current content.
    val isHistoryLoading: Boolean = true,
    val isRecommendedLoading: Boolean = true,
    val error: String? = null,
    val listHistory: List<ListeningHistoryDomain> = listOf(),
    val recommendedTracks: List<RecommendedTrackDomain> = listOf(),
    val featuredPlaylists: List<PlaylistMinimal> = listOf(),
    val username: String? = null
)
