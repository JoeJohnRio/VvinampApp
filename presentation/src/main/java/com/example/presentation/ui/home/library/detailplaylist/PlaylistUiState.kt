package com.example.presentation.ui.home.library.detailplaylist

import android.net.Uri
import androidx.compose.ui.graphics.Color
import com.example.domain.entity.playlist.PlaylistComplete
import com.example.domain.entity.playlist.PlaylistTrackSortByDomain
import com.example.domain.entity.song.RecommendedTrackDomain
import com.example.presentation.ui.theme.AppColours

data class PlaylistUiState(
    val error: String? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val playlist: PlaylistComplete? = null,
    val isPlaylistCreator: Boolean = false,
    val dominantColor: Color = AppColours.BackgroundBlack,
    val sortBy: PlaylistTrackSortByDomain = PlaylistTrackSortByDomain.RECENTLY_ADDED,
    val isSortSheetVisible: Boolean = false,
    val isEditDetailSheetVisible: Boolean = false,
    val editName: String = "",
    val editDescription: String = "",
    val editCoverUri: Uri? = null,
    val recommendedTracks: List<RecommendedTrackDomain>? = null
)
