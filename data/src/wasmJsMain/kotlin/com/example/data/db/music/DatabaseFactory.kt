package com.example.data.db.music

import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.web.WebWorkerSQLiteDriver
import org.w3c.dom.Worker
actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<MusicDatabase> {
        println("Creating database with WebWorkerSQLiteDriver...")
        return Room.databaseBuilder<MusicDatabase>(
            name = "music_database.db",
            factory = { MusicDatabaseConstructor.initialize() }
        )
            .setDriver(
                WebWorkerSQLiteDriver(
                    createWorker().also { println("Worker created successfully") }
                )
            )
            .fallbackToDestructiveMigration(true)
    }
}

//fun createWorker() =
//    Worker(js("""new URL("sqlite-web-worker/worker.js", import.meta.url)"""))
@JsFun("""() => new Worker(new URL("sqlite-web-worker/worker.js", import.meta.url), { type: "module" })""")
private external fun createWorker(): Worker