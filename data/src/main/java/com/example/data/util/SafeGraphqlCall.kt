package com.example.data.util

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Query
import com.apollographql.apollo3.exception.ApolloException

suspend fun <D : Mutation.Data, T> ApolloClient.safeMutation(
    mutation: Mutation<D>,
    extract: (D) -> T?
): Result<T> {
    return try {
        val response = this.mutation(mutation).execute()

        when {
            response.hasErrors() -> Result.failure(
                ApolloException("GraphQL Error: ${response.errors?.firstOrNull()?.message}")
            )

            response.data == null -> Result.failure(
                ApolloException("Mutation returned null data")
            )

            else -> {
                val extracted = extract(response.data!!)
                if (extracted != null) {
                    Result.success(extracted)
                } else {
                    Result.failure(ApolloException("Failed to extract data"))
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
                ApolloException("GraphQL Error: ${response.errors?.firstOrNull()?.message}")
            )

            response.data == null -> Result.failure(
                ApolloException("Query returned null data")
            )

            else -> {
                val extracted = extract(response.data!!)
                if (extracted != null) {
                    Result.success(extracted)
                } else {
                    Result.failure(ApolloException("Failed to extract data"))
                }
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
