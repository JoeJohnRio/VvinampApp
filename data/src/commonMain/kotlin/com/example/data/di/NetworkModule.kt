package com.example.data.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.ktor.http.KtorHttpEngine
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
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        }
    }

    single(named("unauthenticatedHttpClient")) {
        HttpClient(get()) {
            install(ContentNegotiation) {
                json(get())
            }
        }
    }

    single(named("unauthenticatedApolloClient")) {
        ApolloClient.Builder()
            .serverUrl(BuildKonfig.BASE_URL + BuildKonfig.BASE_URL_GRAPHQL)
            .httpEngine(KtorHttpEngine(get<HttpClient>(named("unauthenticatedHttpClient"))))
            .build()
    }

    single {
        val authStore = get<AuthStore>()
        val refreshApollo = get<ApolloClient>(named("unauthenticatedApolloClient"))

        HttpClient(get()) {
            install(ContentNegotiation) {
                json(get())
            }

            install(Logging) {
                level = LogLevel.NONE
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
                        val refreshToken = authStore.getRefreshToken() ?: return@refreshTokens null

                        try {
                            val response = refreshApollo.mutation(RefreshTokenMutation(refreshToken)).execute()
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
