package com.example.data.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.example.domain.entity.song.TrackDomain

@Entity(
    tableName = "playlist_tracks",
    primaryKeys = ["playlistId", "id"]
)
data class PlaylistTrackEntity(
    val id: String,
    val playlistId: String,
    val artistName: String,
    val albumCoverUrl: String,
    val title: String,
    val duration: Int,
    val streamCount: Int,
    val explicit: Boolean,
    val releaseDate: String,
    val position: Int // To maintain order
)

fun PlaylistTrackEntity.toDomain() = TrackDomain(
    id = id,
    artistName = artistName,
    albumCoverUrl = albumCoverUrl,
    title = title,
    duration = duration,
    streamCount = streamCount,
    explicit = explicit,
    releaseDate = releaseDate
)

fun TrackDomain.toEntity(playlistId: String, position: Int) = PlaylistTrackEntity(
    id = id,
    playlistId = playlistId,
    artistName = artistName,
    albumCoverUrl = albumCoverUrl,
    title = title,
    duration = duration,
    streamCount = streamCount,
    explicit = explicit,
    releaseDate = releaseDate,
    position = position
)
