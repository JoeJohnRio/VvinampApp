package com.example.data.repository.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.api.search.SearchApiImpl
import com.example.data.repository.pagination.SearchPagingSource
import com.example.domain.entity.search.SearchItemDomain
import com.example.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchApi: SearchApiImpl
) : SearchRepository {
    override fun search(query: String): Flow<PagingData<SearchItemDomain>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SearchPagingSource(searchApi, query)
            }
        ).flow
    }

}