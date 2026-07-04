package com.example.domain.usecase

import com.example.domain.entity.album.AlbumDomain
import com.example.domain.entity.artist.ArtistScreenDomain
import com.example.domain.repository.AlbumRepository
import com.example.domain.repository.ArtistRepository
class AlbumUseCase(
    val getAlbum: GetAlbum,
)

class GetAlbum(
    private val repository: AlbumRepository
) {
    suspend operator fun invoke(albumId: String): Result<AlbumDomain> {
        return repository.getAlbum(albumId = albumId)
    }
}