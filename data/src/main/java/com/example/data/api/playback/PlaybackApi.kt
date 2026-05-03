package com.example.data.api.playback

import com.example.graphql.PlaybackInfoQuery
import com.example.graphql.type.ContentType

interface PlaybackApi {
    suspend fun playbackInfo(
        trackId: String,
        contentType: ContentType,
        contentId: String
    ): Result<PlaybackInfoQuery.PlaybackInfo>
}