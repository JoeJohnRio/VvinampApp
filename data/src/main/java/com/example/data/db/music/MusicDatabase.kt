package com.example.data.db.music

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.db.playlist.PlaylistRemoteKeyDao
import com.example.data.db.playlist.PlaylistTrackDao
import com.example.data.entities.PlaylistRemoteKeyEntity
import com.example.data.entities.PlaylistTrackEntity

@Database(
    entities = [
        PlaylistTrackEntity::class,
        PlaylistRemoteKeyEntity::class
    ],
    version = 3, // Increased version because I removed entities
    exportSchema = false
)
abstract class MusicDatabase : RoomDatabase() {
    abstract fun playlistTrackDao(): PlaylistTrackDao
    abstract fun playlistRemoteKeyDao(): PlaylistRemoteKeyDao
}
