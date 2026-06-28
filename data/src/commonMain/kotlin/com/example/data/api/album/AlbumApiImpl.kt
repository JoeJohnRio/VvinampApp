package com.example.data.api.album

import com.apollographql.apollo.ApolloClient
import com.example.data.util.safeQuery
import com.example.graphql.GetAlbumQuery
import javax.inject.Inject

class AlbumApiImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : AlbumApi {

    override suspend fun getAlbum(albumId: String): Result<GetAlbumQuery.Album> {
        return apolloClient.safeQuery(
            GetAlbumQuery(albumId)
        ) {
            it.album
        }
    }
}