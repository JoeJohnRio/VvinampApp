package com.example.data.repository.playlist

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.map
import com.apollographql.apollo.api.DefaultUpload
import com.apollographql.apollo.api.Optional
import com.example.data.api.playlist.PlaylistApi
import com.example.data.api.playlist.PlaylistApiImpl
import com.example.data.api.playlist.toDomain
import com.example.data.api.playlist.toPlaylistMinimal
import com.example.data.api.playlist.toRemote
import com.example.data.db.music.MusicDatabase
import com.example.data.entities.PlaylistTrackEntity
import com.example.data.entities.toDomain
import com.example.data.repository.pagination.GenericPagingSource
import com.example.domain.entity.PageResponse
import com.example.domain.entity.playlist.PlaylistComplete
import com.example.domain.entity.playlist.PlaylistMinimal
import com.example.domain.entity.playlist.PlaylistSortOrderDomain
import com.example.domain.entity.playlist.PlaylistTrackSortByDomain
import com.example.domain.entity.song.RecommendedTrackDomain
import com.example.domain.entity.song.TrackDomain
import com.example.domain.repository.PlaylistRepository
import com.example.graphql.type.AddSongToPlaylistInput
import com.example.graphql.type.AddYoutubeTrackToPlaylistInput
import com.example.graphql.type.EditPlaylistInput
import com.example.graphql.type.PlaylistSortOrder
import com.example.graphql.type.RepositionSongInput
import com.example.graphql.type.UpdatePlaylistInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
class PlaylistRepositoryImpl(
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

    override suspend fun addYoutubeTrackToPlaylist(
        playlistId: String,
        youtubeId: String,
        title: String,
        artistName: String,
        thumbnailUrl: String?,
        duration: Int,
        position: Int?
    ): Result<Boolean> {
        return api.addYoutubeTrackToPlaylist(
            input = AddYoutubeTrackToPlaylistInput(
                playlistId = playlistId,
                youtubeId = youtubeId,
                title = title,
                artistName = artistName,
                thumbnailUrl = com.apollographql.apollo.api.Optional.presentIfNotNull(thumbnailUrl),
                duration = duration,
                position = com.apollographql.apollo.api.Optional.presentIfNotNull(position)
            )
        ).map {
            !it.id.isNullOrBlank()
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
    override fun getPlaylistTracks(id: String, pageSize: Int, sortBy: PlaylistTrackSortByDomain): Flow<PagingData<TrackDomain>> {
        // On web the list didn't reload after the mediator saved new rows, so the mediator reloads it directly.
        // On Android, Room already does this and the extra reload is harmless.
        var latestSource: PagingSource<Int, PlaylistTrackEntity>? = null
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            remoteMediator = PlaylistTracksRemoteMediator(
                playlistId = id,
                api = api,
                database = database,
                sortBy = sortBy.toRemote(),
                onTracksSaved = { latestSource?.invalidate() }
            ),
            pagingSourceFactory = {
                database.playlistTrackDao().getTracksByPlaylist(id).also { latestSource = it }
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

    override suspend fun updatePlaylist(
        id: String,
        title: String?,
        description: String?,
        isPublic: Boolean?
    ): Result<Boolean> {
        val input = UpdatePlaylistInput(
            title = Optional.presentIfNotNull(title),
            description = Optional.presentIfNotNull(description),
            isPublic = Optional.presentIfNotNull(isPublic)
        )
        return api.updatePlaylist(id, input).map { it != null }
    }

    override suspend fun uploadPlaylistCover(
        playlistId: String,
        fileName: String,
        contentType: String,
        content: ByteArray
    ): Result<String> {
        val upload = DefaultUpload.Builder()
            .content(content)
            .fileName(fileName)
            .contentType(contentType)
            .build()
        return api.uploadPlaylistCover(playlistId, upload)
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

    override suspend fun deletePlaylist(id: String): Result<Boolean> {
        return api.deletePlaylist(id)
    }
}
