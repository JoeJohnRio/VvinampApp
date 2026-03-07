package com.example.data.repository.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.api.search.SearchApi
import com.example.data.api.search.SearchApiImpl
import com.example.domain.entity.PageResponse
import com.example.graphql.SearchQuery

class GenericPagingSource<Value : Any>(
    private val fetchPage: suspend (cursor: String?) -> Result<PageResponse<Value>>
) : PagingSource<String, Value>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Value> {
        return fetchPage(params.key).fold(
            onSuccess = { response ->
                LoadResult.Page(
                    data = response.items,
                    prevKey = null,
                    nextKey = if (response.hasNextPage) response.endCursor else null
                )
            },
            onFailure = { LoadResult.Error(it) }
        )
    }

    override fun getRefreshKey(state: PagingState<String, Value>): String? = null
}