package com.example.vvinamp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.domain.entity.search.SearchItemDomain
import com.example.domain.repository.SearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class SearchViewModel(
    private val repository: SearchRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchResults: Flow<PagingData<SearchItemDomain>> = _uiState
        .map { it.searchText to it.searchType }
        .distinctUntilChanged()
        .debounce(500)
        .flatMapLatest { (searchText, searchType) ->
            if (searchText.isBlank()) return@flatMapLatest emptyFlow()

            when (searchType) {
                SearchType.NORMAL -> repository.search(
                    query = searchText,
                    pageSize = 10
                )
                SearchType.YOUTUBE_API -> repository.searchYoutubeFromApi(
                    query = searchText,
                    pageSize = 10
                ).map { pagingData ->
                    pagingData.map { item ->
                        if (item is SearchItemDomain.YoutubeTrackSearch &&
                            (item.uploaderId.contains("fallback") || item.uploaderId.contains("quota"))
                        ) {
                            _uiState.update { it.copy(statusNotice = FALLBACK_NOTICE) }
                        }
                        item
                    }
                }.catch { e ->
                    if (isQuotaExceeded(e)) {
                        _uiState.update { it.copy(statusNotice = FALLBACK_NOTICE) }
                        emitAll(
                            repository.searchYoutubeFromYtDlp(
                                query = searchText,
                                pageSize = 10
                            )
                        )
                    } else {
                        throw e
                    }
                }
                SearchType.YOUTUBE_DL_P -> repository.searchYoutubeFromYtDlp(
                    query = searchText,
                    pageSize = 10
                )
            }
        }
        .cachedIn(viewModelScope)

    fun onSearchInput(query: String) {
        _uiState.update {
            it.copy(
                searchText = query,
                statusNotice = if (query.isBlank()) null else it.statusNotice
            )
        }
    }

    fun onSearchTypeChanged(type: SearchType) {
        _uiState.update {
            it.copy(
                searchType = type,
                statusNotice = if (type == SearchType.YOUTUBE_API) it.statusNotice else null
            )
        }
    }

    fun dismissStatusNotice() {
        _uiState.update {
            it.copy(statusNotice = null)
        }
    }

    companion object {
        const val FALLBACK_NOTICE = "Daily API quota reached — results powered by fallback search"

        private fun isQuotaExceeded(e: Throwable): Boolean {
            val msg = e.message.orEmpty()
            return msg.contains("403") || msg.contains("quota", ignoreCase = true)
        }
    }
}
