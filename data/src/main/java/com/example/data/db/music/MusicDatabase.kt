package com.example.data.db.music

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.entities.MusicDbData
import com.example.data.entities.MusicRemoteKeyDbData

@Database(
    entities = [MusicDbData::class, MusicRemoteKeyDbData::class],
    version = 1,
    exportSchema = false
)
abstract class MusicDatabase : RoomDatabase() {
    abstract fun movieDao(): MusicDao
    abstract fun movieRemoteKeysDao(): MusicRemoteKeyDao
}