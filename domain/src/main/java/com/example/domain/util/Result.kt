package com.example.domain.util

import com.example.domain.util.Result.Error
import com.example.domain.util.Result.Success

sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error<T>(val error: Throwable) : Result<T>()
}

inline fun <T> Result<T>.onSuccess(
    block: (T) -> Unit
): Result<T> = if (this is Success) also { block(data) } else this

inline fun <T> Result<T>.onError(
    block: (Throwable) -> Unit
): Result<T> = if (this is Error) also { block(error) } else this

fun <T> Result<T>.asSuccessOrNull(): T? = (this as? Success)?.data

inline fun <A, T> Result<T>.map(transform: (T) -> A): Result<A> {
    return when (this) {
        is Success -> Success(transform(data))
        is Error -> Error(error)
    }
}