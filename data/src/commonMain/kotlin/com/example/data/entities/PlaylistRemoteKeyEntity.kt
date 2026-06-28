package com.example.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_remote_keys")
data class PlaylistRemoteKeyEntity(
    @PrimaryKey val trackId: String,
    val playlistId: String,
    val prevKey: String?,
    val nextKey: String?
)
