package com.example.vvinamp.ui.playlist

import androidx.paging.PagingData
import com.example.domain.entity.playlist.PlaylistComplete
import com.example.domain.entity.playlist.PlaylistMinimal
import com.example.domain.entity.playlist.PlaylistSortOrderDomain
import com.example.domain.entity.playlist.PlaylistTrackSortByDomain
import com.example.domain.entity.song.RecommendedTrackDomain
import com.example.domain.entity.song.TrackDomain
import com.example.domain.repository.PlaylistRepository
import com.example.domain.usecase.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakePlaylistRepository : PlaylistRepository {

    var createPlaylistResult: Result<String> = Result.failure(Exception("Not configured"))
    var createPlaylistCallCount: Int = 0
    var lastCreatedPlaylistTitle: String? = null

    var getMyPlaylistFeedCallCount: Int = 0
    var lastFeedPageSize: Int? = null
    var lastFeedSortOrder: PlaylistSortOrderDomain? = null
    var myPlaylistFeedFlow: Flow<PagingData<PlaylistMinimal>> = flowOf(PagingData.empty())

    override suspend fun createPlaylist(title: String): Result<String> {
        createPlaylistCallCount++
        lastCreatedPlaylistTitle = title
        return createPlaylistResult
    }

    override fun getMyPlaylists(pageSize: Int): Flow<PagingData<PlaylistMinimal>> =
        flowOf(PagingData.empty())

    override fun getPlaylistsByUser(userId: String, pageSize: Int): Flow<PagingData<PlaylistMinimal>> =
        flowOf(PagingData.empty())

    override fun getMyPlaylistFeed(
        pageSize: Int,
        sortOrder: PlaylistSortOrderDomain
    ): Flow<PagingData<PlaylistMinimal>> {
        getMyPlaylistFeedCallCount++
        lastFeedPageSize = pageSize
        lastFeedSortOrder = sortOrder
        return myPlaylistFeedFlow
    }

    override suspend fun getPlaylist(id: String): Result<PlaylistComplete> =
        Result.failure(NotImplementedError())

    override fun getPlaylistTracks(
        id: String,
        pageSize: Int,
        sortBy: PlaylistTrackSortByDomain
    ): Flow<PagingData<TrackDomain>> = flowOf(PagingData.empty())

    override suspend fun getRecommendedTracks(): Result<List<RecommendedTrackDomain>> =
        Result.success(emptyList())

    override suspend fun addSongToPlaylist(playlistId: String, trackId: String): Result<Boolean> =
        Result.success(true)

    override suspend fun uploadPlaylistCover(
        playlistId: String,
        fileName: String,
        contentType: String,
        content: ByteArray
    ): Result<String> = Result.success("")

    override suspend fun updatePlaylist(
        id: String,
        title: String?,
        description: String?,
        isPublic: Boolean?
    ): Result<Boolean> = Result.success(true)

    override suspend fun editPlaylist(
        playlistId: String,
        deletedTrackIds: List<String>?,
        repositionedSongs: List<Pair<String, Int>>?
    ): Result<Boolean> = Result.success(true)
}

/**
 * Helper to build PlaylistUseCase wired to the given repository.
 */
fun buildPlaylistUseCase(repository: PlaylistRepository): PlaylistUseCase {
    return PlaylistUseCase(
        getMyPlaylistUseCase = GetMyPlaylistUseCase(repository),
        getMyPlaylistFeedUseCase = GetMyPlaylistFeedUseCase(repository),
        createPlaylistUseCase = CreatePlaylistUseCase(repository),
        getPlaylistTracksUseCase = GetPlaylistTracksUseCase(repository),
        getPlaylistUseCase = GetPlaylistUseCase(repository),
        getRecommendedTracksUseCase = GetRecommendedTracksUseCase(repository),
        addSongToPlaylistUseCase = AddSongToPlaylistUseCase(repository),
        editPlaylistUseCase = EditPlaylistUseCase(repository),
        updatePlaylistUseCase = UpdatePlaylistUseCase(repository),
        uploadPlaylistCoverUseCase = UploadPlaylistCoverUseCase(repository)
    )
}
