package com.example.data.api.auth

import com.example.domain.entity.User
import com.example.graphql.LoginQuery
import com.example.graphql.RegisterMutation

interface AuthApi {
    suspend fun register(user: User): Result<RegisterMutation.Register>
    suspend fun login(email: String, password: String): Result<LoginQuery.Login>
}