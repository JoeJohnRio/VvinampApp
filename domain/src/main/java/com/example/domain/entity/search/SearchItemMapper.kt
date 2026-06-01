package com.example.domain.entity.search

import com.example.domain.entity.song.TrackDomain

fun SearchItemDomain.TrackSearch.toTrackDomain(): TrackDomain {
    return TrackDomain(
        id = id,
        title = title,
        duration = duration,
        albumCoverUrl = albumCoverUrl,
        artistName = artistName,
        streamCount = 0,
        explicit = false,
        releaseDate = "",
    )
}

fun SearchItemDomain.YoutubeTrackSearch.toTrackDomain(): TrackDomain {
    return TrackDomain(
        id = id,
        title = title,
        duration = duration,
        albumCoverUrl = thumbnail,
        artistName = channel,
        streamCount = viewCount.toInt(),
        explicit = false,
        releaseDate = ""
    )
}
