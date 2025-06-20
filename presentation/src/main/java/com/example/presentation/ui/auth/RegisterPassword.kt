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
import com.example.presentation.ui.widget.ButtonVariableSizeSolid
import com.example.presentation.ui.widget.TextFieldPassword
import com.example.presentation.ui.widget.TopBarCustom
import com.example.presentation.util.checkPassword
import com.example.presentation.util.preview.PreviewTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPasswordPage(
    mainRouter: MainRouter
) {
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
                text = stringResource(R.string.title_create_a_password),
                style = TSBodyMediumWhite().copy(
                    fontSize = 26.sp
                )
            )

            TextFieldPassword(
                modifier = Modifier.padding(top = 32.dp)
                    .fillMaxWidth(),
                text = password,
                onValueChanged = {
                    password = it
                },
                textFieldInfo = stringResource(R.string.label_atleast_10_characters)
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
                    enabled = password.checkPassword(),
                    onClick = {
                        mainRouter.navigateToRegisterBirthDatePage()
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
        RegisterPasswordPage(
            mainRouter = MainRouter(
                NavHostController(
                    LocalContext.current
                )
            )
        )
    }
}