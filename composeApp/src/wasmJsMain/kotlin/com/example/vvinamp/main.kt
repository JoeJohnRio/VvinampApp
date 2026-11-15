package com.example.vvinamp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeViewport
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.example.vvinamp.di.initKoin
import androidx.compose.material3.MaterialTheme
import com.example.vvinamp.ui.theme.AppTheme
import com.example.vvinamp.ui.theme.PhoneColumnMaxWidth

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()
    SingletonImageLoader.setSafe { context ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory())
            }
            .build()
    }
    ComposeViewport {
        // App() applies the theme too; this outer one only colours the page around the phone column.
        AppTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceDim),
                contentAlignment = Alignment.TopCenter
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .widthIn(max = PhoneColumnMaxWidth)
                        .fillMaxWidth()
                ) {
                    App()
                }
            }
        }
    }
}
