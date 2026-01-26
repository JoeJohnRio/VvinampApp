package com.example.data

import com.example.data.api.auth.AuthApi
import com.example.data.api.auth.AuthApiImpl
import com.example.data.api.history.HistoryApi
import com.example.data.api.history.HistoryApiImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GraphqlModule {

    @Binds
    @Singleton
    abstract fun bindAuthApi(
        impl: AuthApiImpl
    ): AuthApi

    @Binds
    @Singleton
    abstract fun bindHistoryApi(
        impl: HistoryApiImpl
    ): HistoryApi
}
