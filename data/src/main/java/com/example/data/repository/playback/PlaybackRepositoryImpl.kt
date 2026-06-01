package com.example.data.repository.playback

import com.apollographql.apollo.api.Optional
import com.example.data.api.playback.PlaybackApi
import com.example.data.api.playback.toDomain
import com.example.data.api.playback.toGraphQL
import com.example.domain.entity.enum.ContentTypeDomain
import com.example.domain.entity.enum.PlaybackContextType
import com.example.domain.entity.playbackinfo.PlaybackInfoDomain
import com.example.domain.entity.playbackinfo.PlaybackStateDomain
import com.example.domain.entity.playbackinfo.PlayerQueueDomain
import com.example.domain.repository.PlaybackRepository
import com.example.graphql.type.PlaybackContextInput
import javax.inject.Inject

class PlaybackRepositoryImpl @Inject constructor(private val playbackApi: PlaybackApi) :
    PlaybackRepository {

    override suspend fun playbackInfo(
        trackId: String,
        contentTypeDomain: ContentTypeDomain,
        contentId: String
    ): Result<PlaybackInfoDomain> =
        playbackApi.playbackInfo(
            trackId = trackId,
            contentType = contentTypeDomain.toGraphQL(),
            contentId = contentId
        ).map {
            it.toDomain()
        }

    override suspend fun getPlayerQueue(): Result<PlayerQueueDomain> =
        playbackApi.getPlayerQueue().map { it.toDomain() }

    override suspend fun addToQueue(trackIds: List<String>): Result<PlayerQueueDomain> {
        return playbackApi.addToQueue(trackIds).map { it.toDomain() }
    }

    override suspend fun skipToNext(): Result<PlaybackStateDomain> {
        return playbackApi.skipToNext().map { it.toDomain() }
    }

    override suspend fun skipToPrevious(): Result<PlaybackStateDomain> {
        return playbackApi.skipToPrevious().map { it.toDomain() }
    }

    override suspend fun clearQueue(): Result<PlayerQueueDomain> {
        return playbackApi.clearQueue().map {
            it.toDomain()
        }
    }

    override suspend fun playContext(
        contextType: PlaybackContextType,
        contextId: String?,
        trackIds: List<String>?,
        offsetTrackId: String
    ): Result<PlaybackStateDomain> {
        val input = PlaybackContextInput(
            type = contextType.toGraphQL(),
            id = Optional.presentIfNotNull(contextId),
            trackIds = Optional.presentIfNotNull(trackIds)
        )
        return playbackApi.playContext(input, offsetTrackId).map { it.toDomain() }
    }

    override suspend fun playYoutube(youtubeId: String): Result<PlaybackInfoDomain> {
        return playbackApi.playYoutube(youtubeId).map { it.toDomain() }
    }
}
