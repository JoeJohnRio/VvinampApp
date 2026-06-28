package com.example.data.api.history

import com.apollographql.apollo.ApolloClient
import com.example.data.util.safeQuery
import com.example.graphql.ListRecentHistoryQuery
import javax.inject.Inject

class HistoryApiImpl @Inject constructor(
    private val apolloClient: ApolloClient
): HistoryApi {
    override suspend fun listRecentHistory(): Result<List<ListRecentHistoryQuery.ListRecentHistory?>> {
        return apolloClient.safeQuery(
            ListRecentHistoryQuery()
        ) {
            it.listRecentHistory
        }
    }
}