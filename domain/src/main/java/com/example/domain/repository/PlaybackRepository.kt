package com.example.domain.repository

import com.example.domain.entity.enum.ContentTypeDomain
import com.example.domain.entity.playbackinfo.PlaybackInfoDomain
import com.example.domain.entity.playbackinfo.PlaybackStateDomain
import com.example.domain.entity.playbackinfo.PlayerQueueDomain

interface PlaybackRepository {
    suspend fun playbackInfo(
        trackId: String,
        contentTypeDomain: ContentTypeDomain,
        contentId: String
    ): Result<PlaybackInfoDomain>

    suspend fun getPlayerQueue(): Result<PlayerQueueDomain>
    suspend fun addToQueue(trackIds: List<String>): Result<PlayerQueueDomain>
    suspend fun skipToNext(): Result<PlayerQueueDomain>
    suspend fun skipToPrevious(): Result<PlayerQueueDomain>
    suspend fun clearQueue(): Result<PlayerQueueDomain>
    suspend fun playContext(
        contextType: com.example.domain.entity.enum.PlaybackContextType,
        contextId: String?,
        trackIds: List<String>?,
        offsetTrackId: String
    ): Result<PlaybackStateDomain>
}