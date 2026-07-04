package com.example.domain.usecase

import com.example.domain.entity.enum.ContentTypeDomain
import com.example.domain.entity.enum.PlaybackContextType
import com.example.domain.entity.playbackinfo.PlaybackInfoDomain
import com.example.domain.entity.playbackinfo.PlaybackStateDomain
import com.example.domain.entity.playbackinfo.PlayerQueueDomain
import com.example.domain.entity.song.TrackDomain
import com.example.domain.repository.PlaybackRepository
import kotlinx.coroutines.flow.Flow
class PlaybackUseCase(
    val playbackInfo: PlaybackInfoUseCase,
    val getPlayerQueue: GetPlayerQueueUseCase,
    val addToQueue: AddToQueueUseCase,
    val skipToNext: SkipToNextUseCase,
    val skipToPrevious: SkipToPreviousUseCase,
    val clearQueue: ClearQueueUseCase,
    val playContext: PlayContextUseCase,
    val playYoutube: PlayYoutubeUseCase,
    val saveCurrentTrack: SaveCurrentTrackUseCase,
    val getCurrentTrackFlow: GetCurrentTrackFlowUseCase
)

class PlaybackInfoUseCase(
    private val repository: PlaybackRepository
) {
    suspend operator fun invoke(
        trackId: String,
        contentTypeDomain: ContentTypeDomain,
        contentId: String
    ): Result<PlaybackInfoDomain> {
        return repository.playbackInfo(
            trackId = trackId,
            contentTypeDomain = contentTypeDomain,
            contentId = contentId
        )
    }
}

class GetPlayerQueueUseCase(
    private val repository: PlaybackRepository
) {
    suspend operator fun invoke(): Result<PlayerQueueDomain> = repository.getPlayerQueue()
}

class AddToQueueUseCase(
    private val repository: PlaybackRepository
) {
    suspend operator fun invoke(trackIds: List<String>): Result<PlayerQueueDomain> =
        repository.addToQueue(trackIds)
}

class SkipToNextUseCase(
    private val repository: PlaybackRepository
) {
    suspend operator fun invoke(): Result<PlaybackStateDomain> = repository.skipToNext()
}

class SkipToPreviousUseCase(
    private val repository: PlaybackRepository
) {
    suspend operator fun invoke(): Result<PlaybackStateDomain> = repository.skipToPrevious()
}

class ClearQueueUseCase(
    private val repository: PlaybackRepository
) {
    suspend operator fun invoke(): Result<PlayerQueueDomain> = repository.clearQueue()
}

class PlayContextUseCase(
    private val repository: PlaybackRepository
) {
    suspend operator fun invoke(
        contextType: PlaybackContextType,
        contextId: String?,
        trackIds: List<String>?,
        offsetTrackId: String
    ): Result<PlaybackStateDomain> {
        return repository.playContext(
            contextType = contextType,
            contextId = contextId,
            trackIds = trackIds,
            offsetTrackId = offsetTrackId
        )
    }
}

class PlayYoutubeUseCase(
    private val repository: PlaybackRepository
) {
    suspend operator fun invoke(youtubeId: String): Result<PlaybackInfoDomain> =
        repository.playYoutube(youtubeId)
}

class SaveCurrentTrackUseCase(
    private val repository: PlaybackRepository
) {
    suspend operator fun invoke(track: TrackDomain) {
        repository.saveCurrentTrack(track)
    }
}

class GetCurrentTrackFlowUseCase(
    private val repository: PlaybackRepository
) {
    operator fun invoke(): Flow<TrackDomain?> = repository.getCurrentTrackFlow()
}
