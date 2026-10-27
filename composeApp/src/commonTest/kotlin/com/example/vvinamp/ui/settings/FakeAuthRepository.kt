package com.example.vvinamp.ui.settings

import com.example.domain.entity.AuthSession
import com.example.domain.entity.RegisteredUser
import com.example.domain.entity.user.User
import com.example.domain.repository.AuthRepository

class FakeAuthRepository : AuthRepository {

    var logoutCallCount: Int = 0

    override suspend fun logout() {
        logoutCallCount++
    }

    override suspend fun register(user: User): Result<RegisteredUser> =
        Result.failure(NotImplementedError())

    override suspend fun login(email: String, password: String): Result<AuthSession> =
        Result.failure(NotImplementedError())

    override suspend fun getCurrentUserDetail(): Result<RegisteredUser> =
        Result.failure(NotImplementedError())
}
