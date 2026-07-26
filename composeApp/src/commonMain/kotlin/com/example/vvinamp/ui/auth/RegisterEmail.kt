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
import com.example.vvinamp.ui.widget.TextFieldNormal
import com.example.vvinamp.ui.widget.TopBarCustom
import com.example.vvinamp.util.auth.isValidEmail
import com.example.vvinamp.util.preview.PreviewTheme
import com.example.vvinamp.util.preview.navigatorDummy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterEmailPage(
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
                text = previewSafeString(Res.string.title_what_is_your_email, "What's your email?"),
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
                textFieldInfo = previewSafeString(Res.string.label_you_need_to_confirm_this, "You'll need to confirm this email later.")
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
                    enabled = isValidEmail(viewModel.user.email),
                    onClick = {
                        authRouter.navigateToRegisterPasswordPage()
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
        RegisterEmailPage(
            authRouter = AuthRouter(
                navigatorDummy()
            ),
            viewModel = koinViewModel()
        )
    }
}