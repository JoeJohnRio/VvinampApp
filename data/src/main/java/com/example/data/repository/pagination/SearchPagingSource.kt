package com.example.data.repository.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.api.search.SearchApiImpl
import com.example.data.api.search.toDomain
import com.example.domain.entity.search.SearchItemDomain

class SearchPagingSource(
    private val api: SearchApiImpl,
    private val query: String,
    private val pageSize: Int
) : PagingSource<String, SearchItemDomain>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, SearchItemDomain> {
        return api.search(query, params.key, pageSize).fold(
            onSuccess = { searchData ->
                val items = searchData.edges
                    .mapNotNull { it.node.toDomain() }

                LoadResult.Page(
                    data = items,
                    prevKey = null,
                    nextKey = if (searchData.pageInfo.hasNextPage) searchData.pageInfo.endCursor else null
                )
            },
            onFailure = { LoadResult.Error(it) }
        )
    }

    override fun getRefreshKey(state: PagingState<String, SearchItemDomain>): String? = null
}
