package com.example.presentation.ui.settings

sealed class SettingsEvent {
    data object OnLogoutClicked: SettingsEvent()
}