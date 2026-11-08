package com.example.vvinamp.ui.search

enum class SearchType(val label: String) {
    YOUTUBE("YouTube"),
    CATALOG("Catalog")
}

data class SearchUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchText: String = "",
    val searchType: SearchType = SearchType.YOUTUBE,
    val statusNotice: String? = null,
    val trackForPlaylist: com.example.domain.entity.search.SearchItemDomain.YoutubeTrackSearch? = null,
    val isAddingToPlaylist: Boolean = false
)
