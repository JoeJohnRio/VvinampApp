package com.example.vvinamp.ui.search

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeSearchRepository
    private lateinit var viewModel: SearchViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeSearchRepository()
        viewModel = SearchViewModel(fakeRepository)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial uiState has default values`() = runTest(testDispatcher) {
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("", state.searchText)
            assertEquals(SearchType.NORMAL, state.searchType)
            assertFalse(state.isLoading)
            assertNull(state.error)
        }
    }

    @Test
    fun `onSearchInput emits updated searchText in uiState`() = runTest(testDispatcher) {
        viewModel.uiState.test {
            assertEquals("", awaitItem().searchText)

            viewModel.onSearchInput("Michael Jackson")

            assertEquals("Michael Jackson", awaitItem().searchText)
            expectNoEvents()
        }
    }

    @Test
    fun `onSearchTypeChanged emits updated searchType in uiState`() = runTest(testDispatcher) {
        viewModel.uiState.test {
            assertEquals(SearchType.NORMAL, awaitItem().searchType)

            viewModel.onSearchTypeChanged(SearchType.YOUTUBE_API)

            assertEquals(SearchType.YOUTUBE_API, awaitItem().searchType)
            expectNoEvents()
        }
    }

    @Test
    fun `blank search input does not emit search results or call repository`() = runTest(testDispatcher) {
        viewModel.searchResults.test {
            viewModel.onSearchInput("   ")
            advanceTimeBy(1000)

            expectNoEvents()
            assertEquals(0, fakeRepository.searchCallCount)
            assertEquals(0, fakeRepository.ytApiCallCount)
            assertEquals(0, fakeRepository.ytDlpCallCount)
        }
    }

    @Test
    fun `debounces rapid search input and only executes last query after 500ms`() = runTest(testDispatcher) {
        viewModel.searchResults.test {
            viewModel.onSearchInput("M")
            advanceTimeBy(200)

            viewModel.onSearchInput("Mi")
            advanceTimeBy(200)

            viewModel.onSearchInput("Mic")
            advanceTimeBy(200)

            // Only 200ms passed since "Mic" -> debounce has not fired yet
            expectNoEvents()
            assertEquals(0, fakeRepository.searchCallCount)

            // Advance remaining debounce delay (> 500ms from last input)
            advanceTimeBy(350)

            val pagingData = awaitItem()
            assertNotNull(pagingData)

            assertEquals(1, fakeRepository.searchCallCount)
            assertEquals("Mic", fakeRepository.lastSearchQuery)
            assertEquals(10, fakeRepository.lastSearchPageSize)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `routes search to normal repository when SearchType is NORMAL`() = runTest(testDispatcher) {
        viewModel.searchResults.test {
            viewModel.onSearchInput("Rock with You")
            advanceTimeBy(600)

            awaitItem()

            assertEquals(1, fakeRepository.searchCallCount)
            assertEquals("Rock with You", fakeRepository.lastSearchQuery)
            assertEquals(10, fakeRepository.lastSearchPageSize)
            assertEquals(0, fakeRepository.ytApiCallCount)
            assertEquals(0, fakeRepository.ytDlpCallCount)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `routes search to YouTube API when SearchType is YOUTUBE_API`() = runTest(testDispatcher) {
        viewModel.onSearchTypeChanged(SearchType.YOUTUBE_API)

        viewModel.searchResults.test {
            viewModel.onSearchInput("Billie Jean live")
            advanceTimeBy(600)

            awaitItem()

            assertEquals(1, fakeRepository.ytApiCallCount)
            assertEquals("Billie Jean live", fakeRepository.lastYtApiQuery)
            assertEquals(10, fakeRepository.lastYtApiPageSize)
            assertEquals(0, fakeRepository.searchCallCount)
            assertEquals(0, fakeRepository.ytDlpCallCount)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `routes search to YT-DLP when SearchType is YOUTUBE_DL_P`() = runTest(testDispatcher) {
        viewModel.onSearchTypeChanged(SearchType.YOUTUBE_DL_P)

        viewModel.searchResults.test {
            viewModel.onSearchInput("Thriller 4k")
            advanceTimeBy(600)

            awaitItem()

            assertEquals(1, fakeRepository.ytDlpCallCount)
            assertEquals("Thriller 4k", fakeRepository.lastYtDlpQuery)
            assertEquals(10, fakeRepository.lastYtDlpPageSize)
            assertEquals(0, fakeRepository.searchCallCount)
            assertEquals(0, fakeRepository.ytApiCallCount)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `distinctUntilChanged does not re-trigger repository when same query is set`() = runTest(testDispatcher) {
        viewModel.searchResults.test {
            viewModel.onSearchInput("Smooth Criminal")
            advanceTimeBy(600)

            awaitItem()
            assertEquals(1, fakeRepository.searchCallCount)

            // Emit the exact same query again
            viewModel.onSearchInput("Smooth Criminal")
            advanceTimeBy(600)

            // Turbine confirms no new emission occurred!
            expectNoEvents()
            assertEquals(1, fakeRepository.searchCallCount)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
