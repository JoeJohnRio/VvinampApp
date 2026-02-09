package com.example.domain.entity.playbackinfo

data class PlaybackInfoDomain(
    val hlsUrl: String,
    val mp3Fallback: String?,
    val durationMs: Int?,
)