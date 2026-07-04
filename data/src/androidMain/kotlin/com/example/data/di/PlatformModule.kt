package com.example.data.di

import com.example.data.datasource.local.DataStoreFactory
import com.example.data.db.music.DatabaseFactory
import com.example.data.util.NetworkMonitorImpl
import com.example.domain.util.NetworkMonitor
import org.koin.dsl.module

actual val platformModule = module {
    single { DataStoreFactory(get()).create() }
    single { DatabaseFactory(get()) }
    single<NetworkMonitor> { NetworkMonitorImpl(get()) }
    single { AuthInterceptor(get()) }
    single { TokenAuthenticator(get(), get()) }
}
