package com.example.presentation.navigation.graph

import androidx.navigation3.runtime.NavKey
import com.example.domain.entity.ListeningHistoryDomain
import kotlinx.serialization.Serializable

sealed interface MainDestination: NavKey {
    @Serializable
    data object NavigationBar : MainDestination

    @Serializable
    data class Artist(val history: ListeningHistoryDomain) : MainDestination

    @Serializable
    data object Root : MainDestination

    @Serializable
    data object Main : MainDestination

    @Serializable
    data object Settings : MainDestination
}