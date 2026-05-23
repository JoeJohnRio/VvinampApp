package com.example.presentation.ui.home.library.detailplaylist.editplaylist

import com.example.domain.entity.song.TrackDomain

sealed class PlaylistEditUiEvent {
    data class OnClickPlayTrack(val trackId: String): PlaylistEditUiEvent()
    data class OnClickHold(val trackId: String): PlaylistEditUiEvent()
    data class OnDelete(val trackId: String): PlaylistEditUiEvent()
    data class OnSave(
        val tracks: List<TrackDomain>,
        val movedTrackIds: Set<String>,
        val deletedTrackIds: Set<String>
    ): PlaylistEditUiEvent()
}
