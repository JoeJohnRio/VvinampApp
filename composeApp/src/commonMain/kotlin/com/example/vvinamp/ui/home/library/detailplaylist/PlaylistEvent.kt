package com.example.vvinamp.ui.home.library.detailplaylist

import com.example.domain.entity.song.TrackDomain

sealed class PlaylistEvent {
    data class PlayTrack(val playlistId: String, val trackId: String): PlaylistEvent()

    data class PlayTrackRecommended(val track: TrackDomain): PlaylistEvent()
    data class NavigateToEditScreen(val playlistId: String): PlaylistEvent()
    object OnClickShuffle: PlaylistEvent()
    object RefreshTracks: PlaylistEvent()
    object NavigateToSearch: PlaylistEvent()
    object PlaylistDeleted: PlaylistEvent()
}
