package com.example.presentation.ui.auth.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.auth.AuthStore
import com.example.domain.entity.AuthSession
import com.example.domain.entity.RegisteredUser
import com.example.domain.entity.User
import com.example.domain.usecase.AuthUseCases
import com.example.presentation.navigation.router.AuthRouter
import com.example.presentation.util.UiState
import com.example.presentation.util.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val authStore: AuthStore
) : ViewModel() {
    var user by mutableStateOf(
        User()
    )
    val session = authStore.getSession().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000), // Stop when not observed for 5s
        null
    )
    private val _loginState = MutableStateFlow<UiState<AuthSession>>(UiState.Idle)
    val loginState = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<UiState<RegisteredUser>>(UiState.Idle)
    val registerState = _registerState.asStateFlow()

    fun resetAndPopBackStack(authRouter: AuthRouter) {
        user = User()
        _registerState.value = UiState.Idle

        authRouter.popBackToAuthRootPage()
    }

    fun login(
        userCred: User
    ) {
        viewModelScope.launch {
            _loginState.value = UiState.Loading
            val result = authUseCases.login(userCred.email, userCred.password)
            _loginState.value = result.toUiState()

            if (result.isSuccess) {
                val user = result.getOrNull()

                user?.let {
                    authStore.saveTokens(it.accessToken, it.refreshToken, it.expiresAt)
                }
            }
        }
    }

    fun register() {
        viewModelScope.launch {
            _registerState.value = UiState.Loading
            _registerState.value = authUseCases.register.invoke(user).toUiState()
        }
    }
}
