package com.example.presentation.ui.search.searchresult


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.domain.entity.search.SearchItemDomain
import com.example.presentation.navigation.router.BottomNavRouter
import com.example.presentation.ui.main.ScreenWrapper
import com.example.presentation.ui.search.SearchUiState
import com.example.presentation.ui.search.SearchViewModel
import com.example.presentation.ui.widget.TextFieldSearch

@Composable
fun SearchResultsScreen(bottomNavRouter: BottomNavRouter) {
    val viewModel: SearchViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val searchItems = viewModel.searchResults.collectAsLazyPagingItems()

    ScreenWrapper(
        isLoading = uiState.isLoading,
        error = uiState.error,
        content = {
            SearchResultsContent(
                uiState = uiState,
                router = bottomNavRouter,
                onTypeSearch = {
                    viewModel.onSearchInput(it)
                },
                searchItems = searchItems,
                onBackClicked = {
                    bottomNavRouter.navigateBack()
                }
            )
        }
    )
}

@Composable
fun SearchResultsContent(
    uiState: SearchUiState,
    router: BottomNavRouter,
    onTypeSearch: (String) -> Unit,
    searchItems: LazyPagingItems<SearchItemDomain>,
    onBackClicked: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TextFieldSearch(
                text = uiState.searchText,
                onValueChanged = {
                    onTypeSearch(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                onBackClicked = onBackClicked
            )
        }
    ) { contentPadding ->

        LazyColumn(
            modifier = Modifier.padding(contentPadding)
        ) {
            item {
                Spacer(Modifier.height(16.dp))
            }

            items(
                count = searchItems.itemCount,
                key = { index ->
                    // Unique key for stable lists (optional but recommended)
                    val item = searchItems[index]
                    when (item) {
                        is SearchItemDomain.TrackSearch -> "track_${item.id}"
                        is SearchItemDomain.AlbumSearch -> "album_${item.id}"
                        else -> index
                    }
                }
            ) { index ->
                SearchItemComponent(searchItems[index])
//                val item = searchItems[index]
//                if (item != null) {
//                    Text(
//                        "Item: ${item}"
//                    )
//                }
            }

            // Handle Loading/Error States
//            when {
//                searchItems.loadState.refresh is LoadState.Loading -> {
//                    item { CircularProgressIndicator() }
//                }
//                searchItems.loadState.append is LoadState.Loading -> {
//                    item { CircularProgressIndicator() } // Footer loader
//                }
//                searchItems.loadState.refresh is LoadState.Error -> {
//                    item { Text("Error loading data") }
//                }
//            }
        }
    }

}