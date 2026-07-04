package com.example.data.repository.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.api.search.SearchApi
import com.example.data.api.search.toDomain
import com.example.data.repository.pagination.GenericPagingSource
import com.example.domain.entity.PageResponse
import com.example.domain.entity.search.SearchItemDomain
import com.example.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class SearchRepositoryImpl(private val api: SearchApi) : SearchRepository {
    override fun search(query: String, pageSize: Int): Flow<PagingData<SearchItemDomain>> {
        return Pager(PagingConfig(pageSize = pageSize)) {
            GenericPagingSource { cursor ->
                api.search(query, cursor, pageSize).map { searchData ->
                    PageResponse(
                        items = searchData.edges.mapNotNull { it.node.toDomain() },
                        endCursor = searchData.pageInfo.endCursor,
                        hasNextPage = searchData.pageInfo.hasNextPage
                    )
                }
            }
        }.flow
    }

    override fun searchYoutubeFromApi(query: String, pageSize: Int): Flow<PagingData<SearchItemDomain>> {
        return Pager(PagingConfig(pageSize = 5)) {
            GenericPagingSource { cursor ->
                api.searchYoutubeFromApi(query, cursor, 5).map { searchData ->
                    PageResponse(
                        items = searchData.edges.map { it.node.toDomain() },
                        endCursor = searchData.pageInfo.endCursor,
                        hasNextPage = searchData.pageInfo.hasNextPage
                    )
                }
            }
        }.flow
    }

    override fun searchYoutubeFromYtDlp(query: String, pageSize: Int): Flow<PagingData<SearchItemDomain>> {
        return Pager(PagingConfig(pageSize = 5)) {
            GenericPagingSource { cursor ->
                api.searchYoutubeFromYtDlp(query, cursor, 5).map { searchData ->
                    PageResponse(
                        items = searchData.edges.map { it.node.toDomain() },
                        endCursor = searchData.pageInfo.endCursor,
                        hasNextPage = searchData.pageInfo.hasNextPage
                    )
                }
            }
        }.flow
    }

    override suspend fun playYoutube(youtubeId: String): Result<Unit> {
        return api.playYoutube(youtubeId).map { }
    }
}
