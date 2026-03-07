package com.example.domain.entity

data class PageResponse<T>(
    val items: List<T>,
    val endCursor: String?,
    val hasNextPage: Boolean
)