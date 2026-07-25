package com.example.data.di

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.data.datasource.local.DataStoreFactory
import com.example.data.db.music.DatabaseFactory
import com.example.data.util.NetworkMonitorImpl
import com.example.domain.util.NetworkMonitor
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

actual val platformModule = module {
    single { DataStoreFactory(get()).create() }
    single { DatabaseFactory(get()) }
    single<NetworkMonitor> { NetworkMonitorImpl(get()) }

    single {
        ChuckerInterceptor.Builder(androidContext())
            .collector(ChuckerCollector(androidContext()))
            .build()
    }

    single<HttpClientEngine> {
        OkHttp.create {
            config {
                connectTimeout(30, TimeUnit.SECONDS)
                readTimeout(60, TimeUnit.SECONDS)
                writeTimeout(30, TimeUnit.SECONDS)
                addInterceptor(get<ChuckerInterceptor>())
                addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            }
        }
    }
}
