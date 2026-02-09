package com.example.domain.repository

import com.example.domain.entity.playbackinfo.PlaybackInfoDomain

interface PlaybackRepository {
    suspend fun playbackInfo(trackId: String): Result<PlaybackInfoDomain>
}