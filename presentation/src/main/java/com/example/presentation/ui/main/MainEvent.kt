package com.example.presentation.ui.main

sealed class MainEvent {
    data object onSettingsClicked : MainEvent()
}