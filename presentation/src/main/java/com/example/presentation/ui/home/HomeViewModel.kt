package com.example.presentation.ui.home

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.entity.ListeningHistory
import com.example.domain.usecase.HistoryUseCase
import com.example.domain.util.NetworkMonitor
import com.example.presentation.base.BaseViewModel
import com.example.presentation.entities.MusicListItem
import com.example.presentation.util.singleSharedFlowJoel
import com.example.presentation.ui.home.usecase.GetMoviesWithSeparators
import com.example.presentation.util.UiState
import com.example.presentation.util.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val networkMonitor: NetworkMonitor,
    getMoviesWithSeparators: GetMoviesWithSeparators,
    val historyUseCase: HistoryUseCase
): BaseViewModel() {

    val movies: Flow<PagingData<MusicListItem>> = getMoviesWithSeparators.movies(
        pageSize = 90
    ).cachedIn(viewModelScope)

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<HomeUiEvent>()
    val events = _events.asSharedFlow()

    private val _refreshListState: MutableSharedFlow<Unit> = singleSharedFlowJoel()
    val refreshListState = _refreshListState.asSharedFlow()

    private val _getListeningHistory = MutableStateFlow<UiState<List<ListeningHistory>>>(UiState.Idle)
    val getListeningHistory = _getListeningHistory.asStateFlow()

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnHistoryClicked -> {
                viewModelScope.launch {
                    _events.emit(HomeUiEvent.NavigateToArtistPage(event.history))
                }
            }
        }
    }
    init {
        observeNetworkStatus()
    }

    fun setLoading(isLoading: Boolean) {
        // Use .update to atomically modify the state.
        // 'it' refers to the current HomeUiState instance.
        _uiState.update { currentState ->
            currentState.copy(isLoading = isLoading)
        }
    }

    fun setErrorMessage(message: String?) {
        _uiState.update {
            it.copy(
                error = message,
                isLoading = false // Typically, when an error occurs, loading should stop.
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun observeNetworkStatus() {
        networkMonitor.networkState
            .onEach { if (it.shouldRefresh) onRefresh() }
            .launchIn(viewModelScope)
    }

    fun onLoadStateUpdate(loadState: CombinedLoadStates) {
        val showLoading = loadState.refresh is LoadState.Loading

        val error = when (val refresh = loadState.refresh) {
            is LoadState.Error -> refresh.error.message
            else -> null
        }

        _uiState.update { it.copy(isLoading = showLoading, error = error) }
    }

    fun getListeningHistory() {
        viewModelScope.launch {
            _getListeningHistory.value = UiState.Loading
            _getListeningHistory.value = historyUseCase.getListRecentHistoryUseCase.invoke().toUiState()
        }
    }
    fun onRefresh() = launch {
        _refreshListState.emit(Unit)
    }

}