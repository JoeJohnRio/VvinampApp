package com.example.data.api.auth

import com.apollographql.apollo.ApolloClient
import com.example.data.util.safeMutation
import com.example.data.util.safeQuery
import com.example.domain.entity.User
import com.example.graphql.LoginQuery
import com.example.graphql.RegisterMutation
import com.example.graphql.type.RegisterInput
import javax.inject.Inject


class AuthApiImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : AuthApi {

    override suspend fun register(user: User): Result<RegisterMutation.Register> {
        return apolloClient.safeMutation(
            RegisterMutation(
                RegisterInput(
                    profilePicture = user.profilePicture,
                    email = user.email,
                    password = user.password,
                    name = user.name,
                    //joel123 not implemented yet
                    roles = listOf("Normal")
                )

            )
        ) { data ->
            data.register
        }
    }

    override suspend fun login(email: String, password: String): Result<LoginQuery.Login> {
        return apolloClient.safeQuery(
            LoginQuery(
                email = email,
                password = password
            )
        ) { data ->
            data.login
        }
    }
}