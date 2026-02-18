package com.example.data.api.search

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.example.data.util.safeQuery
import com.example.graphql.SearchQuery
import javax.inject.Inject

class SearchApiImpl @Inject constructor(
    private val apolloClient: ApolloClient
): SearchApi {

    override suspend fun search(
        query: String,
        cursorAfter: String?
    ): Result<SearchQuery.Search> {
        return apolloClient.safeQuery(
            SearchQuery(
                query = Optional.presentIfNotNull(query),
                after = Optional.presentIfNotNull(cursorAfter)
            )
        ) {
            it.search
        }
    }
}