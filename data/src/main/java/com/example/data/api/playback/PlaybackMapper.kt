package com.example.data.api.playback

import com.example.domain.entity.playbackinfo.PlaybackInfoDomain
import com.example.graphql.PlaybackInfoQuery

fun PlaybackInfoQuery.PlaybackInfo.toDomain(): PlaybackInfoDomain {
    return PlaybackInfoDomain(
        hlsUrl = this.hlsUrl,
        mp3Fallback = this.mp3Fallback,
        durationMs = this.durationMs
    )
}