package com.example.presentation.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.presentation.R
import com.example.presentation.ui.main.MainRouter
import com.example.presentation.ui.theme.TSBodyMediumWhite
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.ui.widget.ButtonVariableOutlined
import com.example.presentation.ui.widget.ButtonVariableSizeSolid
import com.example.presentation.ui.widget.TextFieldNormal
import com.example.presentation.ui.widget.TextFieldPassword
import com.example.presentation.ui.widget.TopBarCustom
import com.example.presentation.util.checkPassword
import com.example.presentation.util.preview.PreviewTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginEmailPage(
    mainRouter: MainRouter
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        backgroundColor = MaterialTheme.colorScheme.onPrimary,
        topBar = {
            TopBarCustom(
                title = stringResource(R.string.title_create_account),
                icon = painterResource(R.drawable.ic_back_arrow),
                onBackClick = {
                    mainRouter.navigateBack()
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
                text = stringResource(R.string.title_email_or_username),
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
                text = stringResource(R.string.title_password),
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
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ButtonVariableSizeSolid(
                    modifier = Modifier.padding(top = 32.dp),
                    modifierText = Modifier.padding(horizontal = 16.dp),
                    text = stringResource(R.string.label_log_in),
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    enabled = email.isNotEmpty() && password.checkPassword(),
                    onClick = {
                        mainRouter.navigateToNavigationBar()
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ButtonVariableOutlined(
                    modifier = Modifier.padding(top = 32.dp),
                    text = stringResource(R.string.label_login_without_password),
                    textStyle = TSDisplayMediumWhite().copy(
                        fontSize = 8.sp
                    ),
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    enabled = email.isNotEmpty(),
                    onClick = {
//                        mainRouter.navigateToNavigationBar()
                    },
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    buttonHeight = 24.dp
                )
            }
        }

    }
}

@Preview(apiLevel = 33)
@Composable
private fun PagePreview() {
    PreviewTheme {
        LoginEmailPage(
            mainRouter = MainRouter(
                NavHostController(
                    LocalContext.current
                )
            )
        )
    }
}