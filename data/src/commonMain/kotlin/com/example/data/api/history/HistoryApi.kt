package com.example.data.api.history

import com.example.graphql.ListRecentHistoryQuery

interface HistoryApi {
    suspend fun listRecentHistory(): Result<List<ListRecentHistoryQuery.ListRecentHistory?>>
}