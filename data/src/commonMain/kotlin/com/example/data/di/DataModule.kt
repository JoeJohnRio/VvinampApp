package com.example.data.di

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
import com.example.data.api.playlist.PlaylistApi
import com.example.data.api.playlist.PlaylistApiImpl
import com.example.data.api.search.SearchApi
import com.example.data.api.search.SearchApiImpl
import com.example.data.datasource.local.DataStoreAuthStore
import com.example.data.repository.album.AlbumRepositoryImpl
import com.example.data.repository.artist.ArtistRepositoryImpl
import com.example.data.repository.auth.AuthRepositoryImpl
import com.example.data.repository.auth.AuthStore
import com.example.data.repository.history.HistoryRepositoryImpl
import com.example.data.repository.playback.PlaybackRepositoryImpl
import com.example.data.repository.playlist.PlaylistRepositoryImpl
import com.example.data.repository.search.SearchRepositoryImpl
import com.example.domain.repository.AlbumRepository
import com.example.domain.repository.ArtistRepository
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.HistoryRepository
import com.example.domain.repository.PlaybackRepository
import com.example.domain.repository.PlaylistRepository
import com.example.domain.repository.SearchRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }
    singleOf(::AlbumRepositoryImpl) { bind<AlbumRepository>() }
    singleOf(::ArtistRepositoryImpl) { bind<ArtistRepository>() }
    singleOf(::HistoryRepositoryImpl) { bind<HistoryRepository>() }
    singleOf(::PlaybackRepositoryImpl) { bind<PlaybackRepository>() }
    singleOf(::PlaylistRepositoryImpl) { bind<PlaylistRepository>() }
    singleOf(::SearchRepositoryImpl) { bind<SearchRepository>() }
}

val apiModule = module {
    singleOf(::AuthApiImpl) { bind<AuthApi>() }
    singleOf(::AlbumApiImpl) { bind<AlbumApi>() }
    singleOf(::ArtistApiImpl) { bind<ArtistApi>() }
    singleOf(::HistoryApiImpl) { bind<HistoryApi>() }
    singleOf(::PlaybackApiImpl) { bind<PlaybackApi>() }
    singleOf(::PlaylistApiImpl) { bind<PlaylistApi>() }
    singleOf(::SearchApiImpl) { bind<SearchApi>() }
}

val localModule = module {
    single { DataStoreAuthStore(get()) }
    single<AuthStore> { get<DataStoreAuthStore>() }
}

val dataModule = module {
    includes(repositoryModule, apiModule, localModule, networkModule, databaseModule)
}
