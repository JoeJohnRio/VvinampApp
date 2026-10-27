package com.example.vvinamp.ui.main

import app.cash.turbine.test
import com.example.domain.entity.AuthSession
import com.example.domain.entity.SessionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeAuthStore: FakeAuthStore

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeAuthStore = FakeAuthStore()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial uiState has default values`() = runTest(testDispatcher) {
        val viewModel = MainViewModel(fakeAuthStore)

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals("", state.searchQuery)
            assertNull(state.error)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `sessionState starts as Loading and maps to LoggedOut when session is null`() = runTest(testDispatcher) {
        fakeAuthStore.sessionFlow.value = null
        val viewModel = MainViewModel(fakeAuthStore)

        viewModel.sessionState.test {
            assertEquals(SessionState.Loading, awaitItem())
            assertEquals(SessionState.LoggedOut, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `sessionState starts as Loading and maps to LoggedIn when valid session exists`() = runTest(testDispatcher) {
        fakeAuthStore.sessionFlow.value = AuthSession(
            accessToken = "valid_access_token",
            refreshToken = "refresh_token",
            expiresAt = "2026-12-31"
        )
        val viewModel = MainViewModel(fakeAuthStore)

        viewModel.sessionState.test {
            assertEquals(SessionState.Loading, awaitItem())
            assertEquals(SessionState.LoggedIn, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `sessionState maps to LoggedOut when access token is blank`() = runTest(testDispatcher) {
        fakeAuthStore.sessionFlow.value = AuthSession(
            accessToken = "   ",
            refreshToken = null,
            expiresAt = "2026-12-31"
        )
        val viewModel = MainViewModel(fakeAuthStore)

        viewModel.sessionState.test {
            assertEquals(SessionState.Loading, awaitItem())
            assertEquals(SessionState.LoggedOut, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `sessionState dynamically updates when auth session changes`() = runTest(testDispatcher) {
        fakeAuthStore.sessionFlow.value = null
        val viewModel = MainViewModel(fakeAuthStore)

        viewModel.sessionState.test {
            assertEquals(SessionState.Loading, awaitItem())
            assertEquals(SessionState.LoggedOut, awaitItem())

            fakeAuthStore.sessionFlow.value = AuthSession(
                accessToken = "new_login_token",
                refreshToken = "refresh",
                expiresAt = "2026-12-31"
            )
            assertEquals(SessionState.LoggedIn, awaitItem())

            fakeAuthStore.sessionFlow.value = null
            assertEquals(SessionState.LoggedOut, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }
}
