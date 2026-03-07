package com.example.data.api.playlist

import com.example.domain.entity.playlist.PlaylistDomain
import com.example.domain.entity.song.TrackDomain
import com.example.graphql.GetMyPlaylistsQuery

fun GetMyPlaylistsQuery.GetMyPlaylists.toDomain(): PlaylistDomain {
    this.edges.map {

    }
}

fun GetMyPlaylistsQuery.Node.toDomain(): PlaylistDomain {
    return PlaylistDomain(
        id = this.id,
        title = this.title,
        coverImage = this.coverImage,
        isPublic = this.isPublic,
        savedCount = this.savedCount,
        isSavedByMe = this.isSavedByMe,
        tracks = this.tracks.map{
            it.toDomain()
        }

    )
}

fun GetMyPlaylistsQuery.Track1.toDomain(): TrackDomain {
    return TrackDomain(
        albumCoverUrl = albumCoverUrl
    )
}