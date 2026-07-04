package com.example.domain.usecase

import com.example.domain.entity.AuthSession
import com.example.domain.entity.RegisteredUser
import com.example.domain.entity.user.User
import com.example.domain.repository.AuthRepository
class AuthUseCases(
    val login: LoginUseCase,
    val register: RegisterUseCase,
    val logout: LogoutUseCase,
    val getCurrentUserDetail: GetCurrentUserDetailUseCase
)

class LoginUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<AuthSession> {
        return repository.login(email, password)
    }
}
class RegisterUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(user: User): Result<RegisteredUser> {
        return repository.register(user)
    }
}

class LogoutUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() {
        return repository.logout()
    }
}

class GetCurrentUserDetailUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Result<RegisteredUser> {
        return repository.getCurrentUserDetail()
    }
}
