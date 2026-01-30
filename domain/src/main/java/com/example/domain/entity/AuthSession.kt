package com.example.domain.entity

data class AuthSession(
    val accessToken: String,
    val refreshToken: String?,
    val expiresAt: String
)
