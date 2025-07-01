package com.example.presentation.ui.auth

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.presentation.R
import com.example.presentation.ui.auth.viewmodel.AuthViewModel
import com.example.presentation.ui.main.MainRouter
import com.example.presentation.ui.theme.TSBodyMediumWhite
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.ui.widget.ButtonVariableSizeSolid
import com.example.presentation.ui.widget.LoadingOverlay
import com.example.presentation.ui.widget.RowWithRoundCheckbox
import com.example.presentation.ui.widget.SnackbarStyledHost
import com.example.presentation.ui.widget.TextFieldNormal
import com.example.presentation.ui.widget.TopBarCustom
import com.example.presentation.ui.widget.isAnyLoading
import com.example.presentation.util.UiState
import com.example.presentation.util.preview.PreviewTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterNamePage(mainRouter: MainRouter) {
    val viewModel = hiltViewModel<AuthViewModel>()
    val registerState by viewModel.registerState.collectAsState()
    var nameValue by remember { mutableStateOf("") }
    var isUserReceiveMarketing by remember { mutableStateOf(false) }
    var isUserShareRegistrationData by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(registerState) {
        when (registerState) {
            is UiState.Error -> {
                Log.d("joel123", "Error")
                val error = (registerState as UiState.Error).exception
                snackbarHostState.showSnackbar(
                    message = error.message ?: "Unknown error occurred",
                )
            }

            is UiState.Idle -> Log.d("joel123", "Idle")
            is UiState.Loading -> {
                Log.d("joel123", "Loading ${isAnyLoading(registerState)}")
            }

            is UiState.Success -> mainRouter.navigateToNavigationBar()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            snackbarHost = {
                SnackbarStyledHost(snackbarHostState)
            },
            backgroundColor = MaterialTheme.colorScheme.onPrimary,
            topBar = {
                TopBarCustom(
                    title = stringResource(R.string.title_create_account),
                    icon = painterResource(R.drawable.ic_back_arrow),
                    onBackClick = {
                        mainRouter.navigateBack()
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
                onNameChange = { nameValue = it },
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
            text = stringResource(R.string.title_what_is_your_name),
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
            textFieldInfo = stringResource(R.string.label_this_appears_on_your)
        )

        HorizontalDivider(
            modifier = Modifier
                .padding(vertical = 16.dp),
            thickness = 1.dp,
        )

        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = stringResource(R.string.label_by_tapping_create_account_terms_of_use),
            style = TSDisplayMediumWhite().copy(
                fontSize = 9.5.sp,
            )
        )

        Text(
            modifier = Modifier.padding(bottom = 24.dp),
            text = stringResource(R.string.label_terms_of_use),
            style = TSDisplayMediumWhite().copy(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 9.5.sp,
            )
        )

        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = stringResource(R.string.label_by_tapping_create_account_privacy_policy),
            style = TSDisplayMediumWhite().copy(
                fontSize = 9.5.sp,
            )
        )

        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = stringResource(R.string.label_privacy_policy),
            style = TSDisplayMediumWhite().copy(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 9.5.sp,
            )
        )

        RowWithRoundCheckbox(
            modifier = Modifier.padding(bottom = 4.dp),
            text = stringResource(R.string.label_i_would_prefer_not),
            isChecked = isUserReceiveMarketing,
            onCheckedChange = { value ->
                Log.d("joel123", "RegisterNamePage: $value")
                onMarketingChecked(value)
            }
        )

        RowWithRoundCheckbox(
            text = stringResource(R.string.label_share_my_registration_data),
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
            text = stringResource(R.string.title_create_account),
            containerColor = MaterialTheme.colorScheme.onBackground,
            enabled = nameValue.isNotEmpty(),
            onClick = {
                onClick()
            }
        )
    }
}

@Preview(apiLevel = 33)
@Composable
private fun PagePreview() {
    PreviewTheme {
        RegisterNamePage(
            mainRouter = MainRouter(
                NavHostController(
                    LocalContext.current
                )
            )
        )
    }
}