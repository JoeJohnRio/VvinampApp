package com.example.vvinamp.ui.auth

import androidx.compose.runtime.mutableStateOf
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import app.cash.turbine.test
import com.example.domain.entity.AuthSession
import com.example.domain.entity.RegisteredUser
import com.example.domain.entity.user.User
import com.example.domain.usecase.AuthUseCases
import com.example.domain.usecase.GetCurrentUserDetailUseCase
import com.example.domain.usecase.LoginUseCase
import com.example.domain.usecase.LogoutUseCase
import com.example.domain.usecase.RegisterUseCase
import com.example.vvinamp.navigation.NavigationState
import com.example.vvinamp.navigation.Navigator
import com.example.vvinamp.navigation.graph.AuthDestination
import com.example.vvinamp.navigation.router.AuthRouter
import com.example.vvinamp.ui.auth.viewmodel.AuthViewModel
import com.example.vvinamp.ui.main.FakeAuthStore
import com.example.vvinamp.util.UiState
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
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeAuthRepo: FakeAuthRepository
    private lateinit var fakeAuthStore: FakeAuthStore
    private lateinit var viewModel: AuthViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        fakeAuthRepo = FakeAuthRepository()
        fakeAuthStore = FakeAuthStore()

        val authUseCases = AuthUseCases(
            login = LoginUseCase(fakeAuthRepo),
            register = RegisterUseCase(fakeAuthRepo),
            logout = LogoutUseCase(fakeAuthRepo),
            getCurrentUserDetail = GetCurrentUserDetailUseCase(fakeAuthRepo)
        )
        viewModel = AuthViewModel(authUseCases, fakeAuthStore)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial states have default values`() = runTest(testDispatcher) {
        assertEquals(User(), viewModel.user)
        assertEquals(UiState.Idle, viewModel.loginState.value)
        assertEquals(UiState.Idle, viewModel.registerState.value)
    }

    @Test
    fun `login success transitions from Idle to Loading to Success`() = runTest(testDispatcher) {
        val dummySession = AuthSession(
            accessToken = "token123",
            refreshToken = "refresh123",
            expiresAt = "2026-12-31"
        )
        fakeAuthRepo.loginResult = Result.success(dummySession)

        viewModel.loginState.test {
            assertEquals(UiState.Idle, awaitItem())

            viewModel.login(User(email = "test@example.com", password = "secretPassword"))

            assertEquals(UiState.Loading, awaitItem())

            val successItem = awaitItem()
            assertIs<UiState.Success<AuthSession>>(successItem)
            assertEquals(dummySession, successItem.data)

            assertEquals(1, fakeAuthRepo.loginCallCount)
            assertEquals("test@example.com", fakeAuthRepo.lastLoginEmail)
            assertEquals("secretPassword", fakeAuthRepo.lastLoginPassword)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `login failure transitions from Idle to Loading to Error`() = runTest(testDispatcher) {
        val errorMsg = "Invalid email or password"
        fakeAuthRepo.loginResult = Result.failure(Exception(errorMsg))

        viewModel.loginState.test {
            assertEquals(UiState.Idle, awaitItem())

            viewModel.login(User(email = "wrong@example.com", password = "wrongPassword"))

            assertEquals(UiState.Loading, awaitItem())

            val errorItem = awaitItem()
            assertIs<UiState.Error>(errorItem)
            assertEquals(errorMsg, errorItem.exception.message)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `register success transitions from Idle to Loading to Success`() = runTest(testDispatcher) {
        val registeredUser = RegisteredUser(
            id = "user_001",
            username = "joel",
            email = "joel@example.com"
        )
        fakeAuthRepo.registerResult = Result.success(registeredUser)

        val registerUser = User(name = "Joel", username = "joel", email = "joel@example.com", password = "pwd")
        viewModel.user = registerUser

        viewModel.registerState.test {
            assertEquals(UiState.Idle, awaitItem())

            viewModel.register()

            assertEquals(UiState.Loading, awaitItem())

            val successItem = awaitItem()
            assertIs<UiState.Success<RegisteredUser>>(successItem)
            assertEquals(registeredUser, successItem.data)

            assertEquals(1, fakeAuthRepo.registerCallCount)
            assertEquals(registerUser, fakeAuthRepo.lastRegisteredUser)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `register failure transitions from Idle to Loading to Error`() = runTest(testDispatcher) {
        val errorMsg = "Email already in use"
        fakeAuthRepo.registerResult = Result.failure(Exception(errorMsg))

        viewModel.registerState.test {
            assertEquals(UiState.Idle, awaitItem())

            viewModel.register()

            assertEquals(UiState.Loading, awaitItem())

            val errorItem = awaitItem()
            assertIs<UiState.Error>(errorItem)
            assertEquals(errorMsg, errorItem.exception.message)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `resetAndPopBackStack resets user, resets registerState, and pops router to AuthRoot`() = runTest(testDispatcher) {
        viewModel.user = User(name = "Modified User", email = "modified@example.com")

        val backStack = NavBackStack<NavKey>(AuthDestination.AuthRoot, AuthDestination.RegisterEmail)
        val navigationState = NavigationState(
            startRoute = AuthDestination.AuthRoot,
            topLevelRoute = mutableStateOf(AuthDestination.AuthRoot),
            backStacks = mapOf(AuthDestination.AuthRoot to backStack)
        )
        val authRouter = AuthRouter(Navigator(navigationState))

        assertEquals(2, backStack.size)

        viewModel.resetAndPopBackStack(authRouter)

        assertEquals(User(), viewModel.user)
        assertEquals(UiState.Idle, viewModel.registerState.value)
        assertEquals(1, backStack.size)
        assertEquals(AuthDestination.AuthRoot, backStack.last())
    }

    @Test
    fun `session state flow exposes authStore session`() = runTest(testDispatcher) {
        val dummySession = AuthSession(
            accessToken = "session_token",
            refreshToken = null,
            expiresAt = "2026-12-31"
        )
        fakeAuthStore.sessionFlow.value = dummySession

        viewModel.session.test {
            assertEquals(null, awaitItem())
            assertEquals(dummySession, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }
}
