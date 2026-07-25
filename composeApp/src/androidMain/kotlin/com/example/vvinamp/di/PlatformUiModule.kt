package com.example.vvinamp.di

import org.koin.dsl.module

actual val platformUiModule = module {
    includes(androidViewModelModule, appModule)
}
