package com.example.data.repository.auth

import com.example.data.api.auth.AuthApi
import com.example.data.util.toDomain
import com.example.domain.entity.AuthSession
import com.example.domain.entity.RegisteredUser
import com.example.domain.entity.user.User
import com.example.domain.repository.AuthRepository
class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val authStore: AuthStore
) : AuthRepository {
    override suspend fun register(user: User): Result<RegisteredUser> {
        return authApi.register(user).map {
            it.toDomain()
        }
    }

    override suspend fun login(email: String, password: String): Result<AuthSession> {
        val result = authApi.login(email, password).map {
            it.toDomain()
        }
        result.getOrNull()?.let { session ->
            authStore.saveTokens(session.accessToken, session.refreshToken, session.expiresAt)
            getCurrentUserDetail().getOrNull()?.let { user ->
                authStore.saveUser(
                    userId = user.id,
                    username = user.username,
                    email = user.email,
                    profilePicture = user.profilePicture
                )
            }
        }
        return result
    }

    override suspend fun logout() {
        authStore.clear()
    }

    override suspend fun getCurrentUserDetail(): Result<RegisteredUser> {
        return authApi.getCurrentUserDetail().map {
            it.toDomain()
        }
    }
}
