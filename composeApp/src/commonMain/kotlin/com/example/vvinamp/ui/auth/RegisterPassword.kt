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
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
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
                title = stringResource(Res.string.title_create_account),
                icon = painterResource(Res.drawable.ic_back_arrow),
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
                text = stringResource(Res.string.title_create_a_password),
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
                isError = {
                    !isValidPassword(viewModel.user.password)
                },
                textFieldInfo = stringResource(Res.string.label_atleast_10_characters)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ButtonVariableSizeSolid(
                    modifier = Modifier.padding(top = 32.dp),
                    modifierText = Modifier.padding(horizontal = 16.dp),
                    text = stringResource(Res.string.label_continue),
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