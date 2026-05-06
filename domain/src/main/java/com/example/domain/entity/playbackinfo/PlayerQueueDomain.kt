package com.example.domain.entity.playbackinfo

data class PlayerQueueDomain(
    val currentTrackId: String?,
    val upcomingTrackIds: List<String>
)
