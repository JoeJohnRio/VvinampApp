package com.example.data.api.playlist

import com.example.domain.entity.playlist.PlaylistMinimal
import com.example.domain.entity.playlist.PlaylistSortOrderDomain
import com.example.graphql.GetMyPlaylistFeedQuery
import com.example.graphql.GetMyPlaylistsQuery
import com.example.graphql.GetPlaylistsByUserQuery
import com.example.graphql.type.PlaylistSortOrder

fun GetMyPlaylistsQuery.Node.toPlaylistMinimal(): PlaylistMinimal {
    return PlaylistMinimal(
        id = this.id,
        title = this.title,
        coverImage = this.coverImage,
        isSavedByMe = this.isSavedByMe,
        savedCount = this.savedCount,
        username = this.creator.username
    )
}

fun GetMyPlaylistFeedQuery.Node.toPlaylistMinimal(): PlaylistMinimal {
    return PlaylistMinimal(
        id = this.id,
        title = this.title,
        coverImage = this.coverImage,
        isSavedByMe = this.isSavedByMe,
        savedCount = this.savedCount,
        username = this.creator.username
    )
}

fun GetPlaylistsByUserQuery.Node.toPlaylistMinimal(): PlaylistMinimal {
    return PlaylistMinimal(
        id = this.id,
        title = this.title,
        coverImage = this.coverImage,
        isSavedByMe = this.isSavedByMe,
        savedCount = this.savedCount,
        username = this.creator.username
    )
}

fun PlaylistSortOrderDomain.toRemote(): PlaylistSortOrder {
    return when (this) {
        PlaylistSortOrderDomain.RECENTS -> PlaylistSortOrder.RECENTS
        PlaylistSortOrderDomain.RECENTLY_ADDED -> PlaylistSortOrder.RECENTLY_ADDED
        PlaylistSortOrderDomain.ALPHABETICAL -> PlaylistSortOrder.ALPHABETICAL
        PlaylistSortOrderDomain.CREATOR -> PlaylistSortOrder.CREATOR
        PlaylistSortOrderDomain.UNKNOWN__ -> PlaylistSortOrder.UNKNOWN__
    }
}