package com.example.domain.repository

import com.example.domain.entity.artist.ArtistScreenDomain

interface ArtistRepository {
    suspend fun getArtistScreenData(artistId: String): Result<ArtistScreenDomain>
}