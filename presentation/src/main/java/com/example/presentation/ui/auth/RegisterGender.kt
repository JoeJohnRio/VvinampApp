package com.example.presentation.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.domain.entity.Gender
import com.example.presentation.R
import com.example.presentation.ui.auth.viewmodel.AuthViewModel
import com.example.presentation.navigation.router.AuthRouter
import com.example.presentation.ui.theme.AppColours
import com.example.presentation.ui.theme.TSBodyMediumWhite
import com.example.presentation.ui.widget.ButtonListWithState
import com.example.presentation.ui.widget.TopBarCustom
import com.example.presentation.util.preview.PreviewTheme
import com.example.presentation.util.preview.navigatorDummy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterGenderPage(
    authRouter: AuthRouter,
    viewModel: AuthViewModel = hiltViewModel()
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
                text = stringResource(R.string.title_what_is_your_gender),
                style = TSBodyMediumWhite().copy(
                    fontSize = 26.sp
                )
            )

            ButtonListWithState(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth(),
                listButton = listOf(
                    stringResource(R.string.label_female),
                    stringResource(R.string.label_male),
                    stringResource(R.string.label_non_binary),
                    stringResource(R.string.label_other),
                    stringResource(R.string.label_prefer_not_to_say),
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

@Preview(apiLevel = 33)
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
