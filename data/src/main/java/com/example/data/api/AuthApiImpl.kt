package com.example.data.api

import com.apollographql.apollo3.ApolloClient
import com.example.data.util.safeMutation
import com.example.graphql.RegisterMutation
import javax.inject.Inject


class AuthApiImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : AuthApi {

    override suspend fun register(): Result<RegisterMutation.Register> {
        return apolloClient.safeMutation(RegisterMutation(

        )) { data ->
            data.register
        }
    }
}