package com.example.domain.entity.playbackinfo

data class PlaybackStateDomain(
    val queue: PlayerQueueDomain,
    val playbackInfo: PlaybackInfoDomain
)