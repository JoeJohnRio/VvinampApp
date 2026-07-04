package com.example.vvinamp.ui.main

data class MainUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val error: String? = null
)