package com.example.data.api.playback

import com.example.graphql.AddToQueueMutation
import com.example.graphql.ClearQueueMutation
import com.example.graphql.GetPlayerQueueQuery
import com.example.graphql.DownloadProgressSubscription
import com.example.graphql.PlayContextMutation
import com.example.graphql.PlayYoutubeMutation
import com.example.graphql.PlaybackInfoQuery
import com.example.graphql.SkipToNextMutation
import com.example.graphql.SkipToPreviousMutation
import com.example.graphql.StartDownloadMutation
import com.example.graphql.type.ContentType
import com.example.graphql.type.PlaybackContextInput
import kotlinx.coroutines.flow.Flow

interface PlaybackApi {
    suspend fun playbackInfo(
        trackId: String,
        contentType: ContentType,
        contentId: String
    ): Result<PlaybackInfoQuery.PlaybackInfo>

    suspend fun getPlayerQueue(): Result<GetPlayerQueueQuery.PlayerQueue>

    suspend fun addToQueue(trackIds: List<String>): Result<AddToQueueMutation.AddToQueue>

    suspend fun skipToNext(): Result<SkipToNextMutation.SkipToNext>

    suspend fun skipToPrevious(): Result<SkipToPreviousMutation.SkipToPrevious>

    suspend fun clearQueue(): Result<ClearQueueMutation.ClearQueue>

    suspend fun playContext(
        context: PlaybackContextInput,
        offsetTrackId: String
    ): Result<PlayContextMutation.PlayContext>

    suspend fun playYoutube(youtubeId: String): Result<PlayYoutubeMutation.PlayYoutube>

    suspend fun startDownload(youtubeUrl: String): Result<StartDownloadMutation.StartDownload>

    fun downloadProgress(id: String): Flow<Result<DownloadProgressSubscription.DownloadProgress>>
}
