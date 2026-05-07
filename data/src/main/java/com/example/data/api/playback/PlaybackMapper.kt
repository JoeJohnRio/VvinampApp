package com.example.data.api.playback

import com.example.domain.entity.enum.ContentType as ContentTypeDomain
import com.example.domain.entity.playbackinfo.PlaybackInfoDomain
import com.example.graphql.PlaybackInfoQuery
import com.example.graphql.type.ContentType
import com.example.domain.entity.playbackinfo.PlayerQueueDomain
import com.example.graphql.GetPlayerQueueQuery
import com.example.graphql.AddToQueueMutation
import com.example.graphql.SkipToNextMutation
import com.example.graphql.ClearQueueMutation

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

fun GetPlayerQueueQuery.PlayerQueue.toDomain(): PlayerQueueDomain {
    return PlayerQueueDomain(
        currentTrackId = this.currentTrackId,
        upcomingTrackIds = this.upcomingTrackIds
    )
}

fun AddToQueueMutation.AddToQueue.toDomain(): PlayerQueueDomain {
    return PlayerQueueDomain(
        currentTrackId = this.currentTrackId,
        upcomingTrackIds = this.upcomingTrackIds
    )
}

fun SkipToNextMutation.SkipToNext.toDomain(): PlayerQueueDomain {
    return PlayerQueueDomain(
        currentTrackId = this.currentTrackId,
        upcomingTrackIds = this.upcomingTrackIds
    )
}

fun ClearQueueMutation.ClearQueue.toDomain(): PlayerQueueDomain {
    return PlayerQueueDomain(
        currentTrackId = this.currentTrackId,
        upcomingTrackIds = this.upcomingTrackIds
    )
}
