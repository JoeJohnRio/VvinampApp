package com.example.data.util

import com.example.domain.entity.AuthSession
import com.example.domain.entity.RegisteredUser
import com.example.graphql.LoginQuery
import com.example.graphql.RegisterMutation


fun RegisterMutation.Register.toDomain(): RegisteredUser {
    return RegisteredUser(
        id = user.id,
        username = user.username,
        email = user.email
    )
}

fun LoginQuery.Login.toDomain(): AuthSession {
    return AuthSession(
        accessToken = session.accessToken,
        refreshToken = session.refreshToken,
        expiresAt = session.expiresAt
    )
}
