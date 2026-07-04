package com.example.vvinamp.ui.settings

sealed class SettingsEvent {
    data object OnLogoutClicked: SettingsEvent()
}