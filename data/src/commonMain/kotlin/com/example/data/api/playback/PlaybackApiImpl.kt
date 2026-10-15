package com.example.data.api.playback

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.example.data.util.safeMutation
import com.example.data.util.safeQuery
import com.apollographql.apollo.exception.ApolloGraphQLException
import com.apollographql.apollo.exception.DefaultApolloException
import com.example.graphql.AddToQueueMutation
import com.example.graphql.ClearQueueMutation
import com.example.graphql.DownloadProgressSubscription
import com.example.graphql.GetPlayerQueueQuery
import com.example.graphql.PlayContextMutation
import com.example.graphql.PlayYoutubeMutation
import com.example.graphql.PlaybackInfoQuery
import com.example.graphql.SkipToNextMutation
import com.example.graphql.SkipToPreviousMutation
import com.example.graphql.StartDownloadMutation
import com.example.graphql.type.ContentType
import com.example.graphql.type.DownloadInput
import com.example.graphql.type.PlaybackContextInput
import com.example.graphql.type.PlaybackInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
class PlaybackApiImpl(val apolloClient: ApolloClient) : PlaybackApi {
    override suspend fun playbackInfo(
        trackId: String,
        contentType: ContentType,
        contentId: String,
    ): Result<PlaybackInfoQuery.PlaybackInfo> =
        apolloClient.safeQuery(
            PlaybackInfoQuery(
                playbackInput = PlaybackInput(
                    trackId = trackId,
                    contentType = Optional.present(contentType),
                    contentId = Optional.present(contentId)
                ),
            )
        ) {
            it.playbackInfo
        }

    override suspend fun getPlayerQueue(): Result<GetPlayerQueueQuery.PlayerQueue> =
        apolloClient.safeQuery(GetPlayerQueueQuery()) { it.playerQueue }

    override suspend fun addToQueue(trackIds: List<String>): Result<AddToQueueMutation.AddToQueue> =
        apolloClient.safeMutation(AddToQueueMutation(ids = trackIds)) { it.addToQueue }

    override suspend fun skipToNext(): Result<SkipToNextMutation.SkipToNext> =
        apolloClient.safeMutation(SkipToNextMutation()) { it.skipToNext }

    override suspend fun skipToPrevious(): Result<SkipToPreviousMutation.SkipToPrevious> =
        apolloClient.safeMutation(SkipToPreviousMutation()) { it.skipToPrevious }

    override suspend fun clearQueue(): Result<ClearQueueMutation.ClearQueue> =
        apolloClient.safeMutation(ClearQueueMutation()) { it.clearQueue }

    override suspend fun playContext(
        context: PlaybackContextInput,
        offsetTrackId: String
    ): Result<PlayContextMutation.PlayContext> =
        apolloClient.safeMutation(
            PlayContextMutation(
                context = context,
                offsetTrackId = offsetTrackId
            )
        ) { it.playContext }

    override suspend fun playYoutube(youtubeId: String): Result<PlayYoutubeMutation.PlayYoutube> {
        return apolloClient.safeMutation(
            PlayYoutubeMutation(youtubeId)
        ) { it.playYoutube }
    }

    override suspend fun startDownload(youtubeUrl: String): Result<StartDownloadMutation.StartDownload> {
        return apolloClient.safeMutation(
            StartDownloadMutation(DownloadInput(youtubeUrl = youtubeUrl))
        ) { it.startDownload }
    }

    override fun downloadProgress(id: String): Flow<Result<DownloadProgressSubscription.DownloadProgress>> {
        return apolloClient.subscription(DownloadProgressSubscription(id))
            .toFlow()
            .map { response ->
                if (response.hasErrors()) {
                    Result.failure(ApolloGraphQLException(response.errors.orEmpty()))
                } else {
                    val data = response.data?.downloadProgress
                    if (data != null) {
                        Result.success(data)
                    } else {
                        Result.failure(DefaultApolloException(message = "No download progress data"))
                    }
                }
            }
            .catch { e ->
                emit(Result.failure(e))
            }
    }
}
