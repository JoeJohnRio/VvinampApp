package com.example.presentation.ui.main

sealed class MainUiEvent {
    data object NavigateToSettings : MainUiEvent()
}