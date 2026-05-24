package com.example.domain.repository

import com.example.domain.entity.AuthSession
import com.example.domain.entity.RegisteredUser
import com.example.domain.entity.user.User

interface AuthRepository {
    suspend fun register(user: User): Result<RegisteredUser>
    suspend fun login(email: String, password: String): Result<AuthSession>
    suspend fun logout()
    suspend fun getCurrentUserDetail(): Result<RegisteredUser>
}
