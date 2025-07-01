package com.example.presentation.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.RegisteredUser
import com.example.domain.usecase.AuthUsecase
import com.example.presentation.util.UiState
import com.example.presentation.util.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val registerUseCase: AuthUsecase
) : ViewModel() {

    private val _registerState = MutableStateFlow<UiState<RegisteredUser>>(UiState.Idle)
    val registerState = _registerState.asStateFlow()

    fun register() {
        viewModelScope.launch {
            _registerState.value = UiState.Loading
            _registerState.value = registerUseCase().toUiState()
        }
    }
}
