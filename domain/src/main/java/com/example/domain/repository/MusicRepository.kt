package com.example.domain.repository

import androidx.paging.PagingData
import com.example.domain.entity.MusicEntity
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
    fun music(): Flow<PagingData<MusicEntity>>
}