package com.example.data.db.music

import androidx.room3.Room
import androidx.room3.RoomDatabase
import platform.Foundation.NSHomeDirectory

actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<MusicDatabase> {
        val dbFile = NSHomeDirectory() + "/music_database.db"
        return Room.databaseBuilder<MusicDatabase>(
            name = dbFile,
            factory = { MusicDatabaseConstructor.initialize() }
        )
    }
}
