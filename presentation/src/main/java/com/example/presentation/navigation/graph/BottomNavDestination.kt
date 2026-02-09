package com.example.presentation.navigation.graph

import androidx.navigation3.runtime.NavKey
import com.example.domain.entity.ListeningHistoryDomain
import kotlinx.serialization.Serializable

sealed interface BottomNavDestination: NavKey {
    @Serializable
    data object Home : MainDestination

    @Serializable
    data object Search : MainDestination

    @Serializable
    data object Library : MainDestination

    @Serializable
    data object Premium : MainDestination

    @Serializable
    data object Create : MainDestination

    @Serializable
    data class Artist(val history: ListeningHistoryDomain) : MainDestination

    @Serializable
    data object Settings : MainDestination
}