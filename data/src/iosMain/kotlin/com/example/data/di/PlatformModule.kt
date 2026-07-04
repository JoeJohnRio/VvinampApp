package com.example.data.di

import com.example.data.datasource.local.DataStoreFactory
import com.example.data.db.music.DatabaseFactory
import org.koin.dsl.module

actual val platformModule = module {
    single { DataStoreFactory().create() }
    single { DatabaseFactory() }
}
