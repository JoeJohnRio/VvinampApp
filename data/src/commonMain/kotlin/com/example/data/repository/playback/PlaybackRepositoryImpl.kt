package com.example.data.repository.playback

import com.apollographql.apollo.api.Optional
import com.example.data.api.playback.PlaybackApi
import com.example.data.api.playback.toDomain
import com.example.data.api.playback.toGraphQL
import com.example.data.db.playback.RecentPlaybackDao
import com.example.data.entities.toDomain
import com.example.data.entities.toRecentEntity
import com.example.domain.entity.enum.ContentTypeDomain
import com.example.domain.entity.enum.PlaybackContextType
import com.example.domain.entity.playbackinfo.PlaybackInfoDomain
import com.example.domain.entity.playbackinfo.PlaybackStateDomain
import com.example.domain.entity.playbackinfo.PlayerQueueDomain
import com.example.domain.entity.song.TrackDomain
import com.example.domain.repository.PlaybackRepository
import com.example.domain.entity.youtube.YoutubeDownloadState
import com.example.graphql.type.PlaybackContextInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.takeWhile
class PlaybackRepositoryImpl(
    private val playbackApi: PlaybackApi,
    private val recentPlaybackDao: RecentPlaybackDao
) : PlaybackRepository {

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

    override fun downloadAndPlayYoutube(youtubeUrl: String): Flow<YoutubeDownloadState> = flow {
        val startResult = playbackApi.startDownload(youtubeUrl)
        val task = startResult.getOrElse { e ->
            emit(YoutubeDownloadState.Error(e.message ?: "Failed to start download"))
            return@flow
        }

        if (task.status.equals("completed", ignoreCase = true)) {
            val playbackResult = playbackApi.playYoutube(youtubeUrl)
            playbackResult.fold(
                onSuccess = { emit(YoutubeDownloadState.Completed(it.toDomain())) },
                onFailure = { emit(YoutubeDownloadState.Error(it.message ?: "Failed to load playback")) }
            )
            return@flow
        }

        emit(YoutubeDownloadState.Progress(task.progress))

        var isDone = false
        playbackApi.downloadProgress(task.id)
            .takeWhile { !isDone }
            .collect { result ->
                result.fold(
                    onSuccess = { progressData ->
                        if (!progressData.error.isNullOrBlank() || progressData.status.equals("failed", ignoreCase = true)) {
                            isDone = true
                            emit(YoutubeDownloadState.Error(progressData.error ?: "Download failed"))
                        } else if (progressData.status.equals("completed", ignoreCase = true)) {
                            isDone = true
                            val playbackResult = playbackApi.playYoutube(youtubeUrl)
                            playbackResult.fold(
                                onSuccess = { emit(YoutubeDownloadState.Completed(it.toDomain())) },
                                onFailure = { emit(YoutubeDownloadState.Error(it.message ?: "Failed to load playback")) }
                            )
                        } else {
                            emit(YoutubeDownloadState.Progress(progressData.progress))
                        }
                    },
                    onFailure = { error ->
                        isDone = true
                        emit(YoutubeDownloadState.Error(error.message ?: "Subscription error"))
                    }
                )
            }
    }

    override fun getCurrentTrackFlow(): Flow<TrackDomain?> {
        return recentPlaybackDao.getCurrentTrackFlow().map { it?.toDomain() }
    }

    override suspend fun saveCurrentTrack(track: TrackDomain) {
        recentPlaybackDao.saveCurrentTrack(track.toRecentEntity())
    }
}
