package com.example.domain.usecase

import com.example.domain.entity.playbackinfo.PlaybackInfoDomain
import com.example.domain.repository.PlaybackRepository
import javax.inject.Inject

class PlaybackUseCase @Inject constructor(
    val playbackInfo: PlaybackInfoUseCase
)

class PlaybackInfoUseCase @Inject constructor(
    private val repository: PlaybackRepository
) {
    suspend operator fun invoke(trackId: String): Result<PlaybackInfoDomain> {
        return repository.playbackInfo(trackId)
    }
}