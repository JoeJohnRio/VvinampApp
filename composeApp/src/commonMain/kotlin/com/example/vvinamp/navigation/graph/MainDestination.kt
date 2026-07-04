package com.example.vvinamp.navigation.graph

import androidx.navigation3.runtime.NavKey
import com.example.domain.entity.ListeningHistoryDomain
import kotlinx.serialization.Serializable

sealed interface MainDestination: NavKey {
    @Serializable
    data object NavigationBar : MainDestination

    @Serializable
    data object Root : MainDestination

    @Serializable
    data object Main : MainDestination

}