package com.example.vvinamp.ui.search

enum class SearchType(val label: String) {
    YOUTUBE("YouTube"),
    CATALOG("Catalog")
}

data class SearchUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchText: String = "",
    val submittedText: String = "",
    val searchType: SearchType = SearchType.YOUTUBE,
    val trackForPlaylist: com.example.domain.entity.search.SearchItemDomain? = null,
    val isAddingToPlaylist: Boolean = false,
    // Set when search was opened from a playlist's "Add": songs go straight into it.
    val targetPlaylistId: String? = null,
    val targetPlaylistTitle: String? = null,
    // Keys "playlistId/songId" for songs added this visit, so their button shows a tick.
    val addedKeys: Set<String> = emptySet(),
    val addingIds: Set<String> = emptySet()
) {
    fun isAddedToTarget(itemId: String): Boolean =
        targetPlaylistId != null && "$targetPlaylistId/$itemId" in addedKeys

    val activeQuery: String
        get() = if (searchType == SearchType.YOUTUBE) submittedText else searchText
}

// Catalog and YouTube ids can overlap, so the key includes the kind.
fun com.example.domain.entity.search.SearchItemDomain.addKey(): String = when (this) {
    is com.example.domain.entity.search.SearchItemDomain.TrackSearch -> "track_$id"
    is com.example.domain.entity.search.SearchItemDomain.YoutubeTrackSearch -> "youtube_$id"
    else -> toString()
}
