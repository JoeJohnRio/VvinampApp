package com.example.domain.repository

import com.example.domain.entity.enum.ContentType
import com.example.domain.entity.playbackinfo.PlaybackInfoDomain

interface PlaybackRepository {
    suspend fun playbackInfo(
        trackId: String,
        contentType: ContentType,
        contentId: String
    ): Result<PlaybackInfoDomain>
}