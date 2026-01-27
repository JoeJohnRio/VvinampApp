package com.example.presentation.ui.home

import com.example.domain.entity.ListeningHistoryDomain

sealed class HomeEvent {
    data class OnHistoryClicked(val history: ListeningHistoryDomain): HomeEvent()
}