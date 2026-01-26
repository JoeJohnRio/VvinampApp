package com.example.domain.repository

import com.example.domain.entity.ListeningHistory

interface HistoryRepository {
    suspend fun listRecentHistory(): Result<List<ListeningHistory>>
}