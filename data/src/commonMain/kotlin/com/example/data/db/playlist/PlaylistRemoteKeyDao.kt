package com.example.data.db.playlist

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.example.data.entities.PlaylistRemoteKeyEntity

@Dao
interface PlaylistRemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<PlaylistRemoteKeyEntity>)

    @Query("SELECT * FROM playlist_remote_keys WHERE trackId = :trackId AND playlistId = :playlistId")
    suspend fun remoteKeysByTrackAndPlaylist(trackId: String, playlistId: String): PlaylistRemoteKeyEntity?

    @Query("DELETE FROM playlist_remote_keys WHERE playlistId = :playlistId")
    suspend fun clearRemoteKeysByPlaylist(playlistId: String)
}
