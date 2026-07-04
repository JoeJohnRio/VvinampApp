package com.example.domain.usecase

import com.example.domain.entity.ListeningHistoryDomain
import com.example.domain.repository.HistoryRepository
class HistoryUseCase(
    val getListRecentHistoryUseCase: GetListRecentHistoryUseCase
)

class GetListRecentHistoryUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(): Result<List<ListeningHistoryDomain>> {
        return historyRepository.listRecentHistory()
    }
}

