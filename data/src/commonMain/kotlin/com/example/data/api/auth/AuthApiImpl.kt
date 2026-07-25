package com.example.data.api.auth

import com.apollographql.apollo.ApolloClient
import com.example.data.util.safeMutation
import com.example.data.util.safeQuery
import com.example.domain.entity.user.User
import com.example.graphql.GetCurrentUserDetailQuery
import com.example.graphql.LoginQuery
import com.example.graphql.RegisterMutation
import com.example.graphql.type.RegisterInput
import kotlinx.coroutines.withTimeout

class AuthApiImpl(
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
                    //joel456 not implemented yet
                    roles = listOf("Normal")
                )

            )
        ) { data ->
            data.register
        }
    }

    override suspend fun login(email: String, password: String): Result<LoginQuery.Login> {

        println("joel123 Before execute")
//        val response = withTimeout(5000) {
//            apolloClient
//                .query(LoginQuery(email, password))
//                .execute()
//        }
//
//        println("joel123 After execute")
//        println("joel123 response $response")
        println("joel123 LoginApiImpl")
        return apolloClient.safeQuery(
            LoginQuery(
                email = email,
                password = password
            )
        ) { data ->
            data.login
        }
    }

    override suspend fun getCurrentUserDetail(): Result<GetCurrentUserDetailQuery.GetCurrentUserDetail> {
        return apolloClient.safeQuery(
            GetCurrentUserDetailQuery()
        ) { data ->
            data.getCurrentUserDetail
        }
    }
}
