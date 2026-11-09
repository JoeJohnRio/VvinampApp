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
            assertEquals(SearchType.YOUTUBE, state.searchType)
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
            assertEquals(SearchType.YOUTUBE, awaitItem().searchType)

            viewModel.onSearchTypeChanged(SearchType.CATALOG)

            assertEquals(SearchType.CATALOG, awaitItem().searchType)
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
    fun `typing alone does not search YouTube`() = runTest(testDispatcher) {
        viewModel.searchResults.test {
            viewModel.onSearchInput("Michael")
            advanceTimeBy(1000)

            expectNoEvents()
            assertEquals(0, fakeRepository.ytApiCallCount)
        }
    }

    @Test
    fun `submitting searches YouTube with the trimmed text`() = runTest(testDispatcher) {
        viewModel.searchResults.test {
            viewModel.onSearchInput("  Michael Jackson ")
            viewModel.onSearchSubmit()
            advanceTimeBy(1)

            assertNotNull(awaitItem())
            assertEquals(1, fakeRepository.ytApiCallCount)
            assertEquals("Michael Jackson", fakeRepository.lastYtApiQuery)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `catalog debounces rapid input and only executes last query after 500ms`() = runTest(testDispatcher) {
        viewModel.onSearchTypeChanged(SearchType.CATALOG)

        viewModel.searchResults.test {
            viewModel.onSearchInput("M")
            advanceTimeBy(200)

            viewModel.onSearchInput("Mi")
            advanceTimeBy(200)

            viewModel.onSearchInput("Mic")
            advanceTimeBy(200)

            expectNoEvents()
            assertEquals(0, fakeRepository.searchCallCount)

            advanceTimeBy(350)

            assertNotNull(awaitItem())
            assertEquals(1, fakeRepository.searchCallCount)
            assertEquals("Mic", fakeRepository.lastSearchQuery)
            assertEquals(0, fakeRepository.ytApiCallCount)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `switching to YouTube searches the typed text`() = runTest(testDispatcher) {
        viewModel.onSearchTypeChanged(SearchType.CATALOG)
        viewModel.onSearchInput("Thriller")

        viewModel.searchResults.test {
            advanceTimeBy(600)
            awaitItem()

            viewModel.onSearchTypeChanged(SearchType.YOUTUBE)
            advanceTimeBy(1)
            awaitItem()

            assertEquals(1, fakeRepository.ytApiCallCount)
            assertEquals("Thriller", fakeRepository.lastYtApiQuery)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `routes search to catalog repository when SearchType is CATALOG`() = runTest(testDispatcher) {
        viewModel.onSearchTypeChanged(SearchType.CATALOG)

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
    fun `routes search to YouTube API when SearchType is YOUTUBE`() = runTest(testDispatcher) {
        viewModel.onSearchTypeChanged(SearchType.YOUTUBE)

        viewModel.searchResults.test {
            viewModel.onSearchInput("Billie Jean live")
            viewModel.onSearchSubmit()
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
    fun `distinctUntilChanged does not re-trigger repository when same query is submitted`() = runTest(testDispatcher) {
        viewModel.searchResults.test {
            viewModel.onSearchInput("Smooth Criminal")
            viewModel.onSearchSubmit()
            advanceTimeBy(600)

            awaitItem()
            assertEquals(1, fakeRepository.ytApiCallCount)

            viewModel.onSearchSubmit()
            advanceTimeBy(600)

            expectNoEvents()
            assertEquals(1, fakeRepository.ytApiCallCount)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
