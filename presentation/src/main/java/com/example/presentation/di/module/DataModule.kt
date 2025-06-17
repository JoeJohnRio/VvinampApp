package com.example.presentation.di.module

import android.content.Context
import com.example.data.api.MusicApi
import com.example.data.db.music.MusicDao
import com.example.data.db.music.MusicRemoteKeyDao
import com.example.data.repository.music.MusicDataSource
import com.example.data.repository.music.MusicLocalDataSource
import com.example.data.repository.music.MusicRemoteDataSource
import com.example.data.repository.music.MusicRemoteMediator
import com.example.data.repository.music.MusicRepositoryImpl
import com.example.data.util.NetworkMonitorImpl
import com.example.domain.repository.MusicRepository
import com.example.domain.util.NetworkMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideMusicRepository(
        remote: MusicDataSource.Remote,
        local: MusicDataSource.Local,
        remoteMediator: MusicRemoteMediator
    ): MusicRepository {
        return MusicRepositoryImpl(
            remote = remote,
            local = local,
            remoteMediator = remoteMediator
        )
    }


    @Provides
    @Singleton
    fun provideMovieMediator(
        movieLocalDataSource: MusicDataSource.Local,
        movieRemoteDataSource: MusicDataSource.Remote
    ): MusicRemoteMediator {
        return MusicRemoteMediator(movieLocalDataSource, movieRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideMovieRemoteDataSource(movieApi: MusicApi): MusicDataSource.Remote {
        return MusicRemoteDataSource(movieApi)
    }

    @Provides
    @Singleton
    fun provideMovieLocalDataSource(movieApi: MusicDao, movieRemoteKeyDao: MusicRemoteKeyDao): MusicDataSource.Local {
        return MusicLocalDataSource(
            musicDao = movieApi,
            remoteKeyDao = movieRemoteKeyDao
        )
    }

    @Provides
    fun provideNetworkMonitor(
        @ApplicationContext context: Context
    ): NetworkMonitor = NetworkMonitorImpl(context)

}