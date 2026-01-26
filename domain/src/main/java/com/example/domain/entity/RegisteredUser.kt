package com.example.domain.entity

data class RegisteredUser (
    val id: String,
    val username: String,
    val email: String,
    val passwordHash: String? = null,
    val profilePicture: String? = null,
    val joinDate: Any? = null,
    val subscriptionType: String? = null,
    val lastLogin: Any? = null,
)