package com.example.vvinamp.ui.main.player

import com.example.domain.entity.song.TrackDomain
import kotlin.test.Test
import kotlin.test.assertEquals

class ResolveArtistImageUrlTest {

    @Test
    fun `returns empty string when track is null`() {
        assertEquals("", resolveArtistImageUrl(null))
    }

    @Test
    fun `returns explicit artistProfileImageUrl when present`() {
        val track = TrackDomain(
            id = "track-1",
            artistName = "Bernadya",
            albumCoverUrl = "https://example.com/cover.jpg",
            title = "Satu Bulan",
            duration = 180,
            streamCount = 100,
            explicit = false,
            releaseDate = "2024",
            artistProfileImageUrl = "https://example.com/bernadya_explicit.jpg"
        )
        assertEquals("https://example.com/bernadya_explicit.jpg", resolveArtistImageUrl(track))
    }

    @Test
    fun `extracts artist image from supabase structured album cover path`() {
        val track = TrackDomain(
            id = "track-2",
            artistName = "Bernadya",
            albumCoverUrl = "https://bumcqqhbtmmrkeiwlsyy.supabase.co/storage/v1/object/public/media/artist/bernadya/album/sialnya/cover.jpg",
            title = "Kata Mereka Ini Berlebihan",
            duration = 190,
            streamCount = 200,
            explicit = false,
            releaseDate = "2024"
        )
        val expected = "https://bumcqqhbtmmrkeiwlsyy.supabase.co/storage/v1/object/public/media/artist/bernadya/bernadya.jpg"
        assertEquals(expected, resolveArtistImageUrl(track))
    }

    @Test
    fun `falls back to albumCoverUrl for youtube tracks without fabricating supabase url`() {
        val track = TrackDomain(
            id = "t84PAaVX8z4",
            artistName = "Music4u",
            albumCoverUrl = "https://i.ytimg.com/vi/t84PAaVX8z4/hqdefault.jpg",
            title = "Music4u - Unsaid Apologies",
            duration = 180,
            streamCount = 5000,
            explicit = false,
            releaseDate = ""
        )
        // Must NOT return fake https://bumcqqhbtmmrkeiwlsyy.supabase.co/storage/v1/object/public/media/artist/music4u/music4u.jpg
        assertEquals("https://i.ytimg.com/vi/t84PAaVX8z4/hqdefault.jpg", resolveArtistImageUrl(track))
    }
}
