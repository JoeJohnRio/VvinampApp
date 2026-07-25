package com.example.data.db.music

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual class DatabaseFactory(private val context: Context) {
    actual fun create(): RoomDatabase.Builder<MusicDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath("music_database.db")
        return Room.databaseBuilder<MusicDatabase>(
            context = appContext,
            name = dbFile.absolutePath,
            factory = { MusicDatabaseConstructor.initialize() }
        )
    }
}
