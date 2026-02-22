package com.example.data.api.album

import com.example.domain.entity.album.AlbumDomain
import com.example.domain.entity.album.SimplifiedTrackDomain
import com.example.graphql.GetAlbumQuery

fun GetAlbumQuery.Album.toDomain(): AlbumDomain {
    return AlbumDomain(
        id = id,
        title = title,
        artistName = artistName,
        artistImage = artistImage.orEmpty(),
        releaseDate = releaseDate,
        coverArt = coverArt,
        tracks = tracks.map { it.toDomain(coverArt.orEmpty()) },
        trackCount = trackCount,
        totalDuration = totalDuration,
    )
}

fun GetAlbumQuery.Track.toDomain(albumCover: String): SimplifiedTrackDomain {
    return SimplifiedTrackDomain(
        id = id,
        title = title,
        duration = duration,
        artistName = artistName,
        albumCover = albumCover
    )
}