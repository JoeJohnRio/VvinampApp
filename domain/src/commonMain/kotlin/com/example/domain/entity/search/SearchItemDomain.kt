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

    data class YoutubeTrackSearch(
        val id: String,
        val title: String,
        val duration: Int,
        val durationString: String,
        val thumbnail: String,
        val channel: String,
        val viewCount: Long,
        val channelId: String,
        val uploaderId: String,
        val fromApi: Boolean
    ) : SearchItemDomain()
}

val dummySearchItems: List<SearchItemDomain> = listOf(
    // 1. Artist (The Man Himself)
    SearchItemDomain.ArtistSearch(
        id = "artist_mj_01",
        name = "Michael Jackson",
        verified = true,
        profileImage = "https://example.com/mj_profile.jpg"
    ),

    // 2. Top Tracks
    SearchItemDomain.TrackSearch(
        id = "track_billie_jean",
        title = "Billie Jean",
        duration = 294, // 4:54
        albumCoverUrl = "https://example.com/thriller_cover.jpg",
        artistName = "Michael Jackson"
    ),

    // 3. Iconic Albums
    SearchItemDomain.AlbumSearch(
        id = "album_thriller",
        title = "Thriller",
        coverArt = "https://example.com/thriller_cover.jpg",
        artistName = "Michael Jackson"
    ),

    // 4. Playlists
    SearchItemDomain.PlaylistSearch(
        id = "playlist_king_of_pop",
        title = "This Is Michael Jackson",
        coverImage = "https://example.com/playlist_essentials.jpg"
    ),

    // 5. Podcast Episodes about MJ
    SearchItemDomain.PodcastEpisodeSearch(
        id = "pod_ep_mj_legacy",
        title = "The Legacy of Thriller: 40 Years Later",
        coverArt = "https://example.com/music_history_pod.jpg",
        duration = 3600 // 60 mins
    ),

    // 6. Podcast Series
    SearchItemDomain.PodcastSeriesSearch(
        id = "pod_series_pop_icons",
        title = "Icons of Pop Music",
        coverArt = "https://example.com/pop_icons_cover.jpg",
        publisher = "Music History Network"
    )
)
