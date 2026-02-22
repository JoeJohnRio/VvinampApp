package com.example.domain.repository

import com.example.domain.entity.album.AlbumDomain

interface AlbumRepository {
    suspend fun getAlbum(albumId: String): Result<AlbumDomain>
}