package com.example.domain.entity.playbackinfo

data class PlayerQueueDomain(
    val currentTrack: TrackMinimalDomain?,
    val upcomingTrackIds: List<TrackMinimalDomain>,
    val previousTrack: List<TrackMinimalDomain>
)
