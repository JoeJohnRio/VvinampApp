package com.example.data.repository.music

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.data.entities.toDomain
import com.example.domain.entity.MusicEntity
import com.example.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class MusicRepositoryImpl(
    private val remote: MusicDataSource.Remote,
    private val local: MusicDataSource.Local,
    private val remoteMediator: MusicRemoteMediator
): MusicRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun music(): Flow<PagingData<MusicEntity>> =  Pager(
        config = PagingConfig(
            pageSize = 90,
            enablePlaceholders = false
        ),
        remoteMediator = remoteMediator,
        pagingSourceFactory = { local.movies() }
    ).flow.map { pagingData ->
        pagingData.map { it.toDomain() }
    }
}