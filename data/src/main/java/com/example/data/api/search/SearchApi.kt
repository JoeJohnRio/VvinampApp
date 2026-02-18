package com.example.data.api.search

import com.example.graphql.SearchQuery

interface SearchApi {

    suspend fun search(query: String, cursorAfter: String?): Result<SearchQuery.Search>
}