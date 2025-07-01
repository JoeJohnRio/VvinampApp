package com.example.data.util

import com.example.domain.entity.RegisteredUser
import com.example.graphql.RegisterMutation

fun RegisterMutation.Register.toDomain(): RegisteredUser {
    return RegisteredUser(
        id = user.id,
        username = user.username,
        email = user.email
    )
}