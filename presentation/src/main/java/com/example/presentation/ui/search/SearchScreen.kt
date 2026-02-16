package com.example.presentation.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.presentation.R
import com.example.presentation.navigation.router.BottomNavRouter
import com.example.presentation.ui.main.ScreenWrapper
import com.example.presentation.ui.widget.TextFieldSearch
import com.example.presentation.ui.widget.TopNavigationWithButtons
import com.example.presentation.util.preview.PreviewTheme
import com.example.presentation.util.preview.navigatorDummy

@Composable
fun SearchScreen(bottomNavRouter: BottomNavRouter) {
    val viewModel: SearchViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    ScreenWrapper(
        isLoading = uiState.isLoading,
        error = uiState.error,
        content = {
            SearchContent(
                uiState = uiState,
                router = bottomNavRouter
            )
        }
    )
}

@Composable
fun SearchContent(uiState: SearchUiState, router: BottomNavRouter) {
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
                    listOfButtons = listOf(
                        Pair(
                            painterResource(R.drawable.ic_email),
                            router.navigateBack()
                        )
                    )
                )
            }

            item {
                Spacer(Modifier.height(16.dp))
            }

            stickyHeader {
                TextFieldSearch(
                    onValueChanged = {

                    }
                )
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
            router = BottomNavRouter(navigatorDummy())
        )
    }
}