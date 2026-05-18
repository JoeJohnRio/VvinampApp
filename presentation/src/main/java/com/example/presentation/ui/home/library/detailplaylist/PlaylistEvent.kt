package com.example.presentation.ui.home.library.detailplaylist

import com.example.domain.entity.song.TrackDomain

sealed class PlaylistEvent {
    data class PlayTrack(val playlistId: String, val trackId: String): PlaylistEvent()

    data class PlayTrackRecommended(val track: TrackDomain): PlaylistEvent()
    object OnClickShuffle: PlaylistEvent()
}