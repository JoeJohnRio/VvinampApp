package com.example.data.util

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Mutation
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.exception.ApolloGraphQLException
import com.apollographql.apollo.exception.DefaultApolloException
import com.example.graphql.RegisterMutation

suspend fun <D : Mutation.Data, T> ApolloClient.safeMutation(
    mutation: Mutation<D>,
    extract: (D) -> T?
): Result<T> {
    return try {
        val response = this.mutation(mutation).execute()

        when {
            response.hasErrors() -> Result.failure(
                ApolloGraphQLException(response.errors.orEmpty())
            )

            response.data == null -> Result.failure(
                ApolloGraphQLException(response.errors.orEmpty())
            )

            else -> {
                val extracted = extract(response.data!!)
                if (extracted != null) {
                    Result.success(extracted)
                } else {
                    Result.failure(DefaultApolloException(message = "Failed to extract data"))
                }
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

suspend fun <D : Query.Data, T> ApolloClient.safeQuery(
    query: Query<D>,
    extract: (D) -> T?
): Result<T> {
    return try {
        val response = this.query(query).execute()

        when {
            response.hasErrors() -> Result.failure(
                ApolloGraphQLException(response.errors.orEmpty())
            )

            response.data == null -> Result.failure(
                ApolloGraphQLException(response.errors.orEmpty())
            )

            else -> {
                val extracted = extract(response.data!!)
                if (extracted != null) {
                    Result.success(extracted)
                } else {
                    Result.failure(DefaultApolloException(message = "Failed to extract data"))
                }
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
