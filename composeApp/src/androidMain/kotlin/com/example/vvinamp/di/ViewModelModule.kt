package com.example.vvinamp.di

import com.example.vvinamp.ui.main.playback.PlaybackViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val androidViewModelModule = module {
    singleOf(::PlaybackViewModel)
}
