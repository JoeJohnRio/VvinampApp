package com.example.domain.entity

sealed class SessionState {
    object Loading : SessionState()
    object LoggedIn : SessionState()
    object LoggedOut : SessionState()
}