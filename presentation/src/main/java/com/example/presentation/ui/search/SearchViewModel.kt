package com.example.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.entity.search.SearchItemDomain
import com.example.domain.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    // Example: Search triggered by user typing
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchResults: Flow<PagingData<SearchItemDomain>> = _uiState
        .debounce(300) // Wait 300ms after typing stops
        .filter { it.searchText.isNotBlank() } // Don't search empty strings
        .flatMapLatest { uiState ->
            repository.search(uiState.searchText)
        }
        .cachedIn(viewModelScope) // Important: Caches data for rotation/navigation

    fun onSearchInput(query: String) {
        _uiState.update {
            it.copy(searchText = query)
        }
    }
}