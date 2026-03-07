package com.example.domain.repository

import androidx.paging.PagingData
import com.example.domain.entity.search.SearchItemDomain
import kotlinx.coroutines.flow.Flow

class PlaylistRepository {
    fun search(pageSize: Int): Flow<PagingData<SearchItemDomain>>

    fun search(query: String, pageSize: Int): Flow<PagingData<SearchItemDomain>>
}