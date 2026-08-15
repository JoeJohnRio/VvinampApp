package com.example.vvinamp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.entity.SessionState
import com.example.vvinamp.ui.main.AppNavHost
import com.example.vvinamp.ui.main.MainViewModel
import com.example.vvinamp.ui.theme.AppTheme
import com.example.vvinamp.ui.widget.LoadingScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    AppTheme {
        val mainViewModel: MainViewModel = koinViewModel()
        val sessionState by mainViewModel.sessionState.collectAsStateWithLifecycle()

        when (sessionState) {
            is SessionState.Loading -> {
                LoadingScreen()
            }

            is SessionState.LoggedIn -> {
                AppNavHost(
                    isLoggedIn = true
                )
            }

            is SessionState.LoggedOut -> {
                AppNavHost(
                    isLoggedIn = false
                )
            }
        }
    }
}
