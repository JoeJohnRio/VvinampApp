package com.example.data.repository.playlist

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.apollographql.apollo.api.Optional
import com.example.data.api.playlist.PlaylistApiImpl
import com.example.data.api.playlist.toDomain
import com.example.data.api.playlist.toPlaylistMinimal
import com.example.data.api.playlist.toRemote
import com.example.data.db.music.MusicDatabase
import com.example.data.entities.toDomain
import com.example.data.repository.pagination.GenericPagingSource
import com.example.domain.entity.PageResponse
import com.example.domain.entity.playlist.PlaylistComplete
import com.example.domain.entity.playlist.PlaylistMinimal
import com.example.domain.entity.playlist.PlaylistSortOrderDomain
import com.example.domain.entity.song.RecommendedTrackDomain
import com.example.domain.entity.song.TrackDomain
import com.example.domain.repository.PlaylistRepository
import com.example.graphql.type.AddSongToPlaylistInput
import com.example.graphql.type.EditPlaylistInput
import com.example.graphql.type.PlaylistSortOrder
import com.example.graphql.type.RepositionSongInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlaylistRepositoryImpl @Inject constructor(
    private val api: PlaylistApiImpl,
    private val database: MusicDatabase
) : PlaylistRepository {

    override fun getPlaylistsByUser(
        userId: String,
        pageSize: Int,
    ): Flow<PagingData<PlaylistMinimal>> {
        return Pager(PagingConfig(pageSize = pageSize)) {
            GenericPagingSource { cursor ->
                api.getPlaylistsByUser(userId, cursor).map { searchData ->
                    PageResponse(
                        items = searchData.edges.mapNotNull { it.node.toPlaylistMinimal() },
                        endCursor = searchData.pageInfo.endCursor,
                        hasNextPage = searchData.pageInfo.hasNextPage
                    )
                }
            }
        }.flow
    }

    override fun getMyPlaylistFeed(
        pageSize: Int,
        sortOrder: PlaylistSortOrderDomain
    ): Flow<PagingData<PlaylistMinimal>> {
        return Pager(PagingConfig(pageSize = pageSize)) {
            GenericPagingSource { cursor ->
                api.getMyPlaylistFeed(sortOrder = sortOrder.toRemote(), after = cursor)
                    .map { searchData ->
                        PageResponse(
                            items = searchData.edges.mapNotNull { it.node.toPlaylistMinimal() },
                            endCursor = searchData.pageInfo.endCursor,
                            hasNextPage = searchData.pageInfo.hasNextPage
                        )
                    }
            }
        }.flow
    }

    override suspend fun createPlaylist(
        title: String
    ): Result<String> {
        return api.createPlaylist(title).map {
            it.id
        }
    }

    override suspend fun getRecommendedTracks(): Result<List<RecommendedTrackDomain>> {
        return api.getRecommendedTracks().map {
            it.map {
                it.toDomain()
            }
        }
    }

    override suspend fun addSongToPlaylist(
        playlistId: String,
        trackId: String
    ): Result<Boolean> {
        return api.addSongToPlaylist(
            input = AddSongToPlaylistInput(
                playlistId = playlistId,
                trackId = trackId
            )
        ).map {
            if (it.id.isNullOrBlank()) {
                false
            } else {
                true
            }
        }
    }

    override suspend fun getPlaylist(
        id: String
    ): Result<PlaylistComplete> {
        return api.getPlaylist(id).map {
            it.toDomain()
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPlaylistTracks(id: String, pageSize: Int): Flow<PagingData<TrackDomain>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            remoteMediator = PlaylistTracksRemoteMediator(
                playlistId = id,
                api = api,
                database = database
            ),
            pagingSourceFactory = {
                database.playlistTrackDao().getTracksByPlaylist(id)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override fun getMyPlaylists(pageSize: Int): Flow<PagingData<PlaylistMinimal>> {
        return Pager(PagingConfig(pageSize = pageSize)) {
            GenericPagingSource { cursor ->
                api.getMyPlaylists(cursor).map { searchData ->
                    PageResponse(
                        items = searchData.edges.mapNotNull { it.node.toPlaylistMinimal() },
                        endCursor = searchData.pageInfo.endCursor,
                        hasNextPage = searchData.pageInfo.hasNextPage
                    )
                }
            }
        }.flow
    }

    override suspend fun editPlaylist(
        playlistId: String,
        deletedTrackIds: List<String>?,
        repositionedSongs: List<Pair<String, Int>>?
    ): Result<Boolean> {
        val input = EditPlaylistInput(
            playlistId = playlistId,
            deletedTrackIds = Optional.presentIfNotNull(deletedTrackIds),
            repositionedSongs = Optional.presentIfNotNull(repositionedSongs?.map {
                RepositionSongInput(
                    trackId = it.first,
                    position = it.second
                )
            })
        )
        return api.editPlaylist(input)
    }
}
