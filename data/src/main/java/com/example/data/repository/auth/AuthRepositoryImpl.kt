package com.example.data.repository.auth

import com.example.data.api.AuthApi
import com.example.data.util.toDomain
import com.example.domain.entity.RegisteredUser
import com.example.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi
) : AuthRepository {
    override suspend fun register(): Result<RegisteredUser> {
        return authApi.register().map {
            it.toDomain()
        }
    }
}