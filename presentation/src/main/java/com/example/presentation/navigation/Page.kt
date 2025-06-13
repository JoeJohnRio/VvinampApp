package com.example.youtubemusicclone.navigation

import kotlinx.serialization.Serializable

sealed class Page {
    @Serializable
    data object NavigationBar : Page()

    @Serializable
    data object Music : Page()

    @Serializable
    data object Samples : Page()

    @Serializable
    data object Explore : Page()

    @Serializable
    data object Library : Page()

    @Serializable
    data object Search : Page()

    @Serializable
    data class MusicDetails(val movieId: Int) : Page()
}

sealed class Graph {
    @Serializable
    data object Main : Graph()
}

fun Page.route(): String? {
    return this.javaClass.canonicalName
}