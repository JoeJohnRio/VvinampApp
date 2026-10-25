package com.example.vvinamp.ui.search

import androidx.paging.PagingData
import com.example.domain.entity.search.SearchItemDomain
import com.example.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeSearchRepository : SearchRepository {
    var lastSearchQuery: String? = null
    var lastSearchPageSize: Int? = null
    var searchCallCount: Int = 0

    var lastYtApiQuery: String? = null
    var lastYtApiPageSize: Int? = null
    var ytApiCallCount: Int = 0

    var lastYtDlpQuery: String? = null
    var lastYtDlpPageSize: Int? = null
    var ytDlpCallCount: Int = 0

    var searchFlowToReturn: Flow<PagingData<SearchItemDomain>> = flowOf(PagingData.empty())
    var ytApiFlowToReturn: Flow<PagingData<SearchItemDomain>> = flowOf(PagingData.empty())
    var ytDlpFlowToReturn: Flow<PagingData<SearchItemDomain>> = flowOf(PagingData.empty())

    override fun search(query: String, pageSize: Int): Flow<PagingData<SearchItemDomain>> {
        lastSearchQuery = query
        lastSearchPageSize = pageSize
        searchCallCount++
        return searchFlowToReturn
    }

    override fun searchYoutubeFromApi(query: String, pageSize: Int): Flow<PagingData<SearchItemDomain>> {
        lastYtApiQuery = query
        lastYtApiPageSize = pageSize
        ytApiCallCount++
        return ytApiFlowToReturn
    }

    override fun searchYoutubeFromYtDlp(query: String, pageSize: Int): Flow<PagingData<SearchItemDomain>> {
        lastYtDlpQuery = query
        lastYtDlpPageSize = pageSize
        ytDlpCallCount++
        return ytDlpFlowToReturn
    }

    override suspend fun playYoutube(youtubeId: String): Result<Unit> {
        return Result.success(Unit)
    }
}
