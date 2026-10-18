package com.example.data.util

import com.example.domain.util.NetworkMonitor
import com.example.domain.util.NetworkState
import kotlinx.browser.window
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.w3c.dom.events.Event

class NetworkMonitorImpl : NetworkMonitor {

    private var lastKnownStateWasOffline: Boolean = isNetworkAvailable().not()

    override val networkState: Flow<NetworkState> = callbackFlow {

        launch { send(createNetworkStatus(isNetworkAvailable())) }

        val onlineCallback: (Event) -> Unit = {
            val networkStatus = createNetworkStatus(true)
            launch { send(networkStatus) }
            lastKnownStateWasOffline = false
        }

        val offlineCallback: (Event) -> Unit = {
            val networkStatus = createNetworkStatus(false)
            launch { send(networkStatus) }
            lastKnownStateWasOffline = true
        }

        window.addEventListener("online", onlineCallback)
        window.addEventListener("offline", offlineCallback)

        awaitClose {
            window.removeEventListener("online", onlineCallback)
            window.removeEventListener("offline", offlineCallback)
        }
    }.distinctUntilChanged()

    private fun createNetworkStatus(isOnline: Boolean): NetworkState {
        val shouldRefresh = lastKnownStateWasOffline && isOnline
        return NetworkState(isOnline = isOnline, shouldRefresh = shouldRefresh)
    }

    fun isNetworkAvailable(): Boolean = window.navigator.onLine
}

