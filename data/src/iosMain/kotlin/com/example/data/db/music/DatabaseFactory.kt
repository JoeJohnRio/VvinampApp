package com.example.data.db.music

import androidx.room.Room
import androidx.room.RoomDatabase
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
