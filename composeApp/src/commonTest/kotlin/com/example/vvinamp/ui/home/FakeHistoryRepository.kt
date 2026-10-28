package com.example.vvinamp.ui.home

import com.example.domain.entity.ListeningHistoryDomain
import com.example.domain.repository.HistoryRepository

class FakeHistoryRepository : HistoryRepository {

    var resultToReturn: Result<List<ListeningHistoryDomain>> = Result.success(emptyList())
    var callCount: Int = 0

    override suspend fun listRecentHistory(): Result<List<ListeningHistoryDomain>> {
        callCount++
        return resultToReturn
    }
}
