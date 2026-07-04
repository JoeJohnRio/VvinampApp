package com.example.vvinamp.ui.search
import vvinampapp.composeapp.generated.resources.*


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import androidx.paging.compose.collectAsLazyPagingItems

import com.example.vvinamp.entities.ButtonData
import com.example.vvinamp.navigation.router.BottomNavRouter
import com.example.vvinamp.ui.main.ScreenWrapper
import com.example.vvinamp.ui.search.component.StartBrowsingComponent
import com.example.vvinamp.ui.widget.TextFieldSearchNoClick
import com.example.vvinamp.ui.widget.TopNavigationWithButtons
import com.example.vvinamp.util.preview.PreviewTheme
import com.example.vvinamp.util.preview.navigatorDummy

@Composable
fun SearchScreen(bottomNavRouter: BottomNavRouter) {
    val viewModel: SearchViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val searchItems = viewModel.searchResults.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.onSearchInput("")
    }

    ScreenWrapper(
        isLoading = uiState.isLoading,
        error = uiState.error,
        content = {
            SearchContent(
                uiState = uiState,
                router = bottomNavRouter,
                onClickSearch = {
                    bottomNavRouter.navigateToSearchResults()
                }
            )
        }
    )
}

@Composable
fun SearchContent(uiState: SearchUiState, router: BottomNavRouter, onClickSearch: () -> Unit) {
    val scrollState = rememberLazyListState()

    Scaffold() { contentPadding ->
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .padding(contentPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            item {
                TopNavigationWithButtons(
                    title = stringResource(Res.string.title_search),
                    listOfButtons = listOf(
                        ButtonData(
                            iconRes = Res.drawable.ic_camera,
                            onClick = {

                            }
                        )
                    )
                )
            }

            item {
                Spacer(Modifier.height(16.dp))
            }

            stickyHeader {
                TextFieldSearchNoClick(
                    onValueChanged = {

                    },
                    onClick = {
                        onClickSearch()
                    }
                )
            }

            item {
                Spacer(Modifier.height(16.dp))
            }

            item {
                StartBrowsingComponent()
            }

            item {
                Spacer(Modifier.height(1000.dp))
            }

        }
    }
}

@Preview
@Composable
fun SearchContentPreview() {
    PreviewTheme {
        SearchContent(
            uiState = SearchUiState(),
            router = BottomNavRouter(navigatorDummy()),
            onClickSearch = {}
        )
    }
}