package com.example.vvinamp.ui.home.library.createplaylist

import com.example.vvinamp.util.KmpLogger
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vvinamp.navigation.router.BottomNavRouter
import com.example.vvinamp.ui.home.library.LibraryEvent
import com.example.vvinamp.ui.main.ScreenWrapper
import com.example.vvinamp.ui.theme.TSHeadlineMediumWhite
import com.example.vvinamp.ui.widget.ButtonVariableOutlined
import com.example.vvinamp.ui.widget.ButtonVariableSizeSolid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlaylistScreen(
    onCancel: () -> Unit = {},
    bottomNavRouter: BottomNavRouter,
    viewModel: CreatePlaylistViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is CreatePlaylistEvent.NavigateToPlaylistDetail -> {
                    bottomNavRouter.navigateToPlaylistDetail(event.playlistId)
                }
            }
        }
    }

    ScreenWrapper(
        isLoading = uiState.isLoading,
        error = uiState.error,
        onRetry = {

        },
        content = {
            CreatePlaylistContent(
                onCancel = onCancel,
                onEvent = {
                    viewModel.onEvent(it)
                },
                playlistName = uiState.playlistName,
            )
        }
    )
}

@Composable
fun CreatePlaylistContent(
    onEvent: (CreatePlaylistUiEvent) -> Unit,
    onCancel: () -> Unit,
    playlistName: String,
) {
    Box(
        modifier = Modifier
            .systemBarsPadding()
            .imePadding()
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF7e7e7e),
                        Color(0xFF494949),
                        Color(0xff131313)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Give your playlist a name",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            TextField(
                value = playlistName,
                onValueChange = { onEvent(CreatePlaylistUiEvent.onChangePlaylistName(it)) },
                textStyle = TSHeadlineMediumWhite().copy(
                    color = MaterialTheme.colorScheme.inversePrimary,
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(48.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ButtonVariableOutlined(
                    onClick = onCancel,
                    containerColor = Color.Transparent,
                    outlineColor = MaterialTheme.colorScheme.inversePrimary,
                    text = "Cancel",
                )

                Spacer(Modifier.width(16.dp))

                ButtonVariableSizeSolid(
                    onClick = { onEvent(CreatePlaylistUiEvent.onCreatePlaylistClick) },
                    text = "Create",
                )
            }
        }
    }
}
