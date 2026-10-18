package com.example.domain.entity.search

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class SearchItemMapperTest {

    @Test
    fun `TrackSearch toTrackDomain maps all fields correctly`() {
        val trackSearch = SearchItemDomain.TrackSearch(
            id = "track_123",
            title = "Billie Jean",
            duration = 294,
            albumCoverUrl = "https://example.com/thriller.jpg",
            artistName = "Michael Jackson"
        )

        val trackDomain = trackSearch.toTrackDomain()

        assertEquals("track_123", trackDomain.id)
        assertEquals("Billie Jean", trackDomain.title)
        assertEquals(294, trackDomain.duration)
        assertEquals("https://example.com/thriller.jpg", trackDomain.albumCoverUrl)
        assertEquals("Michael Jackson", trackDomain.artistName)
        assertEquals(0, trackDomain.streamCount)
        assertFalse(trackDomain.explicit)
        assertEquals("", trackDomain.releaseDate)
    }

    @Test
    fun `YoutubeTrackSearch toTrackDomain maps all fields correctly`() {
        val ytSearch = SearchItemDomain.YoutubeTrackSearch(
            id = "yt_456",
            title = "Beat It",
            duration = 258,
            durationString = "4:18",
            thumbnail = "https://example.com/thumb.jpg",
            channel = "Michael Jackson Official",
            channelId = "chan_789",
            uploaderId = "up_101",
            viewCount = 1_000_000L,
            fromApi = true
        )

        val trackDomain = ytSearch.toTrackDomain()

        assertEquals("yt_456", trackDomain.id)
        assertEquals("Beat It", trackDomain.title)
        assertEquals(258, trackDomain.duration)
        assertEquals("https://example.com/thumb.jpg", trackDomain.albumCoverUrl)
        assertEquals("Michael Jackson Official", trackDomain.artistName)
        assertEquals(1_000_000, trackDomain.streamCount)
        assertFalse(trackDomain.explicit)
        assertEquals("", trackDomain.releaseDate)
    }
}
