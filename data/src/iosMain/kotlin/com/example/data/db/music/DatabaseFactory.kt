package com.example.data.db.music

import androidx.room3.Room
import androidx.room3.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class DatabaseFactory {
    @OptIn(ExperimentalForeignApi::class)
    actual fun create(): RoomDatabase.Builder<MusicDatabase> {
        val directory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        val dbFile = (directory?.path ?: "") + "/music_database.db"
        return Room.databaseBuilder<MusicDatabase>(
            name = dbFile,
            factory = { MusicDatabaseConstructor.initialize() }
        ).fallbackToDestructiveMigration(true)
    }
}
