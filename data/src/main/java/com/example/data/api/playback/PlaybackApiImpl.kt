package com.example.data.api.playback

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.example.data.util.safeQuery
import com.example.graphql.PlaybackInfoQuery
import com.example.graphql.type.ContentType
import com.example.graphql.type.PlaybackInput
import javax.inject.Inject

class PlaybackApiImpl @Inject constructor(val apolloClient: ApolloClient) : PlaybackApi {
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
}