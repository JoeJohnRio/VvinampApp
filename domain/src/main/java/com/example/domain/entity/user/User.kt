package com.example.domain.entity.user

import com.example.domain.entity.Gender
import com.example.domain.entity.SubscriptionType

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