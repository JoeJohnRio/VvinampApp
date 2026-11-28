package com.example.vvinamp.ui.auth
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.entity.user.User
import com.example.vvinamp.navigation.router.AuthRouter
import com.example.vvinamp.ui.auth.viewmodel.AuthViewModel
import com.example.vvinamp.ui.theme.circularStdFontFamily
import com.example.vvinamp.ui.widget.ButtonOutlined
import com.example.vvinamp.ui.widget.ButtonSolid
import com.example.vvinamp.ui.widget.SnackbarStyledHost
import com.example.vvinamp.util.UiState
import com.example.vvinamp.util.preview.PreviewTheme
import com.example.vvinamp.util.preview.navigatorDummy
import com.example.vvinamp.util.preview.previewSafePainter
import com.example.vvinamp.util.preview.previewSafeString
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthPage(
    authRouter: AuthRouter,
    viewModel: AuthViewModel = koinViewModel()
) {
    val loginState by viewModel.loginState.collectAsState()
    val isLoggingIn = loginState is UiState.Loading
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(loginState) {
        if (loginState is UiState.Error) {
            val error = (loginState as UiState.Error).exception
            snackbarHostState.showSnackbar(
                message = error.message ?: "Failed to log in as guest"
            )
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarStyledHost(snackbarHostState)
        },
        containerColor = Color.Transparent,
        modifier = Modifier.background(
            Brush.verticalGradient(
                listOf(
                    MaterialTheme.colorScheme.secondary,
                    MaterialTheme.colorScheme.surfaceContainerLow,
                    MaterialTheme.colorScheme.background,
                    MaterialTheme.colorScheme.background
                )
            )
        )
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            ImageTextRegister(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.62f),
                titleText = previewSafeString(Res.string.title_millions_of_songs, "Hundreds of songs.\nFor Free.")
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .padding(bottom = 28.dp)
                    .weight(0.38f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ButtonSolid(
                    text = if (isLoggingIn) "Logging in as Guest..." else "Try as Guest (Demo)",
                    modifier = Modifier.fillMaxWidth(),
                    imageVector = Icons.Default.Person,
                    containerColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimary,
                    enabled = !isLoggingIn,
                    onClick = {
                        viewModel.login(
                            userCred = User(
                                email = "joel@joel.joel",
                                password = "joel123password"
                            )
                        )
                    }
                )

                ButtonSolid(
                    text = "Sign up free",
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    textColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = {
                        authRouter.navigateToAuthMethodSelectionPage()
                    }
                )

                ButtonOutlined(
                    text = "Log in",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        authRouter.navigateToLoginMethodSelectionPage()
                    }
                )
            }
        }
    }
}

@Composable
fun ImageTextRegister(
    modifier: Modifier,
    titleText: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.size(96.dp),
            tint = Color.White,
            painter = previewSafePainter(Res.drawable.ic_new_v),
            contentDescription = ""
        )
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = titleText,
            fontFamily = circularStdFontFamily(),
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            style = TextStyle(
                letterSpacing = 0.sp,
                lineHeight = 28.sp // ~140% of fontSize is usually good
            )
        )
    }
}

@Preview
@Composable
private fun PagePreview() {
    PreviewTheme {
        AuthPage(
            authRouter = AuthRouter(
                navigatorDummy()
            )
        )
    }
}
