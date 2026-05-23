package com.example.data.repository.playlist

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.data.api.playlist.PlaylistApiImpl
import com.example.data.api.playlist.toDomain
import com.example.data.db.music.MusicDatabase
import com.example.data.entities.PlaylistRemoteKeyEntity
import com.example.data.entities.PlaylistTrackEntity
import com.example.data.entities.toEntity

@OptIn(ExperimentalPagingApi::class)
class PlaylistTracksRemoteMediator(
    private val playlistId: String,
    private val api: PlaylistApiImpl,
    private val database: MusicDatabase
) : RemoteMediator<Int, PlaylistTrackEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PlaylistTrackEntity>
    ): MediatorResult {
        val cursor = when (loadType) {
            LoadType.REFRESH -> null
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        return try {
            val result = api.getPlaylistTracks(playlistId, cursor)
            
            result.fold(
                onSuccess = { listData ->
                    val endOfPaginationReached = !listData.pageInfo.hasNextPage
                    val tracks = listData.edges.mapNotNull { it.node.track.toDomain() }

                    database.withTransaction {
                        if (loadType == LoadType.REFRESH) {
                            database.playlistTrackDao().clearTracksByPlaylist(playlistId)
                            database.playlistRemoteKeyDao().clearRemoteKeysByPlaylist(playlistId)
                        }

                        val prevKey = cursor
                        val nextKey = if (endOfPaginationReached) null else listData.pageInfo.endCursor
                        
                        var lastPosition = database.playlistTrackDao().getLastPosition(playlistId) ?: 0
                        
                        val entities = tracks.map { track ->
                            track.toEntity(playlistId, ++lastPosition)
                        }
                        
                        val keys = entities.map {
                            PlaylistRemoteKeyEntity(
                                trackId = it.id,
                                playlistId = playlistId,
                                prevKey = prevKey,
                                nextKey = nextKey
                            )
                        }

                        database.playlistTrackDao().insertAll(entities)
                        database.playlistRemoteKeyDao().insertAll(keys)
                    }
                    MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                },
                onFailure = {
                    MediatorResult.Error(it)
                }
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PlaylistTrackEntity>): PlaylistRemoteKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { track ->
            database.playlistRemoteKeyDao().remoteKeysByTrackAndPlaylist(track.id, playlistId)
        }
    }
}
