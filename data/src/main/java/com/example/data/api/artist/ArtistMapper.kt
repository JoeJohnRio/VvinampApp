package com.example.data.api.artist

import com.example.domain.entity.artist.ArtistProfileDomain
import com.example.domain.entity.artist.ArtistScreenDomain
import com.example.domain.entity.song.AlbumDomain
import com.example.domain.entity.song.TopReleasesDomain
import com.example.domain.entity.song.TopSongsDomain
import com.example.domain.entity.song.TrackDomain
import com.example.graphql.GetArtistScreenDataQuery

fun GetArtistScreenDataQuery.GetArtistScreenData.toDomain(): ArtistScreenDomain {
    return ArtistScreenDomain(
        artistProfile = artistProfile?.toDomain(),
        topSongs = topSongs?.toDomain(),
        topReleases = topReleases?.toDomain(),
    )
}

fun GetArtistScreenDataQuery.ArtistProfile.toDomain(): ArtistProfileDomain {
    return ArtistProfileDomain(
        id = id,
        name = name,
        bio = bio,
        profileImage = profileImage,
        verified = verified,
        monthlyListeners = monthlyListeners,
    )
}

fun GetArtistScreenDataQuery.TopSongs.toDomain(): TopSongsDomain {
    return TopSongsDomain(
        success = success,
        error = error,
        tracks = tracks.map { it.toDomain() },
    )
}

fun GetArtistScreenDataQuery.Track.toDomain(): TrackDomain {
    return TrackDomain(
        id = id,
        title = title,
        duration = duration,
        streamCount = streamCount,
        explicit = explicit,
        releaseDate = releaseDate,
        audioFileUrl = audioFileUrl,
    )
}

fun GetArtistScreenDataQuery.TopReleases.toDomain(): TopReleasesDomain {
    return TopReleasesDomain(
        success = success,
        error = error,
        albums = albums.map { it.toDomain() },
    )
}

fun GetArtistScreenDataQuery.Album.toDomain(): AlbumDomain {
    return AlbumDomain(
        id = id,
        title = title,
        releaseDate = releaseDate,
        coverArt = coverArt
    )
}