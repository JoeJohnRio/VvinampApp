package com.example.data.db.music

import androidx.room3.RoomDatabase

expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<MusicDatabase>
}
