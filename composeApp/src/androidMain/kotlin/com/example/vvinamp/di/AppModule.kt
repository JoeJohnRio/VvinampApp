package com.example.vvinamp.di

import android.content.Context
import com.example.data.util.DiskExecutor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single(named("AppSettings")) {
        androidContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
    }

    single { DiskExecutor() }
}
