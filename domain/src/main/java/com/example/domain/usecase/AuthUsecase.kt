package com.example.domain.usecase

import com.example.domain.entity.AuthSession
import com.example.domain.entity.RegisteredUser
import com.example.domain.entity.user.User
import com.example.domain.repository.AuthRepository
import javax.inject.Inject
import kotlin.Result

class AuthUseCases @Inject constructor(
    val login: LoginUseCase,
    val register: RegisterUseCase,
    val logout: LogoutUseCase
)

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<AuthSession> {
        return repository.login(email, password)
    }
}
class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(user: User): Result<RegisteredUser> {
        return repository.register(user)
    }
}

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() {
        return repository.logout()
    }
}