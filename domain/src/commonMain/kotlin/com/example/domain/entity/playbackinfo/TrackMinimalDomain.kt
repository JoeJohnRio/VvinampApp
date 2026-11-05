package com.example.domain.entity.playbackinfo

data class TrackMinimalDomain(
    val trackId: String,
    val title: String,
    val artist: String,
    val coverImageUrl: String,
    val artistBio: String? = null,
    val artistProfileImageUrl: String? = null,
    val artistMonthlyListeners: Long? = null,
)