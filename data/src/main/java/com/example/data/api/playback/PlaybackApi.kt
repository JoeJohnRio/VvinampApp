package com.example.data.api.playback

import com.example.graphql.PlaybackInfoQuery

interface PlaybackApi {

    suspend fun playbackInfo(trackId: String): Result<PlaybackInfoQuery.PlaybackInfo>
}