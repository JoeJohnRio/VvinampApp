package com.example.domain.usecase

import com.example.domain.entity.RegisteredUser
import com.example.domain.repository.AuthRepository
import javax.inject.Inject
import kotlin.Result

class AuthUsecase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Result<RegisteredUser> {
        return repository.register()
    }
}