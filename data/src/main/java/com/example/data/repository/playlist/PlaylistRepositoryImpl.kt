package com.example.data.repository.playlist

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.api.playlist.PlaylistApiImpl
import com.example.data.api.playlist.toPlaylistMinimal
import com.example.data.api.playlist.toRemote
import com.example.data.repository.pagination.GenericPagingSource
import com.example.domain.entity.PageResponse
import com.example.domain.entity.playlist.PlaylistMinimal
import com.example.domain.entity.playlist.PlaylistSortOrderDomain
import com.example.domain.repository.PlaylistRepository
import com.example.graphql.type.PlaylistSortOrder
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlaylistRepositoryImpl @Inject constructor(
    private val api: PlaylistApiImpl
) : PlaylistRepository {
    //    suspend fun unsavePlaylist(input: UnsavePlaylistInput): Result<Boolean>
//    suspend fun savePlaylist(input: SavePlaylistInput): Result<SavePlaylistMutation.SavePlaylist>
//    suspend fun removeSongFromPlaylist(input: RemoveSongFromPlaylistInput): Result<Boolean>
//    suspend fun addSongToPlaylist(input: AddSongToPlaylistInput): Result<AddSongToPlaylistMutation.AddSongToPlaylist>
//    suspend fun deletePlaylist(id: String): Result<Boolean>
//    suspend fun updatePlaylist(
//        id: String,
//        input: UpdatePlaylistInput
//    ): Result<UpdatePlaylistMutation.UpdatePlaylist>
//
//    suspend fun createPlaylist(input: CreatePlaylistInput): Result<CreatePlaylistMutation.CreatePlaylist>
//    suspend fun getPlaylist(id: String): Result<GetPlaylistQuery.GetPlaylist?>
//    suspend fun getMyPlaylists(
//        after: String?,
//    ): Result<GetMyPlaylistsQuery.GetMyPlaylists>
//

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