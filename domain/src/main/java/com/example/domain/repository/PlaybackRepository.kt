package com.example.domain.repository

import com.example.domain.entity.enum.ContentType
import com.example.domain.entity.playbackinfo.PlaybackInfoDomain

interface PlaybackRepository {
    suspend fun playbackInfo(
        trackId: String,
        contentType: ContentType,
        contentId: String
    ): Result<PlaybackInfoDomain>

    suspend fun getPlayerQueue(): Result<PlayerQueueDomain>
    suspend fun addToQueue(trackIds: List<String>): Result<PlayerQueueDomain>
    suspend fun skipToNext(): Result<PlayerQueueDomain>
    suspend fun clearQueue(): Result<PlayerQueueDomain>
}