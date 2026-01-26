package com.example.data.repository.auth

import android.util.Log
import com.example.data.api.auth.AuthApi
import com.example.data.util.toDomain
import com.example.domain.entity.AuthSession
import com.example.domain.entity.RegisteredUser
import com.example.domain.entity.User
import com.example.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val authStore: AuthStore
) : AuthRepository {
    override suspend fun register(user: User): Result<RegisteredUser> {
        return authApi.register(user).map {
            it.toDomain()
        }
    }

    override suspend fun login(email: String, password: String): Result<AuthSession> {
        return authApi.login(email, password).map {
            it.toDomain()
        }
    }

    override suspend fun logout() {
        authStore.clear() // Just call your existing clear function
        var x = authStore.getAccessToken()

        Log.d("joel123", x.toString())
    }

}