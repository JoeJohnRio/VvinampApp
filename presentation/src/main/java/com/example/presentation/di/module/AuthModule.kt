package com.example.presentation.di.module

import com.example.data.repository.artist.ArtistRepositoryImpl
import com.example.data.repository.auth.AuthRepositoryImpl
import com.example.data.repository.history.HistoryRepositoryImpl
import com.example.domain.repository.ArtistRepository
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.HistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository
}
