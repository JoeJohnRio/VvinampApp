package com.example.data.api.playback

import com.example.domain.entity.enum.ContentType as ContentTypeDomain
import com.example.domain.entity.playbackinfo.PlaybackInfoDomain
import com.example.graphql.PlaybackInfoQuery
import com.example.graphql.type.ContentType

fun PlaybackInfoQuery.PlaybackInfo.toDomain(): PlaybackInfoDomain {
    return PlaybackInfoDomain(
        hlsUrl = this.hlsUrl,
        mp3Fallback = this.mp3Fallback,
        durationMs = this.durationMs
    )
}


fun ContentTypeDomain.toGraphQL(): ContentType {
    return when (this) {
        ContentTypeDomain.TRACK -> ContentType.TRACK
        ContentTypeDomain.PODCAST_EPISODE -> ContentType.PODCAST_EPISODE
        ContentTypeDomain.PLAYLIST -> ContentType.PLAYLIST
        ContentTypeDomain.PODCAST_SERIES -> ContentType.PODCAST_SERIES
        ContentTypeDomain.ALBUM -> ContentType.ALBUM
        ContentTypeDomain.ARTIST -> ContentType.ARTIST
        ContentTypeDomain.UNKNOWN__ -> ContentType.UNKNOWN__
    }
}
