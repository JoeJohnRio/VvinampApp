package com.example.data.repository.history

import com.example.data.api.history.HistoryApi
import com.example.data.api.history.toDomain
import com.example.domain.entity.ListeningHistoryDomain
import com.example.domain.repository.HistoryRepository
class HistoryRepositoryImpl(
    private val historyApi: HistoryApi
): HistoryRepository {
    override suspend fun listRecentHistory(): Result<List<ListeningHistoryDomain>> {
        return historyApi.listRecentHistory().map {
            it.toDomain()
        }
    }
}