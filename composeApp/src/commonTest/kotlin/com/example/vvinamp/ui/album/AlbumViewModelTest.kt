package com.example.vvinamp.ui.album

import androidx.compose.ui.graphics.Color
import app.cash.turbine.test
import com.example.domain.entity.album.dummyAlbumDomain
import com.example.domain.usecase.AlbumUseCase
import com.example.domain.usecase.GetAlbum
import com.example.vvinamp.ui.home.album.AlbumViewModel
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
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeAlbumRepository
    private lateinit var viewModel: AlbumViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        fakeRepository = FakeAlbumRepository()

        // Pass the fake repository to the real UseCase!
        val albumUseCase = AlbumUseCase(getAlbum = GetAlbum(fakeRepository))
        viewModel = AlbumViewModel(albumUseCase)
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
            assertNull(state.album)
            assertTrue(state.tracks.isEmpty())
            assertEquals(Color.Unspecified, state.dominantColor)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getAlbum success updates uiState with album data and tracks`() = runTest(testDispatcher) {
        fakeRepository.resultToReturn = Result.success(dummyAlbumDomain)

        viewModel.uiState.test {
            // Initial state
            assertFalse(awaitItem().isLoading)

            viewModel.getAlbum("album_thriller_001")

            // Loading state
            assertTrue(awaitItem().isLoading)

            // Success state
            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertEquals(dummyAlbumDomain, successState.album)
            assertEquals(dummyAlbumDomain.tracks, successState.tracks)
            assertNull(successState.error)

            assertEquals(1, fakeRepository.getAlbumCallCount)
            assertEquals("album_thriller_001", fakeRepository.lastAlbumId)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getAlbum failure updates uiState with error message`() = runTest(testDispatcher) {
        val errorMessage = "Unable to connect to server"
        fakeRepository.resultToReturn = Result.failure(Exception(errorMessage))

        viewModel.uiState.test {
            // Initial state
            assertNull(awaitItem().error)

            viewModel.getAlbum("invalid_id")

            // Loading state
            assertTrue(awaitItem().isLoading)

            // Failure state
            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertNull(errorState.album)
            assertEquals(errorMessage, errorState.error)

            assertEquals(1, fakeRepository.getAlbumCallCount)
            assertEquals("invalid_id", fakeRepository.lastAlbumId)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `updateDominantColor updates dominantColor in uiState`() = runTest(testDispatcher) {
        viewModel.uiState.test {
            assertEquals(Color.Unspecified, awaitItem().dominantColor)

            viewModel.updateDominantColor(Color.Green)

            assertEquals(Color.Green, awaitItem().dominantColor)

            cancelAndIgnoreRemainingEvents()
        }
    }
}