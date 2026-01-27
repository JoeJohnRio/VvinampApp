package com.example.data.api.artist

import com.apollographql.apollo.ApolloClient
import com.example.data.util.safeQuery
import com.example.graphql.GetArtistScreenDataQuery
import com.example.graphql.ListRecentHistoryQuery
import javax.inject.Inject

class ArtistApiImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : ArtistApi {
    override suspend fun getArtistScreenData(artistId: String): Result<GetArtistScreenDataQuery.GetArtistScreenData> {
        return apolloClient.safeQuery(
            GetArtistScreenDataQuery(artistId)
        ) {
            it.getArtistScreenData
        }
    }
}