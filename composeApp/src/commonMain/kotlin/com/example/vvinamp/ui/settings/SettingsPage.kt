package com.example.vvinamp.ui.settings
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey

import com.example.vvinamp.ui.widget.ButtonSolidCloseIconAndText

@Composable
fun EntryProviderScope<NavKey>.SettingsScreen() {
    Scaffold(
        topBar = {

        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize(1f)
                .padding(paddingValues)
        ) {
            SettingsContent()
        }
    }
}

@Composable
fun SettingsContent(
    settingsViewModel: SettingsViewModel = koinViewModel()
) {
    ButtonSolidCloseIconAndText(
        modifier = Modifier.padding(16.dp),
        onClick = {
            settingsViewModel.onEvent(SettingsEvent.OnLogoutClicked)
        },
        text = "Logout",
        textColor = MaterialTheme.colorScheme.error,
        containerColor = MaterialTheme.colorScheme.errorContainer,
        icon = painterResource(Res.drawable.ic_logout),
        iconTint = MaterialTheme.colorScheme.error,
    )
}