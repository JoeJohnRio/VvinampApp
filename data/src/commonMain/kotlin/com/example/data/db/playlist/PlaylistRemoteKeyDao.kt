package com.example.data.db.playlist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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
