package com.example.domain.usecase

import com.example.domain.entity.album.AlbumDomain
import com.example.domain.entity.artist.ArtistScreenDomain
import com.example.domain.repository.AlbumRepository
import com.example.domain.repository.ArtistRepository
import javax.inject.Inject


class AlbumUseCase @Inject constructor(
    val getAlbum: GetAlbum,
)

class GetAlbum @Inject constructor(
    private val repository: AlbumRepository
) {
    suspend operator fun invoke(albumId: String): Result<AlbumDomain> {
        return repository.getAlbum(albumId = albumId)
    }
}