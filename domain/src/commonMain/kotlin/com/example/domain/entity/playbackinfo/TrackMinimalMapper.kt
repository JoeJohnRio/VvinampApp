package com.example.domain.entity.playbackinfo

import com.example.domain.entity.song.TrackDomain

fun TrackMinimalDomain.toTrackDomain(): TrackDomain {
    return TrackDomain(
        id = this.trackId,
        artistName = this.artist,
        albumCoverUrl = this.coverImageUrl,
        title = this.title,
        duration = 0, // Not available in TrackMinimal
        streamCount = 0,
        explicit = false,
        releaseDate = ""
    )
}