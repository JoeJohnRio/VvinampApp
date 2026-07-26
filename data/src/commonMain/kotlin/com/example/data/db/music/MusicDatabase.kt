package com.example.data.db.music

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import com.example.data.db.playback.RecentPlaybackDao
import com.example.data.db.playlist.PlaylistRemoteKeyDao
import com.example.data.db.playlist.PlaylistTrackDao
import com.example.data.entities.PlaylistRemoteKeyEntity
import com.example.data.entities.PlaylistTrackEntity
import com.example.data.entities.RecentPlaybackEntity

@Database(
    entities = [
        PlaylistTrackEntity::class,
        PlaylistRemoteKeyEntity::class,
        RecentPlaybackEntity::class
    ],
    version = 4, // Increased version for new entity
    exportSchema = false
)
@ConstructedBy(MusicDatabaseConstructor::class)
abstract class MusicDatabase : RoomDatabase() {
    abstract fun playlistTrackDao(): PlaylistTrackDao
    abstract fun playlistRemoteKeyDao(): PlaylistRemoteKeyDao
    abstract fun recentPlaybackDao(): RecentPlaybackDao
}

expect object MusicDatabaseConstructor : RoomDatabaseConstructor<MusicDatabase> {
    override fun initialize(): MusicDatabase
}
