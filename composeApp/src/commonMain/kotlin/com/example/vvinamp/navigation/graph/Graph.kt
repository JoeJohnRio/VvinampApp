package com.example.vvinamp.navigation.graph

import kotlinx.serialization.Serializable

@Serializable
sealed class Graph {
    @Serializable
    data object Main : Graph()

    @Serializable
    data object MainFlow : Graph()

    @Serializable
    data object AuthFlow : Graph()
}