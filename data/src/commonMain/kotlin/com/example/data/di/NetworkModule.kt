package com.example.data.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.ktor.http.KtorHttpEngine
import com.example.data.BuildConfig
import com.example.data.BuildKonfig
import com.example.data.repository.auth.AuthStore
import com.example.graphql.RefreshTokenMutation
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        }
    }

    single {
        val authStore = get<AuthStore>()

        // We need a way to refresh tokens using Apollo, but Apollo will use THIS HttpClient.
        // To avoid circular dependency, we can provide the HttpClient, and then
        // the Auth plugin will use a separate mechanism or we lazy-load the ApolloClient.

        HttpClient(get()) {
            install(ContentNegotiation) {
                json(get())
            }

            install(Logging) {
                level = LogLevel.ALL
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val accessToken = authStore.getAccessToken()
                        val refreshToken = authStore.getRefreshToken()
                        if (accessToken != null) {
                            BearerTokens(accessToken, refreshToken ?: "")
                        } else {
                            null
                        }
                    }

                    refreshTokens {
                        println(">>> joel123 refreshTokens")
                        // Use a basic HttpClient or a specialized call to refresh token 
                        // to avoid circular dependency with the main ApolloClient if it uses this HttpClient.
                        // However, Apollo RefreshTokenMutation is GraphQL.
                        
                        // Option: Use a separate simple HttpClient for refresh, or just call the endpoint directly.
                        // For simplicity in this migration, we'll assume the AuthStore might have a refresh method
                        // or we use the provided apolloClient lazily.
                        
                        val refreshToken = authStore.getRefreshToken() ?: return@refreshTokens null
                        
                        // We can't easily use the 'apolloClient' here if it's not yet created or depends on this.
                        // But in Koin, we can use get() inside the block.
                        val apolloClient = get<ApolloClient>() 

                        try {
                            val response = apolloClient.mutation(RefreshTokenMutation(refreshToken)).execute()
                            val session = response.data?.refreshToken?.session
                            if (session != null) {
                                authStore.saveTokens(
                                    session.accessToken,
                                    session.refreshToken,
                                    session.expiresAt
                                )
                                BearerTokens(session.accessToken, session.refreshToken ?: "")
                            } else {
                                authStore.clear()
                                null
                            }
                        } catch (e: Exception) {
                            authStore.clear()
                            null
                        }
                    }
                }
            }
        }
    }

    single {
        ApolloClient.Builder()
            .serverUrl(BuildKonfig.BASE_URL + BuildKonfig.BASE_URL_GRAPHQL)
            .httpEngine(KtorHttpEngine(get<HttpClient>()))
            .build()
    }
}
