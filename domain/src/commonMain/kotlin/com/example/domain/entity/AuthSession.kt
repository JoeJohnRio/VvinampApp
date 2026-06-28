package com.example.domain.entity

data class AuthSession(
    val accessToken: String,
    val refreshToken: String?,
    val expiresAt: String,
    val userId: String? = null,
    val username: String? = null,
    val profilePicture: String? = null,
    val email: String? = null
)
