package com.example.data.repository.playlist

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.api.playlist.PlaylistApiImpl
import com.example.data.api.playlist.toDomain
import com.example.data.api.playlist.toPlaylistMinimal
import com.example.data.api.playlist.toRemote
import com.example.data.repository.pagination.GenericPagingSource
import com.example.domain.entity.PageResponse
import com.example.domain.entity.playlist.PlaylistComplete
import com.example.domain.entity.playlist.PlaylistMinimal
import com.example.domain.entity.playlist.PlaylistSortOrderDomain
import com.example.domain.entity.song.TrackDomain
import com.example.domain.repository.PlaylistRepository
import com.example.graphql.type.PlaylistSortOrder
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlaylistRepositoryImpl @Inject constructor(
    private val api: PlaylistApiImpl
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

    override suspend fun getPlaylist(
        id: String
    ): Result<PlaylistComplete> {
        return api.getPlaylist(id).map {
            it.toDomain()
        }
    }

    override fun getPlaylistTracks(id: String, pageSize: Int): Flow<PagingData<TrackDomain>> {
        return Pager(PagingConfig(pageSize = pageSize)) {
            GenericPagingSource { cursor ->
                api.getPlaylistTracks(playlistId = id, after = cursor).map { listData ->
                    PageResponse(
                        items = listData.edges.mapNotNull { it.node.track.toDomain() },
                        endCursor = listData.pageInfo.endCursor,
                        hasNextPage = listData.pageInfo.hasNextPage
                    )
                }
            }
        }.flow
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
}