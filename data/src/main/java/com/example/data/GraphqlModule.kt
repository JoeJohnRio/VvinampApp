package com.example.data

import com.example.data.api.AuthApi
import com.example.data.api.AuthApiImpl
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
}
