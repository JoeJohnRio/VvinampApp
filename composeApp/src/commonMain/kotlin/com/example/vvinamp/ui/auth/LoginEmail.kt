package com.example.vvinamp.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.entity.user.User
import com.example.vvinamp.navigation.router.AuthRouter
import com.example.vvinamp.navigation.router.MainRouter
import com.example.vvinamp.ui.auth.viewmodel.AuthViewModel
import com.example.vvinamp.ui.theme.TSBodyMediumWhite
import com.example.vvinamp.ui.theme.TSDisplayMediumWhite
import com.example.vvinamp.ui.widget.ButtonVariableOutlined
import com.example.vvinamp.ui.widget.ButtonVariableSizeSolid
import com.example.vvinamp.ui.widget.SnackbarStyledHost
import com.example.vvinamp.ui.widget.TextFieldNormal
import com.example.vvinamp.ui.widget.TextFieldPassword
import com.example.vvinamp.ui.widget.TopBarCustom
import com.example.vvinamp.ui.widget.isAnyLoading
import com.example.vvinamp.util.KmpLogger
import com.example.vvinamp.util.UiState
import com.example.vvinamp.util.auth.isValidEmail
import com.example.vvinamp.util.auth.isValidPassword
import com.example.vvinamp.util.preview.PreviewTheme
import com.example.vvinamp.util.preview.navigatorDummy
import com.example.vvinamp.util.preview.previewSafeString
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import vvinampapp.composeapp.generated.resources.Res
import vvinampapp.composeapp.generated.resources.label_log_in
import vvinampapp.composeapp.generated.resources.label_login_without_password
import vvinampapp.composeapp.generated.resources.title_email_or_username
import vvinampapp.composeapp.generated.resources.title_login
import vvinampapp.composeapp.generated.resources.title_password

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginEmailPage(
    authRouter: AuthRouter,
    parentMainRouter: MainRouter,
    viewModel: AuthViewModel = koinViewModel(),
) {
    val loginState by viewModel.loginState.collectAsState()
    val session by viewModel.session.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val token: String? = session?.accessToken

    LaunchedEffect(loginState) {
        when (loginState) {
            is UiState.Error -> {
                val error = (loginState as UiState.Error).exception
                snackbarHostState.showSnackbar(
                    message = error.message ?: "Unknown error occurred",
                )
            }

            is UiState.Idle -> {

            }
            is UiState.Loading -> {
                KmpLogger.d("joel123", "Loading ${isAnyLoading(loginState)}")
            }

            is UiState.Success -> {
                snackbarHostState.showSnackbar(
                    message = "Login Success ${token}",
                )
            }
        }
    }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        snackbarHost = {
            SnackbarStyledHost(snackbarHostState)
        },
        containerColor = MaterialTheme.colorScheme.onPrimary,
        topBar = {
            TopBarCustom(
                title = previewSafeString(Res.string.title_login, "Login"),
                onBackClick = {
                    authRouter.navigateBack()
                }
            )
        }
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .padding(contentPadding)
                .background(MaterialTheme.colorScheme.onPrimary)
                .fillMaxSize()
                .padding(32.dp)
        ) {

            Text(
                text = previewSafeString(Res.string.title_email_or_username, "Email or username"),
                style = TSBodyMediumWhite().copy(
                    fontSize = 26.sp
                )
            )

            TextFieldNormal(
                text = email,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                onValueChanged = {
                    email = it
                },
            )

            Text(
                modifier = Modifier.padding(top = 32.dp),
                text = previewSafeString(Res.string.title_password, "Password"),
                style = TSBodyMediumWhite().copy(
                    fontSize = 26.sp
                )
            )

            TextFieldPassword(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                text = password,
                onValueChanged = {
                    password = it
                },
                isError = {
                    !isValidPassword(password)
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ButtonVariableSizeSolid(
                    modifier = Modifier.padding(top = 32.dp),
                    modifierText = Modifier.padding(horizontal = 16.dp),
                    text = previewSafeString(Res.string.label_log_in, "Log in"),
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    enabled = isValidEmail(email) && isValidPassword(password),
                    onClick = {
//                        mainRouter.navigateToNavigationBar()

                        println("joel123 jalan nih 2")
                        viewModel.login(userCred = User(
                            email = email,
                            password = password
                        ))
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ButtonVariableOutlined(
                    modifier = Modifier.padding(top = 32.dp),
                    text = previewSafeString(Res.string.label_login_without_password, "Log in without password"),
                    textStyle = TSDisplayMediumWhite().copy(
                        fontSize = 8.sp
                    ),
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    enabled = email.isNotEmpty(),
                    onClick = {
                        parentMainRouter.navigateToRootNav()
                    },
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    buttonHeight = 24.dp
                )
            }
        }

    }
}

@Preview
@Composable
private fun PagePreview() {
    PreviewTheme {
        LoginEmailPage(
            authRouter = AuthRouter(
                navigatorDummy()
            ),
            parentMainRouter = MainRouter(
                navigatorDummy()
            )
        )
    }
}