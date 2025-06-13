package com.example.data.db.music

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.entities.MusicRemoteKeyDbData

@Dao
interface MusicRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRemoteKey(keys: MusicRemoteKeyDbData)

    @Query("SELECT * FROM music_remote_keys WHERE id=:id")
    suspend fun getRemoteKeyByMovieId(id: Int): MusicRemoteKeyDbData?

    @Query("DELETE FROM music_remote_keys")
    suspend fun clearRemoteKeys()

    @Query("SELECT * FROM music_remote_keys WHERE id = (SELECT MAX(id) FROM music_remote_keys)")
    suspend fun getLastRemoteKey(): MusicRemoteKeyDbData?
}