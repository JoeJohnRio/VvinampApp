package com.example.vvinamp.ui.home
import vvinampapp.composeapp.generated.resources.*

import androidx.lifecycle.viewModelScope
import com.example.data.repository.auth.AuthStore
import com.example.domain.entity.enum.ContentTypeDomain
import com.example.domain.usecase.AuthUseCases
import com.example.domain.usecase.HistoryUseCase
import com.example.domain.util.NetworkMonitor
import com.example.vvinamp.base.BaseViewModel
import com.example.vvinamp.util.singleSharedFlowJoel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
class HomeViewModel(
    val networkMonitor: NetworkMonitor,
    val historyUseCase: HistoryUseCase,
    private val authUseCases: AuthUseCases,
    private val authStore: AuthStore
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<HomeEvent>()
    val events = _events.asSharedFlow()

    private val _refreshListState: MutableSharedFlow<Unit> = singleSharedFlowJoel()
    val refreshListState = _refreshListState.asSharedFlow()

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnHistoryClicked -> {
                viewModelScope.launch {
                    when(event.history.contentTypeDomain) {
                        ContentTypeDomain.TRACK -> {
                            _events.emit(HomeEvent.PlayTrack(event.history))
                        }
                        ContentTypeDomain.PLAYLIST -> {
                            _events.emit(HomeEvent.NavigateToPlaylistScreen(event.history))
                        }
                        ContentTypeDomain.ALBUM -> {
                            _events.emit(HomeEvent.NavigateToAlbumScreen(event.history))
                        }
                        ContentTypeDomain.ARTIST -> {
                            _events.emit(HomeEvent.NavigateToArtistScreen(event.history))
                        }
                        else -> {

                        }
                    }
                }
            }

            HomeUiEvent.OnSettingsClicked -> {
                viewModelScope.launch {
                    _events.emit(HomeEvent.NavigateToSettingsScreen)
                }
            }

            HomeUiEvent.OnSearchClicked -> {
                viewModelScope.launch {
                    _events.emit(HomeEvent.NavigateToSearchResultScreen)
                }
            }
        }
    }

    init {
        observeNetworkStatus()
        fetchUserDetailsIfNeeded()
    }

    private fun fetchUserDetailsIfNeeded() {
        viewModelScope.launch {
            val session = authStore.getSession().first()
            // If we have tokens but no user ID/name, fetch details
            if (session?.accessToken != null && session.userId == null) {
                authUseCases.getCurrentUserDetail().onSuccess { userDetails ->
                    authStore.saveUser(
                        userId = userDetails.id,
                        username = userDetails.username,
                        email = userDetails.email,
                        profilePicture = userDetails.profilePicture
                    )
                }
            }
        }
    }

    private fun observeNetworkStatus() {
        networkMonitor.networkState
            .onEach { if (it.shouldRefresh) onRefresh() }
            .launchIn(viewModelScope)
    }

    fun getListeningHistory() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = historyUseCase.getListRecentHistoryUseCase.invoke()

            result.onSuccess { data ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        listHistory = data,
                        error = null
                    )
                }
            }.onFailure {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = it.error ?: "Failed to get history data",
                    )
                }
            }
        }
    }

    fun onRefresh() = launch {
        _refreshListState.emit(Unit)
    }

}
