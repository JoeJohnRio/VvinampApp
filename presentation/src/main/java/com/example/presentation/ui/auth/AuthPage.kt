package com.example.presentation.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.presentation.R
import com.example.presentation.ui.main.MainRouter
import com.example.presentation.ui.theme.Gotham
import com.example.presentation.ui.theme.whiteBlackGradientBackground
import com.example.presentation.ui.widget.ButtonOutlined
import com.example.presentation.ui.widget.ButtonSolid
import com.example.presentation.util.preview.PreviewTheme

@Composable
fun AuthPage(
    mainRouter: MainRouter,
) {
    Column(
        modifier = Modifier
            .whiteBlackGradientBackground(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        ImageTextRegister(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f),
            titleText = stringResource(R.string.title_millions_of_songs)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f)
        ) {
            ButtonSolid(
                text = "Sign up free",
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .padding(bottom = 12.dp),
                onClick = {
                    mainRouter.navigateToAuthMethodSelectionPage()
                }
            )
            ButtonOutlined(
                text = "Log in",
                modifier = Modifier.padding(horizontal = 32.dp),
                onClick = {
                    mainRouter.navigateToLoginMethodSelectionPage()
                }
            )
        }
    }
}

@Composable
fun ImageTextRegister(
    modifier: Modifier,
    titleText: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.size(96.dp),
            tint = Color.White,
            painter = painterResource(id = R.drawable.ic_new_v),
            contentDescription = ""
        )
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = titleText,
            fontFamily = Gotham,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            style = TextStyle(
                letterSpacing = 0.sp,
                lineHeight = 28.sp // ~140% of fontSize is usually good
            )
        )
    }
}


@Preview(apiLevel = 33)
@Composable
private fun PagePreview() {
    PreviewTheme {
        AuthPage(
            mainRouter = MainRouter(
                NavHostController(
                    LocalContext.current
                )
            )
        )
    }
}