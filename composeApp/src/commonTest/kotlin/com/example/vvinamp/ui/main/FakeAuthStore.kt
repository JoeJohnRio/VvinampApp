package com.example.vvinamp.ui.main

import com.example.data.repository.auth.AuthStore
import com.example.domain.entity.AuthSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeAuthStore(
    initialSession: AuthSession? = null
) : AuthStore {

    val sessionFlow = MutableStateFlow<AuthSession?>(initialSession)
    var clearCallCount: Int = 0

    override fun getSession(): Flow<AuthSession?> = sessionFlow

    override suspend fun getAccessToken(): String? = sessionFlow.value?.accessToken

    override suspend fun getRefreshToken(): String? = sessionFlow.value?.refreshToken

    override suspend fun getExpiresAt(): String? = sessionFlow.value?.expiresAt

    override suspend fun clear() {
        clearCallCount++
        sessionFlow.value = null
    }

    override suspend fun saveTokens(access: String, refresh: String?, expiresAt: String) {
        val current = sessionFlow.value
        sessionFlow.value = AuthSession(
            accessToken = access,
            refreshToken = refresh,
            expiresAt = expiresAt,
            userId = current?.userId,
            username = current?.username,
            profilePicture = current?.profilePicture,
            email = current?.email
        )
    }

    override suspend fun saveUser(
        userId: String,
        username: String,
        email: String,
        profilePicture: String?
    ) {
        val current = sessionFlow.value
        sessionFlow.value = current?.copy(
            userId = userId,
            username = username,
            email = email,
            profilePicture = profilePicture
        )
    }
}
