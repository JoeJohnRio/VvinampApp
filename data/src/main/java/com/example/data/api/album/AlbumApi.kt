package com.example.data.api.album

import com.example.graphql.GetAlbumQuery

interface AlbumApi {
    suspend fun getAlbum(albumId: String): Result<GetAlbumQuery.Album>
}