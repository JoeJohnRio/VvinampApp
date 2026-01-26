package com.example.data.repository.auth

import com.example.domain.entity.AuthSession
import kotlinx.coroutines.flow.Flow

interface AuthStore {
    suspend fun saveTokens(access: String, refresh: String?)
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun clear()
    fun getSession(): Flow<AuthSession?>
}
