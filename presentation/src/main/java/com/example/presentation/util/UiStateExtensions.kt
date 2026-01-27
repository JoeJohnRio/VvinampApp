package com.example.presentation.util

import androidx.compose.runtime.Composable
import com.apollographql.apollo.exception.OfflineException.message

@Composable
fun <T> UiState<T>.Render(
    onSuccess: @Composable (T) -> Unit,
    onLoading: @Composable () -> Unit = {},
    onError: @Composable (String?) -> Unit = {}
) {
    when (this) {
        is UiState.Success -> onSuccess(data)
        is UiState.Loading -> onLoading()
        is UiState.Error -> onError(message)
        is UiState.Idle -> { }
    }
}