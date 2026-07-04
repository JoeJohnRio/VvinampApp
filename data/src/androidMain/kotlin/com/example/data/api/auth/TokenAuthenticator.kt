package com.example.data.api.auth

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.example.data.repository.auth.AuthStore
import com.example.graphql.RefreshTokenMutation
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
class TokenAuthenticator(
    private val authStore: AuthStore,
    private val apolloClient: ApolloClient
) : Authenticator {

    private val mutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? {

        if (response.request.header("Token-Refreshed") != null) {
            return null
        }

        val newToken = runBlocking {
            mutex.withLock {
                val currentToken = authStore.getAccessToken()
                val requestToken = response.request.header("Authorization")?.removePrefix("Bearer ")

                if (currentToken != requestToken) {
                    return@withLock currentToken
                }

                refreshTokens()
            }
        }

        return newToken?.let {
            response.request.newBuilder()
                .header("Authorization", "Bearer $it")
                .header("Token-Refreshed", "true")
                .build()
        }
    }

    private suspend fun refreshTokens(): String? {
        val refreshToken = authStore.getRefreshToken() ?: return null

        return try {
            val response = apolloClient.mutation(RefreshTokenMutation(refreshToken)).execute()

            response.data?.let {
                authStore.saveTokens(
                    it.refreshToken.session.accessToken,
                    it.refreshToken.session.refreshToken,
                    it.refreshToken.session.expiresAt
                )
                it.refreshToken.session.accessToken
            }
        } catch (e: Exception) {
            authStore.clear()
            null
        }
    }
}
