package com.example.vvinamp.ui.search

enum class SearchType(val label: String) {
    NORMAL("Normal"),
    YOUTUBE_API("YouTube API"),
    YOUTUBE_DL_P("YT-DLP")
}

data class SearchUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchText: String = "",
    val searchType: SearchType = SearchType.NORMAL,
    val statusNotice: String? = null,
    val trackForPlaylist: com.example.domain.entity.search.SearchItemDomain.YoutubeTrackSearch? = null,
    val isAddingToPlaylist: Boolean = false
)
