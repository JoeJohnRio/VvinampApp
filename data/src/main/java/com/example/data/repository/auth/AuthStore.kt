package com.example.data.repository.auth

import com.example.domain.entity.AuthSession
import kotlinx.coroutines.flow.Flow

interface AuthStore {
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun getExpiresAt(): String?
    suspend fun clear()
    fun getSession(): Flow<AuthSession?>
    suspend fun saveTokens(access: String, refresh: String?, expiresAt: String)
    suspend fun saveUser(userId: String, username: String, email: String, profilePicture: String?)
}
