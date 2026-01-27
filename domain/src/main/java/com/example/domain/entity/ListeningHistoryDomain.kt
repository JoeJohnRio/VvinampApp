package com.example.domain.entity

import com.example.domain.entity.enum.ContentType
import kotlinx.serialization.Serializable

@Serializable
data class ListeningHistoryDomain(
    val id: String,
    val playedAt: String,
    val progress: Int,
    val contentType: ContentType,
    val contentId: String,
    val contentImage: String,
    val contentName: String
)
