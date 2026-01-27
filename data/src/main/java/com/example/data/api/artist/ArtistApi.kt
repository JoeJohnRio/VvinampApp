package com.example.data.api.artist

import com.example.graphql.GetArtistScreenDataQuery

interface ArtistApi {
    suspend fun getArtistScreenData(artistId: String): Result<GetArtistScreenDataQuery.GetArtistScreenData>
}