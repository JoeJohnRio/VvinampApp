package com.example.domain.util

import kotlinx.coroutines.flow.Flow

interface NetworkMonitor {
    val networkState: Flow<NetworkState>
}

data class NetworkState(
    val isOnline: Boolean,
    val shouldRefresh: Boolean
)