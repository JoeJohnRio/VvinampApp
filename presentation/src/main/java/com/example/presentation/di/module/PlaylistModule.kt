package com.example.presentation.di.module

import com.example.data.repository.playback.PlaybackRepositoryImpl
import com.example.data.repository.playlist.PlaylistRepositoryImpl
import com.example.domain.repository.PlaybackRepository
import com.example.domain.repository.PlaylistRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PlaylistModule {
    @Binds
    @Singleton
    abstract fun bindPlaybackRepository(
        impl: PlaylistRepositoryImpl
    ): PlaylistRepository
}