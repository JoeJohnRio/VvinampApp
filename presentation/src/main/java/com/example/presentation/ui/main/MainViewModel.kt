package com.example.presentation.ui.main


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.auth.AuthStore // Make sure this import is correct
import com.example.domain.entity.SessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authStore: AuthStore
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
    private val _events = MutableSharedFlow<MainUiEvent>()
    val events = _events.asSharedFlow()

    val sessionState: StateFlow<SessionState> = authStore.getSession()
        .map { session ->
            if (session != null && session.accessToken.isNotBlank()) {
                SessionState.LoggedIn
            } else {
                SessionState.LoggedOut
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SessionState.Loading
        )
}