package com.example.data.repository.playback

import com.example.data.api.playback.PlaybackApi
import com.example.data.api.playback.toDomain
import com.example.domain.entity.playbackinfo.PlaybackInfoDomain
import com.example.domain.repository.PlaybackRepository
import javax.inject.Inject

class PlaybackRepositoryImpl @Inject constructor(val PlaybackApi: PlaybackApi) :
    PlaybackRepository {

    override suspend fun playbackInfo(trackId: String): Result<PlaybackInfoDomain> =
        PlaybackApi.playbackInfo(trackId).map {
            it.toDomain()
        }
}