package com.example.data.api.playback

import com.example.domain.entity.enum.PlaybackContextType as PlaybackContextTypeDomain
import com.example.domain.entity.playbackinfo.PlaybackInfoDomain
import com.example.domain.entity.playbackinfo.PlaybackStateDomain
import com.example.domain.entity.playbackinfo.PlayerQueueDomain
import com.example.domain.entity.playbackinfo.TrackMinimalDomain
import com.example.domain.entity.enum.ContentTypeDomain
import com.example.graphql.AddToQueueMutation
import com.example.graphql.ClearQueueMutation
import com.example.graphql.GetPlayerQueueQuery
import com.example.graphql.PlayContextMutation
import com.example.graphql.PlaybackInfoQuery
import com.example.graphql.SkipToNextMutation
import com.example.graphql.SkipToPreviousMutation
import com.example.graphql.PlayYoutubeMutation
import com.example.graphql.type.ContextType
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

fun PlaybackContextTypeDomain.toGraphQL(): ContextType {
    return when (this) {
        PlaybackContextTypeDomain.PLAYLIST -> ContextType.PLAYLIST
        PlaybackContextTypeDomain.ALBUM -> ContextType.ALBUM
        PlaybackContextTypeDomain.ARTIST -> ContextType.ARTIST
        PlaybackContextTypeDomain.SEARCH_RESULTS -> ContextType.SEARCH_RESULTS
    }
}

fun GetPlayerQueueQuery.PlayerQueue.toDomain(): PlayerQueueDomain {
    return PlayerQueueDomain(
        currentTrack = this.currentTrack?.toDomain(),
        upcomingTrackIds = this.upcomingTrack.mapNotNull { it?.toDomain() },
        previousTrack = this.previousTrack.mapNotNull { it?.toDomain() }
    )
}

fun AddToQueueMutation.AddToQueue.toDomain(): PlayerQueueDomain {
    return PlayerQueueDomain(
        currentTrack = this.currentTrack?.toDomain(),
        upcomingTrackIds = this.upcomingTrack.mapNotNull { it?.toDomain() },
        previousTrack = this.previousTrack.mapNotNull { it?.toDomain() }
    )
}

fun SkipToNextMutation.SkipToNext.toDomain(): PlaybackStateDomain {
    return PlaybackStateDomain(
        queue = this.queue.toDomain(),
        playbackInfo = this.playbackInfo.toDomain()
    )
}

fun SkipToNextMutation.Queue.toDomain(): PlayerQueueDomain {
    return PlayerQueueDomain(
        currentTrack = this.currentTrack?.toDomain(),
        upcomingTrackIds = this.upcomingTrack.mapNotNull { it?.toDomain() },
        previousTrack = this.previousTrack.mapNotNull { it?.toDomain() }
    )
}

fun SkipToNextMutation.PlaybackInfo.toDomain(): PlaybackInfoDomain {
    return PlaybackInfoDomain(
        hlsUrl = this.hlsUrl,
        mp3Fallback = this.mp3Fallback,
        durationMs = this.durationMs
    )
}

fun SkipToPreviousMutation.SkipToPrevious.toDomain(): PlaybackStateDomain {
    return PlaybackStateDomain(
        queue = this.queue.toDomain(),
        playbackInfo = this.playbackInfo.toDomain()
    )
}

fun SkipToPreviousMutation.Queue.toDomain(): PlayerQueueDomain {
    return PlayerQueueDomain(
        currentTrack = this.currentTrack?.toDomain(),
        upcomingTrackIds = this.upcomingTrack.mapNotNull { it?.toDomain() },
        previousTrack = this.previousTrack.mapNotNull { it?.toDomain() }
    )
}

fun SkipToPreviousMutation.PlaybackInfo.toDomain(): PlaybackInfoDomain {
    return PlaybackInfoDomain(
        hlsUrl = this.hlsUrl,
        mp3Fallback = this.mp3Fallback,
        durationMs = this.durationMs
    )
}

fun ClearQueueMutation.ClearQueue.toDomain(): PlayerQueueDomain {
    return PlayerQueueDomain(
        currentTrack = this.currentTrack?.toDomain(),
        upcomingTrackIds = emptyList(),
        previousTrack = emptyList()
    )
}

fun PlayContextMutation.PlayContext.toDomain(): PlaybackStateDomain {
    return PlaybackStateDomain(
        queue = this.queue.toDomain(),
        playbackInfo = this.playbackInfo.toDomain()
    )
}

fun PlayContextMutation.Queue.toDomain(): PlayerQueueDomain {
    return PlayerQueueDomain(
        currentTrack = this.currentTrack?.toDomain(),
        upcomingTrackIds = this.upcomingTrack.mapNotNull { it?.toDomain() },
        previousTrack = this.previousTrack.mapNotNull { it?.toDomain() }
    )
}

fun PlayContextMutation.PlaybackInfo.toDomain(): PlaybackInfoDomain {
    return PlaybackInfoDomain(
        hlsUrl = this.hlsUrl,
        mp3Fallback = this.mp3Fallback,
        durationMs = this.durationMs
    )
}

