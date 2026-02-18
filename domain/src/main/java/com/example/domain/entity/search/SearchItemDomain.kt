package com.example.domain.entity.search

sealed class SearchItemDomain {
    // Domain/Model/SearchItem.kt
    data class TrackSearch(
        val id: String,
        val title: String,
        val duration: Int,
        val albumCoverUrl: String,
        val artistName: String,
    ) : SearchItemDomain()

    data class AlbumSearch(
        val id: String,
        val title: String,
        val coverArt: String?,
        val artistName: String,
    ) : SearchItemDomain()

    data class ArtistSearch(
        val id: String,
        val name: String,
        val verified: Boolean,
        val profileImage: String?,
    ) : SearchItemDomain()

    data class PlaylistSearch(
        val id: String,
        val title: String,
        val coverImage: String?,
    ) : SearchItemDomain()

    data class PodcastEpisodeSearch(
        val id: String,
        val title: String,
        val coverArt: String?,
        val duration: Int,
    ) : SearchItemDomain()

    data class PodcastSeriesSearch(
        val id: String,
        val title: String,
        val coverArt: String?,
        val publisher: String?,
    ) : SearchItemDomain()
}