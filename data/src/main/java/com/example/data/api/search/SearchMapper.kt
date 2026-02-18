package com.example.data.api.search

import com.example.domain.entity.search.SearchItemDomain
import com.example.graphql.SearchQuery

fun SearchQuery.Node.toDomain(): SearchItemDomain? {
    return when {
        onTrack != null -> onTrack?.let { track ->
            SearchItemDomain.TrackSearch(
                id = track.id,
                title = track.title,
                duration = track.duration,
                albumCoverUrl = track.albumCoverUrl,
                artistName = track.artistName
            )
        }

        onAlbum != null -> onAlbum?.let { album ->
            SearchItemDomain.AlbumSearch(
                id = album.id,
                title = album.title,
                coverArt = album.coverArt,
                artistName = album.artist.name
            )
        }

        onArtist != null -> onArtist?.let { artist ->
            SearchItemDomain.ArtistSearch(
                id = artist.id,
                name = artist.name,
                verified = artist.verified,
                profileImage = artist.profileImage
            )
        }

        onPlaylist != null -> onPlaylist?.let { playlist ->
            SearchItemDomain.PlaylistSearch(
                id = playlist.id,
                title = playlist.title,
                coverImage = playlist.coverImage
            )
        }

        onPodcastEpisode != null -> onPodcastEpisode?.let { episode ->
            SearchItemDomain.PodcastEpisodeSearch(
                id = episode.id,
                title = episode.title,
                coverArt = episode.coverArt,
                duration = episode.duration
            )
        }

        onPodcastSeries != null -> onPodcastSeries?.let { series ->
            SearchItemDomain.PodcastSeriesSearch(
                id = series.id,
                title = series.title,
                coverArt = series.coverArt,
                publisher = series.publisher
            )
        }

        else -> null
    }
}
