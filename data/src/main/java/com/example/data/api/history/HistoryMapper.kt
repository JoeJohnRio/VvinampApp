package com.example.data.api.history

import com.example.domain.entity.ListeningHistoryDomain
import com.example.graphql.ListRecentHistoryQuery
import com.example.graphql.type.ContentType
import com.example.domain.entity.enum.ContentTypeDomain as DomainContentType


fun List<ListRecentHistoryQuery.ListRecentHistory?>.toDomain(): List<ListeningHistoryDomain> {
    return this.mapNotNull { data ->
        data?.let {
            ListeningHistoryDomain(
                id = data.id,
                playedAt = data.playedAt.toString(),
                progress = data.progress ?: 0,
                contentName = data.contentName,
                contentTypeDomain = data.contentType.toContentType(),
                contentId = data.contentId,
                contentImage = data.contentImage.orEmpty(),
                contentSubtitle = data.contentSubtitle.orEmpty()
            )
        }
    }
}

fun ContentType.toContentType(): DomainContentType {
    when (this) {
        ContentType.TRACK -> return DomainContentType.TRACK
        ContentType.PODCAST_EPISODE -> return DomainContentType.PODCAST_EPISODE
        ContentType.PLAYLIST -> return DomainContentType.PLAYLIST
        ContentType.PODCAST_SERIES -> return DomainContentType.PODCAST_SERIES
        ContentType.ALBUM -> return DomainContentType.ALBUM
        ContentType.ARTIST -> return DomainContentType.ARTIST
        ContentType.UNKNOWN__ -> return DomainContentType.UNKNOWN__
    }
}