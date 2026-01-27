package com.example.domain.usecase

import com.example.domain.entity.AuthSession
import com.example.domain.entity.artist.ArtistScreenDomain
import com.example.domain.repository.ArtistRepository
import com.example.domain.repository.AuthRepository
import javax.inject.Inject


class ArtistUseCase @Inject constructor(
    val getArtistScreenData: GetArtistScreenDataUseCase,
)

class GetArtistScreenDataUseCase @Inject constructor(
    private val repository: ArtistRepository
) {
    suspend operator fun invoke(artistId: String): Result<ArtistScreenDomain> {
        return repository.getArtistScreenData(artistId = artistId)
    }
}