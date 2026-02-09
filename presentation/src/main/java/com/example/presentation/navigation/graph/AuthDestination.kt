package com.example.presentation.navigation.graph

//import com.example.domain.entity.User
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface AuthDestination: NavKey {
    @Serializable
    data class MusicDetails(val musicId: Int) : AuthDestination

    @Serializable
    data object Auth : AuthDestination

    @Serializable
    data object AuthRoot : AuthDestination

    @Serializable
    data object RegisterEmail : AuthDestination

    @Serializable
    data object RegisterPassword : AuthDestination

    @Serializable
    data object RegisterBirthDate : AuthDestination

    @Serializable
    data object RegisterGender : AuthDestination

    @Serializable
    data object RegisterName : AuthDestination

    @Serializable
    data object RegisterMethodSelection : AuthDestination

    @Serializable
    data object LoginEmail : AuthDestination

    @Serializable
    data object LoginMethodSelection : AuthDestination
}
