package com.example.data.repository.playback

import com.example.data.api.playback.PlaybackApi
import com.example.data.api.playback.toDomain
import com.example.data.api.playback.toGraphQL
import com.example.domain.entity.enum.ContentType
import com.example.domain.entity.playbackinfo.PlaybackInfoDomain
import com.example.domain.repository.PlaybackRepository
import javax.inject.Inject

class PlaybackRepositoryImpl @Inject constructor(val PlaybackApi: PlaybackApi) :
    PlaybackRepository {

    override suspend fun playbackInfo(
        trackId: String,
        contentType: ContentType,
        contentId: String
    ): Result<PlaybackInfoDomain> =
        PlaybackApi.playbackInfo(
            trackId = trackId,
            contentType = contentType.toGraphQL(),
            contentId = contentId
        ).map {
            it.toDomain()
        }
}