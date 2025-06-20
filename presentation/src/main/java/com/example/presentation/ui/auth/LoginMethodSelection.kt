package com.example.presentation.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.presentation.R
import com.example.presentation.ui.main.MainRouter
import com.example.presentation.ui.theme.AppColours
import com.example.presentation.ui.theme.TSBodyMediumWhite
import com.example.presentation.ui.theme.TSDisplayMediumWhite
import com.example.presentation.ui.widget.ButtonOutlined
import com.example.presentation.ui.widget.ButtonSolid
import com.example.presentation.util.preview.PreviewTheme

@Composable
fun LoginMethodSelectionPage(
    mainRouter: MainRouter
) {
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.onPrimary),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        ImageTextRegister(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f),
            titleText = stringResource(R.string.title_sign_up_to_start_listening)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .weight(0.5f)
        ) {
            LoginSelectionButtons(
                mainRouter
            )
        }
    }
}

@Composable
fun LoginSelectionButtons(
    mainRouter: MainRouter
) {
    ButtonSolid(
        modifier = Modifier
            .padding(bottom = 12.dp),
        text = stringResource(R.string.label_continue_with_email),
        icon = painterResource(R.drawable.ic_email),
        onClick = {
            mainRouter.navigateToLoginEmailPage()
        }
    )

    ButtonOutlined(
        modifier = Modifier.padding(bottom = 12.dp),
        text = stringResource(R.string.label_continue_with_phone),
        icon = painterResource(R.drawable.ic_smartphone),
        onClick = {}
    )

    ButtonOutlined(
        modifier = Modifier.padding(bottom = 12.dp),
        text = stringResource(R.string.label_continue_with_google),
        icon = painterResource(R.drawable.ic_google),
        iconTint = Color.Unspecified,
        onClick = {}
    )

    ButtonOutlined(
        modifier = Modifier.padding(bottom = 48.dp),
        text = stringResource(R.string.label_continue_with_facebook),
        icon = painterResource(R.drawable.ic_facebook),
        iconTint = Color.Unspecified,
        onClick = {}
    )


    Text(
        modifier = Modifier
            .padding(bottom = 24.dp)
            .fillMaxWidth(),
        text = stringResource(R.string.label_already_have_account),
        style = TSDisplayMediumWhite().copy(
            textAlign = TextAlign.Center,
            color = AppColours.Outline
        )
    )

    Text(
        modifier = Modifier
            .padding(bottom = 24.dp)
            .fillMaxWidth(),
        text = stringResource(R.string.label_log_in),
        style = TSBodyMediumWhite().copy(
            textAlign = TextAlign.Center,
            color = AppColours.White
        )
    )
}


@Preview(apiLevel = 33)
@Composable
private fun PagePreview() {
    PreviewTheme {
        LoginMethodSelectionPage(
            mainRouter = MainRouter(
                NavHostController(
                    LocalContext.current
                )
            )
        )
    }
}
