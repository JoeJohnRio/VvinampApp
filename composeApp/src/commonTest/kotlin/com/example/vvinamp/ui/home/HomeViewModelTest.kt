package com.example.vvinamp.ui.home

import app.cash.turbine.test
import com.example.domain.entity.AuthSession
import com.example.domain.entity.ListeningHistoryDomain
import com.example.domain.entity.RegisteredUser
import com.example.domain.entity.enum.ContentTypeDomain
import com.example.domain.entity.listeningHistoryDummy
import com.example.domain.usecase.AuthUseCases
import com.example.domain.usecase.GetCurrentUserDetailUseCase
import com.example.domain.usecase.GetListRecentHistoryUseCase
import com.example.domain.usecase.HistoryUseCase
import com.example.domain.usecase.LoginUseCase
import com.example.domain.usecase.LogoutUseCase
import com.example.domain.usecase.RegisterUseCase
import com.example.domain.util.NetworkState
import com.example.vvinamp.ui.auth.FakeAuthRepository
import com.example.vvinamp.ui.main.FakeAuthStore
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
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeNetworkMonitor: FakeNetworkMonitor
    private lateinit var fakeHistoryRepository: FakeHistoryRepository
    private lateinit var fakeAuthRepo: FakeAuthRepository
    private lateinit var fakeAuthStore: FakeAuthStore
    private lateinit var historyUseCase: HistoryUseCase
    private lateinit var authUseCases: AuthUseCases

    private fun createViewModel(): HomeViewModel {
        return HomeViewModel(
            networkMonitor = fakeNetworkMonitor,
            historyUseCase = historyUseCase,
            authUseCases = authUseCases,
            authStore = fakeAuthStore
        )
    }

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        fakeNetworkMonitor = FakeNetworkMonitor()
        fakeHistoryRepository = FakeHistoryRepository()
        fakeAuthRepo = FakeAuthRepository()
        fakeAuthStore = FakeAuthStore()

        historyUseCase = HistoryUseCase(GetListRecentHistoryUseCase(fakeHistoryRepository))
        authUseCases = AuthUseCases(
            login = LoginUseCase(fakeAuthRepo),
            register = RegisterUseCase(fakeAuthRepo),
            logout = LogoutUseCase(fakeAuthRepo),
            getCurrentUserDetail = GetCurrentUserDetailUseCase(fakeAuthRepo)
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial uiState has default values`() = runTest(testDispatcher) {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.isHistoryLoading)
            assertNull(state.error)
            assertTrue(state.listHistory.isEmpty())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getListeningHistory success updates uiState with history list`() = runTest(testDispatcher) {
        fakeHistoryRepository.resultToReturn = Result.success(listeningHistoryDummy)
        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertTrue(awaitItem().isHistoryLoading)

            viewModel.getListeningHistory()

            val successState = awaitItem()
            assertFalse(successState.isHistoryLoading)
            assertEquals(listeningHistoryDummy, successState.listHistory)
            assertNull(successState.error)

            assertEquals(1, fakeHistoryRepository.callCount)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getListeningHistory failure updates uiState with error message`() = runTest(testDispatcher) {
        val errorMsg = "Network timeout"
        fakeHistoryRepository.resultToReturn = Result.failure(Exception(errorMsg))
        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertNull(awaitItem().error)

            viewModel.getListeningHistory()

            val errorState = awaitItem()
            assertFalse(errorState.isHistoryLoading)
            assertEquals("Failed to get history data", errorState.error)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent OnHistoryClicked for TRACK emits HomeEvent PlayTrack`() = runTest(testDispatcher) {
        val viewModel = createViewModel()
        val trackHistory = ListeningHistoryDomain(
            id = "lh_track",
            playedAt = "2026-02-06T22:15:00Z",
            progress = 50,
            contentTypeDomain = ContentTypeDomain.TRACK,
            contentId = "track_123",
            contentImage = emptyList(),
            contentName = "Song Name",
            contentSubtitle = "Artist Name"
        )

        viewModel.events.test {
            viewModel.onEvent(HomeUiEvent.OnHistoryClicked(trackHistory))

            val event = awaitItem()
            assertIs<HomeEvent.PlayTrack>(event)
            assertEquals(trackHistory, event.history)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent OnHistoryClicked for PLAYLIST emits HomeEvent NavigateToPlaylistScreen`() = runTest(testDispatcher) {
        val viewModel = createViewModel()
        val playlistHistory = listeningHistoryDummy.first { it.contentTypeDomain == ContentTypeDomain.PLAYLIST }

        viewModel.events.test {
            viewModel.onEvent(HomeUiEvent.OnHistoryClicked(playlistHistory))

            val event = awaitItem()
            assertIs<HomeEvent.NavigateToPlaylistScreen>(event)
            assertEquals(playlistHistory, event.history)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent OnHistoryClicked for ALBUM emits HomeEvent NavigateToAlbumScreen`() = runTest(testDispatcher) {
        val viewModel = createViewModel()
        val albumHistory = listeningHistoryDummy.first { it.contentTypeDomain == ContentTypeDomain.ALBUM }

        viewModel.events.test {
            viewModel.onEvent(HomeUiEvent.OnHistoryClicked(albumHistory))

            val event = awaitItem()
            assertIs<HomeEvent.NavigateToAlbumScreen>(event)
            assertEquals(albumHistory, event.history)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent OnHistoryClicked for ARTIST emits HomeEvent NavigateToArtistScreen`() = runTest(testDispatcher) {
        val viewModel = createViewModel()
        val artistHistory = ListeningHistoryDomain(
            id = "lh_artist",
            playedAt = "2026-02-06T22:15:00Z",
            progress = 0,
            contentTypeDomain = ContentTypeDomain.ARTIST,
            contentId = "artist_123",
            contentImage = emptyList(),
            contentName = "Artist Name",
            contentSubtitle = ""
        )

        viewModel.events.test {
            viewModel.onEvent(HomeUiEvent.OnHistoryClicked(artistHistory))

            val event = awaitItem()
            assertIs<HomeEvent.NavigateToArtistScreen>(event)
            assertEquals(artistHistory, event.history)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent OnSettingsClicked emits HomeEvent NavigateToSettingsScreen`() = runTest(testDispatcher) {
        val viewModel = createViewModel()

        viewModel.events.test {
            viewModel.onEvent(HomeUiEvent.OnSettingsClicked)

            val event = awaitItem()
            assertIs<HomeEvent.NavigateToSettingsScreen>(event)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent OnSearchClicked emits HomeEvent NavigateToSearchResultScreen`() = runTest(testDispatcher) {
        val viewModel = createViewModel()

        viewModel.events.test {
            viewModel.onEvent(HomeUiEvent.OnSearchClicked)

            val event = awaitItem()
            assertIs<HomeEvent.NavigateToSearchResultScreen>(event)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `network state shouldRefresh triggers onRefresh and emits to refreshListState`() = runTest(testDispatcher) {
        val viewModel = createViewModel()

        viewModel.refreshListState.test {
            fakeNetworkMonitor.networkStateFlow.emit(NetworkState(isOnline = true, shouldRefresh = true))

            awaitItem()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `init fetches user details and saves to authStore when session token exists without userId`() = runTest(testDispatcher) {
        fakeAuthStore.sessionFlow.value = AuthSession(
            accessToken = "token_abc",
            refreshToken = "refresh_abc",
            expiresAt = "2026-12-31",
            userId = null
        )
        fakeAuthRepo.currentUserDetailResult = Result.success(
            RegisteredUser(
                id = "user_789",
                username = "music_fan",
                email = "music@example.com",
                profilePicture = "https://example.com/pic.jpg"
            )
        )

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(1, fakeAuthRepo.getCurrentUserDetailCallCount)
        assertEquals(1, fakeAuthStore.saveUserCallCount)
        assertEquals("user_789", fakeAuthStore.sessionFlow.value?.userId)
        assertEquals("music_fan", fakeAuthStore.sessionFlow.value?.username)
        assertEquals("music@example.com", fakeAuthStore.sessionFlow.value?.email)
    }
}
