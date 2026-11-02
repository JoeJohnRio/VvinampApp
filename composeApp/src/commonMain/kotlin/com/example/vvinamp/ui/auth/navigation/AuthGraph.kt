package com.example.vvinamp.ui.auth.navigation

import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.vvinamp.navigation.graph.AuthDestination
import com.example.vvinamp.ui.auth.AuthPage
import com.example.vvinamp.ui.auth.RegisterMethodSelectionPage
import com.example.vvinamp.ui.auth.viewmodel.AuthViewModel
import com.example.vvinamp.navigation.router.AuthRouter
import com.example.vvinamp.navigation.router.MainRouter
import com.example.vvinamp.ui.auth.LoginEmailPage
import com.example.vvinamp.ui.auth.LoginMethodSelectionPage
import com.example.vvinamp.ui.auth.RegisterBirthDatePage
import com.example.vvinamp.ui.auth.RegisterEmailPage
import com.example.vvinamp.ui.auth.RegisterGenderPage
import com.example.vvinamp.ui.auth.RegisterNamePage
import com.example.vvinamp.ui.auth.RegisterPasswordPage

@Composable
fun EntryProviderScope<NavKey>.AuthGraph(
    authRouter: AuthRouter,
    parentMainRouter: MainRouter
) {
    val viewModel: AuthViewModel = koinViewModel()
    entry<AuthDestination.AuthRoot> {
        AuthPage(
            authRouter = authRouter,
            viewModel = viewModel
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