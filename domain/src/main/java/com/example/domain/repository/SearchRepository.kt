package com.example.domain.repository

import androidx.paging.PagingData
import com.example.domain.entity.search.SearchItemDomain
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun search(query: String, pageSize: Int): Flow<PagingData<SearchItemDomain>>
    fun searchYoutubeFromApi(query: String, pageSize: Int): Flow<PagingData<SearchItemDomain>>
    fun searchYoutubeFromYtDlp(query: String, pageSize: Int): Flow<PagingData<SearchItemDomain>>
    suspend fun playYoutube(youtubeId: String): Result<Unit>
}
