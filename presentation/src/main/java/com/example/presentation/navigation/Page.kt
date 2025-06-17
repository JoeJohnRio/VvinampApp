package com.example.presentation.navigation

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
    data class MusicDetails(val musicId: Int) : Page()

    @Serializable
    data object Register : Page()
}

sealed class Graph {
    @Serializable
    data object Main : Graph()
}

fun Page.route(): String? {
    return this.javaClass.canonicalName
}