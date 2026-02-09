package com.example.presentation.di.module

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import com.example.data.repository.history.HistoryRepositoryImpl
import com.example.data.repository.playback.PlaybackRepositoryImpl
import com.example.domain.repository.HistoryRepository
import com.example.domain.repository.PlaybackRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class PlaybackModule {
    @Binds
    @Singleton
    abstract fun bindPlaybackRepository(
        impl: PlaybackRepositoryImpl
    ): PlaybackRepository
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object PlayerModule {
    @Provides
    @ActivityRetainedScoped
    fun provideExoPlayer(@ApplicationContext context: Context): ExoPlayer =
        ExoPlayer.Builder(context).build()
}