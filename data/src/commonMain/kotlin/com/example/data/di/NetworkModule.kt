package com.example.data.di

import com.apollographql.apollo.ApolloClient
import com.example.data.api.auth.AuthHttpInterceptor
import org.koin.dsl.module

val networkModule = module {
    single { AuthHttpInterceptor(get()) }
    
    single {
        ApolloClient.Builder()
            .serverUrl("http://10.0.2.2:4000/graphql") // Default local dev URL, should be configurable
            .addHttpInterceptor(get<AuthHttpInterceptor>())
            .build()
    }
}
