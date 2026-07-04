package com.example.data.api.search

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.example.data.util.safeMutation
import com.example.data.util.safeQuery
import com.example.graphql.PlayYoutubeMutation
import com.example.graphql.SearchQuery
import com.example.graphql.SearchYoutubeFromApiQuery
import com.example.graphql.SearchYoutubeFromYtDlpQuery
class SearchApiImpl(
    private val apolloClient: ApolloClient
): SearchApi {

    override suspend fun search(
        query: String,
        cursorAfter: String?,
        pageSize: Int
    ): Result<SearchQuery.Search> {
        return apolloClient.safeQuery(
            SearchQuery(
                query = Optional.presentIfNotNull(query),
                after = Optional.presentIfNotNull(cursorAfter),
                limit = Optional.presentIfNotNull(pageSize)
            )
        ) {
            it.search
        }
    }

    override suspend fun searchYoutubeFromApi(
        query: String,
        after: String?,
        pageSize: Int
    ): Result<SearchYoutubeFromApiQuery.SearchYoutubeFromApi> {
        return apolloClient.safeQuery(
            SearchYoutubeFromApiQuery(
                searchValue = query,
                after = Optional.presentIfNotNull(after),
                first = Optional.presentIfNotNull(pageSize)
            )
        ) {
            it.searchYoutubeFromApi
        }
    }

    override suspend fun searchYoutubeFromYtDlp(
        query: String,
        after: String?,
        pageSize: Int
    ): Result<SearchYoutubeFromYtDlpQuery.SearchYoutubeFromYtDlp> {
        return apolloClient.safeQuery(
            SearchYoutubeFromYtDlpQuery(
                searchValue = query,
                after = Optional.presentIfNotNull(after),
                first = Optional.presentIfNotNull(pageSize)
            )
        ) {
            it.searchYoutubeFromYtDlp
        }
    }

    override suspend fun playYoutube(youtubeId: String): Result<PlayYoutubeMutation.PlayYoutube> {
        return apolloClient.safeMutation(
            PlayYoutubeMutation(youtubeId)
        ) {
            it.playYoutube
        }
    }
}
