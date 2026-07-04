package com.example.vvinamp.di

import com.example.data.di.initKoin as initDataKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    initDataKoin(additionalModules = listOf(uiModule), appDeclaration = appDeclaration)

/**
 * Special initialization function for iOS that doesn't require a lambda
 */
fun initKoinIos() = initKoin {}
