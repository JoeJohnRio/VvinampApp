package com.example.vvinamp.ui.playlist

import app.cash.turbine.test
import com.example.vvinamp.ui.home.library.createplaylist.CreatePlaylistEvent
import com.example.vvinamp.ui.home.library.createplaylist.CreatePlaylistUiEvent
import com.example.vvinamp.ui.home.library.createplaylist.CreatePlaylistViewModel
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
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CreatePlaylistViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakePlaylistRepository
    private lateinit var viewModel: CreatePlaylistViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        fakeRepository = FakePlaylistRepository()
        val playlistUseCase = buildPlaylistUseCase(fakeRepository)
        viewModel = CreatePlaylistViewModel(playlistUseCase)
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
            assertEquals("", state.playlistName)
            assertNull(state.playlistId)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent onChangePlaylistName updates playlistName in uiState`() = runTest(testDispatcher) {
        viewModel.uiState.test {
            assertEquals("", awaitItem().playlistName)

            // Trigger event to change playlist name
            viewModel.onEvent(CreatePlaylistUiEvent.onChangePlaylistName("Synthwave Beats"))

            val updatedState = awaitItem()
            assertEquals("Synthwave Beats", updatedState.playlistName)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent onCreatePlaylistClick success emits NavigateToPlaylistDetail event`() = runTest(testDispatcher) {
        // Arrange
        val expectedPlaylistId = "new_playlist_123"
        fakeRepository.createPlaylistResult = Result.success(expectedPlaylistId)

        // Set playlist name first
        viewModel.onEvent(CreatePlaylistUiEvent.onChangePlaylistName("Workout Hits"))

        // IMPORTANT FOR SHAREDFLOW:
        // Subscribe to `viewModel.events` BEFORE triggering the action!
        // SharedFlow has no replay buffer, so unobserved events are lost.
        viewModel.events.test {
            // Act: trigger create playlist click
            viewModel.onEvent(CreatePlaylistUiEvent.onCreatePlaylistClick)

            // Assert: await the navigation event emitted by SharedFlow
            val event = awaitItem()
            assertIs<CreatePlaylistEvent.NavigateToPlaylistDetail>(event)
            assertEquals(expectedPlaylistId, event.playlistId)

            // Verify repository interactions
            assertEquals(1, fakeRepository.createPlaylistCallCount)
            assertEquals("Workout Hits", fakeRepository.lastCreatedPlaylistTitle)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent onCreatePlaylistClick failure updates uiState with error message`() = runTest(testDispatcher) {
        // Arrange: simulate repository failure
        val errorMessage = "Playlist title already exists"
        fakeRepository.createPlaylistResult = Result.failure(Exception(errorMessage))

        viewModel.onEvent(CreatePlaylistUiEvent.onChangePlaylistName("Duplicate Name"))

        viewModel.uiState.test {
            assertNull(awaitItem().error)

            // Act
            viewModel.onEvent(CreatePlaylistUiEvent.onCreatePlaylistClick)

            // Loading state
            assertTrue(awaitItem().isLoading)

            // Failure state
            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertEquals(errorMessage, errorState.error)

            assertEquals(1, fakeRepository.createPlaylistCallCount)
            assertEquals("Duplicate Name", fakeRepository.lastCreatedPlaylistTitle)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
