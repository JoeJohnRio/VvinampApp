package com.example.presentation.di.module

import com.example.data.repository.album.AlbumRepositoryImpl
import com.example.domain.repository.AlbumRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AlbumModule {
    @Binds
    @Singleton
    abstract fun bindAlbumRepository(
        impl: AlbumRepositoryImpl
    ): AlbumRepository
}