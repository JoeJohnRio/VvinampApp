package com.example.data.api.search

import com.example.graphql.PlayYoutubeMutation
import com.example.graphql.SearchQuery
import com.example.graphql.SearchYoutubeFromApiQuery
import com.example.graphql.SearchYoutubeFromYtDlpQuery

interface SearchApi {

    suspend fun search(
        query: String,
        cursorAfter: String?,
        pageSize: Int
    ): Result<SearchQuery.Search>

    suspend fun searchYoutubeFromApi(
        query: String,
        after: String?,
        pageSize: Int
    ): Result<SearchYoutubeFromApiQuery.SearchYoutubeFromApi>

    suspend fun searchYoutubeFromYtDlp(
        query: String,
        after: String?,
        pageSize: Int
    ): Result<SearchYoutubeFromYtDlpQuery.SearchYoutubeFromYtDlp>

    suspend fun playYoutube(youtubeId: String): Result<PlayYoutubeMutation.PlayYoutube>
}
