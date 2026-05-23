package com.example.data.api.playlist

import com.example.graphql.AddSongToPlaylistMutation
import com.example.graphql.CreatePlaylistMutation
import com.example.graphql.EditPlaylistMutation
import com.example.graphql.GetMyPlaylistFeedQuery
import com.example.graphql.GetMyPlaylistsQuery
import com.example.graphql.GetPlaylistQuery
import com.example.graphql.GetPlaylistTracksQuery
import com.example.graphql.GetPlaylistsByUserQuery
import com.example.graphql.RecommendedTracksQuery
import com.example.graphql.SavePlaylistMutation
import com.example.graphql.UpdatePlaylistMutation
import com.example.graphql.type.AddSongToPlaylistInput
import com.example.graphql.type.CreatePlaylistInput
import com.example.graphql.type.EditPlaylistInput
import com.example.graphql.type.PlaylistSortOrder
import com.example.graphql.type.RemoveSongFromPlaylistInput
import com.example.graphql.type.SavePlaylistInput
import com.example.graphql.type.UnsavePlaylistInput
import com.example.graphql.type.UpdatePlaylistInput

interface PlaylistApi {
    suspend fun editPlaylist(input: EditPlaylistInput): Result<Boolean>
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
    suspend fun getPlaylist(id: String): Result<GetPlaylistQuery.GetPlaylist>
    suspend fun getMyPlaylists(
        after: String?,
    ): Result<GetMyPlaylistsQuery.GetMyPlaylists>

    suspend fun getPlaylistsByUser(
        userId: String,
        after: String?,
    ): Result<GetPlaylistsByUserQuery.GetPlaylistsByUser>

    suspend fun getMyPlaylistFeed(
        sortOrder: PlaylistSortOrder,
        after: String?
    ): Result<GetMyPlaylistFeedQuery.GetMyPlaylistFeed>

    suspend fun createPlaylist(title: String): Result<CreatePlaylistMutation.CreatePlaylist>
    suspend fun getPlaylistTracks(
        playlistId: String,
        after: String?
    ): Result<GetPlaylistTracksQuery.GetPlaylistTracks>

    suspend fun getRecommendedTracks(): Result<List<RecommendedTracksQuery.RecommendedTrack>>
}