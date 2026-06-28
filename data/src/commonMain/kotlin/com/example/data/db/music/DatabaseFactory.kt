package com.example.data.db.music

import androidx.room.RoomDatabase

expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<MusicDatabase>
}
