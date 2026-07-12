package com.example.vvinamp.ui.auth
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.style.TextAlign
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.example.vvinamp.navigation.router.AuthRouter
import com.example.vvinamp.ui.theme.AppColours
import com.example.vvinamp.ui.theme.TSBodyMediumWhite
import com.example.vvinamp.ui.theme.TSDisplayMediumWhite
import com.example.vvinamp.ui.widget.ButtonOutlined
import com.example.vvinamp.ui.widget.ButtonSolid
import com.example.vvinamp.util.preview.PreviewTheme
import com.example.vvinamp.util.preview.navigatorDummy

@Composable
fun LoginMethodSelectionPage(
    authRouter: AuthRouter
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
            titleText = stringResource(Res.string.title_sign_up_to_start_listening)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .weight(0.5f)
        ) {
            LoginSelectionButtons(
                authRouter
            )
        }
    }
}

@Composable
fun LoginSelectionButtons(
    mainRouter: AuthRouter
) {
    ButtonSolid(
        modifier = Modifier
            .padding(bottom = 12.dp),
        text = stringResource(Res.string.label_continue_with_email),
        icon = painterResource(Res.drawable.ic_email),
        onClick = {
            mainRouter.navigateToLoginEmailPage()
        }
    )

    ButtonOutlined(
        modifier = Modifier.padding(bottom = 12.dp),
        text = stringResource(Res.string.label_continue_with_phone),
        icon = painterResource(Res.drawable.ic_smartphone),
        onClick = {}
    )

    ButtonOutlined(
        modifier = Modifier.padding(bottom = 12.dp),
        text = stringResource(Res.string.label_continue_with_google),
        icon = painterResource(Res.drawable.ic_google),
        iconTint = Color.Unspecified,
        onClick = {}
    )

    ButtonOutlined(
        modifier = Modifier.padding(bottom = 48.dp),
        text = stringResource(Res.string.label_continue_with_facebook),
        icon = painterResource(Res.drawable.ic_facebook),
        iconTint = Color.Unspecified,
        onClick = {}
    )


    Text(
        modifier = Modifier
            .padding(bottom = 24.dp)
            .fillMaxWidth(),
        text = stringResource(Res.string.label_already_have_account),
        style = TSDisplayMediumWhite().copy(
            textAlign = TextAlign.Center,
            color = AppColours.Outline
        )
    )

    Text(
        modifier = Modifier
            .padding(bottom = 24.dp)
            .clickable(
                onClick = {
                    mainRouter.navigateToLoginEmailPage()
                }
            )
            .fillMaxWidth(),
        text = stringResource(Res.string.label_log_in),
        style = TSBodyMediumWhite().copy(
            textAlign = TextAlign.Center,
            color = AppColours.White
        )
    )
}


@Preview
@Composable
private fun PagePreview() {
    PreviewTheme {
        LoginMethodSelectionPage(
            authRouter = AuthRouter(
                navigatorDummy()
            )
        )
    }
}
