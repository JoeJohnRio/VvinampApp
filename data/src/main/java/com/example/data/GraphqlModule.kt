package com.example.data

import com.example.data.api.album.AlbumApi
import com.example.data.api.album.AlbumApiImpl
import com.example.data.api.artist.ArtistApi
import com.example.data.api.artist.ArtistApiImpl
import com.example.data.api.auth.AuthApi
import com.example.data.api.auth.AuthApiImpl
import com.example.data.api.history.HistoryApi
import com.example.data.api.history.HistoryApiImpl
import com.example.data.api.playback.PlaybackApi
import com.example.data.api.playback.PlaybackApiImpl
import com.example.data.api.search.SearchApi
import com.example.data.api.search.SearchApiImpl
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

    @Binds
    @Singleton
    abstract fun bindArtistApi(
        impl: ArtistApiImpl
    ): ArtistApi

    @Binds
    @Singleton
    abstract fun bindAlbumApi(
        impl: AlbumApiImpl
    ): AlbumApi

    @Binds
    @Singleton
    abstract fun bindPlaybackApi(
        impl: PlaybackApiImpl
    ): PlaybackApi

    @Binds
    @Singleton
    abstract fun bindSearchApi(
        impl: SearchApiImpl
    ): SearchApi
}
