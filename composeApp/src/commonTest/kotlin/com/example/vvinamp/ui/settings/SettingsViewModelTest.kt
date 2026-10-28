package com.example.vvinamp.ui.settings

import app.cash.turbine.test
import com.example.domain.usecase.AuthUseCases
import com.example.domain.usecase.GetCurrentUserDetailUseCase
import com.example.domain.usecase.LoginUseCase
import com.example.domain.usecase.LogoutUseCase
import com.example.domain.usecase.RegisterUseCase
import com.example.vvinamp.ui.auth.FakeAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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
class SettingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeAuthRepository
    private lateinit var viewModel: SettingsViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        fakeRepository = FakeAuthRepository()
        val authUseCases = AuthUseCases(
            login = LoginUseCase(fakeRepository),
            register = RegisterUseCase(fakeRepository),
            logout = LogoutUseCase(fakeRepository),
            getCurrentUserDetail = GetCurrentUserDetailUseCase(fakeRepository)
        )
        viewModel = SettingsViewModel(authUseCases)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial uiState has default values`() = runTest(testDispatcher) {
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertNull(state.error)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent OnLogoutClicked invokes auth logout`() = runTest(testDispatcher) {
        assertEquals(0, fakeRepository.logoutCallCount)

        viewModel.onEvent(SettingsEvent.OnLogoutClicked)
        advanceUntilIdle()

        assertEquals(1, fakeRepository.logoutCallCount)
    }

    @Test
    fun `multiple OnLogoutClicked events trigger logout multiple times`() = runTest(testDispatcher) {
        assertEquals(0, fakeRepository.logoutCallCount)

        viewModel.onEvent(SettingsEvent.OnLogoutClicked)
        viewModel.onEvent(SettingsEvent.OnLogoutClicked)
        advanceUntilIdle()

        assertEquals(2, fakeRepository.logoutCallCount)
    }
}
