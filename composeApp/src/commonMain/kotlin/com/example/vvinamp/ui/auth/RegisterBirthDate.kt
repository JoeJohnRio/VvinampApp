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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.LaunchedEffect
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import com.example.vvinamp.util.preview.previewSafeString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel
import com.example.data.util.toLocalDate

import com.example.vvinamp.ui.auth.viewmodel.AuthViewModel
import com.example.vvinamp.navigation.router.AuthRouter
import com.example.vvinamp.ui.theme.TSBodyMediumWhite
import com.example.vvinamp.ui.widget.ButtonVariableSizeSolid
import com.example.vvinamp.ui.widget.TopBarCustom
import com.example.vvinamp.util.auth.isAtLeast17YearsOld
import com.example.vvinamp.util.preview.PreviewTheme
import com.example.vvinamp.util.preview.navigatorDummy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterBirthDatePage(
    authRouter: AuthRouter,
    viewModel: AuthViewModel = koinViewModel()
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
                text = previewSafeString(Res.string.title_what_is_your_date_of_birth, "What's your date of birth?"),
                style = TSBodyMediumWhite().copy(
                    fontSize = 26.sp
                )
            )

            val today = remember { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date }
            // Typing mode first: entering a birth year is quicker than paging back through a calendar.
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = viewModel.user.birthDate.takeIf { it.isNotEmpty() }
                    ?.let { LocalDate.parse(it).atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds() },
                yearRange = 1900..today.year,
                initialDisplayMode = DisplayMode.Input
            )
            // The picker reports midnight UTC of the chosen day; keep it as yyyy-MM-dd like the rest of the app.
            LaunchedEffect(datePickerState.selectedDateMillis) {
                val birthDate = datePickerState.selectedDateMillis
                    ?.let { Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.UTC).date.toString() }
                    .orEmpty()
                viewModel.user = viewModel.user.copy(birthDate = birthDate)
            }

            DatePicker(
                state = datePickerState,
                modifier = Modifier.padding(top = 16.dp),
                title = null,
                colors = DatePickerDefaults.colors(containerColor = MaterialTheme.colorScheme.onPrimary)
            )

            val isOldEnough = viewModel.user.birthDate.toLocalDate().isAtLeast17YearsOld()
            if (viewModel.user.birthDate.isNotEmpty() && !isOldEnough) {
                Text(
                    text = previewSafeString(Res.string.label_must_be_17, "You need to be at least 17 to sign up."),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ButtonVariableSizeSolid(
                    modifier = Modifier.padding(top = 32.dp),
                    modifierText = Modifier.padding(horizontal = 16.dp),
                    text = previewSafeString(Res.string.label_continue, "Next"),
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    enabled = isOldEnough,
                    onClick = {
                        authRouter.navigateToRegisterGenderPage()
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
        RegisterBirthDatePage(
            authRouter = AuthRouter(
                navigatorDummy()
            )
        )
    }
}
