package com.example.domain.repository

import androidx.paging.PagingData
import com.example.domain.entity.playlist.PlaylistComplete
import com.example.domain.entity.playlist.PlaylistMinimal
import com.example.domain.entity.playlist.PlaylistSortOrderDomain
import com.example.domain.entity.song.RecommendedTrackDomain
import com.example.domain.entity.song.TrackDomain
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun getMyPlaylists(pageSize: Int): Flow<PagingData<PlaylistMinimal>>
    fun getPlaylistsByUser(userId: String, pageSize: Int): Flow<PagingData<PlaylistMinimal>>
    fun getMyPlaylistFeed(
        pageSize: Int,
        sortOrder: PlaylistSortOrderDomain
    ): Flow<PagingData<PlaylistMinimal>>

    suspend fun createPlaylist(title: String): Result<String>
    suspend fun getPlaylist(id: String): Result<PlaylistComplete>
    fun getPlaylistTracks(id: String, pageSize: Int): Flow<PagingData<TrackDomain>>
    suspend fun getRecommendedTracks(): Result<List<RecommendedTrackDomain>>
    suspend fun addSongToPlaylist(playlistId: String, trackId: String): Result<Boolean>
    suspend fun editPlaylist(
        playlistId: String,
        deletedTrackIds: List<String>?,
        repositionedSongs: List<Pair<String, Int>>?
    ): Result<Boolean>
}
