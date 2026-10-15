package com.example.domain.repository

import com.example.domain.entity.enum.ContentTypeDomain
import com.example.domain.entity.playbackinfo.PlaybackInfoDomain
import com.example.domain.entity.playbackinfo.PlaybackStateDomain
import com.example.domain.entity.playbackinfo.PlayerQueueDomain
import com.example.domain.entity.song.TrackDomain
import com.example.domain.entity.youtube.YoutubeDownloadState
import kotlinx.coroutines.flow.Flow

interface PlaybackRepository {
    suspend fun playbackInfo(
        trackId: String,
        contentTypeDomain: ContentTypeDomain,
        contentId: String
    ): Result<PlaybackInfoDomain>

    suspend fun getPlayerQueue(): Result<PlayerQueueDomain>
    suspend fun addToQueue(trackIds: List<String>): Result<PlayerQueueDomain>
    suspend fun skipToNext(): Result<PlaybackStateDomain>
    suspend fun skipToPrevious(): Result<PlaybackStateDomain>
    suspend fun clearQueue(): Result<PlayerQueueDomain>
    suspend fun playContext(
        contextType: com.example.domain.entity.enum.PlaybackContextType,
        contextId: String?,
        trackIds: List<String>?,
        offsetTrackId: String
    ): Result<PlaybackStateDomain>
    suspend fun playYoutube(youtubeId: String): Result<PlaybackInfoDomain>
    fun downloadAndPlayYoutube(youtubeUrl: String): Flow<YoutubeDownloadState>
    
    // New methods for persistence
    fun getCurrentTrackFlow(): Flow<TrackDomain?>
    suspend fun saveCurrentTrack(track: TrackDomain)
}
