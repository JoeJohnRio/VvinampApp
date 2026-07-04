package com.example.data.repository.album

import com.example.data.api.album.AlbumApi
import com.example.data.api.album.toDomain
import com.example.domain.entity.album.AlbumDomain
import com.example.domain.repository.AlbumRepository
class AlbumRepositoryImpl(
    private val albumApi: AlbumApi
) : AlbumRepository {
    override suspend fun getAlbum(albumId: String): Result<AlbumDomain> {
        return albumApi.getAlbum(albumId).map {
            it.toDomain()
        }
    }
}