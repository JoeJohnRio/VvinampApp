package com.example.vvinamp.di

import android.content.Context
import com.example.vvinamp.util.AndroidAppSettings
import com.example.vvinamp.util.AppSettings
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single(named("SharedPreferences")) {
        androidContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
    }
    single<AppSettings>(named("AppSettings")) {
        AndroidAppSettings(get(named("SharedPreferences")))
    }
}
