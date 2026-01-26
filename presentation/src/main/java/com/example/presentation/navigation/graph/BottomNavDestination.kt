package com.example.presentation.navigation.graph

import androidx.navigation3.runtime.NavKey
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
}