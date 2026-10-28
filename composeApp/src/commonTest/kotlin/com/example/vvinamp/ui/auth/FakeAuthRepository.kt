package com.example.vvinamp.ui.auth

import com.example.domain.entity.AuthSession
import com.example.domain.entity.RegisteredUser
import com.example.domain.entity.user.User
import com.example.domain.repository.AuthRepository

class FakeAuthRepository : AuthRepository {

    var logoutCallCount: Int = 0
    var loginResult: Result<AuthSession> = Result.failure(Exception("Not configured"))
    var lastLoginEmail: String? = null
    var lastLoginPassword: String? = null
    var loginCallCount: Int = 0

    var registerResult: Result<RegisteredUser> = Result.failure(Exception("Not configured"))
    var lastRegisteredUser: User? = null
    var registerCallCount: Int = 0

    var currentUserDetailResult: Result<RegisteredUser> = Result.failure(Exception("Not configured"))
    var getCurrentUserDetailCallCount: Int = 0

    override suspend fun logout() {
        logoutCallCount++
    }

    override suspend fun login(email: String, password: String): Result<AuthSession> {
        loginCallCount++
        lastLoginEmail = email
        lastLoginPassword = password
        return loginResult
    }

    override suspend fun register(user: User): Result<RegisteredUser> {
        registerCallCount++
        lastRegisteredUser = user
        return registerResult
    }

    override suspend fun getCurrentUserDetail(): Result<RegisteredUser> {
        getCurrentUserDetailCallCount++
        return currentUserDetailResult
    }
}
