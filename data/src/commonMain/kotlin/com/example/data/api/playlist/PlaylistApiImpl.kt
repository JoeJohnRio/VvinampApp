package com.example.data.api.playlist

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.apollographql.apollo.api.Upload
import com.example.data.util.safeMutation
import com.example.data.util.safeQuery
import com.example.graphql.AddSongToPlaylistMutation
import com.example.graphql.AddYoutubeTrackToPlaylistMutation
import com.example.graphql.CreatePlaylistMutation
import com.example.graphql.DeletePlaylistMutation
import com.example.graphql.EditPlaylistMutation
import com.example.graphql.GetArtistScreenDataQuery
import com.example.graphql.GetMyPlaylistFeedQuery
import com.example.graphql.GetMyPlaylistsQuery
import com.example.graphql.GetPlaylistQuery
import com.example.graphql.GetPlaylistTracksQuery
import com.example.graphql.GetPlaylistsByUserQuery
import com.example.graphql.ListRecentHistoryQuery
import com.example.graphql.RecommendedTracksQuery
import com.example.graphql.RemoveSongFromPlaylistMutation
import com.example.graphql.SavePlaylistMutation
import com.example.graphql.UnsavePlaylistMutation
import com.example.graphql.UpdatePlaylistMutation
import com.example.graphql.UploadPlaylistCoverMutation
import com.example.graphql.type.AddSongToPlaylistInput
import com.example.graphql.type.AddYoutubeTrackToPlaylistInput
import com.example.graphql.type.CreatePlaylistInput
import com.example.graphql.type.EditPlaylistInput
import com.example.graphql.type.PlaylistSortOrder
import com.example.graphql.type.PlaylistTrackSortBy
import com.example.graphql.type.RemoveSongFromPlaylistInput
import com.example.graphql.type.SavePlaylistInput
import com.example.graphql.type.UnsavePlaylistInput
import com.example.graphql.type.UpdatePlaylistInput
class PlaylistApiImpl(
    private val api: ApolloClient
) : PlaylistApi {

    override suspend fun uploadPlaylistCover(playlistId: String, file: Upload): Result<String> {
        return api.safeMutation(
            UploadPlaylistCoverMutation(playlistId, file)
        ) {
            it.uploadPlaylistCover
        }
    }

    override suspend fun editPlaylist(input: EditPlaylistInput): Result<Boolean> {
        return api.safeMutation(
            EditPlaylistMutation(input)
        ) {
            it.editPlaylist
        }
    }

    override suspend fun getPlaylist(id: String): Result<GetPlaylistQuery.GetPlaylist> {
        return api.safeQuery(
            GetPlaylistQuery(id)
        ) {
            it.getPlaylist
        }
    }

    override suspend fun getRecommendedTracks(): Result<List<RecommendedTracksQuery.RecommendedTrack>> {
        return api.safeQuery(
            RecommendedTracksQuery()
        ) {
            it.recommendedTracks
        }
    }

    override suspend fun getMyPlaylists(
        after: String?,
    ): Result<GetMyPlaylistsQuery.GetMyPlaylists> {
        return api.safeQuery(
            GetMyPlaylistsQuery(
                after = Optional.presentIfNotNull(after),
            )
        ) {
            it.getMyPlaylists
        }
    }

    override suspend fun getPlaylistTracks(
        playlistId: String,
        after: String?,
        sortBy: PlaylistTrackSortBy?,
    ): Result<GetPlaylistTracksQuery.GetPlaylistTracks> {
        return api.safeQuery(
            GetPlaylistTracksQuery(
                playlistId = playlistId,
                after = Optional.presentIfNotNull(after),
                sortBy = Optional.presentIfNotNull(sortBy)
            )
        ) {
            it.getPlaylistTracks
        }
    }

    override suspend fun getMyPlaylistFeed(
        sortOrder: PlaylistSortOrder,
        after: String?
    ): Result<GetMyPlaylistFeedQuery.GetMyPlaylistFeed> {
        return api.safeQuery(
            GetMyPlaylistFeedQuery(
                after = Optional.presentIfNotNull(after),
                sortOrder = Optional.presentIfNotNull(sortOrder)
            )
        ) {
            it.getMyPlaylistFeed
        }
    }

    override suspend fun createPlaylist(title: String): Result<CreatePlaylistMutation.CreatePlaylist> {
        return api.safeMutation(
            CreatePlaylistMutation(
                input = CreatePlaylistInput(
                    title = title
                )
            )
        ) {
            it.createPlaylist
        }
    }

    override suspend fun getPlaylistsByUser(
        userId: String,
        after: String?,
    ): Result<GetPlaylistsByUserQuery.GetPlaylistsByUser> {
        return api.safeQuery(
            GetPlaylistsByUserQuery(
                userId = userId,
                after = Optional.presentIfNotNull(after),
            )
        ) {
            it.getPlaylistsByUser
        }
    }

    override suspend fun createPlaylist(
        input: CreatePlaylistInput
    ): Result<CreatePlaylistMutation.CreatePlaylist> {
        return api.safeMutation(
            CreatePlaylistMutation(input)
        ) {
            it.createPlaylist
        }
    }

    override suspend fun updatePlaylist(
        id: String,
        input: UpdatePlaylistInput
    ): Result<UpdatePlaylistMutation.UpdatePlaylist> {
        return api.safeMutation(
            UpdatePlaylistMutation(id, input)
        ) {
            it.updatePlaylist
        }
    }

    override suspend fun deletePlaylist(id: String): Result<Boolean> {
        return api.safeMutation(
            DeletePlaylistMutation(id)
        ) {
            it.deletePlaylist
        }
    }

    override suspend fun addSongToPlaylist(
        input: AddSongToPlaylistInput
    ): Result<AddSongToPlaylistMutation.AddSongToPlaylist> {
        return api.safeMutation(
            AddSongToPlaylistMutation(input)
        ) {
            it.addSongToPlaylist
        }
    }

    override suspend fun addYoutubeTrackToPlaylist(
        input: AddYoutubeTrackToPlaylistInput
    ): Result<AddYoutubeTrackToPlaylistMutation.AddYoutubeTrackToPlaylist> {
        return api.safeMutation(
            AddYoutubeTrackToPlaylistMutation(input)
        ) {
            it.addYoutubeTrackToPlaylist
        }
    }

    override suspend fun removeSongFromPlaylist(
        input: RemoveSongFromPlaylistInput
    ): Result<Boolean> {
        return api.safeMutation(
            RemoveSongFromPlaylistMutation(input)
        ) {
            it.removeSongFromPlaylist
        }
    }

    override suspend fun savePlaylist(
        input: SavePlaylistInput
    ): Result<SavePlaylistMutation.SavePlaylist> {
        return api.safeMutation(
            SavePlaylistMutation(input)
        ) {
            it.savePlaylist
        }
    }

    override suspend fun unsavePlaylist(
        input: UnsavePlaylistInput
    ): Result<Boolean> {
        return api.safeMutation(
            UnsavePlaylistMutation(input)
        ) {
            it.unsavePlaylist
        }
    }
}