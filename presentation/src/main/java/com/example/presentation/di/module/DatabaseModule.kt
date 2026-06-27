package com.example.presentation.di.module

import android.content.Context
import androidx.room.Room
import com.example.data.db.music.MusicDatabase
import com.example.data.db.playback.RecentPlaybackDao
import com.example.data.db.playlist.PlaylistRemoteKeyDao
import com.example.data.db.playlist.PlaylistTrackDao
import com.example.data.util.DiskExecutor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideMusicDatabase(
        @ApplicationContext context: Context,
        diskExecutor: DiskExecutor
    ): MusicDatabase {
        return Room
            .databaseBuilder(context, MusicDatabase::class.java, "vvinamp.db")
            .setQueryExecutor(diskExecutor)
            .setTransactionExecutor(diskExecutor)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providePlaylistTrackDao(database: MusicDatabase): PlaylistTrackDao {
        return database.playlistTrackDao()
    }

    @Provides
    fun providePlaylistRemoteKeyDao(database: MusicDatabase): PlaylistRemoteKeyDao {
        return database.playlistRemoteKeyDao()
    }

    @Provides
    fun provideRecentPlaybackDao(database: MusicDatabase): RecentPlaybackDao {
        return database.recentPlaybackDao()
    }
}
