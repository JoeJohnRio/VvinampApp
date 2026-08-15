package com.example.data.di

import com.example.data.datasource.local.DataStoreFactory
import com.example.data.db.music.DatabaseFactory
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js
import org.koin.dsl.module

actual val platformModule = module {
    single { DataStoreFactory().create() }
    single { DatabaseFactory() }
    single<HttpClientEngine> { Js.create() }
}
