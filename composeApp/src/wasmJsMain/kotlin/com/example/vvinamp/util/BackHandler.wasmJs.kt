package com.example.vvinamp.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.browser.window
import org.w3c.dom.events.KeyboardEvent

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    val currentOnBack by rememberUpdatedState(onBack)

    DisposableEffect(enabled) {
        if (enabled) {
            // Define the listener
            val handleKeyDown = { event: org.w3c.dom.events.Event ->
                val keyboardEvent = event as? KeyboardEvent
                if (keyboardEvent?.key == "Escape") {
                    currentOnBack()
                }
            }

            // Add the listener to the window
            window.addEventListener("keydown", handleKeyDown)

            onDispose {
                // Clean up when the component is removed or enabled becomes false
                window.removeEventListener("keydown", handleKeyDown)
            }
        } else {
            onDispose {}
        }
    }
}
