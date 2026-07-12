package com.example.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.entity.song.TrackDomain
import kotlinx.datetime.Clock

@Entity(tableName = "recent_playback")
data class RecentPlaybackEntity(
    @PrimaryKey val id: Int = 1, // Fixed ID to always store only one "current" state
    val trackId: String,
    val title: String,
    val artistName: String,
    val albumCoverUrl: String,
    val duration: Int,
    val lastPlayedAt: Long = kotlin.time.Clock.System.now().toEpochMilliseconds()
)

fun RecentPlaybackEntity.toDomain() = TrackDomain(
    id = trackId,
    title = title,
    artistName = artistName,
    albumCoverUrl = albumCoverUrl,
    duration = duration,
    streamCount = 0,
    explicit = false,
    releaseDate = ""
)

fun TrackDomain.toRecentEntity() = RecentPlaybackEntity(
    trackId = id,
    title = title,
    artistName = artistName,
    albumCoverUrl = albumCoverUrl,
    duration = duration
)
