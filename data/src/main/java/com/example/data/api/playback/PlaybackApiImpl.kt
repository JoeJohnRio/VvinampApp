package com.example.data.api.playback

import com.apollographql.apollo.ApolloClient
import com.example.data.util.safeQuery
import com.example.graphql.PlaybackInfoQuery
import javax.inject.Inject

class PlaybackApiImpl @Inject constructor(val apolloClient: ApolloClient) : PlaybackApi {
    override suspend fun playbackInfo(trackId: String): Result<PlaybackInfoQuery.PlaybackInfo> =
        apolloClient.safeQuery(PlaybackInfoQuery(trackId = trackId)) {
            it.playbackInfo
        }
}