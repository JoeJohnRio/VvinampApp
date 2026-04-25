package com.example.presentation.ui.home.library.detailplaylist

import com.example.domain.entity.song.TrackDomain

sealed class PlaylistEvent {
    data class PlayTrack(val track: TrackDomain): PlaylistEvent()
}