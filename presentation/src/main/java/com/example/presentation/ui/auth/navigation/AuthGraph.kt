package com.example.presentation.ui.auth.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.presentation.navigation.graph.AuthDestination
import com.example.presentation.ui.auth.AuthPage
import com.example.presentation.ui.auth.RegisterMethodSelectionPage
import com.example.presentation.ui.auth.viewmodel.AuthViewModel
import com.example.presentation.navigation.router.AuthRouter
import com.example.presentation.navigation.router.MainRouter
import com.example.presentation.ui.auth.LoginEmailPage
import com.example.presentation.ui.auth.LoginMethodSelectionPage
import com.example.presentation.ui.auth.RegisterBirthDatePage
import com.example.presentation.ui.auth.RegisterEmailPage
import com.example.presentation.ui.auth.RegisterGenderPage
import com.example.presentation.ui.auth.RegisterNamePage
import com.example.presentation.ui.auth.RegisterPasswordPage

@Composable
fun EntryProviderScope<NavKey>.AuthGraph(
    authRouter: AuthRouter,
    parentMainRouter: MainRouter
) {
    val viewModel: AuthViewModel = hiltViewModel()
    entry<AuthDestination.AuthRoot> {
        AuthPage(
            authRouter = authRouter,
        )
    }

    entry<AuthDestination.RegisterMethodSelection> {
        RegisterMethodSelectionPage(
            authRouter = authRouter,
        )
    }

    entry<AuthDestination.RegisterEmail> {
        RegisterEmailPage(
            authRouter = authRouter,
            viewModel = viewModel
        )
    }

    entry<AuthDestination.RegisterPassword> {
        RegisterPasswordPage(
            authRouter = authRouter,
            viewModel = viewModel
        )
    }

    entry<AuthDestination.RegisterBirthDate> {
        RegisterBirthDatePage(
            authRouter = authRouter,
            viewModel = viewModel
        )
    }

    entry<AuthDestination.RegisterGender> {
        RegisterGenderPage(
            authRouter = authRouter,
            viewModel = viewModel
        )
    }

    entry<AuthDestination.RegisterName> {
        RegisterNamePage(
            authRouter = authRouter,
            viewModel = viewModel
        )
    }

    entry<AuthDestination.LoginMethodSelection> {
        LoginMethodSelectionPage(
            authRouter = authRouter,
        )
    }

    entry<AuthDestination.LoginEmail> {
        LoginEmailPage(
            authRouter = authRouter,
            parentMainRouter = parentMainRouter
        )
    }
}