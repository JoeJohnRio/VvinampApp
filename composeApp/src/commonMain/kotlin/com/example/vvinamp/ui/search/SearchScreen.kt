package com.example.vvinamp.ui.search
import com.example.vvinamp.util.showToast
import vvinampapp.composeapp.generated.resources.*


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import com.example.vvinamp.navigation.router.BottomNavRouter
import com.example.vvinamp.navigation.router.BottomNavRouterImpl
import com.example.vvinamp.ui.main.ScreenWrapper
import com.example.vvinamp.ui.search.component.StartBrowsingComponent
import com.example.vvinamp.ui.widget.TextFieldSearchNoClick
import com.example.vvinamp.ui.widget.TopNavigationWithButtons
import com.example.vvinamp.util.ButtonData
import com.example.vvinamp.util.preview.PreviewTheme
import com.example.vvinamp.util.preview.navigatorDummy
import com.example.vvinamp.util.preview.previewSafeString
import androidx.compose.foundation.ExperimentalFoundationApi

@Composable
fun SearchScreen(bottomNavRouter: BottomNavRouter) {
    val viewModel: SearchViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

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

@OptIn(ExperimentalFoundationApi::class)
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
                    title = previewSafeString(Res.string.title_search, "Search"),
                    listOfButtons = listOf(
                        ButtonData(
                            imageVector = Icons.Default.PhotoCamera,
                            // shortcut: searching by photo isn't built yet.
                            onClick = { showToast("Coming soon") }
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
            router = BottomNavRouterImpl(navigatorDummy()),
            onClickSearch = {}
        )
    }
}
