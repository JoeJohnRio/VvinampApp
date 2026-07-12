package com.example.vvinamp.ui.auth
import vvinampapp.composeapp.generated.resources.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import com.example.domain.entity.Gender

import com.example.vvinamp.ui.auth.viewmodel.AuthViewModel
import com.example.vvinamp.navigation.router.AuthRouter
import com.example.vvinamp.ui.theme.AppColours
import com.example.vvinamp.ui.theme.TSBodyMediumWhite
import com.example.vvinamp.ui.widget.ButtonListWithState
import com.example.vvinamp.ui.widget.TopBarCustom
import com.example.vvinamp.util.preview.PreviewTheme
import com.example.vvinamp.util.preview.navigatorDummy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterGenderPage(
    authRouter: AuthRouter,
    viewModel: AuthViewModel = koinViewModel()
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
                text = stringResource(Res.string.title_what_is_your_gender),
                style = TSBodyMediumWhite().copy(
                    fontSize = 26.sp
                )
            )

            ButtonListWithState(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth(),
                listButton = listOf(
                    stringResource(Res.string.label_female),
                    stringResource(Res.string.label_male),
                    stringResource(Res.string.label_non_binary),
                    stringResource(Res.string.label_other),
                    stringResource(Res.string.label_prefer_not_to_say),
                ),
                outlineColor = AppColours.Outline,
                textColor = AppColours.OffWhite,
                containerColor = AppColours.OnPrimaryBlack,
                onClick = { gender ->
                    viewModel.user = viewModel.user.copy(
                        gender = Gender.fromDisplayName(gender)
                    )
                    authRouter.navigateToRegisterNamePage()
                }
            )
        }
    }
}

@Preview
@Composable
private fun PagePreview() {
    PreviewTheme {
        RegisterGenderPage(
            authRouter = AuthRouter(
                navigatorDummy()
            )
        )
    }
}
