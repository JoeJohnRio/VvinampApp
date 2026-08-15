package com.example.vvinamp.di

import com.example.vvinamp.ui.main.playback.PlaybackViewModel
import com.example.vvinamp.util.AppSettings
import com.example.vvinamp.util.WasmAppSettings
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val platformUiModule = module {
    single<AppSettings>(named("AppSettings")) { WasmAppSettings() }
    viewModelOf(::PlaybackViewModel)
}
