package com.example.vvinamp.ui.auth
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import com.example.vvinamp.util.preview.previewSafeString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel

import com.example.vvinamp.ui.auth.viewmodel.AuthViewModel
import com.example.vvinamp.navigation.router.AuthRouter
import com.example.vvinamp.ui.theme.TSBodyMediumWhite
import com.example.vvinamp.ui.widget.ButtonVariableSizeSolid
import com.example.vvinamp.ui.widget.TextFieldPassword
import com.example.vvinamp.ui.widget.TopBarCustom
import com.example.vvinamp.util.auth.isValidPassword
import com.example.vvinamp.util.preview.PreviewTheme
import com.example.vvinamp.util.preview.navigatorDummy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPasswordPage(
    authRouter: AuthRouter,
    viewModel: AuthViewModel
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.onPrimary,
        topBar = {
            TopBarCustom(
                title = previewSafeString(Res.string.title_create_account, "Create account"),
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
                text = previewSafeString(Res.string.title_create_a_password, "Create a password"),
                style = TSBodyMediumWhite().copy(
                    fontSize = 26.sp
                )
            )

            TextFieldPassword(
                modifier = Modifier.padding(top = 32.dp)
                    .fillMaxWidth(),
                text = viewModel.user.password,
                onValueChanged = {
                    viewModel.user = viewModel.user.copy(password = it)
                },
                // Only flag the rule once something is typed, so the empty field doesn't open in an error state.
                isError = {
                    viewModel.user.password.isNotEmpty() && !isValidPassword(viewModel.user.password)
                },
                textFieldInfo = previewSafeString(Res.string.label_atleast_10_characters, "Use at least 10 characters, including a number or symbol.")
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ButtonVariableSizeSolid(
                    modifier = Modifier.padding(top = 32.dp),
                    modifierText = Modifier.padding(horizontal = 16.dp),
                    text = previewSafeString(Res.string.label_continue, "Next"),
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    enabled = isValidPassword(viewModel.user.password),
                    onClick = {
                        authRouter.navigateToRegisterBirthDatePage()
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun PagePreview() {
    PreviewTheme {
        RegisterPasswordPage(
            authRouter = AuthRouter(
                navigatorDummy()
            ),
            viewModel = koinViewModel()
        )
    }
}