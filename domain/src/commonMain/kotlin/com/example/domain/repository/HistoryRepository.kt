package com.example.domain.repository

import com.example.domain.entity.ListeningHistoryDomain

interface HistoryRepository {
    suspend fun listRecentHistory(): Result<List<ListeningHistoryDomain>>
}