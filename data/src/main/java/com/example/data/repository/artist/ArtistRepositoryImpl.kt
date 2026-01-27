package com.example.data.repository.artist

import com.example.data.api.artist.ArtistApi
import com.example.data.api.artist.toDomain
import com.example.domain.entity.artist.ArtistScreenDomain
import com.example.domain.repository.ArtistRepository
import javax.inject.Inject

class ArtistRepositoryImpl @Inject constructor(
    private val artistApi: ArtistApi
): ArtistRepository {
    override suspend fun getArtistScreenData(artistId: String): Result<ArtistScreenDomain> {
        return artistApi.getArtistScreenData(artistId).map {
            it.toDomain()
        }
    }
}