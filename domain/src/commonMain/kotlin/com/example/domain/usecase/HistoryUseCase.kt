package com.example.domain.usecase

import com.example.domain.entity.ListeningHistoryDomain
import com.example.domain.repository.HistoryRepository
import javax.inject.Inject


class HistoryUseCase @Inject constructor(
    val getListRecentHistoryUseCase: GetListRecentHistoryUseCase
)

class GetListRecentHistoryUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(): Result<List<ListeningHistoryDomain>> {
        return historyRepository.listRecentHistory()
    }
}

