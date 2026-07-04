package com.example.data.api.auth

import com.example.data.repository.auth.AuthStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
class AuthInterceptor(
    private val authStore: AuthStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = runBlocking { authStore.getAccessToken() }

        return if (token != null) {
            val authenticated = original.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
            chain.proceed(authenticated)
        } else {
            chain.proceed(original)
        }
    }
}