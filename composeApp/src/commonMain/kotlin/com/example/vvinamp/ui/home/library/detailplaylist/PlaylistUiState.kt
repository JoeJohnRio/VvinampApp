package com.example.vvinamp.ui.home.library.detailplaylist

import androidx.compose.ui.graphics.Color
import com.example.domain.entity.playlist.PlaylistComplete
import com.example.domain.entity.playlist.PlaylistTrackSortByDomain
import com.example.domain.entity.song.RecommendedTrackDomain

data class PlaylistUiState(
    val error: String? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val playlist: PlaylistComplete? = null,
    val isPlaylistCreator: Boolean = false,
    val dominantColor: Color = Color.Unspecified,
    val sortBy: PlaylistTrackSortByDomain = PlaylistTrackSortByDomain.CUSTOM,
    val isSortSheetVisible: Boolean = false,
    val isEditDetailSheetVisible: Boolean = false,
    val isDeleteConfirmVisible: Boolean = false,
    val editName: String = "",
    val editDescription: String = "",
    val editCoverUri: String? = null,
    val recommendedTracks: List<RecommendedTrackDomain>? = null
)
