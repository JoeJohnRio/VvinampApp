package com.example.vvinamp.ui.artist

import com.example.domain.entity.artist.ArtistScreenDomain
import com.example.domain.repository.ArtistRepository

class FakeArtistRepository : ArtistRepository {
    var resultToReturn: Result<ArtistScreenDomain> = Result.failure(Exception("Not configured"))
    var getArtistScreenDataCallCount: Int = 0
    var lastArtistId: String? = null

    override suspend fun getArtistScreenData(artistId: String): Result<ArtistScreenDomain> {
        getArtistScreenDataCallCount++
        lastArtistId = artistId
        return resultToReturn
    }
}
