package com.example.vvinamp.ui.auth
import vvinampapp.composeapp.generated.resources.*

import com.example.vvinamp.util.KmpLogger
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel

import com.example.vvinamp.ui.auth.viewmodel.AuthViewModel
import com.example.vvinamp.navigation.router.AuthRouter
import com.example.vvinamp.ui.theme.TSBodyMediumWhite
import com.example.vvinamp.ui.theme.TSDisplayMediumWhite
import com.example.vvinamp.ui.widget.ButtonVariableSizeSolid
import com.example.vvinamp.ui.widget.LoadingOverlay
import com.example.vvinamp.ui.widget.RowWithRoundCheckbox
import com.example.vvinamp.ui.widget.SnackbarStyledHost
import com.example.vvinamp.ui.widget.TextFieldNormal
import com.example.vvinamp.ui.widget.TopBarCustom
import com.example.vvinamp.ui.widget.isAnyLoading
import com.example.vvinamp.util.UiState
import com.example.vvinamp.util.preview.PreviewTheme
import com.example.vvinamp.util.preview.navigatorDummy


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterNamePage(
    authRouter: AuthRouter,
    viewModel: AuthViewModel = koinViewModel()
) {
    val registerState by viewModel.registerState.collectAsState()
    var nameValue by remember { mutableStateOf("") }
    var isUserReceiveMarketing by remember { mutableStateOf(false) }
    var isUserShareRegistrationData by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(registerState) {
        when (registerState) {
            is UiState.Error -> {
                val error = (registerState as UiState.Error).exception
                snackbarHostState.showSnackbar(
                    message = error.message ?: "Unknown error occurred",
                )
            }

            is UiState.Idle -> {
                // do nothing
            }
            is UiState.Loading -> {
                KmpLogger.d("joel123", "Loading ${isAnyLoading(registerState)}")
            }

            is UiState.Success -> {
                viewModel.resetAndPopBackStack(authRouter)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            snackbarHost = {
                SnackbarStyledHost(snackbarHostState)
            },
            containerColor = MaterialTheme.colorScheme.onPrimary,
            topBar = {
                TopBarCustom(
                    title = stringResource(Res.string.title_create_account),
                    onBackClick = {
                        authRouter.navigateBack()
                    }
                )
            },
            bottomBar = {
                RegisterNameBottom(
                    nameValue = nameValue,
                    onClick = {
                        viewModel.register()
                    }
                )
            }
        ) { contentPadding ->
            RegisterNameFormContent(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
                nameValue = nameValue,
                onNameChange = {
                    nameValue = it
                    viewModel.user = viewModel.user.copy(
                        name = it
                    )
                },
                isUserReceiveMarketing = isUserReceiveMarketing,
                onMarketingChecked = { isUserReceiveMarketing = it },
                isUserShareRegistrationData = isUserShareRegistrationData,
                onDataSharingChecked = { isUserShareRegistrationData = it }
            )
        }

        // Full-screen overlay above everything
        LoadingOverlay(registerState is UiState.Loading)
    }
}

@Composable
fun RegisterNameFormContent(
    modifier: Modifier,
    nameValue: String,
    onNameChange: (String) -> Unit,
    isUserReceiveMarketing: Boolean,
    onMarketingChecked: (Boolean) -> Unit,
    isUserShareRegistrationData: Boolean,
    onDataSharingChecked: (Boolean) -> Unit
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.onPrimary)
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Text(
            text = stringResource(Res.string.title_what_is_your_name),
            style = TSBodyMediumWhite().copy(
                fontSize = 26.sp
            )
        )

        TextFieldNormal(
            text = nameValue,
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth(),
            onValueChanged = {
                onNameChange(it)
            },
            textFieldInfo = stringResource(Res.string.label_this_appears_on_your)
        )

        HorizontalDivider(
            modifier = Modifier
                .padding(vertical = 16.dp),
            thickness = 1.dp,
        )

        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = stringResource(Res.string.label_by_tapping_create_account_terms_of_use),
            style = TSDisplayMediumWhite().copy(
                fontSize = 9.5.sp,
            )
        )

        Text(
            modifier = Modifier.padding(bottom = 24.dp),
            text = stringResource(Res.string.label_terms_of_use),
            style = TSDisplayMediumWhite().copy(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 9.5.sp,
            )
        )

        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = stringResource(Res.string.label_by_tapping_create_account_privacy_policy),
            style = TSDisplayMediumWhite().copy(
                fontSize = 9.5.sp,
            )
        )

        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = stringResource(Res.string.label_privacy_policy),
            style = TSDisplayMediumWhite().copy(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 9.5.sp,
            )
        )

        RowWithRoundCheckbox(
            modifier = Modifier.padding(bottom = 4.dp),
            text = stringResource(Res.string.label_i_would_prefer_not),
            isChecked = isUserReceiveMarketing,
            onCheckedChange = { value ->
                onMarketingChecked(value)
            }
        )

        RowWithRoundCheckbox(
            text = stringResource(Res.string.label_share_my_registration_data),
            isChecked = isUserShareRegistrationData,
            onCheckedChange = { value ->
                onDataSharingChecked(value)
            }
        )
    }
}

@Composable
fun RegisterNameBottom(
    nameValue: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
            .background(color = MaterialTheme.colorScheme.onPrimary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ButtonVariableSizeSolid(
            text = stringResource(Res.string.title_create_account),
            containerColor = MaterialTheme.colorScheme.onBackground,
            enabled = nameValue.isNotEmpty(),
            onClick = {
                onClick()
            }
        )
    }
}

@Preview
@Composable
private fun PagePreview() {
    PreviewTheme {
        RegisterNamePage(
            authRouter = AuthRouter(
                navigatorDummy()
            )
        )
    }
}