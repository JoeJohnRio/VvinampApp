package com.example.data.api

import com.example.graphql.RegisterMutation

interface AuthApi {
    suspend fun register(): Result<RegisterMutation.Register>
}