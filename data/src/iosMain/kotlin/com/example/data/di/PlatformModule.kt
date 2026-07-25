package com.example.data.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import com.example.data.datasource.local.DataStoreFactory
import com.example.data.db.music.DatabaseFactory
import org.koin.dsl.module

actual val platformModule = module {
    single { DataStoreFactory().create() }
    single { DatabaseFactory() }
    single<HttpClientEngine> { Darwin.create() }
}