fun PlayContextMutation.CurrentTrack.toDomain(): TrackMinimalDomain {
    return TrackMinimalDomain(
        trackId = this.trackId,
        title = this.title ?: "",
        artist = this.artist ?: "",
        coverImageUrl = this.coverImageUrl ?: ""
    )
}

fun PlayContextMutation.UpcomingTrack.toDomain(): TrackMinimalDomain {
    return TrackMinimalDomain(
        trackId = this.trackId,
        title = this.title ?: "",
        artist = this.artist ?: "",
        coverImageUrl = this.coverImageUrl ?: ""
    )
}

fun PlayContextMutation.PreviousTrack.toDomain(): TrackMinimalDomain {
    return TrackMinimalDomain(
        trackId = this.trackId,
        title = this.title ?: "",
        artist = this.artist ?: "",
        coverImageUrl = this.coverImageUrl ?: ""
    )
}

fun GetPlayerQueueQuery.CurrentTrack.toDomain(): TrackMinimalDomain {
    return TrackMinimalDomain(
        trackId = this.trackId,
        title = this.title ?: "",
        artist = this.artist ?: "",
        coverImageUrl = this.coverImageUrl ?: ""
    )
}

fun GetPlayerQueueQuery.UpcomingTrack.toDomain(): TrackMinimalDomain {
    return TrackMinimalDomain(
        trackId = this.trackId,
        title = this.title ?: "",
        artist = this.artist ?: "",
        coverImageUrl = this.coverImageUrl ?: ""
    )
}

fun GetPlayerQueueQuery.PreviousTrack.toDomain(): TrackMinimalDomain {
    return TrackMinimalDomain(
        trackId = this.trackId,
        title = this.title ?: "",
        artist = this.artist ?: "",
        coverImageUrl = this.coverImageUrl ?: ""
    )
}

fun AddToQueueMutation.CurrentTrack.toDomain(): TrackMinimalDomain {
    return TrackMinimalDomain(
        trackId = this.trackId,
        title = this.title ?: "",
        artist = this.artist ?: "",
        coverImageUrl = this.coverImageUrl ?: ""
    )
}

fun AddToQueueMutation.UpcomingTrack.toDomain(): TrackMinimalDomain {
    return TrackMinimalDomain(
        trackId = this.trackId,
        title = this.title ?: "",
        artist = this.artist ?: "",
        coverImageUrl = this.coverImageUrl ?: ""
    )
}

fun AddToQueueMutation.PreviousTrack.toDomain(): TrackMinimalDomain {
    return TrackMinimalDomain(
        trackId = this.trackId,
        title = this.title ?: "",
        artist = this.artist ?: "",
        coverImageUrl = this.coverImageUrl ?: ""
    )
}

fun SkipToNextMutation.CurrentTrack.toDomain(): TrackMinimalDomain {
    return TrackMinimalDomain(
        trackId = this.trackId,
        title = this.title ?: "",
        artist = this.artist ?: "",
        coverImageUrl = this.coverImageUrl ?: ""
    )
}

fun SkipToNextMutation.UpcomingTrack.toDomain(): TrackMinimalDomain {
    return TrackMinimalDomain(
        trackId = this.trackId,
        title = this.title ?: "",
        artist = this.artist ?: "",
        coverImageUrl = this.coverImageUrl ?: ""
    )
}

fun SkipToNextMutation.PreviousTrack.toDomain(): TrackMinimalDomain {
    return TrackMinimalDomain(
        trackId = this.trackId,
        title = this.title ?: "",
        artist = this.artist ?: "",
        coverImageUrl = this.coverImageUrl ?: ""
    )
}

fun SkipToPreviousMutation.CurrentTrack.toDomain(): TrackMinimalDomain {
    return TrackMinimalDomain(
        trackId = this.trackId,
        title = this.title ?: "",
        artist = this.artist ?: "",
        coverImageUrl = this.coverImageUrl ?: ""
    )
}

fun SkipToPreviousMutation.UpcomingTrack.toDomain(): TrackMinimalDomain {
    return TrackMinimalDomain(
        trackId = this.trackId,
        title = this.title ?: "",
        artist = this.artist ?: "",
        coverImageUrl = this.coverImageUrl ?: ""
    )
}

fun SkipToPreviousMutation.PreviousTrack.toDomain(): TrackMinimalDomain {
    return TrackMinimalDomain(
        trackId = this.trackId,
        title = this.title ?: "",
        artist = this.artist ?: "",
        coverImageUrl = this.coverImageUrl ?: ""
    )
}

fun ClearQueueMutation.CurrentTrack.toDomain(): TrackMinimalDomain {
    return TrackMinimalDomain(
        trackId = this.trackId,
        title = this.title ?: "",
        artist = this.artist ?: "",
        coverImageUrl = this.coverImageUrl ?: ""
    )
}

fun PlayYoutubeMutation.PlayYoutube.toDomain(): PlaybackInfoDomain {
    return PlaybackInfoDomain(
        hlsUrl = this.hlsUrl,
        mp3Fallback = this.fileUrl,
        durationMs = 0
    )
}
