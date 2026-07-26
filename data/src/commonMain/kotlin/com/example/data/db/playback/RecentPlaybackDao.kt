package com.example.data.db.playback

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.example.data.entities.RecentPlaybackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentPlaybackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCurrentTrack(track: RecentPlaybackEntity)

    @Query("SELECT * FROM recent_playback WHERE id = 1")
    fun getCurrentTrackFlow(): Flow<RecentPlaybackEntity?>

    @Query("SELECT * FROM recent_playback WHERE id = 1")
    suspend fun getCurrentTrack(): RecentPlaybackEntity?

    @Query("DELETE FROM recent_playback")
    suspend fun clear()
}
