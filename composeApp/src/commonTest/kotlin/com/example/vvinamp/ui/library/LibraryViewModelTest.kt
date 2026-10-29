package com.example.vvinamp.ui.library

import androidx.paging.PagingData
import app.cash.turbine.test
import com.example.domain.entity.playlist.LibraryCategory
import com.example.domain.entity.playlist.PlaylistMinimal
import com.example.domain.entity.playlist.playlistMinimalDummy
import com.example.domain.entity.playlist.PlaylistSortOrderDomain
import com.example.vvinamp.ui.home.library.LibraryEvent
import com.example.vvinamp.ui.home.library.LibraryUiEvent
import com.example.vvinamp.ui.home.library.LibraryViewModel
import com.example.vvinamp.ui.playlist.FakePlaylistRepository
import com.example.vvinamp.ui.playlist.buildPlaylistUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LibraryViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakePlaylistRepository
    private lateinit var viewModel: LibraryViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakePlaylistRepository()
        val playlistUseCase = buildPlaylistUseCase(fakeRepository)
        viewModel = LibraryViewModel(playlistUseCase)
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
            assertEquals(LibraryCategory.ALL, state.libraryCategory)
            assertEquals(PlaylistSortOrderDomain.RECENTS, state.currentSort)
            assertFalse(state.isSortSheetVisible)
            assertFalse(state.isLibraryViewModeGrid)
            assertFalse(state.isCreatePlaylistOptionSheetVisible)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onLibraryCategoryClicked updates category in uiState`() = runTest(testDispatcher) {
        viewModel.uiState.test {
            assertEquals(LibraryCategory.ALL, awaitItem().libraryCategory)

            viewModel.onEvent(LibraryUiEvent.onLibraryCategoryClicked(LibraryCategory.PLAYLIST))
            assertEquals(LibraryCategory.PLAYLIST, awaitItem().libraryCategory)

            viewModel.onEvent(LibraryUiEvent.onLibraryCategoryClicked(LibraryCategory.ALBUM))
            assertEquals(LibraryCategory.ALBUM, awaitItem().libraryCategory)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onLibraryViewModesClicked toggles grid mode`() = runTest(testDispatcher) {
        viewModel.uiState.test {
            assertFalse(awaitItem().isLibraryViewModeGrid)

            viewModel.onEvent(LibraryUiEvent.onLibraryViewModesClicked)
            assertTrue(awaitItem().isLibraryViewModeGrid)

            viewModel.onEvent(LibraryUiEvent.onLibraryViewModesClicked)
            assertFalse(awaitItem().isLibraryViewModeGrid)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSortByClicked shows sort sheet and updateSort changes sort and hides sheet`() = runTest(testDispatcher) {
        viewModel.uiState.test {
            val initial = awaitItem()
            assertFalse(initial.isSortSheetVisible)
            assertEquals(PlaylistSortOrderDomain.RECENTS, initial.currentSort)

            viewModel.onEvent(LibraryUiEvent.onSortByClicked)
            assertTrue(awaitItem().isSortSheetVisible)

            viewModel.updateSort(PlaylistSortOrderDomain.ALPHABETICAL)
            val updated = awaitItem()
            assertEquals(PlaylistSortOrderDomain.ALPHABETICAL, updated.currentSort)
            assertFalse(updated.isSortSheetVisible)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onCreateClicked shows create sheet and hideCreatePlaylistSheet hides it`() = runTest(testDispatcher) {
        viewModel.uiState.test {
            assertFalse(awaitItem().isCreatePlaylistOptionSheetVisible)

            viewModel.onEvent(LibraryUiEvent.onCreateClicked)
            assertTrue(awaitItem().isCreatePlaylistOptionSheetVisible)

            viewModel.hideCreatePlaylistSheet()
            assertFalse(awaitItem().isCreatePlaylistOptionSheetVisible)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onCreateNewPlaylistClicked hides create sheet and emits NavigateToCreatePlaylist event`() = runTest(testDispatcher) {
        viewModel.showCreatePlaylistOptionSheet()

        viewModel.events.test {
            viewModel.onEvent(LibraryUiEvent.onCreateNewPlaylistClicked)

            val event = awaitItem()
            assertIs<LibraryEvent.NavigateToCreatePlaylist>(event)
            assertFalse(viewModel.uiState.value.isCreatePlaylistOptionSheetVisible)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSearchClicked hides create sheet and emits NavigateToSearchResult event`() = runTest(testDispatcher) {
        viewModel.showCreatePlaylistOptionSheet()

        viewModel.events.test {
            viewModel.onEvent(LibraryUiEvent.onSearchClicked)

            val event = awaitItem()
            assertIs<LibraryEvent.NavigateToSearchResult>(event)
            assertFalse(viewModel.uiState.value.isCreatePlaylistOptionSheetVisible)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onPlaylistClicked emits NavigateToPlaylistDetail event with correct id`() = runTest(testDispatcher) {
        val playlistId = "pl_synthwave_99"

        viewModel.events.test {
            viewModel.onEvent(LibraryUiEvent.onPlaylistClicked(playlistId))

            val event = awaitItem()
            assertIs<LibraryEvent.NavigateToPlaylistDetail>(event)
            assertEquals(playlistId, event.playlistId)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `myPlaylists emits paging data and re-triggers with new sort order on updateSort`() = runTest(testDispatcher) {
        val dummyPagingData = PagingData.from(listOf(playlistMinimalDummy))
        fakeRepository.myPlaylistFeedFlow = flowOf(dummyPagingData)

        viewModel.myPlaylists.test {
            val initialPagingData = awaitItem()
            assertNotNull(initialPagingData)
            assertEquals(1, fakeRepository.getMyPlaylistFeedCallCount)
            assertEquals(PlaylistSortOrderDomain.RECENTS, fakeRepository.lastFeedSortOrder)

            viewModel.updateSort(PlaylistSortOrderDomain.ALPHABETICAL)

            val newPagingData = awaitItem()
            assertNotNull(newPagingData)
            assertEquals(2, fakeRepository.getMyPlaylistFeedCallCount)
            assertEquals(PlaylistSortOrderDomain.ALPHABETICAL, fakeRepository.lastFeedSortOrder)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
