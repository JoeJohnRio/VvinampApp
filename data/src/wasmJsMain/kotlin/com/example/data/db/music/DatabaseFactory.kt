package com.example.data.db.music

import androidx.room3.Room
import androidx.room3.RoomDatabase

actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<MusicDatabase> {
        return Room.databaseBuilder(
            name = "music_database.db",
            factory = { MusicDatabaseConstructor.initialize() }
        )
    }
}