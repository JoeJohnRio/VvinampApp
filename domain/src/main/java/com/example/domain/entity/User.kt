package com.example.domain.entity

data class User(
    var name: String = "",
    var username: String = "",
    var email: String = "",
    var password: String = "",
    var profilePicture: String = "",
    var joinDate: String = "",
    var birthDate: String = "",
    var gender: Gender? = Gender.OTHER,
    var subscriptionType: SubscriptionType? = null,
)
