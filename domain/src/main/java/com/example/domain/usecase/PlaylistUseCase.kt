package com.example.domain.usecase

import androidx.paging.PagingData
import com.example.domain.entity.playlist.PlaylistComplete
import com.example.domain.entity.playlist.PlaylistMinimal
import com.example.domain.entity.playlist.PlaylistSortOrderDomain
import com.example.domain.entity.song.RecommendedTrackDomain
import com.example.domain.entity.song.TrackDomain
import com.example.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class PlaylistUseCase @Inject constructor(
    val getMyPlaylistUseCase: GetMyPlaylistUseCase,
    val getMyPlaylistFeedUseCase: GetMyPlaylistFeedUseCase,
    val createPlaylistUseCase: CreatePlaylistUseCase,
    val getPlaylistTracksUseCase: GetPlaylistTracksUseCase,
    val getPlaylistUseCase: GetPlaylistUseCase,
    val getRecommendedTracksUseCase: GetRecommendedTracksUseCase,
    val addSongToPlaylistUseCase: AddSongToPlaylistUseCase
)

class GetMyPlaylistUseCase @Inject constructor(
    private val repository: PlaylistRepository
) {
    operator fun invoke(): Flow<PagingData<PlaylistMinimal>> {
        return repository.getMyPlaylists(10)
    }
}

class GetPlaylistTracksUseCase @Inject constructor(
    private val repository: PlaylistRepository
) {
    operator fun invoke(id: String): Flow<PagingData<TrackDomain>> {
        return repository.getPlaylistTracks(id, 10)
    }
}

class GetPlaylistUseCase @Inject constructor(
    private val repository: PlaylistRepository
) {
    suspend operator fun invoke(id: String): Result<PlaylistComplete> {
        return repository.getPlaylist(id)
    }
}

class GetRecommendedTracksUseCase @Inject constructor(
    private val repository: PlaylistRepository
) {
    suspend operator fun invoke(): Result<List<RecommendedTrackDomain>> {
        return repository.getRecommendedTracks()
    }
}

class AddSongToPlaylistUseCase @Inject constructor(
    private val repository: PlaylistRepository
) {
    suspend operator fun invoke(
        playlistId: String,
        trackId: String
    ): Result<Boolean> {
        return repository.addSongToPlaylist(
            playlistId = playlistId,
            trackId = trackId
        )
    }
}

class GetMyPlaylistFeedUseCase @Inject constructor(
    private val repository: PlaylistRepository
) {
    operator fun invoke(sortOrder: PlaylistSortOrderDomain): Flow<PagingData<PlaylistMinimal>> {
        return repository.getMyPlaylistFeed(10, sortOrder = sortOrder)
    }
}

class CreatePlaylistUseCase @Inject constructor(
    private val repository: PlaylistRepository
) {
    suspend operator fun invoke(title: String): Result<String> {
        return repository.createPlaylist(title = title)
    }
}

