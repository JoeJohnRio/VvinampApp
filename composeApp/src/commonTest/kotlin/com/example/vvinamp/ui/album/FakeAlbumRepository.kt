package com.example.vvinamp.ui.album

import com.example.domain.entity.album.AlbumDomain
import com.example.domain.repository.AlbumRepository
import com.example.domain.usecase.AlbumUseCase
import com.example.domain.usecase.GetAlbum

class FakeAlbumRepository : AlbumRepository {

    var resultToReturn: Result<AlbumDomain> = Result.failure(Exception("Not configured"))
    var getAlbumCallCount: Int = 0
    var lastAlbumId: String? = null

    override suspend fun getAlbum(albumId: String): Result<AlbumDomain> {
        getAlbumCallCount++
        lastAlbumId = albumId
        return resultToReturn
    }
}