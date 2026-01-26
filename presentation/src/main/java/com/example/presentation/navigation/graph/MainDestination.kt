package com.example.presentation.navigation.graph

import androidx.navigation3.runtime.NavKey
import com.example.domain.entity.ListeningHistory
import kotlinx.serialization.Serializable

sealed interface MainDestination: NavKey {
    @Serializable
    data object NavigationBar : MainDestination

    @Serializable
    data class Artist(val history: ListeningHistory) : MainDestination

    @Serializable
    data object Root : MainDestination

    @Serializable
    data object Main : MainDestination

    @Serializable
    data object Settings : MainDestination
}