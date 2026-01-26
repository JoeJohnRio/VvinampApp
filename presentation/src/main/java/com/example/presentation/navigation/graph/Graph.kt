package com.example.presentation.navigation.graph

import kotlinx.serialization.Serializable

sealed class Graph {
    @Serializable
    data object Main : Graph()

    @Serializable
    data object MainFlow : Graph()

    @Serializable
    data object AuthFlow : Graph()
}