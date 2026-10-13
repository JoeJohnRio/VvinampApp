package com.example.data.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(
    tableName = "playlist_remote_keys",
    primaryKeys = ["playlistId", "trackId"]
)
data class PlaylistRemoteKeyEntity(
    val trackId: String,
    val playlistId: String,
    val prevKey: String?,
    val nextKey: String?
)
