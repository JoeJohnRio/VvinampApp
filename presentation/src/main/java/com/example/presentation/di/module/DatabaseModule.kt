package com.example.youtubemusicclone.di.module

import android.content.Context
import androidx.room.Room
import com.example.data.db.music.MusicDao
import com.example.data.db.music.MusicDatabase
import com.example.data.db.music.MusicRemoteKeyDao
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
    fun provideMovieDatabase(
        @ApplicationContext context: Context,
        diskExecutor: DiskExecutor
    ): MusicDatabase {
        return Room
            .databaseBuilder(context, MusicDatabase::class.java, "movie.db")
            .setQueryExecutor(diskExecutor)
            .setTransactionExecutor(diskExecutor)
            .build()
    }

    @Provides
    fun provideMovieDao(movieDatabase: MusicDatabase): MusicDao {
        return movieDatabase.movieDao()
    }

    @Provides
    fun provideMovieRemoteKeyDao(movieDatabase: MusicDatabase): MusicRemoteKeyDao {
        return movieDatabase.movieRemoteKeysDao()
    }
}