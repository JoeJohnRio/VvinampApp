package com.example.data.di

import com.example.data.db.music.DatabaseFactory
import com.example.data.db.music.MusicDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { get<DatabaseFactory>().create().build() }
    single { get<MusicDatabase>().playlistTrackDao() }
    single { get<MusicDatabase>().playlistRemoteKeyDao() }
    single { get<MusicDatabase>().recentPlaybackDao() }
}
