package com.example.domain.entity.album

import com.example.domain.entity.search.SearchItemDomain
import com.example.domain.entity.song.TrackDomain

fun SimplifiedTrackDomain.toTrackDomain(): TrackDomain {
    return TrackDomain(
        id = id,
        title = title,
        duration = duration,
        albumCoverUrl = "",
        artistName = artistName,
        streamCount = 0,
        explicit = false,
        releaseDate = "",
    )
}