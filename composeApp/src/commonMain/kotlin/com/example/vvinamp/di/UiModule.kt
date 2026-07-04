package com.example.vvinamp.di

import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformUiModule: Module

val uiModule = module {
    includes(commonViewModelModule, platformUiModule)
}
