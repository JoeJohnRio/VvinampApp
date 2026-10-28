package com.example.vvinamp.ui.home

import com.example.domain.util.NetworkMonitor
import com.example.domain.util.NetworkState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeNetworkMonitor : NetworkMonitor {

    val networkStateFlow = MutableSharedFlow<NetworkState>(replay = 1)

    override val networkState: Flow<NetworkState> = networkStateFlow
}
