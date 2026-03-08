package com.example.domain.usecase

import androidx.paging.PagingData
import com.example.domain.entity.playlist.PlaylistMinimal
import com.example.domain.entity.playlist.PlaylistSortOrderDomain
import com.example.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlaylistUseCase @Inject constructor(
    val getMyPlaylistUseCase: GetMyPlaylistUseCase,
    val getMyPlaylistFeedUseCase: GetMyPlaylistFeedUseCase
) {
    operator fun invoke(): Flow<PagingData<PlaylistMinimal>> {
        return getMyPlaylistUseCase()
    }

    operator fun invoke(sortOrder: PlaylistSortOrderDomain): Flow<PagingData<PlaylistMinimal>> {
        return getMyPlaylistFeedUseCase(sortOrder)
    }
}

class GetMyPlaylistUseCase @Inject constructor(
    private val repository: PlaylistRepository
) {
    operator fun invoke(): Flow<PagingData<PlaylistMinimal>> {
        return repository.getMyPlaylists(10)
    }
}

class GetMyPlaylistFeedUseCase @Inject constructor(
    private val repository: PlaylistRepository
) {
    operator fun invoke(sortOrder: PlaylistSortOrderDomain): Flow<PagingData<PlaylistMinimal>> {
        return repository.getMyPlaylistFeed(10, sortOrder = sortOrder)
    }
}
