package com.example.presentation.ui.main

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.example.presentation.di.AppSettingsSharedPreference
import com.example.presentation.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        const val DARK_MODE = "dark_mode"
    }

    @Inject
    @AppSettingsSharedPreference
    lateinit var appSettings: SharedPreferences

    private fun enableDarkMode(enable: Boolean) = appSettings.edit().putBoolean(DARK_MODE, enable).commit()

    private fun isDarkModeEnabled() = appSettings.getBoolean(DARK_MODE, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            var darkMode by remember { mutableStateOf(isDarkModeEnabled()) }

            AppTheme(darkMode) {
                MainGraph(
                    mainNavController = navController,
                    darkMode = darkMode,
                    onThemeUpdated = {
                        val updated = !darkMode
                        enableDarkMode(updated)
                        darkMode = updated
                    }
                )
            }
        }
    }
}
