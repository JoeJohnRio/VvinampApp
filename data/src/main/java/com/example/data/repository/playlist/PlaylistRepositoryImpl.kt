package com.example.data.repository.playlist

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.api.playlist.PlaylistApiImpl
import com.example.data.api.search.toDomain
import com.example.data.repository.pagination.GenericPagingSource
import com.example.domain.entity.PageResponse
import com.example.domain.entity.playlist.PlaylistDomain
import com.example.domain.entity.search.SearchItemDomain
import com.example.domain.repository.PlaylistRepository
import com.example.graphql.AddSongToPlaylistMutation
import com.example.graphql.CreatePlaylistMutation
import com.example.graphql.GetMyPlaylistsQuery
import com.example.graphql.GetPlaylistQuery
import com.example.graphql.GetPlaylistsByUserQuery
import com.example.graphql.SavePlaylistMutation
import com.example.graphql.UpdatePlaylistMutation
import com.example.graphql.type.AddSongToPlaylistInput
import com.example.graphql.type.CreatePlaylistInput
import com.example.graphql.type.RemoveSongFromPlaylistInput
import com.example.graphql.type.SavePlaylistInput
import com.example.graphql.type.UnsavePlaylistInput
import com.example.graphql.type.UpdatePlaylistInput
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlaylistRepositoryImpl @Inject constructor(
    private val api: PlaylistApiImpl
): PlaylistRepository {
    suspend fun unsavePlaylist(input: UnsavePlaylistInput): Result<Boolean>
    suspend fun savePlaylist(input: SavePlaylistInput): Result<SavePlaylistMutation.SavePlaylist>
    suspend fun removeSongFromPlaylist(input: RemoveSongFromPlaylistInput): Result<Boolean>
    suspend fun addSongToPlaylist(input: AddSongToPlaylistInput): Result<AddSongToPlaylistMutation.AddSongToPlaylist>
    suspend fun deletePlaylist(id: String): Result<Boolean>
    suspend fun updatePlaylist(
        id: String,
        input: UpdatePlaylistInput
    ): Result<UpdatePlaylistMutation.UpdatePlaylist>

    suspend fun createPlaylist(input: CreatePlaylistInput): Result<CreatePlaylistMutation.CreatePlaylist>
    suspend fun getPlaylist(id: String): Result<GetPlaylistQuery.GetPlaylist?>
    suspend fun getMyPlaylists(
        after: String?,
    ): Result<GetMyPlaylistsQuery.GetMyPlaylists>

    suspend fun getPlaylistsByUser(
        userId: String,
        after: String?,
    ): Result<GetPlaylistsByUserQuery.GetPlaylistsByUser>

    override fun getMyPlaylists(id: String, pageSize: Int): Flow<PagingData<PlaylistDomain>> {
        return Pager(PagingConfig(pageSize = pageSize)) {
            GenericPagingSource { cursor ->
                api.getMyPlaylists( cursor, pageSize).map { searchData ->
                    PageResponse(
                        items = searchData.edges.mapNotNull { it.node.toDomain() },
                        endCursor = searchData.pageInfo.endCursor,
                        hasNextPage = searchData.pageInfo.hasNextPage
                    )
                }
            }
        }.flow
    }
}