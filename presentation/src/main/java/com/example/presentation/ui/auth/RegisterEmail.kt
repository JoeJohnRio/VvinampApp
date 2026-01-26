package com.example.presentation.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.presentation.R
import com.example.presentation.ui.auth.viewmodel.AuthViewModel
import com.example.presentation.navigation.router.AuthRouter
import com.example.presentation.ui.theme.TSBodyMediumWhite
import com.example.presentation.ui.widget.ButtonVariableSizeSolid
import com.example.presentation.ui.widget.TextFieldNormal
import com.example.presentation.ui.widget.TopBarCustom
import com.example.presentation.util.auth.isValidEmail
import com.example.presentation.util.preview.PreviewTheme
import com.example.presentation.util.preview.navigatorDummy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterEmailPage(
    authRouter: AuthRouter,
    viewModel: AuthViewModel
) {
    Scaffold(
        backgroundColor = MaterialTheme.colorScheme.onPrimary,
        topBar = {
            TopBarCustom(
                title = stringResource(R.string.title_create_account),
                icon = painterResource(R.drawable.ic_back_arrow),
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
                text = stringResource(R.string.title_what_is_your_email),
                style = TSBodyMediumWhite().copy(
                    fontSize = 26.sp
                )
            )

            TextFieldNormal(
                text = viewModel.user.email,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                onValueChanged = {
                    viewModel.user = viewModel.user.copy(
                        email = it
                    )
                },
                isError = {
                    if (viewModel.user.email.isEmpty() || isValidEmail(viewModel.user.email)) {
                        false
                    } else {
                        true
                    }
                },
                textFieldInfo = stringResource(R.string.label_you_need_to_confirm_this)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ButtonVariableSizeSolid(
                    modifier = Modifier.padding(top = 32.dp),
                    modifierText = Modifier.padding(horizontal = 16.dp),
                    text = stringResource(R.string.label_continue),
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    enabled = isValidEmail(viewModel.user.email),
                    onClick = {
                        authRouter.navigateToRegisterPasswordPage()
                    }
                )
            }
        }

    }
}

@Preview(apiLevel = 33)
@Composable
private fun PagePreview() {
    PreviewTheme {
        RegisterEmailPage(
            authRouter = AuthRouter(
                navigatorDummy()
            ),
            viewModel = hiltViewModel()
        )
    }
}