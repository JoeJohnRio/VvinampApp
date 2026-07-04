package com.example.data.di

import com.example.domain.di.useCaseModule
import org.koin.dsl.module

val sharedModule = module {
    includes(dataModule, useCaseModule, platformModule)
}
