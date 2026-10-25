package com.example.vvinamp.ui.artist

import androidx.compose.ui.graphics.Color
import app.cash.turbine.test
import com.example.domain.entity.artist.ArtistScreenDomain
import com.example.domain.entity.artist.artistProfileDummy
import com.example.domain.entity.song.topReleaseDummy
import com.example.domain.entity.song.topSongsDummy
import com.example.domain.usecase.ArtistUseCase
import com.example.domain.usecase.GetArtistScreenDataUseCase
import com.example.vvinamp.ui.home.artist.ArtistViewModel
import com.example.vvinamp.ui.theme.AppColours
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
class ArtistViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeArtistRepository
    private lateinit var viewModel: ArtistViewModel

    private val dummyArtistScreenData = ArtistScreenDomain(
        artistProfile = artistProfileDummy,
        topSongs = topSongsDummy,
        topReleases = topReleaseDummy
    )

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        fakeRepository = FakeArtistRepository()
        val artistUseCase = ArtistUseCase(
            getArtistScreenData = GetArtistScreenDataUseCase(fakeRepository)
        )
        viewModel = ArtistViewModel(artistUseCase)
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
            assertNull(state.artistProfile)
            assertNull(state.topSongs)
            assertNull(state.topReleases)
            assertEquals(AppColours.BackgroundBlack, state.dominantColor)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getArtistScreenData success updates uiState with artist profile top songs and releases`() = runTest(testDispatcher) {
        // 1. Arrange: configure fake repository to return dummy artist screen data
        fakeRepository.resultToReturn = Result.success(dummyArtistScreenData)

        viewModel.uiState.test {
            // Initial state emission
            assertFalse(awaitItem().isLoading)

            // 2. Act: request artist screen data
            viewModel.getArtistScreenData("mj_001")

            // 3. Assert loading state
            assertTrue(awaitItem().isLoading)

            // 4. Assert success state populated with data
            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertEquals(artistProfileDummy, successState.artistProfile)
            assertEquals(topSongsDummy, successState.topSongs)
            assertEquals(topReleaseDummy, successState.topReleases)
            assertNull(successState.error)

            // Verify repository interactions
            assertEquals(1, fakeRepository.getArtistScreenDataCallCount)
            assertEquals("mj_001", fakeRepository.lastArtistId)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getArtistScreenData failure updates uiState with error message`() = runTest(testDispatcher) {
        // 1. Arrange: simulate network or server failure
        val errorMessage = "Artist not found"
        fakeRepository.resultToReturn = Result.failure(Exception(errorMessage))

        viewModel.uiState.test {
            assertNull(awaitItem().error)

            // 2. Act
            viewModel.getArtistScreenData("invalid_artist_id")

            // 3. Assert loading state
            assertTrue(awaitItem().isLoading)

            // 4. Assert error state
            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertNull(errorState.artistProfile)
            assertNull(errorState.topSongs)
            assertNull(errorState.topReleases)
            assertEquals(errorMessage, errorState.error)

            assertEquals(1, fakeRepository.getArtistScreenDataCallCount)
            assertEquals("invalid_artist_id", fakeRepository.lastArtistId)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `clearUiState resets artist profile songs releases and error`() = runTest(testDispatcher) {
        // 1. Arrange: populate state first with artist data
        fakeRepository.resultToReturn = Result.success(dummyArtistScreenData)

        viewModel.uiState.test {
            awaitItem() // initial
            viewModel.getArtistScreenData("mj_001")
            awaitItem() // loading
            awaitItem() // success

            // 2. Act: clear the UI state
            viewModel.clearUiState()

            // 3. Assert: all properties reset to null / defaults
            val clearedState = awaitItem()
            assertFalse(clearedState.isLoading)
            assertNull(clearedState.artistProfile)
            assertNull(clearedState.topSongs)
            assertNull(clearedState.topReleases)
            assertNull(clearedState.error)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `updateDominantColor updates dominantColor in uiState`() = runTest(testDispatcher) {
        viewModel.uiState.test {
            // Initial color
            assertEquals(AppColours.BackgroundBlack, awaitItem().dominantColor)

            // Act
            viewModel.updateDominantColor(Color.Cyan)

            // Assert
            assertEquals(Color.Cyan, awaitItem().dominantColor)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
