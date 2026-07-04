package com.example.domain.usecase

import com.example.domain.entity.AuthSession
import com.example.domain.entity.artist.ArtistScreenDomain
import com.example.domain.repository.ArtistRepository
import com.example.domain.repository.AuthRepository
class ArtistUseCase(
    val getArtistScreenData: GetArtistScreenDataUseCase,
)

class GetArtistScreenDataUseCase(
    private val repository: ArtistRepository
) {
    suspend operator fun invoke(artistId: String): Result<ArtistScreenDomain> {
        return repository.getArtistScreenData(artistId = artistId)
    }
}