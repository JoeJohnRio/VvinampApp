package com.example.youtubemusicclone.ui.home.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.example.domain.repository.MusicRepository
import com.example.youtubemusicclone.entities.MusicListItem
import com.example.youtubemusicclone.mapper.toPresentation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMoviesWithSeparators @Inject constructor(
    private val musicRepository: MusicRepository,
    private val insertSeparatorIntoPagingData: InsertSeparatorIntoPagingData
) {
    fun movies(pageSize: Int): Flow<PagingData<MusicListItem>> = musicRepository.music().map {
        val pagingData: PagingData<MusicListItem.Music> = it.map { movie -> movie.toPresentation() }
        insertSeparatorIntoPagingData.insert(pagingData)
    }
}