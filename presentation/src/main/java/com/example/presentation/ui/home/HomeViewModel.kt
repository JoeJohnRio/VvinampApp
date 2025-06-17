package com.example.presentation.ui.home

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.util.NetworkMonitor
import com.example.presentation.base.BaseViewModel
import com.example.presentation.entities.MusicListItem
import com.example.presentation.util.singleSharedFlowJoel
import com.example.presentation.ui.home.usecase.GetMoviesWithSeparators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val networkMonitor: NetworkMonitor,
    getMoviesWithSeparators: GetMoviesWithSeparators,
): BaseViewModel() {

    val movies: Flow<PagingData<MusicListItem>> = getMoviesWithSeparators.movies(
        pageSize = 90
    ).cachedIn(viewModelScope)

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationState: MutableSharedFlow<HomeNavigationState> = singleSharedFlowJoel()
    val navigationState = _navigationState.asSharedFlow()

    private val _refreshListState: MutableSharedFlow<Unit> = singleSharedFlowJoel()
    val refreshListState = _refreshListState.asSharedFlow()

    init {
        observeNetworkStatus()
    }

    private fun observeNetworkStatus() {
        networkMonitor.networkState
            .onEach { if (it.shouldRefresh) onRefresh() }
            .launchIn(viewModelScope)
    }

    fun onMovieClicked(movieId: Int) =
        _navigationState.tryEmit(HomeNavigationState.MusicDetails(movieId))

    fun onLoadStateUpdate(loadState: CombinedLoadStates) {
        val showLoading = loadState.refresh is LoadState.Loading

        val error = when (val refresh = loadState.refresh) {
            is LoadState.Error -> refresh.error.message
            else -> null
        }

        _uiState.update { it.copy(showLoading = showLoading, errorMessage = error) }
    }

    fun onRefresh() = launch {
        _refreshListState.emit(Unit)
    }

}