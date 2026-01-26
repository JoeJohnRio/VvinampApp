package com.example.presentation.ui.home

import com.example.domain.entity.ListeningHistory

sealed class HomeEvent {
    data class OnHistoryClicked(val history: ListeningHistory): HomeEvent()
}