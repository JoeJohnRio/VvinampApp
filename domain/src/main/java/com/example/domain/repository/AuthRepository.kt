package com.example.domain.repository

import com.example.domain.entity.RegisteredUser

interface AuthRepository {
    suspend fun register(): Result<RegisteredUser>
}
