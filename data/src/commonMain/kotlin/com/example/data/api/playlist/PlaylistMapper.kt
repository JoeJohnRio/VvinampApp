package com.example.data.api.playlist

import com.example.domain.entity.playlist.PlaylistComplete
import com.example.domain.entity.playlist.PlaylistMinimal
import com.example.domain.entity.playlist.PlaylistSortOrderDomain
import com.example.domain.entity.playlist.PlaylistTrackSortByDomain
import com.example.domain.entity.song.RecommendedTrackDomain
import com.example.domain.entity.song.TrackDomain
import com.example.domain.entity.user.UserMinimal
import com.example.graphql.GetMyPlaylistFeedQuery
import com.example.graphql.GetMyPlaylistsQuery
import com.example.graphql.GetPlaylistQuery
import com.example.graphql.GetPlaylistTracksQuery
import com.example.graphql.GetPlaylistsByUserQuery
import com.example.graphql.RecommendedTracksQuery
import com.example.graphql.type.PlaylistSortOrder
import com.example.graphql.type.PlaylistTrackSortBy

fun GetMyPlaylistsQuery.Node.toPlaylistMinimal(): PlaylistMinimal {
    return PlaylistMinimal(
        id = this.id,
        title = this.title,
        albumCovers = this.albumCovers.orEmpty(),
        isSavedByMe = this.isSavedByMe,
        savedCount = this.savedCount,
        username = this.creator.username,
        coverImage = this.coverImage.orEmpty()
    )
}

fun GetPlaylistQuery.GetPlaylist.toDomain(): PlaylistComplete {
    return PlaylistComplete(
        id = this.id,
        title = this.title,
        description = this.description,
        creator = this.creator.toDomain(),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        isPublic = this.isPublic,
        coverImage = this.coverImage,
        albumCovers = this.albumCovers.orEmpty(),
        savedCount = this.savedCount,
        totalDuration = this.totalDuration,
        isSavedByMe = this.isSavedByMe
    )
}

fun PlaylistTrackSortByDomain.toRemote(): PlaylistTrackSortBy {
    return when (this) {
        PlaylistTrackSortByDomain.CUSTOM -> PlaylistTrackSortBy.CUSTOM
        PlaylistTrackSortByDomain.TITLE -> PlaylistTrackSortBy.TITLE
        PlaylistTrackSortByDomain.ARTIST_NAME -> PlaylistTrackSortBy.ARTIST_NAME
        PlaylistTrackSortByDomain.ALBUM_NAME -> PlaylistTrackSortBy.ALBUM_NAME
        PlaylistTrackSortByDomain.RECENTLY_ADDED -> PlaylistTrackSortBy.RECENTLY_ADDED
    }
}

fun RecommendedTracksQuery.RecommendedTrack.toDomain(): RecommendedTrackDomain {
    return RecommendedTrackDomain(
        id = this.id,
        title = this.title,
        duration = this.duration,
        streamCount = this.streamCount,
        audioFileUrl = this.audioFileUrl,
        albumCoverUrl = this.albumCoverUrl,
        artistName = this.artistName,
        explicit = this.explicit,
        lyrics = this.lyrics,
        releaseDate = this.releaseDate
    )
}
fun RecommendedTrackDomain.toTrackDomain(): TrackDomain {
    return TrackDomain(
        id = this.id,
        artistName = this.artistName,
        albumCoverUrl = this.albumCoverUrl,
        title = this.title,
        duration = this.duration,
        streamCount = this.streamCount,
        explicit = this.explicit,
        releaseDate = this.releaseDate
    )
}
fun RecommendedTrackDomain.toChecked(): RecommendedTrackDomain {
    return RecommendedTrackDomain(
        id = this.id,
        title = this.title,
        duration = this.duration,
        streamCount = this.streamCount,
        audioFileUrl = this.audioFileUrl,
        albumCoverUrl = this.albumCoverUrl,
        artistName = this.artistName,
        explicit = this.explicit,
        lyrics = this.lyrics,
        releaseDate = this.releaseDate,
        isAdded = true
    )
}

fun GetPlaylistTracksQuery.Track.toDomain(): TrackDomain {
    return TrackDomain(
        id = this.id,
        artistName = this.artistName,
        albumCoverUrl = this.albumCoverUrl,
        title = this.title,
        duration = this.duration,
        streamCount = this.streamCount,
        explicit = this.explicit,
        releaseDate = this.releaseDate
    )
}

fun GetPlaylistQuery.Creator.toDomain(): UserMinimal {
    return UserMinimal(
        id = this.id,
        username = this.username,
        profilePicture = this.profilePicture
    )
}

fun GetMyPlaylistFeedQuery.Node.toPlaylistMinimal(): PlaylistMinimal {
    return PlaylistMinimal(
        id = this.id,
        title = this.title,
        albumCovers = this.albumCovers.orEmpty(),
        isSavedByMe = this.isSavedByMe,
        savedCount = this.savedCount,
        username = this.creator.username,
        coverImage = this.coverImage.orEmpty()
    )
}

fun GetPlaylistsByUserQuery.Node.toPlaylistMinimal(): PlaylistMinimal {
    return PlaylistMinimal(
        id = this.id,
        title = this.title,
        albumCovers = this.albumCovers.orEmpty(),
        isSavedByMe = this.isSavedByMe,
        savedCount = this.savedCount,
        username = this.creator.username,
        coverImage = this.coverImage.orEmpty()
    )
}

fun PlaylistSortOrderDomain.toRemote(): PlaylistSortOrder {
    return when (this) {
        PlaylistSortOrderDomain.RECENTS -> PlaylistSortOrder.RECENTS
        PlaylistSortOrderDomain.RECENTLY_ADDED -> PlaylistSortOrder.RECENTLY_ADDED
        PlaylistSortOrderDomain.ALPHABETICAL -> PlaylistSortOrder.ALPHABETICAL
        PlaylistSortOrderDomain.CREATOR -> PlaylistSortOrder.CREATOR
        PlaylistSortOrderDomain.UNKNOWN -> PlaylistSortOrder.UNKNOWN__
    }
}